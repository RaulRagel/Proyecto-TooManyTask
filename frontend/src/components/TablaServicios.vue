<template>
  <div> <!-- :sort-by="['beneficiario']"  -->
    <v-snackbar
      transition="slide-y-transition"
      v-model="snackbar"
      :timeout="timeout"
      :color="snackbarColor"
      top
    > 
      {{snackbarText}}
    </v-snackbar>

    <v-snackbar
      transition="slide-y-transition"
      v-model="snackbarWarnings"
      :color="snackbarColor"
      top
    >

    <ul v-if="snackbarText != ''">
      <li v-for="warns in snackbarText" :key="warns">
          {{warns}}
      </li>
    </ul>

    <span v-else><i>No hay avisos</i></span>

      <template v-slot:action="{ attrs }">
        <v-btn
          color="red"
          text
          v-bind="attrs"
          @click="snackbarWarnings = false"
        >
          Cerrar
        </v-btn>
      </template>
    </v-snackbar>

    <v-data-table
      :headers="headers"
      :items="servicios"
      class="elevation-5 secondary_variant"
      :custom-sort="ordenPersonalizado"
      
    >
      <template v-slot:[`item.createdAt`]="{item}">
        {{formatDate(item.createdAt)}}
      </template>
      
      <template v-slot:top>
        <v-toolbar flat class="blanco--text" color="primary_variant">

          <v-toolbar-title><h2>Servicios</h2></v-toolbar-title>
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
              
              <v-btn color="primary" dark class="mb-2" v-bind="attrs" v-on="on" title="Añadir un nuevo servicio.">
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
              
              <!-- MODAL -->
              <v-card-text class="blanco--text">
                <v-container>
                  <v-form v-model="valid" ref="form1" >
                    <v-row>
                      <v-col sm="7">
                        <v-text-field
                          v-model="editedItem.beneficiary"
                          label="Beneficiario"
                          required
                          :rules="benefRules"
                        ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col sm="7">
                        <v-text-field
                          v-model="editedItem.name"
                          label="Nombre"
                          required
                          :rules="nameRules"
                        ></v-text-field>
                        </v-col>
                        <v-col sm="5" v-if="editedIndex != -1">
                          <fecha-modal @nuevaFecha="recibirNuevaFecha" :mostrarFecha="editedItem.createdAt"></fecha-modal>
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
                <v-btn color="white" text @click="save">
                  Guardar
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
          <v-dialog v-model="dialogDelete" max-width="500px">
            <v-card class="pa-2">
              <v-card-title class="headline">
                ¿Estás seguro de que quieres borrarlo?
              </v-card-title>
              <p>Las tareas y bolsas de horas asignadas se borrarán también.</p>
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


      <template v-slot:[`item.tasks`]="{ item }">

        {{item.tasks}}
        <v-icon title="Añadir tareas a este servicio" small  @click="goToPathAdd(item,'/tareas')"> mdi-plus-circle </v-icon>
      </template>

      <template v-slot:[`item.hourBags`]="{ item }">

        {{item.hourBags}}
        <v-icon title="Añadir bolsas a este servicio" small  @click="goToPathAdd(item,'/bolsahoras')"> mdi-plus-circle </v-icon>
      </template>

      <template v-slot:[`item.actions`]="{ item }">

        <v-icon title="Ver detalles del servicio" small class="mr-2" @click="goToDetails(item)"> mdi-eye </v-icon>
        <v-icon title="Editar y ver tareas o bolsas de horas." small  @click="editItem(item)"> mdi-pencil </v-icon>
        <v-icon title="Borrar servicio." small @click="deleteItem(item)" class="ml-2"> mdi-delete </v-icon>
      </template>


      <template v-slot:[`item.warnings`]="{ item }">
        <v-chip :color="getColor(item.warnings)" text-color="black" outlined label @click="showWarnings(item.warningList)">
          {{ item.warnings }}
        </v-chip>
      </template>

      <template v-slot:no-data>
        <v-btn color="secondary" @click="getFromBd"> Recargar datos </v-btn>
      </template>

    </v-data-table>

  </div>
</template>

