import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import vuetify from "./plugins/vuetify";
import moment from "../node_modules/moment";
import "zingchart/es6";
import zingchartVue from "zingchart-vue";
Vue.config.productionTip = false;

Vue.component("zingchart", zingchartVue);

new Vue({
  router,
  vuetify,
  moment,
  render: (h) => h(App),
}).$mount("#app");
