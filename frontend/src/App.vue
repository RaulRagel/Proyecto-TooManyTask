<template>
  <v-app>
    <Header />
    <v-main>
      <router-view />
    </v-main>
    <Footer />
  </v-app>
</template>

<script>
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
export default {
  name: "App",
  components: {
    Header,
    Footer,
  },
};
</script>

<style>
.theme--light.v-application {
  background-color: var(--v-background-base, #e5e0db) !important;
}

.theme--dark.v-application {
  background-color: var(--v-background-base, #685a4b) !important;
}
</style>
