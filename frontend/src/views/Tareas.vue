<template>
  <v-sheet color="background" class="overflow-hidden" >
    <v-row>
      <!--FILTROS-->
      <v-col cols="2">

        <v-navigation-drawer absolute permanent color="secondary">
          
          <filtros-tareas @misFiltros="rellenaFiltros" ref="filtro"/>

        </v-navigation-drawer>
    
      </v-col>

      <!--TABLA-->
      <v-col cols="9" class="pt-10">
        
        <tabla-tareas v-bind:headers="headers"/>
      </v-col>
    </v-row>

  </v-sheet>
</template>

<script>
import moment from 'moment';
import FiltrosTareas from "@/components/FiltrosTareas.vue";

import TablaTareas from "@/components/TablaTareas.vue";

export default {
  name: "Tareas",
  components: {
    FiltrosTareas,
    TablaTareas
  },
  data() {
    return {

      filtro:{
        switchHoras: false,
        filtroServicio: "-- Mostrar todos --",
        filtroTitulo: "",
        filtroTiempo: "",
        filtroPrioridad: "Sin filtro",
        filtroEstado: "Sin filtro",
        filtroFechas: {
          inicio: null,
          fin: null,
        },
      },
      headers: [],
    };
  },
  created() {
    this.initialize();
  },
  computed:{
    
  },
  methods:{
    rellenaFiltros(value){

      //console.log(value);

      this.filtro.switchHoras = value.switchHoras,
      this.filtro.filtroServicio = value.filtroServicio;
      this.filtro.filtroTitulo = value.filtroTitulo;
      this.filtro.filtroFechas.inicio = value.filtroFechas.inicio;
      this.filtro.filtroFechas.fin = value.filtroFechas.fin;
      this.filtro.filtroTiempo = value.filtroTiempo;
      this.filtro.filtroPrioridad = value.filtroPrioridad;
      this.filtro.filtroEstado = value.filtroEstado;
    },

    initialize() {

      this.headers = [
        { text: "SERVICIO", value: "contractBN", align: "start", class: "primary--text secondary",
         filter: f => {
           if(this.filtro.filtroServicio == "-- Mostrar todos --"){
             return true;
           }else
            return ( f + '' ).toLowerCase().includes( this.filtro.filtroServicio.toLowerCase() ) 
          }},
        { text: "TITULO", value: "title", class: "primary--text secondary",
         filter: f => { return ( f + '' ).toLowerCase().includes( this.filtro.filtroTitulo.toLowerCase() ) }},
        { text: "DESCRIPCIÓN", value: "description", class: "primary--text secondary",},
        { text: "FECHA DE CREACIÓN", value: "createdAt", class: "primary--text secondary",
         
         filter: value => { 
            if (!this.filtro.filtroFechas.inicio && !this.filtro.filtroFechas.fin){
                return true;

            }else if(!this.filtro.filtroFechas.inicio){

              return (moment(value).isBefore(this.filtro.filtroFechas.fin) || moment(value).isSame(this.filtro.filtroFechas.fin,'day'));
            
            }else if(!this.filtro.filtroFechas.fin){

              return (moment(value).isAfter(this.filtro.filtroFechas.inicio) || moment(value).isSame(this.filtro.filtroFechas.inicio,'day'));

            }else{
              return ((moment(value).isBefore(this.filtro.filtroFechas.fin) || moment(value).isSame(this.filtro.filtroFechas.fin,'day')) && (moment(value).isAfter(this.filtro.filtroFechas.inicio) || moment(value).isSame(this.filtro.filtroFechas.inicio,'day')));
            }
          }
         },
        { text: "FECHA DE FIN", value: "finishedAt", class: "primary--text secondary",},
        { text: "TIEMPO INVERTIDO", value: "investedTime", class: "primary--text secondary",
         filter: value => {

           if(this.filtro.switchHoras){
              if (!this.filtro.filtroTiempo) return true; 
              else return value < parseInt(this.filtro.filtroTiempo);
            }else{
              if (!this.filtro.filtroTiempo) return true; 
              else return value > parseInt(this.filtro.filtroTiempo);
            }
         }},
        { text: "PRIORIDAD", value: "priority", class: "primary--text secondary",
         filter: f => { 
           if(this.filtro.filtroPrioridad == "Sin filtro"){
             return true;
           }else{
             return ( f + '' ).toLowerCase().includes( this.filtro.filtroPrioridad.toLowerCase() ) 
           }
         }},
        { text: "ESTADO", value: "state", class: "primary--text secondary",
         filter: f => { 
            if(this.filtro.filtroEstado == "Sin filtro"){
             return true;
           }else{
             return ( f + '' ).toLowerCase().includes( this.filtro.filtroEstado.toLowerCase() ) 
           }
         }},
        { text: "ACCIONES", value: "actions", sortable: false, class: "primary--text secondary" },
        { text: "FIJOS", value: "pin", sortable: false, align: "start", class: "primary--text secondary"},
      ]
    },
  }
};
</script>

