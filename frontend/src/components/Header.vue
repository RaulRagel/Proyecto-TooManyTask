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

    <v-spacer></v-spacer>

    <v-btn @click="goToPath('/servicios')" color=" on_button" text>Servicios</v-btn>
    <v-btn @click="goToPath('/tareas')" color=" on_button" text>Tareas</v-btn>
    <v-btn @click="goToPath('/bolsahoras')" color=" on_button" text>Bolsa de horas</v-btn>
  </v-app-bar>
</template>

<script>
export default {
  name: "Header",
  methods: {
    goToPath(path) {
      if (this.$router.currentRoute.path != path) this.$router.push(path);
    },
  },
};
</script>
