<template>
    <div class="calendars">
        <!--calendario incio -->
        <v-menu
          ref="startMenu"
          :close-on-content-click="false"
          offset-y
          min-width="290px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              :value="fechaInicio"
              label="Desde"
              prepend-icon="mdi-calendar-arrow-right"
              readonly
              v-bind="attrs"
              v-on="on"
            ></v-text-field>
          </template>

          <v-date-picker v-model="fechas.inicio" no-title scrollable locale="es-ES" @change="okIni">
            <v-spacer></v-spacer>
            <v-btn text color="primary" @click="$refs.startMenu.isActive = false" >
              Cancel
            </v-btn>
          </v-date-picker>
        </v-menu>

        <!-- Calendario fin -->
        <v-menu
          ref="endMenu"
          :close-on-content-click="false"
          offset-y
          min-width="290px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              :value="fechaFin"
              label="Hasta"
              prepend-icon="mdi-calendar-arrow-left"
              readonly
              v-bind="attrs"
              v-on="on"
              
            ></v-text-field>
          </template>
          <v-date-picker v-model="fechas.fin" no-title scrollable locale="es-ES" @change="okFin">
            <v-spacer></v-spacer>
            <v-btn text color="primary" @click="$refs.endMenu.isActive = false">
              Cancel
            </v-btn>
          </v-date-picker>
        </v-menu>
          <v-btn text color="primary" @click="limpiayEnvia">Limpiar</v-btn>
  
      </div>
</template>

<script>
import moment from "moment";
export default {
    
  name: "Calendar",
  data: () => ({

    fechas:{
      inicio: null,
      fin:null
    }
  }),

  computed:{ 
    fechaInicio () {
      
      return this.fechas.inicio ? moment(this.fechas.inicio).format('DD-MM-YYYY') : '';
    },
    fechaFin () {

      return this.fechas.fin ? moment(this.fechas.fin).format('DD-MM-YYYY') : '';
    },
  },
  methods:{
    limpiayEnvia(){
      this.fechas.inicio=null;
      this.fechas.fin=null;
      this.$emit("misFechas",this.fechas);
    },
    
    okIni(){
      this.$refs.startMenu.save(this.fechas.inicio);
      this.$emit("misFechas",this.fechas);
    },
    okFin(){
      this.$refs.endMenu.save(this.fechas.fin);
      this.$emit("misFechas",this.fechas);
    },
  },
};
   
</script>
