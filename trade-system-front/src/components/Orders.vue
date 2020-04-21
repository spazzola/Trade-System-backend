<template>
<div>
  <nav-menu></nav-menu>
  <left-menu></left-menu>
  <div class="orders-content">
      <router-link to="/addOrder"> <button class="btn btn-success btn-sm">Dodaj wysyłke</button></router-link>

      <select
      style="height: 28px; margin-left: 100px;"
      id="year"
      @change="onChangeYear($event)"
      :selected:="year"
      >
      <option value="2020">2020</option>
      <option value="2021">2021</option>
      <option value="2022">2022</option>
      <option value="2023">2023</option>
      <option value="2024">2024</option>
      <option value="2025">2025</option>
      <option value="2026">2026</option>
      <option value="2027">2027</option>
      <option value="2028">2028</option>
      <option value="2029">2029</option>
      <option value="2030">2030</option>
    </select>

    <select style="height: 28px;" id="month" @change="onChangeMonth($event)">
      <option value="1">Styczeń</option>
      <option value="2">Luty</option>
      <option value="3">Marzec</option>
      <option value="4">Kwiecień</option>
      <option value="5">Maj</option>
      <option value="6">Czerwiec</option>
      <option value="7">Lipiec</option>
      <option value="8">Sierpień</option>
      <option value="9">Wrzesien</option>
      <option value="10">Październik</option>
      <option value="11">Listopad</option>
      <option value="12">Grudzień</option>
    </select>

    <button class="btn btn-success btn-sm" @click="refresh">Odśwież</button>
    <table class="table table-striped">
      <thead>
        <tr>
          <th scope="col">Data</th>
          <th scope="col">Dodaci list</th>
          <th scope="col">Sortyment</th>
          <th scope="col">Ilość m3</th>
          <th scope="col">Kupiec</th>
          <th scope="col">Kupiec suma</th>
          <th scope="col">Dostawca</th>
          <th scope="col">Dostawca suma</th>
          <th scope="col">Komentarz systemu</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(order, index) in orders.slice().reverse()" v-bind:key="index">
            <td scope="row">{{ order.date }}</td>
            <td scope="row">{{ order.orderDetails[0].transportNumber }}</td>
            <td scope="row">{{ order.orderDetails[0].product.product}}</td>
             <td scope="row">{{ order.orderDetails[0].quantity | numeralFormat('0,0[.]00') }}</td>
            <td scope="row">{{ order.buyer.name }}</td>
            <td scope="row">{{ order.orderDetails[0].buyerSum | numeralFormat('0,0[.]00') }}</td>
            <td scope="row">{{ order.supplier.name }}</td>
            <td scope="row">{{ order.orderDetails[0].supplierSum | numeralFormat('0,0[.]00') }}</td>
            <td scope="row">{{ order.orderDetails[0].orderComment.systemComment}}</td>           
        </tr>
      </tbody>
    </table>
  </div>
  </div>
</template>

<script>
import axios from '../axios-auth';
import store from '../store'
import NavMenu from '../NavMenu.vue';
import LeftMenu from '../LeftMenu.vue';

export default {
  components: {
    navMenu: NavMenu,
	  leftMenu: LeftMenu
  },
  data() {
    return {
      month: "",
      year: "",
      orders: []
    };
  },
  created() {
    let currentDate = this.getDate();
    this.year = currentDate.slice(0, 4);
    this.month = currentDate.slice(5, 7);
    axios.get("/order/getMonthOrders", {
          headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          },
          params: {
            year: this.year,
            month: this.month
          }
        })
        .then(resp => {
          //this.invoices = [];
          const data = resp.data;
          for (let key in data) {
            const order = data[key];
            order.id = order.id;
            this.orders.push(order);
          }
        });
  },
  methods: {
    onChangeMonth(event) {
      this.month = event.target.value;
    },
    onChangeYear(event) {
      this.year = event.target.value;
    },
    refresh() {
      axios.get("/order/getMonthOrders", {
          headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          },
          params: {
            year: this.year,
            month: this.month
          }
        })
        .then(resp => {
          this.orders = [];
          const data = resp.data;
          for (let key in data) {
            const order = data[key];
            order.id = order.id;
            this.orders.push(order);
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
    }
  }
}
</script>

<style scoped>
td {
  overflow: scroll;
  white-space: nowrap;
}

table {
  width: 1900px;
}

.orders-content {
  position: relative;
  width: 70%;
  height: 750px;
  margin-left: 400px;
  border: 1px solid black;
  border-radius: 5px;
  overflow: scroll;
}

li {
  width: 2500px;
  height: 30px;
  padding: 5px;
  overflow: auto;
  position: relative;
  font-size: 19px;
  margin-top: 1px;
  border: 1px solid black;
}

.grey-background {
  background-color: rgb(184, 183, 183);
}

.white-background {
  background-color: white;
}

span {
  font-weight: bold;
}

.btn {
  background-color: green;
  border: none;
}
</style>