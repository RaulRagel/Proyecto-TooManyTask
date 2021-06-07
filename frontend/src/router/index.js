import Vue from "vue";
import VueRouter from "vue-router";
import Inicio from "../views/Inicio.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Inicio",
    component: Inicio,
  },
  {
    path: "/servicios",
    name: "Servicios",

    component: () => import("../views/Servicios.vue"),
  },
  {
    path: "/tareas",
    name: "Tareas",

    component: () => import("../views/Tareas.vue"),
  },
  {
    path: "/bolsahoras",
    name: "BolsaHoras",

    component: () => import("../views/BolsaHoras.vue"),
  },
  {
    path: "/detalles",
    name: "Detalles",

    component: () => import("../views/Detalles.vue"),
  },
  {
    //Pagina no encontrada
    path: "*",
    name: "pageNotFound",
    component: () => import("../views/PageNotFound.vue"),
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
