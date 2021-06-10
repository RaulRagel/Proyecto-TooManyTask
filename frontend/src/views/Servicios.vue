<template>
  <div>
    <v-row color="background">
      
      <!--FILTROS-->
      <v-col cols="2">

        <v-navigation-drawer absolute permanent color="secondary">
          
          <filtros-servicios @misFiltros="rellenaFiltros"/>

        </v-navigation-drawer>
    
      </v-col>

      <!--TABLA-->
      <v-col cols="9" class="pt-10">
        
        <tabla-servicios v-bind:headers="headers"/>

      </v-col>

    </v-row>
  </div>
</template>

<script>
import moment from "moment";
import FiltrosServicios from "@/components/FiltrosServicios.vue";
import TablaServicios from "@/components/TablaServicios.vue";

export default {
  name: "Servicios",
  components: {
    FiltrosServicios,
    TablaServicios
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
        filtroAvisosPorNumero: true, //intercalamos entre filtrar por número y filtrar por importancia
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

      this.filtro.switchTareas = value.switchTareas,
      this.filtro.switchBolsas = value.switchBolsas,
      this.filtro.switchAvisos = value.switchAvisos,
      this.filtro.filtroFechas.inicio = value.filtroFechas.inicio;
      this.filtro.filtroFechas.fin = value.filtroFechas.fin;
      this.filtro.filtroBeneficiario = value.filtroBeneficiario;
      this.filtro.filtroNombre = value.filtroNombre;
      this.filtro.filtroTareas = value.filtroTareas;
      this.filtro.filtroHoras = value.filtroHoras;
      this.filtro.filtroAvisos = value.filtroAvisos;
      this.filtro.filtroAvisosImportance = value.filtroAvisosImportance;
      this.filtro.filtroAvisosPorNumero = value.filtroAvisosPorNumero;

    },

    initialize() {

      this.headers = [
       
        { text: "BENEFICIARIO", value: "beneficiary", sortable: true, align: "start", class: "primary--text secondary",
          filter: f => { 
            if(this.filtro.filtroBeneficiario != ""){
              
              return ( f + '' ).toLowerCase().includes( this.filtro.filtroBeneficiario.toLowerCase() );
            }else{

              return true;
            }
              
          }},
        { text: "NOMBRE", value: "name", sortable: true, align: "start", class: "primary--text secondary",
          filter: f => { 
            if(this.filtro.filtroNombre != ""){
              
              return ( f + '' ).toLowerCase().includes( this.filtro.filtroNombre.toLowerCase() );
            }else{

              return true;
            }
              
          }},
        { text: "FECHA DE CREACIÓN", value: "createdAt", sortable: true, align: "start", class: "primary--text secondary", divider: true,
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
        { text: "TAREAS", value: "tasks", sortable: true, align: "center", class: "primary--text secondary", width:100, 
          filter: value => {
            
            if(this.filtro.switchTareas){
              if (this.filtro.filtroTareas == "") return true; 
              else return value < parseInt(this.filtro.filtroTareas);
            }else{
              if (this.filtro.filtroTareas == "") return true; 
              else return value > parseInt(this.filtro.filtroTareas);
            }
        }
         },
        { text: "BOLSAS DE HORAS", value: "hourBags", sortable: true, align: "center", class: "primary--text secondary", width:200, divider: true,
         filter: value => {
            if(this.filtro.switchBolsas){
              if (this.filtro.filtroHoras == "") return true; 
              else return value < parseInt(this.filtro.filtroHoras);
            }else{
              if (this.filtro.filtroHoras == "") return true; 
              else return value > parseInt(this.filtro.filtroHoras);
            }
          }
         },
        { text: "AVISOS", value: "warnings", sortable: true, align: "center", class: "primary--text secondary", width:100, 
          filter: value => {

            if(this.filtro.switchAvisos){
              if (this.filtro.filtroAvisos == "") return true; 
              else return value < parseInt(this.filtro.filtroAvisos);
            }else{
              if (this.filtro.filtroAvisos == "") return true; 
              else return value > parseInt(this.filtro.filtroAvisos);
            }
          }
        },
        { text: "!", value: "warningImportance", sortable: true, align: "center", class: "primary--text secondary",  width:10, divider: true, 
          filter: value => {
              
            if(this.filtro.filtroAvisosImportance == "Sin filtro"){
              return true;
            }else{
              return ( value ) == ( this.filtro.filtroAvisosImportance ) ? true : false;
            }
          }
        },
        { text: "ACCIONES", value: "actions", sortable: false, align: "center", class: "primary--text secondary", divider: true, },
        { text: "FIJADOS", value: "pin", sortable: true, align: "center", class: "primary--text secondary",  width:10},
      ]
    },
  }
};
</script>
