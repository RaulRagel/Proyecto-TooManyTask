<template>
  <v-container>
    <zingchart :data="myData" :series="mySeries"></zingchart>
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
  name: "GraficoBolsas",
  components: {
    zingchart: zingchartVue,
  },
  props:{
    datosGrafico: {
      values: {
        arrTotales: Array,
        arrRestantes: Array,
        arrFechas: Array,
      },
    },
  },
  methods:{
  },
  watch:{ //cuando cambia algun componente de datosGrafico, rellenamos datos de mySeries
    datosGrafico:{
      deep: true,
      handler(){
        this.mySeries= [
          {
            type: "bar",
            text: "Totales",
            values: this.datosGrafico.values.arrTotales,
            backgroundColor:"#80CBC4",
            borderColor: "black",
            borderWidth: 3,
            labels: this.datosGrafico.values.arrFechas,
          },
          {
            type: "bar",
            text: "Restantes",
            values: this.datosGrafico.values.arrRestantes,
            backgroundColor:"#00796B",
            borderColor: "black",
            borderWidth: 3,
          },
        ];

        //TODO No podemos asignar labels desde aqui
        //this.myData.scaleX.values = this.datosGrafico.values.arrFechas;
        //console.log(this.datosGrafico.values.arrFechas);
      }
    },
  },
//text: '%t\n%npv%',
  data() {
    return{
      myData:{
        type: "mixed",
        legend: true,
        plot: {
          barsOverlap: '100%',
          hoverState: {
            visible: false
          },
          valueBox: {
            text: "",
            fontFamily: "Open Sans",
            fontSize: '18',
            padding: "20 20",
            'font-color': "black",
          },
          tooltip: {
            fontSize: '18',
            fontFamily: "Open Sans",
            padding: "5 5",
            text: "%v hora/s %t"
          },
          animation: {
            effect: 2,
            method: 5,
            speed: 900,
            sequence: 1,
          }
        },
        backgroundColor: '#E5E0DB',
        height: '500px',
        title: {
          offsetX: 10,
          fontColor: "black",
          text: 'Bolsas de horas',
          align: "left",
          fontFamily: "Open Sans",
          fontSize: 25
        },
        subtitle: {
          offsetX: 10,
          offsetY: 10,
          fontColor: "black",
          fontFamily: "Open Sans",
          fontSize: "16",
          text: "",
          align: "left",
        },
      },
      mySeries: [],
    };
  }
};
</script>

