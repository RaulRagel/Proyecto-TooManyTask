<template>
  <div>
      <v-list-item>
        <v-icon>mdi-magnify</v-icon>

        <v-list-item-content>
          <v-list-item-title class="ml-3"><h3>Filtrar por:</h3></v-list-item-title>
        </v-list-item-content>
      </v-list-item>

      <v-divider></v-divider>

      <v-list dense color="secondary_variant">

        <!--Servicio-->
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-account-circle</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>Servicio</v-list-item-title>
            <v-select
              v-model="filtro.filtroServicio"
              :items="serviciosActivos"
              item-text="name"
              item-value="name"
              color="secondary_variant"
              @change="enviarFiltro"
            ></v-select>
          </v-list-item-content>
        </v-list-item>

        <!--Titulo-->
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-card-text-outline</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>Titulo</v-list-item-title>
            <v-text-field class="mr-4 mt-n3" v-model="filtro.filtroTitulo" placeholder="Titulo" @change="enviarFiltro"></v-text-field>
          </v-list-item-content>
        </v-list-item>

        <!--Fecha-->
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-calendar-month</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>Fecha de creación</v-list-item-title>
            <calendar @misFechas="recibirFechasDeCalendar" ref="calendarComponent"/>
          </v-list-item-content>
        </v-list-item>

        <!--Tiempo invertido-->
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-clipboard-text-multiple-outline</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>
              <span class="d-inline">Horas</span>
              <v-switch
                v-model="filtro.switchHoras"
                class="d-inline"
                @change="enviarFiltro"
              ></v-switch>
            </v-list-item-title>

            <v-text-field class="mr-4 mt-n3" v-model.number="filtro.filtroTiempo" :label="masDeMenosDe(filtro.switchHoras)" @change="enviarFiltro"></v-text-field>
          </v-list-item-content>
        </v-list-item>

        <!--Prioridad-->
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-av-timer</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>Prioridad <span>{{ "("+filtro.filtroPrioridad+")" || 'null' }}</span> </v-list-item-title>

            <v-radio-group v-model="filtro.filtroPrioridad" class="ml-2" @change="enviarFiltro">
              <v-radio label="Sin filtro" value="Sin filtro" color="black"></v-radio>
              <v-radio label="Alta" value="1" color="red"></v-radio>
              <v-radio label="Media" value="2" color="orange"></v-radio>
              <v-radio label="Baja" value="3" color="lime"></v-radio>
            </v-radio-group>
          </v-list-item-content>
        </v-list-item>

        <!--Estado-->
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-alert-outline</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>Estado <span>{{ "("+filtro.filtroEstado+")" || 'null' }}</span></v-list-item-title>

            <v-radio-group v-model="filtro.filtroEstado" class="ml-2" @change="enviarFiltro">
              <v-radio label="Sin filtro" value="Sin filtro" color="black"></v-radio>
              <v-radio label="Pendiente" value="1" color="purple"></v-radio>
              <v-radio label="En curso" value="2" color="blue"></v-radio>
              <v-radio label="Finalizado" value="3" color="green"></v-radio>
            </v-radio-group>
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
  name: "FiltrosTareas",
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
        switchHoras: false,
        filtroServicio:"-- Mostrar todos --",
        filtroTitulo: "",
        filtroTiempo: "",
        filtroPrioridad: "Sin filtro",
        filtroEstado: "Sin filtro",
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
      
      this.filtro.switchHoras = false;
      this.filtro.filtroServicio = "-- Mostrar todos --";
      this.filtro.filtroTitulo = "";
      this.filtro.filtroFechas.inicio = null;
      this.filtro.filtroFechas.fin = null;
      this.filtro.filtroTiempo = "";
      this.filtro.filtroPrioridad = "Sin filtro";
      this.filtro.filtroEstado = "Sin filtro";

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
<style>