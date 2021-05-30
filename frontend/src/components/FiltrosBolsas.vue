<template>
  <div>
      <v-list-item>
        <v-icon>mdi-magnify</v-icon>

        <v-list-item-content>
          <v-list-item-title class="ml-3"><h3>Filtrar por:</h3></v-list-item-title>
        </v-list-item-content>
      </v-list-item>

      <v-divider></v-divider>

      <v-list dense color="secondary_variant" >

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

      <div align="center" class="py-3">
        <v-btn color="primary_variant" elevation="3" @click="limpiayEnvia">Limpiar todos</v-btn>
      </div>
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
      drawer: null,
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

      urlServicios: "http://localhost:8080/contract",
    };
  },
  methods: {
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
        localStorage.verPorFiltro = "";

        this.$emit("misFiltros",this.filtro);
      }
    },
    recibirFechasDeCalendar(value){
      this.filtro.filtroFechas.inicio = value.inicio;
      this.filtro.filtroFechas.fin = value.fin;

      this.$emit("misFiltros",this.filtro);
    },
    enviarFiltro(){
      this.$emit("misFiltros",this.filtro);
    },
    limpiayEnvia(){
      
      this.filtro.switchTotales = false;
      this.filtro.switchRestantes = false;
      this.filtro.filtroFechas.inicio = "";
      this.filtro.filtroFechas.fin = "";
      this.filtro.filtroServicio = "-- Mostrar todos --";
      this.filtro.filtroHorasTotales = "";
      this.filtro.filtroHorasRestantes = "";
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