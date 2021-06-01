<template>
  <div class="ma-10">
    <v-row>
      <v-col cols="9">
        <v-card
          class="pa-3"
          elevation="5"
          color="primary_variant"
        >
          <v-toolbar flat class="blanco--text" color="primary_variant" tile>
            <v-toolbar-title><h2>[{{ servicio.beneficiary }}] {{ servicio.name }}</h2></v-toolbar-title>

            <v-spacer></v-spacer>

            <v-btn icon color="blanco" title="Ir a la tabla Servicios" @click="goToPath('/servicios')">
              <v-icon style="transform: scaleX(-1)">mdi-application-import</v-icon>
            </v-btn>
          </v-toolbar>
        </v-card>

        <v-card color="secondary" class="pa-3 primary--text" tile>
          <v-row>
            <v-col sm="2"><h3>Beneficiario</h3></v-col>
            <v-col sm="5"><h3>Nombre</h3></v-col>
            <v-col sm="2"><h3>Fecha de creación</h3></v-col>
          </v-row>
        </v-card>

        <v-card class="pa-3" color="secondary_variant" tile>
            <v-row>
              <v-col sm="2"> {{ servicio.beneficiary }}</v-col>
              <v-col sm="5">{{servicio.name}}</v-col>
              <v-col sm="2">{{servicio.createdAt}}</v-col>
            </v-row>
        </v-card>

        <v-card color="secondary" class="pa-3 primary--text" tile>
            <v-row align="center">
              <v-col sm="2"><h3>Numero de tareas</h3></v-col>
              <v-col sm="3"><h3>Numero de bolsas de horas</h3></v-col>
              <v-col sm="2"><h3>Total horas contrato</h3></v-col>
              <v-col sm="2"><h4>Total horas invertidas</h4></v-col>
              <v-col sm="2"><h4>Horas disponibles</h4></v-col>
              <v-col sm="1"><h4>Avisos</h4></v-col>
            </v-row>
        </v-card>

        <v-card class="pa-3 mb-5" color="secondary_variant" tile>
            <v-row>
              <v-col sm="2">{{getLength(servicio.taskList)}}</v-col>
              <v-col sm="3">{{getLength(servicio.hourBagsList)}}</v-col>
              <v-col sm="2">{{servicio.totalHours}}</v-col>
              <v-col sm="2">{{servicio.totalInvested}}</v-col>
              <v-col sm="2">{{servicio.availableHours}}</v-col>
              <v-col sm="1">{{getLength(servicio.warningList)}}</v-col>
            </v-row>
        </v-card>
      </v-col>
      <v-col cols="3">
        <v-toolbar flat color="secondary" class="mb-2">
          <v-toolbar-title>
            <h2>Avisos</h2>
          </v-toolbar-title>
        </v-toolbar>


        <v-card color="secondary_variant" class="pa-3" tile>
          <ul v-if="getLength(servicio.warningList) != 0">
            <li v-for="item in servicio.warningList" :key="item">
              {{item}}
            </li>
          </ul>

          <p v-else> <i>No hay avisos</i> </p>
        </v-card>
        
      </v-col>
    </v-row>
    <v-row>
      <v-col sm="7">
        <v-data-table
          :headers="headersTareas"
          :items="tareas"
          class="elevation-5 secondary_variant"
          @click:row="evento"
        >
            <template v-slot:top>
              <v-toolbar flat class="blanco--text" color="primary_variant">

                <v-toolbar-title><h4>Tareas</h4></v-toolbar-title>

                <v-spacer></v-spacer>

                <v-btn icon color="blanco" title="Ir a las tareas de este Servicio" @click="goToPathFilter('/tareas')" :disabled="disabledTask">
                  <v-icon style="transform: scaleX(-1)">mdi-application-import</v-icon>
                </v-btn>

              </v-toolbar> 

              <v-dialog v-model="dialog" width="500">

                <v-card color="secondary_variant">
                  <v-card-title class="headline primary_variant mb-3">
                    Descripción de la tarea
                  </v-card-title>

                  <v-card-text>
                    {{fila.description || "Esta tarea no tiene descripción."}}
                  </v-card-text>

                  <v-divider></v-divider>

                  <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="primary" text @click="dialog = false">
                      Cerrar
                    </v-btn>
                  </v-card-actions>
                </v-card>
                      
              </v-dialog>
                      
            </template>
            
            <template v-slot:[`item.priority`]="{ item }">
              <v-chip :color="getColorPriority(item.priority)" text-color="black" outlined label>
                
                <span v-if="item.priority == '3'">Baja</span>
                <span v-else-if="item.priority == '2'">Media</span>
                <span v-else-if="item.priority == '1'">Alta</span>
              </v-chip>
            </template>
            
            <template v-slot:[`item.state`]="{ item }">
              <v-chip :color="getColorState(item.state)" text-color="black" outlined label>
                
                <span v-if="item.state == '1'">Pendiente</span>
                <span v-else-if="item.state == '2'">En curso</span>
                <span v-else-if="item.state == '3'">Finalizado</span>
              </v-chip>
            </template>

            <template v-slot:[`item.description`]="{ item }">
              <span v-if="item.description != ''">{{item.description.substring(0,15)}}...</span>
              <span v-else><i>Sin descripción</i></span>
            </template>

        </v-data-table>
      </v-col>
      <v-col sm="5">
        <v-data-table
          :headers="headersBolsas"
          :items="bolsas"
          class="elevation-5 secondary_variant"
        >
          <template v-slot:top>

            <v-toolbar flat class="blanco--text" color="primary_variant">
              <v-toolbar-title><h4>Bolsas de horas</h4></v-toolbar-title>

                <v-spacer></v-spacer>

              <v-btn icon color="blanco" title="Ir a las bolsas de horas de este Servicio" @click="goToPathFilter('/bolsahoras')" :disabled="disabledBags">
                <v-icon style="transform: scaleX(-1)">mdi-application-import</v-icon>
              </v-btn>

            </v-toolbar>

          </template>
        </v-data-table>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "Detalles",
  components: {},
  data: () => ({
    dialog: false,
    url: "http://localhost:8080/contractBT",
    servicio: {
      id: "",
      name: "",
      beneficiary: "",
      createdAt: "",
      totalHours: "",
      hourBagsList: [],
      taskList: [],
      warningList: []
    },
    fila: {},
    headersTareas: [],
    headersBolsas: [],
    tareas: [],
    bolsas: [],
    sw: false,
  }),
  created() {
    this.initialize();
  },
  computed:{
    disabledTask(){
      return this.servicio.taskList.length != 0 ? false : true;
    },
    disabledBags(){
      return this.servicio.hourBagsList.length != 0 ? false : true;
    }
  },
  methods: {
    evento(value) {
      this.fila = value;
      this.dialog = true;
    },
    getLength(array) {
      return array.length;
    },
    initialize() {
      this.id = localStorage.id;
      this.getFromBd();
      this.setHeaders();
    },
    async getFromBd() {
      let response = await axios.get(this.url + "/" + this.id);

      this.servicio = response.data;

      this.bolsas = this.servicio.hourBagsList;
      this.bolsas = this.bolsas.sort((a,b)=>{
        return a.contractId - b.contractId;
      });

      this.tareas = this.servicio.taskList;
      this.tareas = this.tareas.sort((a,b)=>{
        return a.id - b.id;
      });

    },
    setHeaders() {
      (this.headersTareas = [
        { text: "TITULO", value: "title", class: "primary--text secondary" },
        { text: "FECHA DE CREACIÓN", value: "createdAt", class: "primary--text secondary",},
        { text: "FECHA DE FIN", value: "finishedAt", class: "primary--text secondary",},
        { text: "DESCRIPCIÓN", value: "description", class: "primary--text secondary",},
        { text: "TIEMPO INVERTIDO", value: "investedTime",class: "primary--text secondary",},
        { text: "PRIORIDAD", value: "priority",class: "primary--text secondary",},
        { text: "ESTADO", value: "state", class: "primary--text secondary" },
      ]),
        (this.headersBolsas = [
          { text: "FECHA DE INICIO", value: "initDate", align: "start", class: "primary--text secondary",},
          { text: "FECHA DE FIN", value: "endDate", align: "start", class: "primary--text secondary",},
          { text: "HORAS TOTALES", value: "hours", align: "start", class: "primary--text secondary",},
          { text: "HORAS RESTANTES", value: "remaining", align: "start", class: "primary--text secondary",},
        ]);
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
    goToPath(path) {
      if (this.$router.currentRoute.path != path) this.$router.push(path);
    },
    goToPathFilter(path) {
      localStorage.verPorFiltro = "["+this.servicio.beneficiary+"] "+this.servicio.name;
      if (this.$router.currentRoute.path != path) this.$router.push(path);
    },
  },
};
</script>
