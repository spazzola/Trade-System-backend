<template>
  <div>
    <nav-menu></nav-menu>
    <left-menu></left-menu>
    <div class="costs-content">
      <form style="float: left;">
        <div class="form-group">
          <label>Nazwa</label>
          <input type="text" id="name" class="form-control" v-model="cost.name" />
        </div>

        <div class="form-group">
          <label>Wartość</label>
          <input type="number" id="value" class="form-control" v-model="cost.value" step="0.01" />
        </div>

        <div class="form-group">
          <label>Data (RRRR-MM-DD)</label>
          <input type="date" id="date" class="form-control" v-model="cost.date" style="width: 160px;" />
        </div>
        <button class="btn btn-success btn-primary btn-sm" @click="sendCost">Dodaj koszt</button>
      </form>
      <div class="month-costs">
        <form>
          <button 
          class="btn btn-success btn-primary" 
          style="margin-left: 20px;"
          @click="getCosts">Obecne koszty na {{ this.cost.date.slice(0, 7)}}</button>
          <ul>
              <li 
              v-for="(cost, index) in costs" 
              v-bind:key="index"
              style="max-width: 200px; margin-left: 180px;"
              class="list-group-item">{{ cost.name }} : {{ cost.value }}zł</li>
          </ul>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from '../axios-auth';
import store from '../store';
import NavMenu from "../NavMenu.vue";
import LeftMenu from "../LeftMenu.vue";

export default {
  components: {
    navMenu: NavMenu,
	  leftMenu: LeftMenu
  }, 
  data() {
    return {
      costs: [],
      cost: {
        name: "",
        value: 0,
        date: ""
      }
    };
  },
  methods: {
    sendCost() {
      axios.post("/cost/create", this.cost, {
        headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          }
      });
    },
    getDate() {
      let currentDate = new Date();
      let currentDateWithFormat = new Date()
        .toJSON()
        .slice(0, 10)
        .replace(/-/g, "/");

      return currentDateWithFormat;
    },
    getCosts() {
        this.costs = []
        axios.get("/cost/getMonthCosts", {
          headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          },
          params: {
            localDate: this.cost.date
          }
        })
        .then(resp => {
          const data = resp.data;
          for (let key in data) {
            const cost = data[key];
            cost.id = cost.id;
            this.costs.push(cost);
          }
        });
    }
  }
};
</script>

<style>
.costs-content {
  position: relative;
  width: 70%;
  height: 750px;
  margin-left: 400px;
  border: 1px solid black;
  border-radius: 5px;
  padding: 5px;
}

.form-control {
  width: 150px;
  height: 25px;
}
</style>