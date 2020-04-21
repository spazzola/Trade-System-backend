<template>
  <div class="left-menu">
    <h1>Balanse</h1>
    <hr style="margin-top: 0px;" />

    <h2>Kupcy</h2>
    <ul>
      <li
        v-for="(buyer, index) in buyers"
        v-bind:key="index"
        :style="{color: checkAmount(buyer.currentBalance)}">
        {{buyer.name}} : {{buyer.currentBalance }} &emsp; | &emsp; {{ buyer.currentBalance * 0.23 + buyer.currentBalance }}</li>
    </ul>

    <h2 style="margin-top: 20px;">Sprzedawcy</h2>
    <ul>
      <li
        v-for="(supplier, index) in suppliers"
        v-bind:key="index"
        :style="{color: checkAmount(supplier.currentBalance)}">
        {{supplier.name}} : {{supplier.currentBalance}}</li>
    </ul>
  </div>
</template>

<script>
import axios from './axios-auth';
import store from './store';

export default {
  data() {
    return {
      buyers: [],
      suppliers: []
    };
  },
  created() {
    axios.get("/buyer/getAllWithBalances", {
      headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          }
    }).then(resp => {
      let data = resp.data;
      for (let key in data) {
        const buyer = data[key];
        buyer.id = buyer.id;
        this.buyers.push(buyer);
      }
    }),
    axios.get("/supplier/getAllWithBalances", {
      headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          }
    }).then(resp => {
      let data = resp.data;
      for (let key in data) {
        const supplier = data[key];
        supplier.id = supplier.id;
        this.suppliers.push(supplier);
      }
    });
  },
  methods: {
    checkAmount(value) {
      if (value < 0) {
        return "red";
      } else {
        return "green";
      }
    }
  }
};
</script>

<style scoped>
.left-menu {
  float: left;
  width: 350px;
  height: 790px;
  border: 1px solid black;
  border-radius: 5px;
  margin-left: 20px;
  position: relative;
  overflow: scroll;
  margin-top: -40px;
}

h1 {
  font-weight: bold;
  text-align: center;
  font-size: 25px;
  padding: 15px;
}

h2 {
  text-align: center;
  font-size: 18px;
  font-weight: bold;
}

li {
  margin: 10px;
  border-bottom: 1px solid black;
}
</style>