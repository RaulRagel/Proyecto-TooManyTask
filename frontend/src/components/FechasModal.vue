<template>
    <div class="calendars">
      <v-row>
      <v-col sm="4">
        <v-menu
          ref="startMenu"
          :close-on-content-click="false"
          offset-y
          min-width="290px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              :value="mostrar1"
              label="Desde"
              prepend-icon="mdi-calendar-month"
              readonly
              v-bind="attrs"
              v-on="on"
            ></v-text-field>
          </template>

          <v-date-picker v-model="fechas.inicio" no-title scrollable locale="es-ES" @change="fechaPulsada1">
            <v-spacer></v-spacer>
            <v-btn text color="primary" @click="$refs.startMenu.isActive = false" >
              Cancel
            </v-btn>
          </v-date-picker>
        </v-menu>
      </v-col>
      <v-col sm="4">
        <v-menu
          ref="endMenu"
          :close-on-content-click="false"
          offset-y
          min-width="290px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              :value="mostrar2"
              label="Hasta"
              prepend-icon="mdi-calendar-month"
              readonly
              v-bind="attrs"
              v-on="on"
            ></v-text-field>
          </template>

          <v-date-picker v-model="fechas.fin" no-title scrollable locale="es-ES" @change="fechaPulsada2">
            <v-spacer></v-spacer>
            <v-btn text color="primary" @click="$refs.endMenu.isActive = false" >
              Cancel
            </v-btn>
          </v-date-picker>
        </v-menu>
      </v-col>
    </v-row>
    </div>
</template>

<script>
import moment from "moment";
export default {
    
  name: "FechasModal",
  props:{
    mostrarFecha1: String,
    mostrarFecha2: String
  },
  data: () => ({

    fechas:{
      inicio: null,
      fin:null
    }
  }),

  computed:{ 

    mostrar1(){

      return this.mostrarFecha1 ? this.formatDate(this.mostrarFecha1) : "";
    },
    mostrar2(){

      return this.mostrarFecha2 ? this.formatDate(this.mostrarFecha2) : "";
    }

  },
  methods:{
    formatDate (fecha) {
      return moment(fecha).format('DD-MM-YYYY');
    },
    limpiayEnvia(){
      this.fechas.inicio=null;
      this.fechas.fin=null;
      this.$emit("nuevaFecha",this.fechas);
    },
    
    fechaPulsada1(){
      
      this.$refs.startMenu.save(this.fechas.inicio);
      this.$emit("nuevaFechaIni",this.fechas);
    },

    fechaPulsada2(){

      this.$refs.endMenu.save(this.fechas.fin);
      this.$emit("nuevaFechaFin",this.fechas);
    },
  },
};
   
</script>
