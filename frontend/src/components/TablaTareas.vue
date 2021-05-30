<template>
  <div>
    <v-snackbar
        v-model="snackbar"
        :timeout="timeout"
        :color="snackbarColor"
        top
      > 
        {{snackbarText}}
      </v-snackbar>
      
    <v-data-table
      :headers="headers"
      :items="tareas"
      class="elevation-1 secondary_variant"
      :custom-sort="ordenPersonalizado"
    >
      <template v-slot:[`item.createdAt`]="{item}">
        {{formatDate(item.createdAt)}}
        
      </template>

      <template v-slot:[`item.finishedAt`]="{item}">
        {{formatDate(item.finishedAt)}}
        
      </template>

      <template v-slot:top>
        <v-toolbar flat class="blanco--text" color="primary_variant">

          <v-toolbar-title><h2>Tareas</h2></v-toolbar-title>
          <v-divider class="mx-4" inset vertical></v-divider>
          <h3>{{formatDate(currentDate)}}</h3>
          <v-divider class="mx-4" inset vertical></v-divider>
          <h3>{{currentTime}}</h3>
          <v-divider class="mx-4" inset vertical></v-divider>
          <v-btn icon color="background" @click="getFromBd" title="Recargar">
            <v-icon>mdi-cached</v-icon>
          </v-btn>
          
          <v-spacer></v-spacer>

          <v-dialog v-model="dialog" max-width="500px">
            <template v-slot:activator="{ on, attrs }">
              
              <v-btn color="primary" dark class="mb-2" v-bind="attrs" v-on="on" title="Añadir una nueva tarea.">
                Nuevo
              </v-btn>

              <v-btn color="primary" dark class="mb-2 mr-4" title="Exportar en formato Excel.">
                Exportar
              </v-btn>
            </template>
            <v-card color="secondary" class="blanco--text">
              <v-card-title>
                <span class="headline">{{ formTitle }}</span>
              </v-card-title>

              <!-- ESTO ES EL MODAL -->
              <v-card-text class="black--text">
                <v-container>
                <v-form v-model="valid" ref="form2" >
                  <v-row v-if="editedIndex == -1">

                    <v-col sm="7" v-if="addingTaks">
                      <span><b>Servicio:</b> {{editedItem.contractBN}}</span>
                    </v-col>

                    <v-col sm="7" v-else>
                      <v-select
                        v-model="editedItem.contractId"
                        :items="benefNombre"
                        item-text="name"
                        item-value="id"
                        label="Servicios activos"
                        color="secondary_variant"
                        required
                        :rules="selectRules"
                      ></v-select>
                    </v-col>
                  </v-row>

                  <v-row v-if="editedIndex != -1">
                    <span><b>Servicio:</b> {{editedItem.contractBN}}</span>
                  </v-row>

                  <v-row>
                    <v-col sm="8">
                      <v-text-field
                        v-model="editedItem.title"
                        label="Titulo"
                        required
                        :rules="titleRules"
                      ></v-text-field>
                    </v-col>
                    <v-col sm="4">
                      <v-text-field
                        v-model.number="editedItem.investedTime"
                        label="Tiempo invertido"
                        required
                        :rules="timeRules"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                  <v-row>
                    <v-col sm="7">
                      <v-textarea
                        v-model="editedItem.description"
                        label="Descripción"
                        rows="2"
                      ></v-textarea>
                    </v-col>
                    <v-col sm="5" v-if="editedIndex != -1">
                      <fecha-modal @nuevaFecha="recibirNuevaFecha" :mostrarFecha="editedItem.createdAt"></fecha-modal>
                    </v-col>
                  </v-row>
                  <v-row>
                    <v-col cols="6">
                      <v-radio-group v-model="editedItem.priority">
                        <v-radio label="Alta" value="1" color="red"></v-radio>
                        <v-radio label="Media" value="2" color="orange"></v-radio>
                        <v-radio label="Baja" value="3" color="lime"></v-radio>
                      </v-radio-group>
                    </v-col>
                    <v-col cols="6">
                      <v-radio-group v-model="editedItem.state">
                        <v-radio label="Pendiente" value="1" color="purple"></v-radio>
                        <v-radio label="En curso" value="2" color="blue"></v-radio>
                        <v-radio label="Finalizado" value="3" color="green"></v-radio>
                      </v-radio-group>
                    </v-col>
                  </v-row>
                  </v-form>
                </v-container>
              </v-card-text>

              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="white" text @click="close">
                  Cancelar
                </v-btn>
                <v-btn color="white" text @click="save"> Guardar </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
          <v-dialog v-model="dialogDelete" max-width="500px">
            <v-card>
              <v-card-title class="headline"
                >¿Estás seguro de que quieres borrarlo?</v-card-title
              >
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn color="black" text @click="closeDelete"
                  >Cancelar</v-btn
                >
                <v-btn color="black" text @click="deleteItemConfirm"
                  >OK</v-btn
                >
                <v-spacer></v-spacer>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-toolbar>
      </template>

      <template v-slot:[`item.priority`]="{ item }">
        <v-chip :color="getColorPriority(item.priority)" text-color="black" outlined label>
          
          <span v-if="item.priority == '3'">Baja</span>
          <span v-else-if="item.priority == '2'">Media</span>
          <span v-else-if="item.priority == '1'">Alta</span>
        </v-chip>
      </template>
      
      <template v-slot:[`item.state`]="{ item }">
        <v-chip :color="getColorState(item.state)" text-color="black" outlined label @click="changeState(item)" 
          :title="titleState(item.state)">
          
          <span v-if="item.state == '1'">Pendiente</span>
          <span v-else-if="item.state == '2'">En curso</span>
          <span v-else-if="item.state == '3'">Finalizado</span>
        </v-chip>
      </template>

      <template v-slot:[`item.actions`]="{ item }">
        <v-icon title="Ver servicio" small class="mr-2" @click="goToDetails(item)"> mdi-eye </v-icon>
        <v-icon title="Editar la tarea." small class="mr-2" @click="editItem(item)"> mdi-pencil </v-icon>
        <v-icon title="Borrar la tarea." small @click="deleteItem(item)"> mdi-delete </v-icon>
      </template>

      <template v-slot:[`item.investedTime`]="{ item }">
        {{item.investedTime}}h
      </template>


      <template v-slot:[`item.description`]="{ item }">
       <span v-if="item.description != '' && item.description.length > 14">{{item.description.substring(0,12)}}...</span>
       <span v-else-if="item.description != ''">{{item.description}}</span>
       <span v-else><i>Sin descripción</i></span>
      </template>

      <template v-slot:no-data>
        <v-btn color="secondary" @click="getFromBd"> Recargar datos </v-btn>
      </template>

    </v-data-table>

  </div>
