import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import { routes } from './routes'
import store from './store'
import Vuex from 'vuex'
import Footer from './company-view/Footer.vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import 'bootstrap/dist/css/bootstrap.min.css'
import VueNumerals from 'vue-numerals';

Vue.use(VueNumerals);

Vue.filter('toCurrency', function (value) {
  if (typeof value !== "number") {
      return value;
  }
  let val = (value/1).toFixed(2).replace('.', ',')
  return val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, " ")
});

Vue.component('spazz-footer', Footer);

Vue.use(VueRouter);
Vue.use(Vuex)

export const router = new VueRouter({
  routes
});

export const eventBus = new Vue();

new Vue({
  el: '#app',
  store,
  router,
  render: h => h(App)
})
