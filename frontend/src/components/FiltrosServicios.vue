<template>
  <div>
      <v-list-item >
        <v-icon>mdi-magnify</v-icon>

        <v-list-item-content>
          <v-list-item-title class="ml-3"><h3>Filtrar por:</h3></v-list-item-title>
        </v-list-item-content>
      </v-list-item>

      <v-divider></v-divider>

      <v-list dense color="secondary_variant">

        <!--Beneficiario-->
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-account-circle</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>Beneficiario</v-list-item-title>
            <v-text-field class="mr-4 mt-n3" v-model="filtro.filtroBeneficiario" placeholder="Beneficiario" @change="enviarFiltro"></v-text-field>
          </v-list-item-content>
        </v-list-item>

        <!--Nombre-->
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-card-text-outline</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>Nombre</v-list-item-title>
            <v-text-field class="mr-4 mt-n3" v-model="filtro.filtroNombre" placeholder="Nombre" @change="enviarFiltro"></v-text-field>
          </v-list-item-content>
        </v-list-item>

        <!--Fecha-->
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-calendar-month</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>Fecha</v-list-item-title>

            <calendar @misFechas="recibirFechasDeCalendar" ref="calendarComponent"/>
          </v-list-item-content>
        </v-list-item>

        <!--Tareas-->
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-clipboard-text-multiple-outline</v-icon>
          </v-list-item-icon>

          <v-list-item-content class="pl-2">
            <v-list-item-title>
              <span class="d-inline">Tareas</span>
              <v-switch
                v-model="filtro.switchTareas"
                class="d-inline"
                @change="enviarFiltro"
              ></v-switch>
            </v-list-item-title>

              <v-text-field class="mr-4 mt-n3" v-model.number="filtro.filtroTareas" :label="masDeMenosDe(filtro.switchTareas)" @change="enviarFiltro"></v-text-field>
            
          </v-list-item-content>
        </v-list-item>

        <!--Bolsas de horas-->
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-av-timer</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>
              <span class="d-inline">Bolsas de horas</span>
              <v-switch
                v-model="filtro.switchBolsas"
                class="d-inline"
                @change="enviarFiltro"
              ></v-switch>
            </v-list-item-title>

            <v-text-field class="mr-4 mt-n3" v-model.number="filtro.filtroHoras" :label="masDeMenosDe(filtro.switchBolsas)" @change="enviarFiltro"></v-text-field>
          </v-list-item-content>
        </v-list-item>

        <!--Avisos-->
        <v-list-item>
          <v-list-item-icon>
            <v-icon>mdi-alert-outline</v-icon>
          </v-list-item-icon>

          <v-list-item-content>
            <v-list-item-title>
              <span class="d-inline">Avisos</span>
              <v-switch
                v-model="filtro.switchAvisos"
                class="d-inline"
                @change="enviarFiltro"
              ></v-switch>
            </v-list-item-title>

            <v-text-field class="mr-4 mt-n3" v-model.number="filtro.filtroAvisos" :label="masDeMenosDe(filtro.switchAvisos)" @change="enviarFiltro"></v-text-field>
          </v-list-item-content>
        </v-list-item>
          
      </v-list>

      <div align="center" class="py-3">
        <v-btn color="primary_variant" elevation="3" @click="limpiayEnvia">Limpiar todos</v-btn>
      </div>
</div>
</template>
<script>
import Calendar from '@/components/Calendar.vue';
export default {
  name: "FiltrosServicios",
  components: {
    Calendar,
  },
  data() {
    return {
      drawer: null,

      filtro:{
        switchTareas: false,
        switchBolsas: false,
        switchAvisos: false,
        filtroBeneficiario: "",
        filtroNombre: "",
        filtroTareas: "",
        filtroHoras: "",
        filtroAvisos: "",
        filtroFechas: {
          inicio: null,
          fin: null,
        },
      },

    };
  },
  methods: {
    recibirFechasDeCalendar(value){
      this.filtro.filtroFechas.inicio = value.inicio;
      this.filtro.filtroFechas.fin = value.fin;

      this.$emit("misFiltros",this.filtro);
    },
    enviarFiltro(){
      this.$emit("misFiltros",this.filtro);
    },
    limpiayEnvia(){
      
      this.filtro.switchTareas = false;
      this.filtro.switchBolsas = false;
      this.filtro.switchAvisos = false;
      this.filtro.filtroFechas.inicio = null;
      this.filtro.filtroFechas.fin = null;
      this.filtro.filtroBeneficiario = "";
      this.filtro.filtroNombre = "";
      this.filtro.filtroTareas = "";
      this.filtro.filtroHoras = "";
      this.filtro.filtroAvisos = "";

      this.$refs.calendarComponent.limpiayEnvia(); //referencia al componente Calendar y llama a su método para limpiar

      //this.$emit("misFiltros",this.filtro); //ya no hace falta porque limpiayEnvia llama a recibirFechasCalendar
    },
    masDeMenosDe(sw){
      if(sw){
        return "Menos de";
      }else{
        return "Más de";
      }
    }
  },
  
};
</script>
