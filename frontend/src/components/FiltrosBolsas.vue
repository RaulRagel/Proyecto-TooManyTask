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

        <!--Servicio-->
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-account-circle</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>Servicio</v-list-item-title>
            <v-select
              item-text="name"
              item-value="name"
              v-model="filtro.filtroServicio"
              :items="serviciosActivos"
              color="secondary_variant"
              @change="enviarFiltro"
            ></v-select>
          </v-list-item-content>
        </v-list-item>

        <!--Fecha calendario-->
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-calendar-month</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>Fecha</v-list-item-title>

            <calendar @misFechas="recibirFechasDeCalendar" ref="calendarComponent"/>
          </v-list-item-content>
        </v-list-item>

        <!--Horas totales-->
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-av-timer</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>
              <span class="d-inline">Horas totales</span>
              <v-switch
                v-model="filtro.switchTotales"
                class="d-inline"
                @change="enviarFiltro"
              ></v-switch>
            </v-list-item-title>

            <v-text-field class="mr-4 mt-n3" v-model.number="filtro.filtroHorasTotales" :label="masDeMenosDe(filtro.switchTotales)" @change="enviarFiltro"></v-text-field>
          </v-list-item-content>
        </v-list-item>

        <!--Avisos-->
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-alert-outline</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>
              <span class="d-inline">Horas restantes</span>
              <v-switch
                v-model="filtro.switchRestantes"
                class="d-inline"
                @change="enviarFiltro"
              ></v-switch>
            </v-list-item-title>

            <v-text-field class="mr-4 mt-n3" v-model.number="filtro.filtroHorasRestantes" :label="masDeMenosDe(filtro.switchRestantes)" @change="enviarFiltro"></v-text-field>
          </v-list-item-content>
        </v-list-item>
          
      </v-list>
</div>
</template>
<script>
import Calendar from '@/components/Calendar.vue';
import axios from 'axios';
export default {
  name: "FiltrosBolsas",
  components: {
    Calendar,
  },
  data() {
    return {

      serviciosActivos: [
        {name: "-- Mostrar todos --"},
      ],
      filtro:{
        switchTotales: false,
        switchRestantes: false,
        filtroServicio: "-- Mostrar todos --",
        filtroNombre: "",
        filtroHorasTotales: "",
        filtroHorasRestantes: "",
        filtroFechas: {
          inicio: null,
          fin: null,
        },
      },
      filtroDefault:{
        switchTotales: false,
        switchRestantes: false,
        filtroServicio: "-- Mostrar todos --",
        filtroNombre: "",
        filtroHorasTotales: "",
        filtroHorasRestantes: "",
        filtroFechas: {
          inicio: null,
          fin: null,
        },
      },

      urlServicios: "http://localhost:8080/contract",
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
    cargarServicios(){
      axios.get(this.urlServicios)
        .then((respuesta)=>{

          respuesta.data.forEach(contract => {

            this.serviciosActivos.push({
              name: "["+contract.beneficiary+"] "+contract.name
            });

          });
        }
      );
    },
    cargarFiltroLocalStorage(){
      if(localStorage.verPorFiltro){ //si ya tenemos un filtro, lo enviamos
        this.filtro.filtroServicio = localStorage.verPorFiltro;
        //localStorage.verPorFiltro = ""; //lo vaciamos al cargar el snackbar en la tabla 

        this.enviarFiltro();
      }
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
    }
  },
  created(){

    this.cargarServicios();
    this.cargarFiltroLocalStorage();
  }
  
};
</script>