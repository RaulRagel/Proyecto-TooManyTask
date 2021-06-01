<template>
  <v-container>
     <v-toolbar flat class="blanco--text" color="primary_variant mb-5" tile>
      <v-toolbar-title><h2>Gráfico general</h2></v-toolbar-title>
    </v-toolbar>
    
    <grafico :datosGrafico="datosGrafico"></grafico>
  </v-container>
</template>
<script>
import Grafico from '@/components/Grafico.vue';

export default {
  name: "GraficoGeneral",
  components: {
    Grafico
  },
  data() {
    return{
      datosGrafico: {
        titulo: "Todos los datos",
        // valuesX: null,
        // valuesY: null
      }
    };
  }
};
</script>

<!-- 
area
bar
pie
line
bullet
calendar
-->
