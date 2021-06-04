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
      :items="bolsas"
      class="elevation-5 secondary_variant"
      :custom-sort="customSorting"
      @dblclick:row="testDbclick"
      @click:row="testClick"
      title="Doble click en una bolsa para ver su servicio"
    >
      <template v-slot:[`item.initDate`]="{item}">
        {{formatDate(item.initDate)}}
      </template>

      <template v-slot:[`item.endDate`]="{item}">
        {{formatDate(item.endDate)}}
      </template>

      <template v-slot:top>
        <v-toolbar flat class="blanco--text" color="primary_variant">

          <v-toolbar-title><h2>Bolsas</h2></v-toolbar-title>
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

              <v-btn color="primary" dark class="mb-2" v-bind="attrs" v-on="on" title="Añadir nueva bolsa." @click="limpiarFechas">
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
              <v-card-text>
                <v-container>
                <v-form v-model="valid" ref="form3" >
                  <v-row v-if="editedIndex == -1">

                    <v-col sm="7" v-if="addingBags">
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
                    <v-col sm="4">
                      <v-text-field
                        v-model.number="editedItem.hours"
                        label="Horas totales"
                        required
                        :rules="timeRules"
                      ></v-text-field>
                    </v-col>
                    <fechas-modal
                      @nuevaFechaIni="recibirFechaIni"
                      @nuevaFechaFin="recibirFechaFin"
                      :mostrarFecha1="editedItem.initDate"
                      :mostrarFecha2="editedItem.endDate">
                    </fechas-modal>
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

      

      <template v-slot:[`item.actions`]="{ item }">
        <v-icon title="Ver servicio" small class="mr-2" @click="goToDetails(item)"> mdi-eye </v-icon>
        <v-icon title="Editar bolsa." small class="mr-2" @click="editItem(item)"> mdi-pencil </v-icon>
        <v-icon title="Borrar bolsa." small @click="deleteItem(item)"> mdi-delete </v-icon>
      </template>

      <template v-slot:no-data>
        <v-btn color="secondary" @click="getFromBd"> Recargar datos </v-btn>
      </template>

    </v-data-table>



  </div>
</template>

