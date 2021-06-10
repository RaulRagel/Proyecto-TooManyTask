<template>
  <div>
      <v-list-item >
        <v-icon>mdi-magnify</v-icon>

        <v-list-item-content>
          <v-list-item-title class="ml-3" ><h3>Filtros:</h3></v-list-item-title>
        </v-list-item-content>
      </v-list-item>

      <!-- ITEMS -->
      <v-list dense color="secondary_variant">

      <v-row justify="center" align="center" class="my-1">
        <v-btn text color="primary" @click="limpiayEnvia" :disabled="isDisabled">Limpiar filtros</v-btn>
      </v-row>

      <v-divider></v-divider>


        <!--Beneficiario-->
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-account-circle</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>Beneficiario</v-list-item-title>
            <v-text-field class="mr-4 mt-n3" v-model="filtro.filtroBeneficiario" placeholder="Beneficiario" @change="enviarFiltro"></v-text-field>
          </v-list-item-content>
        </v-list-item>

        <!--Nombre-->
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-card-text-outline</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>Nombre</v-list-item-title>
            <v-text-field class="mr-4 mt-n3" v-model="filtro.filtroNombre" placeholder="Nombre" @change="enviarFiltro"></v-text-field>
          </v-list-item-content>
        </v-list-item>

        <!--Fecha-->
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-calendar-month</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>Fecha</v-list-item-title>

            <calendar @misFechas="recibirFechasDeCalendar" ref="calendarComponent"/>
          </v-list-item-content>
        </v-list-item>

        <!--Tareas-->
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-clipboard-text-multiple-outline</v-icon>
          </v-list-item-icon>

          <v-list-item-content class="pl-2">
            <v-list-item-title>
              <span class="d-inline">Tareas</span>
              <v-switch
                v-model="filtro.switchTareas"
                class="d-inline"
                @change="enviarFiltro"
              ></v-switch>
            </v-list-item-title>

              <v-text-field class="mr-4 mt-n3" v-model.number="filtro.filtroTareas" :label="masDeMenosDe(filtro.switchTareas)" @change="enviarFiltro"></v-text-field>
            
          </v-list-item-content>
        </v-list-item>

        <!--Bolsas de horas-->
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-av-timer</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>
              <span class="d-inline">Bolsas de horas</span>
              <v-switch
                v-model="filtro.switchBolsas"
                class="d-inline"
                @change="enviarFiltro"
              ></v-switch>
            </v-list-item-title>

            <v-text-field class="mr-4 mt-n3" v-model.number="filtro.filtroHoras" :label="masDeMenosDe(filtro.switchBolsas)" @change="enviarFiltro"></v-text-field>
          </v-list-item-content>
        </v-list-item>

        <!--Avisos-->
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-alert-outline</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>
              <span class="d-inline">Avisos</span>
              <v-switch
                v-model="filtro.switchAvisos"
                class="d-inline"
                @change="enviarFiltro"
              ></v-switch>
            </v-list-item-title>

            <v-text-field class="mr-4 mt-n3" v-model.number="filtro.filtroAvisos" :label="masDeMenosDe(filtro.switchAvisos)" @change="enviarFiltro"></v-text-field>

          </v-list-item-content>

        </v-list-item>

        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-alert-decagram-outline</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>Importancia <span>{{ "("+filtro.filtroAvisosImportance+")" || 'null' }}</span></v-list-item-title>

            <v-radio-group v-model="filtro.filtroAvisosImportance" class="ml-2" @change="enviarFiltro">
              <v-radio label="Sin filtro" value="Sin filtro" color="black"></v-radio>
              <v-radio label="Sin avisos" :value="0" color="grey"></v-radio>
              <v-radio label="Baja" :value="1" color="green"></v-radio>
              <v-radio label="Media" :value="2" color="orange"></v-radio>
              <v-radio label="Alta" :value="3" color="red"></v-radio>
            </v-radio-group>
          </v-list-item-content>
        </v-list-item>
          
      </v-list>
</div>
</template>
<script>
import Calendar from '@/components/Calendar.vue';
export default {
  name: "FiltrosServicios",
  components: {
    Calendar,
  },
  data() {
    return {

      filtro:{
        switchTareas: false,
        switchBolsas: false,
        switchAvisos: false,
        filtroBeneficiario: "",
        filtroNombre: "",
        filtroTareas: "",
        filtroHoras: "",
        filtroAvisos: "",
        filtroAvisosImportance: "Sin filtro",
        filtroFechas: {
          inicio: null,
          fin: null,
        },
      },
      filtroDefault:{
        switchTareas: false,
        switchBolsas: false,
        switchAvisos: false,
        filtroBeneficiario: "",
        filtroNombre: "",
        filtroTareas: "",
        filtroHoras: "",
        filtroAvisos: "",
        filtroAvisosImportance: "Sin filtro",
        filtroFechas: {
          inicio: null,
          fin: null,
        },
      },

    };
  },

  computed:{
    isDisabled(){
      return JSON.stringify(this.filtro)===JSON.stringify(this.filtroDefault) ? true : false;
    }
  },
  methods: {
    enviarFiltro(){
      this.$emit("misFiltros",this.filtro);
    },
    recibirFechasDeCalendar(value){
      this.filtro.filtroFechas.inicio = value.inicio;
      this.filtro.filtroFechas.fin = value.fin;

      this.enviarFiltro();
    },
    limpiayEnvia(){

      this.filtro = Object.assign({}, this.filtroDefault);

      this.$refs.calendarComponent.limpiayEnvia(); //referencia al componente Calendar y llama a su método para limpiar

      //this.$emit("misFiltros",this.filtro); //ya no hace falta porque limpiayEnvia llama a recibirFechasCalendar
    },
    masDeMenosDe(sw){
      if(sw){
        return "Menos de";
      }else{
        return "Más de";
      }
    },
  },
  
};
</script>
