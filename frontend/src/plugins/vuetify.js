/* eslint-disable */
import Vue from "vue";
import Vuetify from "vuetify/lib/framework";

Vue.use(Vuetify);

export default new Vuetify({
  theme: {
    themes: {
      light: {
        primary: "#634329", 
        primary_variant: "#7D624D",
        secondary: "#978270", 
        secondary_variant: "#BAACA0",
        on_button: "#CBC0B8",
        background: "#E5E0DB",
        blanco: "#FFFFFF",
        negro: "#000000",
/** ----------------------------------------------------------------------------- **/
        warning: "#FFFF00", 
        accent: "#8c9eff", 
        error: "#D50000",
        
      },
      dark: {
        primary: "#271a10",  
        primary_variant: "#33271f",
        secondary: "#3e342d", 
        secondary_variant: "#4C4037",
        on_button: "#000000",
        background: "#685A4B",
        blanco: "#FFFFFF",
        negro: "#000000",
/** ----------------------------------------------------------------------------- **/
        warning: "#FFFF00", 
        accent: "#8c9eff", 
        error: "#D50000",
      },
    },
  },
});
