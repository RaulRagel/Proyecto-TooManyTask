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
      filtroDefault:{
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

        this.$emit("misFiltros",this.filtro);
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
<style>