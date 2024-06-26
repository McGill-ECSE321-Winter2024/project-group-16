import { createApp } from "vue";
import App from "./App.vue";
import store from "./store";
import router from "./router";
import "./assets/css/nucleo-icons.css";
import "./assets/css/nucleo-svg.css";
import ArgonDashboard from "./argon-dashboard";
import PrimeVue from 'primevue/config';

import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles';
import { createVuetify } from 'vuetify';
import * as components from 'vuetify/components';
import * as directives from 'vuetify/directives';


const vuetify = createVuetify({
  components,
  directives,

})

const appInstance = createApp(App);
appInstance.use(PrimeVue)
appInstance.use(store);
appInstance.use(vuetify);


appInstance.use(router);
appInstance.use(ArgonDashboard);
appInstance.mount("#app");


