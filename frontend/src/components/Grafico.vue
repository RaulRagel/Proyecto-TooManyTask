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
  name: "Grafico",
  components: {
    zingchart: zingchartVue,
  },
  props:{
    datosGrafico: {
      titulo: String,
      // valuesX: Array,
      // valuesY: Array
    }
  },
  computed:{
    // graphset:[
    // ]
  },
  data() {
    return{
      myData:{
        type: 'area',
        backgroundColor: '#BAACA0',
        height: '420px',
        x: '0px',
        y: '0px',
        scaleX: {
          values: "0:35:5",
          format: "Número %v",
        },
        title: {
          text: this.datosGrafico.titulo,
        }
      },
      
      mySeries: [
        { 
          values: [
            [0,20],
            [3,40],
            [9,25],
            [11,50],
            [30,15],
            [31,45],
            [34,93],
            [35,34]
          ],
          backgroundColor: '#634329 white',
          lineColor: '#634329',
          lineWidth: '3px'
        }
      ],
    };
  }
};
</script>
