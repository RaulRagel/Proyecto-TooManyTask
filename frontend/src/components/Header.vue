<template>
  <v-app-bar app color="primary" class="justify-space-between blanco--text">
    <div class="d-flex align-center">
      <a @click="goToPath('/')" rel="Home">
        <v-img
          alt="Next Logo"
          class="shrink mr-2"
          contain
          src="@/assets/logo_next.png"
          transition="scale-transition"
          width="40"
        />
      </a>
      <h1 class="mr-2">Too Many Tasks</h1>
    </div>
    <!-- <h1 class="ml-5">{{ this.$router.currentRoute.meta.title }}</h1> -->
    <v-spacer></v-spacer>
    <v-btn @click="goToPath('/servicios')" color=" on_button" plain
      >Servicios</v-btn
    >
    <v-btn @click="goToPath('/tareas')" color=" on_button" plain>Tareas</v-btn>
    <v-btn @click="goToPath('/bolsahoras')" color=" on_button" plain
      >Bolsa de horas</v-btn
    >
    <v-btn @click="cambiarTema" color=" on_button" plain>Cambiar tema</v-btn>
    <!--<v-icon class="mr-2">mdi-view-list</v-icon>-->
  </v-app-bar>
</template>

<script>
export default {
  name: "Header",
  methods: {
    goToPath(path) {
      if (this.$router.currentRoute.path != path) this.$router.push(path);
    },
    cambiarTema() {
      this.$vuetify.theme.dark = !this.$vuetify.theme.dark;
    },
  },
};
</script>
