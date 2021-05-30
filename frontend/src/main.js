import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import vuetify from "./plugins/vuetify";
import moment from "../node_modules/moment";

Vue.config.productionTip = false;

new Vue({
  router,
  vuetify,
  moment,
  render: (h) => h(App),
}).$mount("#app");
