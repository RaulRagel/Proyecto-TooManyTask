<template>
  <div>
    <v-snackbar
        v-model="snackbar"
        :timeout="timeout"
        :color="snackbarColor"
        top
      > 
        {{snackbarTitle}}
      </v-snackbar>
      
    <v-data-table
      :headers="headers"
      :items="tareas"
      class="elevation-1 secondary_variant"
      :custom-sort="customSorting"
      @dblclick:row="testDbclick"
      @click:row="testClick"
      sort-by="pin"
      :sort-desc="true"
      title="Doble click en una tarea para ver su servicio"
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
                    <v-col sm="6">
                      <v-text-field
                        v-model="editedItem.title"
                        label="Titulo"
                        required
                        :rules="titleRules"
                      ></v-text-field>
                    </v-col>
                    <v-col sm="3">
                      <v-text-field
                        v-model.number="editedItem.hours"
                        label="Horas"
                        required
                        :rules="timeRules"
                      ></v-text-field>
                    </v-col>
                    <v-col sm="3">
                      <v-text-field
                        v-model.number="editedItem.minutes"
                        label="Minutos"
                        required
                        :rules="minRules"
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

      

      <template v-slot:[`item.investedTime`]="{ item }">
        {{item.investedTime.toFixed(2)}}<b>h</b>
      </template>


      <template v-slot:[`item.description`]="{ item }">
       <span v-if="item.description != '' && item.description.length > 14">{{item.description.substring(0,12)}}...</span>
       <span v-else-if="item.description != ''">{{item.description}}</span>
       <span v-else class="grey--text text--darken-2"><i>Sin descripción</i></span>
      </template>

      <template v-slot:[`item.actions`]="{ item }">
        <v-icon title="Ver servicio" small class="mr-2" @click="goToDetails(item)"> mdi-eye </v-icon>
        <v-icon title="Editar la tarea." small class="mr-2" @click="editItem(item)"> mdi-pencil </v-icon>
        <v-icon title="Borrar la tarea." small @click="deleteItem(item)"> mdi-delete </v-icon>
      </template>

      <template v-slot:[`item.pin`]="{ item }">
        <v-btn icon>
          <v-icon small @click="pinned(item)" :color="pinnedColor(item.pin)" title="Fijar">
            mdi-pin
          </v-icon>
        </v-btn>
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
    //---VALIDACIONES
    titleRules: [
      v => !!v || 'El titulo es requerido',
      v => v.length <= 100 || 'Nombre debe ser menor a 10 caracteres',
    ],
    selectRules: [
      v => !!v || 'El beneficiario es requerido',
    ],
    timeRules: [
      v => v >= 0 || 'Introduce un numero mayor que 0',
      v => v <= 99999 || 'Es un número demasiado grande!',
    ],
    minRules: [
      v => v >= 0 || 'Introduce un numero mayor que 0',
      v => v <=59 || 'Introduce un número menor que 60',
    ],
    valid:false,

    //---DIÁLOGOS
    dialog: false,
    dialogDelete: false,

    //---FECHA Y HORA ACTUAL
    currentDate: '',
    currentTime: '',

    //---RECARGAR
    loader: null,
    loading: false,

    //---ITEMS
    auxItem: "",  //guardamos la fila al hacer doble click
    
    editedIndex: -1,
    editedItem: {
      id: "",
      contractBN: "",
      title: "",
      createdAt: moment(),
      hours: 0,
      minutes: 0,
      priority: "2",
      state: "1",
      description: "",
      contractId: "",
      pin: false,
    },
    defaultItem: {
      id: "",
      contractBN: "",
      title: "",
      createdAt: moment(),
      hours: 0,
      minutes: 0,
      priority: "2",
      state: "1",
      description: "",
      contractId: "",
      pin: false,
    },


    //---SNACKBAR
    snackbarTitle: "",
    snackbarColor: "",
    snackbar: false,
    timeout: 1500,

    //---API
    tareas:[],
    benefNombre:[], //beneficiario+nombre
    url: "http://localhost:8080/task", //de donde cogemos las tareas
    urlServicios: "http://localhost:8080/contract",

    //---Otros
    addingTaks: false, //si venimos del botón agregar tarea al servicio, su valor será true

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
    //---LOCAL STORAGE
    comprobarLocal(){ //si teníamos información, es que estamos añadiendo una tarea desde servicios

      if(localStorage.serviceBN){

        this.addingTaks = true;
        this.editedItem.contractBN = localStorage.serviceBN;
        this.editedItem.contractId = localStorage.serviceID;
        this.dialog = true;
      }
    },

    //---SNACKBARS
    callSnackbar(color,text){
      this.snackbarColor = color;
      this.snackbarTitle = text;
      this.snackbar = true;
    },

    //---FECHAS
    setDateTime(){
      this.currentDate = moment().locale('es');
      this.currentTime = moment().format('LTS');
      setInterval(()=>this.updateCurrent(),1000);
    },
    updateCurrent(){
      this.currentDate = moment().locale('es');
      this.currentTime = moment().format('LTS');
    },
    recibirNuevaFecha(value){ //emit de fecha modal

      this.editedItem.createdAt = value;
    },
    formatDate(date){
      return date ? moment(date).format("DD-MM-YYYY") : '';
    },

    //---OPERACIONES BASE DE DATOS
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

      await axios.post(this.url, this.editedItem).then((data) =>{

        //Salió bien
        console.log(data);
        this.callSnackbar("green","Añadido correctamente");

      }).catch((error) => {

        this.catchedError(error);
      });
    },
    async updateInBd(){

      await axios.put(this.url, this.editedItem).then((data) =>{

        //Salió bien
        console.log(data);
        this.callSnackbar("green","Actualizado correctamente");

      }).catch((error) => {

        this.catchedError(error);
      });
    },
    async deleteInBd(id){

      await axios.delete(this.url+"/"+id).then((data) =>{

        //Salió bien
        console.log(data);
        this.callSnackbar("green","Borrado correctamente");

      }).catch((error) => {

        this.catchedError(error);
      });
    },

    catchedError(error){
      //Hubo un error
      if (error.response) {

        // Se hizo la petición y el servidor respondió con un código de error
        this.callSnackbarSubtitle("red","Error en el servidor");
        console.log(error.response.data);
        console.log(error.response.status);
        console.log(error.response.headers);
      } else if (error.request) {

        // No hubo respuesta al hacer la petición
        this.callSnackbar("red","Sin respuesta del servidor");
        console.log(error.request);
      } else {

        this.callSnackbar("red","Algo salió mal");
        console.log('Error', error.message);
      }
      console.log(error.config);
    },

    //---OPERACIONES DEL USUARIO
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
    async changeState(item){ //hay algo asincrono
      
      let num = parseInt(item.state);

      if(num > 2) num = 1;
      else num++;
      
      item.state = num.toString();

      this.editedItem = Object.assign({}, item); //asignamos para actualizar el editedItem
      
      await this.updateInBd(); //esperar este proceso
      
      await this.getFromBd();

      this.editedItem = Object.assign({}, this.defaultItem); //reseteamos para que al darle a nuevo no tenga info

    },
    async pinned(item){ //click en la chincheta

      item.pin = !item.pin;

      this.editedItem = Object.assign({}, item); //asignamos para actualizar el editedItem

      await this.updateInBd();

      await this.getFromBd();

      this.editedItem = Object.assign({}, this.defaultItem); //reseteamos para que al darle a nuevo no tenga info
    },

    async deleteItemConfirm() {

      await this.deleteInBd(this.editedItem.id);
      this.closeDelete();
      await this.getFromBd();
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

    async save() {
      if(this.$refs.form2.validate()){
        if (this.editedIndex > -1) {

          await this.updateInBd();
          await this.getFromBd();
        } else {

          this.editedItem.contractBN = this.findById(this.editedItem.contractId); //en este punto, editedItem.contractBN está vacío asi que tenemos que rellenarlo
          await this.saveInBd();
          await this.getFromBd();
        }
        this.close();
      }
    },

    //---SORTING
    customSorting(items, index, isDesc) {

      items.sort((a, b) => {
        if (index[0] === "createdAt") {
          
          if (!isDesc[0]) {
            if (a.createdAt > b.createdAt) return 1;
            else if (a.createdAt < b.createdAt) return -1;
            else return 0;

          }else{

            if (a.createdAt < b.createdAt) return 1;
            else if (a.createdAt > b.createdAt) return -1;
            else return 0;
          }
        }else {
          if (!isDesc[0]) {
            return a[index] < b[index] ? -1 : 1;
          } else {
            return a[index] > b[index] ? -1 : 1;
          }
        }
      });
      return items;
    },

    //---IR A OTRA PESTAÑA
    testClick(item){
      this.auxItem = item;
    },
    testDbclick(){

      localStorage.id = this.auxItem.contractId;
      this.goToPath("/detalles");
    },
    goToDetails(item){

      localStorage.id = item.contractId;
      this.goToPath("/detalles");
    },
    goToPath(path) {
      if (this.$router.currentRoute.path != path) this.$router.push(path);
    },

    //---OTROS
    findById(id){ //buscamos el servicio a través de su id para asignarselo antes de guardar la tarea

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
    pinnedColor(pin){
      if(pin) return 'primary'
      else return 'grey darken'
    }
  },
};
</script>