</template>

<script>
import FechaModal from '@/components/FechaModal.vue';
import axios from "axios";
import moment from "moment";
export default { 
  name: "TablaTareas",
  components: {
    FechaModal,
  },
  props:{
    headers:{
      text: String,
      value: String,
      align: String,
      sortable: Boolean,
      class: String,
      filter: Boolean,
    },
  },
  data: () => ({
    titleRules: [
      v => !!v || 'Nombre del titulo es requerido',
      v => v.length <= 100 || 'Nombre debe ser menor a 10 caracteres',
    ],
    selectRules: [
      v => !!v || 'Nombre de beneficiario es requerido',
    ],
    timeRules: [
      v => v >= 0 || 'Introduce un numero mayor que 0',
    ],
    valid:false,
    dialog: false,
    dialogDelete: false,
    currentDate: '',
    currentTime: '',
    fechaAuxiliar: "",
    loader: null,
    loading: false,

    benefNombre:[],
    tareas:[],
    
    editedIndex: -1,
    editedItem: {
      id: "",
      contractBN: "",
      title: "",
      createdAt: moment(),
      investedTime: 0,
      priority: "2",
      state: "1",
      description: "",
      contractId: ""
    },
    defaultItem: {
      id: "",
      contractBN: "",
      title: "",
      createdAt: moment(),
      investedTime: 0,
      priority: "2",
      state: "1",
      description: "",
      contractId: ""
    },

    addingTaks: false,

    snackbarText: "",
    snackbarColor: "",
    snackbar: false,
    timeout: 1500,

    url: "http://localhost:8080/task",
    urlServicios: "http://localhost:8080/contract",
  }),

  computed: { 
    formTitle() {
      return this.editedIndex === -1 ? "Nueva Tarea" : "Editando Tarea";
    },
  },

  watch: {
    dialog(val) {
      val || this.close();
    },
    dialogDelete(val) {
      val || this.closeDelete();
    },
  },

  created() {
    this.getFromBd();
    this.cargarServicios();
    this.setDateTime();

    this.comprobarLocal();
  },
  methods: {
    comprobarLocal(){

      if(localStorage.serviceBN){

        this.addingTaks = true;
        this.editedItem.contractBN = localStorage.serviceBN;
        this.editedItem.contractId = localStorage.serviceID;
        this.dialog = true;
      }
      
    },
    callSnackbar(color,text){
      this.snackbarColor = color;
      this.snackbarText = text;
      this.snackbar = true;
    },
    setDateTime(){
      this.currentDate = moment().locale('es');
      this.currentTime = moment().format('LTS');
      setInterval(()=>this.updateCurrent(),1000);
    },
    updateCurrent(){
      this.currentDate = moment().locale('es');
      this.currentTime = moment().format('LTS');
    },

    async getFromBd(){

      let response = await axios.get(this.url);
      this.tareas = response.data;

      this.tareas = this.tareas.sort((a,b)=>{
        return a.id - b.id;
      });
    },

    async cargarServicios(){

      let response = await axios.get(this.urlServicios); //response.data

      response.data.forEach(contract => {

        this.benefNombre.push({
          name: "["+contract.beneficiary+"] "+contract.name,
          id: contract.id
        });

      });
    },

    async saveInBd(){

      await axios.post(this.url, this.editedItem);
    },
    async updateInBd(){

      await axios.put(this.url, this.editedItem);
    },
    async deleteInBd(id){

      await axios.delete(this.url+"/"+id);
    },
    recibirNuevaFecha(value){

      this.editedItem.createdAt = value;
    },
    formatDate(date){
      return date ? moment(date).format("DD-MM-YYYY") : '';
    },
    ordenPersonalizado(items, index, isDesc) {
      items.sort((a, b) => {
        if (index === "fecha") {
          if (!isDesc[0]) {
            if (a.fecha > b.fecha) return 1;
            else if (a.fecha < b.fecha) return -1;
            else return 0;

          }
        }else {
          if (!isDesc[0]) {
            return a[index] < b[index] ? -1 : 1;
          } else {
            return b[index] < a[index] ? -1 : 1;
          }
        }
      });
      return items;
    },

    editItem(item) {
      this.editedIndex = this.tareas.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },

    deleteItem(item) {
      this.editedIndex = this.tareas.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialogDelete = true;
    },

    deleteItemConfirm() {

      this.deleteInBd(this.editedItem.id);
      this.closeDelete();
      this.getFromBd();
      this.callSnackbar("red","Borrado correctamente");
    },

    close() {
      this.dialog = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;

        this.addingTaks = false; //cuando cierras el cuadro, se reinician si es que veníamos de añadir tareas 
        localStorage.serviceBN = "";
        localStorage.serviceID = "";
      });

      if(this.$refs.form2 != null)
      this.$refs.form2.resetValidation();
    },

    closeDelete() {
      this.dialogDelete = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });

      if(this.$refs.form2 != null)
      this.$refs.form2.resetValidation();
    },

    save() {
      if(this.$refs.form2.validate()){
        if (this.editedIndex > -1) {
          this.getFromBd();
          this.updateInBd();
          this.getFromBd();
          this.callSnackbar("orange","Editado correctamente");
        } else {
          this.getFromBd();
          this.editedItem.contractBN = this.findById(this.editedItem.contractId); //en este punto, editedItem.contractBN está vacío asi que tenemos que rellenarlo
          this.saveInBd();
          this.getFromBd();
          this.callSnackbar("green","Añadido correctamente");
        }
        this.close();
      }
    },
    saveNoValidate(){
        if (this.editedIndex > -1) {
          this.getFromBd();
          this.updateInBd();
          this.getFromBd();
        } else {
          this.getFromBd();
          this.editedItem.contractBN = this.findById(this.editedItem.contractId);
          this.saveInBd();
          this.getFromBd();
        }
      this.editedItem = Object.assign({}, this.defaultItem); //reseteamos para que al darle a nuevo no tenga info
      this.editedIndex = -1;
    },
    findById(id){

      for (let i = 0; i < this.benefNombre.length; i++) {
        if(this.benefNombre[i].id == id){
          return this.benefNombre[i].name;
        }
      }
    },
    getColorPriority (prioridad) {
      
      if (prioridad == "1") return 'red'
      else if (prioridad == "2") return 'orange'
      else if(prioridad == "3") return 'lime'
      else return 'black'
    },
    getColorState (estado) {
      
      if (estado == "1") return 'purple'
      else if (estado == "2") return 'blue'
      else if (estado == "3") return 'green'
      else return 'black'
    },
    changeState(item){
      
      let num = parseInt(item.state);

      if(num > 2) num = 1;
      else num++;
      
      item.state = num.toString();

      this.editedItem = Object.assign({}, item);
      this.editedIndex = this.tareas.indexOf(item);
      
      this.saveNoValidate();
    },
    titleState(state){
      switch(state){
        case "1": 
          return "Cambiar a 'En curso'";
        case "2": 
          return "Cambiar a 'Finalizado'";
        case "3":
          return "Cambiar a 'Pendiente'";
        default:
          return "";
      }
    },
    
    goToDetails(item){

      localStorage.id = item.contractId;
      this.goToPath("/detalles");
    },
    goToPath(path) {
      if (this.$router.currentRoute.path != path) this.$router.push(path);
    },
  },
};
</script>