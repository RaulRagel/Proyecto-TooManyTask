<template>
    <div class="calendars">

        <v-menu
          ref="startMenu"
          :close-on-content-click="false"
          offset-y
          min-width="290px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              :value="mostrar"
              label="Fecha de creación"
              prepend-icon="mdi-calendar-month"
              readonly
              v-bind="attrs"
              v-on="on"
            ></v-text-field>
          </template>

          <v-date-picker v-model="fecha" no-title scrollable locale="es-ES" @change="fechaPulsada">
            <v-spacer></v-spacer>
            <v-btn text color="primary" @click="$refs.startMenu.isActive = false" >
              Cancel
            </v-btn>
          </v-date-picker>
        </v-menu>
  
      </div>
</template>

<script>
import moment from "moment";
export default {
    
  name: "FechaModal",
  props:{
    mostrarFecha: String,
  },
  data: () => ({

    fecha: null,
  }),

  computed:{ 

    mostrar(){
      return this.fecha ? this.formatDate(this.fecha) : this.formatDate(this.mostrarFecha);
    }

  },
  methods:{
    formatDate (fecha) {
      return moment(fecha).format('DD-MM-YYYY');
    },
    limpiayEnvia(){
      this.fecha=null;
      this.$emit("nuevaFecha",this.fecha);
    },
    
    fechaPulsada(){
      this.$refs.startMenu.save(this.fecha);
      this.$emit("nuevaFecha",this.fecha);
    },
  },
};
   
</script>