<script>
import moment from "moment";
import axios from "axios";
import FechasModal from './FechasModal.vue';
export default {
  name: "TablaBolsas",
  components: {
    FechasModal,
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
    selectRules: [
      v => !!v || 'Nombre de beneficiario es requerido',
    ],
    timeRules: [
      v => !!v || 'Añade un tiempo estimado',
    ],
    valid:false,

    //---DIÁLOGOS
    dialog: false,
    dialogDelete: false,

      //---FECHA Y HORA ACTUAL
    currentDate: '',
    currentTime: '',

    //---ITEMS
    auxItem: "",  //guardamos la fila al hacer doble click

    editedIndex: -1,
    editedItem: {
      id: "",
      contractBN: "",
      hours: 0,
      remaining: 0,
      initDate: "",
      endDate: "",
      contractId: ""
    },
    defaultItem: {
      contractBN: "",
      hours: 0,
      remaining: 0,
      initDate: "",
      endDate: "",
      id: "",
      contractId: ""
    },

    //---SNACKBAR
    snackbarText: "",
    snackbarColor: "",
    snackbar: false,
    timeout: 1500,

    //---API
    bolsas: [],
    bolsasAux:[],
    benefNombre:[],
    url: "http://localhost:8080/hoursBag", //guardar,eliminar, editar bolsas
    url2: "http://localhost:8080/contractBT", //coger bolsas con sus calculos
    urlServicios: "http://localhost:8080/contract", //coger contratos

    //---Otros
    addingBags: false,
  }),

  computed: {

    formTitle() {
      return this.editedIndex === -1 ? "Nueva Bolsa" : "Editando Bolsa";
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
    comprobarLocal(){

      if(localStorage.serviceBN){

        this.addingBags = true;
        this.editedItem.contractBN = localStorage.serviceBN;
        this.editedItem.contractId = localStorage.serviceID;
        this.dialog = true;
      }
      
    },

    //---SNACKBARS
    callSnackbar(color,text){
      this.snackbarColor = color;
      this.snackbarText = text;
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
    formatDate(date){

      return date ? moment(date).format("DD-MM-YYYY") : '';
    },
    recibirFechaIni(value){ //emit de fechas modal

      this.editedItem.initDate = value.inicio;
    },
    recibirFechaFin(value){ //emit de fechas modal

      this.editedItem.endDate = value.fin;
    },
    limpiarFechas(){ //al hacer click en nuevo nos aseguramos de que se borran las fechas

      this.editedItem.initDate = null;
      this.editedItem.endDate = null;
    },

    //---OPERACIONES BASE DE DATOS
    async getFromBd(){

      let response = await axios.get(this.url2); //cogemos url2 ya que aqui se encuentran los cálculos de horas restantes

      if(response.data != null){ //como vamos a acceder al método forEach, tenemos que comprobar que no lo estamos haciendo de un null

        response.data.forEach(service =>
          {
            if(service.hourBagsList != null){ //si tiene lista de bolsas, la recorremos

              service.hourBagsList.forEach(bag =>
              this.bolsasAux.push(bag))
            }
          }
        );

        this.bolsas = this.bolsasAux;
        this.bolsasAux = []; //reinciamos variable para no acumular bolsas

        this.bolsas = this.bolsas.sort((a,b)=>{
          return a.contractId - b.contractId;
        });

      }else{

        this.bolsasAux = [];
      }
      
    },

    async cargarServicios(){

      let response = await axios.get(this.urlServicios); //cogemos los servicios que existen

      response.data.forEach(contract => {

        this.benefNombre.push({
          name: "["+contract.beneficiary+"] "+contract.name,
          id: contract.id
        });

      });
    },

    async saveInBd(){

      await axios.post(this.url, this.editedItem);
      this.callSnackbar("green","Añadido correctamente");
    },
    async updateInBd(){

      await axios.put(this.url, this.editedItem);
      this.callSnackbar("green","Editado correctamente");
    },
    async deleteInBd(id){

      await axios.delete(this.url+"/"+id);
      this.callSnackbar("green","Borrado correctamente");
    },

    //---OPERACIONES BASE DE DATOS
    editItem(item) {
      this.editedIndex = this.bolsas.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },

    deleteItem(item) {
      this.editedIndex = this.bolsas.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialogDelete = true;
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

        this.addingBags = false; //cuando cierras el cuadro, se reinician si es que veníamos de añadir tareas 
        localStorage.serviceBN = "";
        localStorage.serviceID = "";
      });

      if(this.$refs.form3 != null)
      this.$refs.form3.resetValidation();
    },

    closeDelete() {
      this.dialogDelete = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });

      if(this.$refs.form3 != null)
      this.$refs.form3.resetValidation();
    },

    async save() {
      if(this.$refs.form3.validate() && (this.editedItem.initDate != null) && (this.editedItem.endDate != null)){ //si esta validado
      
        if(moment(this.editedItem.initDate).isBefore(moment(this.editedItem.endDate)) || moment(this.editedItem.initDate).isSame(moment(this.editedItem.endDate))){//y las fechas estan bien
          
          if (this.editedIndex > -1) {

            await this.updateInBd();
            await this.getFromBd();
          } else {

            this.editedItem.contractBN = this.findById(this.editedItem.contractId); //en este punto, editedItem.contractBN está vacío asi que tenemos que rellenarlo
            await this.saveInBd(); //para guardar la bolsa necesitamos el id de su contrato
            await this.getFromBd();
          }

          this.close();
        }else{

          this.callSnackbar("red","Las fechas están mal ordenadas.");
        }
      }
    },

    //---SORTING
    customSorting(items, index, isDesc) {
      items.sort((a, b) => {
        if (index[0] === "initDate" || index[0] === "endDate") {
          
          if (!isDesc[0]) {
            if (a[index] > b[index]) return 1;
            else if (a[index] < b[index]) return -1;
            else return 0;

          }else{

            if (a[index] < b[index]) return 1;
            else if (a[index] > b[index]) return -1;
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
    findById(id){

      for (let i = 0; i < this.benefNombre.length; i++) {
        if(this.benefNombre[i].id == id){
          return this.benefNombre[i].name;
        }
      }
    },
  },
};
</script>