<script>
import FechaModal from '@/components/FechaModal.vue';
import moment from "moment";
import axios from "axios";
export default {
  name: "TablaServicios",
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
    benefRules: [
      v => !!v || 'Nombre  de beneficiario es requerido',
      v => v.length <= 100 || 'Beneficiario debe ser menor a 10 caracteres',
    ],
    nameRules: [
      v => !!v || 'Nombre del servicio es requerido',
      v => v.length <= 100 || 'Nombre debe ser menor a 10 caracteres',
    ],
    valid:false,
    dialog: false,
    dialogDelete: false,
    currentDate: '',
    currentTime: '',
    item: '',
    
    editedIndex: -1,
    editedItem: {
      beneficiary: "",
      name: "",
      tasks: 0,
      hourBags: 0,
      createdAt: moment(),
      warnings: 0,
      id: "",
      totalHours: 0,
    },
    defaultItem: {
      beneficiary: "",
      name: "",
      tasks: 0,
      hourBags: 0,
      createdAt: moment(),
      warnings: 0,
      id: "",
      totalHours: 0,
    },

    snackbarText: "",
    snackbarColor: "",
    snackbar: false,
    snackbarWarnings: false,
    
    timeout: 1500,

    servicios: [],
    urlCrud: "http://localhost:8080/contract",
    urlGet: "http://localhost:8080/contractBT",

  }),

  computed: { 

    formTitle() {
      return this.editedIndex === -1 ? "Nuevo Servicio" : "Editando Servicio";
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
    this.setDateTime();
  },
  methods: {
    callSnackbar(color,text){
      this.snackbarColor = color;
      this.snackbarText = text;
      this.snackbar = true;
    },
    callWarningSnackbar(color,text){
      this.snackbarColor = color;
      this.snackbarText = text;
      this.snackbarWarnings = true;
    },
    setDateTime(){
      this.currentDate = moment();
      this.currentTime = moment().format('LTS'); //inicializar
      setInterval(()=>this.updateCurrent(),1000); //actualizar
    },
    updateCurrent(){
      this.currentDate = moment().locale('es');
      this.currentTime = moment().format('LTS');
    },

    async getFromBd(){

      let response = await axios.get(this.urlGet);
      this.servicios = response.data;

      this.servicios = this.servicios.sort((a,b)=>{
        return a.id - b.id;
      });
    },

    async saveInBd(){

      await axios.post(this.urlCrud, this.editedItem);
    },
    async updateInBd(){

      await axios.put(this.urlCrud, this.editedItem);
    },
    async deleteInBd(id){

      await axios.delete(this.urlCrud+"/"+id);
    },

    recibirNuevaFecha(value){

      this.editedItem.createdAt = value;
    },

    formatDate(fecha){
      return fecha ? moment(fecha).format("DD-MM-YYYY") : '';
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

    editItem(item) { //al hacer click en el lápiz, rellenamos editedItem con el item que pinchemos
      this.editedIndex = this.servicios.indexOf(item); //editamos el servicio 3, por ejemplo
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },

    deleteItem(item) {
      this.editedIndex = this.servicios.indexOf(item);
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
        
        this.editedItem = Object.assign({}, this.defaultItem); //reiniciamos el editedItem a partir del objeto vacío defaultItem
        this.editedIndex = -1;
      });

      if(this.$refs.form1 != null)
      this.$refs.form1.resetValidation();
    },

    closeDelete() {
      this.dialogDelete = false;
      this.$nextTick(() => {
        
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });

      if(this.$refs.form1 != null)
      this.$refs.form1.resetValidation();
    },

    save() {
      
      if(this.$refs.form1.validate()){

        if (this.editedIndex > -1) {

          //actualizamos
          this.getFromBd();
          this.updateInBd();
          this.getFromBd();
          this.callSnackbar("orange","Editado correctamente");

        } else {

          //guardamos
          this.getFromBd();
          this.saveInBd();
          this.getFromBd();
          this.callSnackbar("green","Añadido correctamente");
        }


        this.close();

      }
    },

    goToDetails(item){

      localStorage.id = item.id;
      this.goToPath("/detalles");
    },

    goToPath(path) {
      if (this.$router.currentRoute.path != path) this.$router.push(path);
    },
    goToPathAdd(item,path) {
      localStorage.serviceBN = "["+item.beneficiary+"] "+item.name;
      localStorage.serviceID = item.id;
      if (this.$router.currentRoute.path != path) this.$router.push(path);
    },
    
    getColor (warnings) {
      if (warnings > 4) return 'red'
      else if (warnings > 2) return 'orange'
      else return 'green'
    },

    showWarnings(warnings){

      this.callWarningSnackbar("secondary_variant",warnings);
    }
  },
};
</script>