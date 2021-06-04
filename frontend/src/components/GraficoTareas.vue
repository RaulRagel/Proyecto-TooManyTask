<template>
  <v-container>
    <zingchart v-if="sw" :data="myData" :series="mySeries"></zingchart>
    <zingchart v-else :data="myData2" :series="mySeries2"></zingchart>
  </v-container>
</template>
<!-- 
area
bar
pie
line
bullet
calendar
-->
<script>
import "zingchart/es6";
import zingchartVue from "zingchart-vue";

export default {
  name: "GraficoTareas",
  components: {
    zingchart: zingchartVue,
  },
  props:{
    datosGraficoEstado: {
      values: {
        pendientes: Number,
        finalizadas: Number,
        enCurso: Number,
      },
    },
    datosGraficoPrioridad: {
      values: {
        alta: Number,
        media: Number,
        baja: Number,
      },
    },
    sw: Boolean,
  },
  computed:{
    // graphset:[
    // ]
  },
  created(){
    
  },
  watch:{ //cuando cambia algun componente de datosGrafico, rellenamos datos de mySeries
    datosGraficoEstado:{
      deep: true,
      handler(){

        this.mySeries= [
          { 
            values: [this.datosGraficoEstado.values.pendientes],
            text: "Pendiente",
            backgroundColor: '#8E24AA',
          },
          {
            values: [this.datosGraficoEstado.values.finalizadas],
            text: "Finalizado",
            backgroundColor: '#43A047',
          },
          {
          values: [this.datosGraficoEstado.values.enCurso],
            text: "En curso",
            backgroundColor: '#039BE5',
          }
        ];
      }
    },
    datosGraficoPrioridad:{
      deep: true,
      handler(){
        this.mySeries2= [
          { 
            values: [this.datosGraficoPrioridad.values.alta],
            text: "Alta",
            backgroundColor: '#E53935',
          },
          {
            values: [this.datosGraficoPrioridad.values.media],
            text: "Media",
            backgroundColor: '#FB8C00',
          },
          {
            values: [this.datosGraficoPrioridad.values.baja],
            text: "Baja",
            backgroundColor: '#7CB342',
          }
        ];
      },
    }
  },

  data() {
    return{
      myData:{
        type: "pie3d",
        plot: {
          borderColor: "black",
          borderWidth: 3,
          // slice: 90,
          valueBox: {
            placement: 'out',
            text: '%t\n%npv%',
            fontFamily: "Open Sans",
            fontSize: '18',
            padding: "20 20",
          },
          tooltip: {
            fontSize: '18',
            fontFamily: "Open Sans",
            padding: "5 5",
            text: "%v Tarea/s"
          },
          animation: {
            effect: 2,
            method: 5,
            speed: 900,
            sequence: 1,
          }
        },
        backgroundColor: '#E5E0DB',
        height: '520px',
        //x: '0px',
        //y: '0px',
        // scaleX: {
        //   values: "0:35:5",
        //   format: "Número %v",
        // },
        title: {
          fontColor: "black",
          text: 'Estado de las tareas',
          align: "left",
          offsetX: 10,
          fontFamily: "Open Sans",
          fontSize: 25
        },
        subtitle: {
          offsetX: 10,
          offsetY: 10,
          fontColor: "black",
          fontFamily: "Open Sans",
          fontSize: "16",
          text: "Cada porción representa el nº de tareas con ese estado",
          align: "left",
        },
      },
      myData2:{
        type: "pie3d",
        plot: {
          borderColor: "black",
          borderWidth: 3,
          // slice: 90,
          valueBox: {
            placement: 'out',
            text: '%t\n%npv%',
            fontFamily: "Open Sans",
            fontSize: '18',
            padding: "20 20",
          },
          tooltip: {
            fontSize: '18',
            fontFamily: "Open Sans",
            padding: "5 5",
            text: "%v Tarea/s"
          },
          animation: {
            effect: 2,
            method: 5,
            speed: 900,
            sequence: 1,
          }
        },
        backgroundColor: '#E5E0DB',
        height: '520px',
        //x: '0px',
        //y: '0px',
        // scaleX: {
        //   values: "0:35:5",
        //   format: "Número %v",
        // },
        title: {
          fontColor: "black",
          text: 'Prioridad de las tareas',
          align: "left",
          offsetX: 10,
          fontFamily: "Open Sans",
          fontSize: 25
        },
        subtitle: {
          offsetX: 10,
          offsetY: 10,
          fontColor: "black",
          fontFamily: "Open Sans",
          fontSize: "16",
          text: "Cada porción representa el nº de tareas con esa prioridad",
          align: "left"
        },
      },
      mySeries: [],
      mySeries2: []
    };
  }
};
</script>

