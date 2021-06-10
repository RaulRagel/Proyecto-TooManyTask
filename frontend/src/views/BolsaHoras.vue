<template>
<div>
  <v-row color="background">
    
    <!--FILTROS-->
    <v-col cols="2">

      <v-navigation-drawer absolute permanent color="secondary">
        
        <filtros-bolsas @misFiltros="rellenaFiltros"/>

      </v-navigation-drawer>

    <!--FILTROS ACTIVOS-->
  
  
    </v-col>

    <!--TABLA-->
    <v-col cols="9" class="pt-10">
      
      <tabla-bolsas v-bind:headers="headers"/>
      
    </v-col>

  </v-row>
</div>
</template>
<script>
import moment from "moment";
import FiltrosBolsas from "@/components/FiltrosBolsas.vue";
import TablaBolsas from "@/components/TablaBolsas.vue";

export default {
  name: "BolsaHoras",
  components: {
     FiltrosBolsas,

     TablaBolsas
  },

  data() {
    return {
      filtro:{
        switchTotales: false,
        switchRestantes: false,
        filtroServicio: "-- Mostrar todos --",
        filtroHorasTotales: "",
        filtroHorasRestantes: "",
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
  methods:{
    rellenaFiltros(value){

      this.filtro.switchTotales = value.switchTotales;
      this.filtro.switchRestantes = value.switchRestantes;
      this.filtro.filtroServicio = value.filtroServicio;
      this.filtro.filtroHorasTotales = value.filtroHorasTotales;
      this.filtro.filtroHorasRestantes = value.filtroHorasRestantes;
      this.filtro.filtroFechas.inicio = value.filtroFechas.inicio;
      this.filtro.filtroFechas.fin = value.filtroFechas.fin;
    },
    initialize() {

      this.headers = [
        { text: "SERVICIO", value: "contractBN", sortable: true, align: "start", class: "primary--text secondary",
          filter: f => { 
            if(this.filtro.filtroServicio != "-- Mostrar todos --"){
              
              return ( f + '' ).toLowerCase().includes( this.filtro.filtroServicio.toLowerCase() );
            }else{

              return true;
            }
              
          }},
        { text: "FECHA DE INICIO", value: "initDate", sortable: true, align: "start", class: "primary--text secondary",

         filter: value => { 
            if (!this.filtro.filtroFechas.inicio){
                return true;
            }else{
              
              if(moment(value).isAfter(this.filtro.filtroFechas.inicio) || moment(value).isSame(this.filtro.filtroFechas.inicio,'day')){
                return true;
              }
            }
          }
         },
        { text: "FECHA DE FIN", value: "endDate", sortable: true, align: "start", class: "primary--text secondary",
         
         filter: value => { 
            if (!this.filtro.filtroFechas.fin){
                return true;
            }else{
              
              if(moment(value).isBefore(this.filtro.filtroFechas.fin) || moment(value).isSame(this.filtro.filtroFechas.fin,'day')){
                return true;
              }
            }
          }
         },
        { text: "HORAS TOTALES", value: "hours", sortable: true, align: "start", class: "primary--text secondary",
          filter: value => {

            if(this.filtro.switchTotales){
              if (this.filtro.filtroHorasTotales == "") return true; 
              else return value < parseInt(this.filtro.filtroHorasTotales);
            }else{
              if (this.filtro.filtroHorasTotales == "") return true; 
              else return value > parseInt(this.filtro.filtroHorasTotales);
            }

          }
         },
        { text: "HORAS RESTANTES", value: "remaining", sortable: true, align: "start", class: "primary--text secondary", divider: true, 
         filter: value => {

            if(this.filtro.switchRestantes){
              if (this.filtro.filtroHorasRestantes == "") return true; 
              else return value < parseInt(this.filtro.filtroHorasRestantes);
            }else{
              if (this.filtro.filtroHorasRestantes == "") return true; 
              else return value > parseInt(this.filtro.filtroHorasRestantes);
            }

          }
         },
        { text: "ACCIONES", value: "actions", sortable: false, align: "start", class: "primary--text secondary" },
      ]
    },
  }
};
</script>
