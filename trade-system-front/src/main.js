import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import { BootstrapVue, BootstrapVueIcons } from 'bootstrap-vue'
import { routes } from './routes'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import store from './store'
import Vuex from 'vuex'

Vue.use(BootstrapVue)
Vue.use(BootstrapVueIcons)

Vue.use(VueRouter);
Vue.use(Vuex)

const router = new VueRouter({
  routes
});

new Vue({
  el: '#app',
  store,
  router,
  render: h => h(App)
})
