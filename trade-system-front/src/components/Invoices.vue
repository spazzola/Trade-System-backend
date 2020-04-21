<template>

  <div>
    <nav-menu></nav-menu>
    <left-menu></left-menu>
    <div class="invoices-content" style="overflow-x:auto;">
      <router-link to="/addInvoice">
        <button class="btn btn-success btn-sm">Dodaj fakture</button>
      </router-link>
      <router-link to="/payInvoice">
        <button class="btn btn-success btn-sm">Zapłać</button>
      </router-link>

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

      <button
        class="btn btn-success btn-sm"
        style="margin-left: 457.5px;"
        @click="transferInvoices"
        >Przenieś faktury na następny miesiąc</button>

      <table class="table table-striped">
        <thead>
          <tr>
            <th scope="col">Id</th>
            <th scope="col">Kupiec</th>
            <th scope="col">Dostawca</th>
            <th scope="col">Nr FV</th>
            <th scope="col">Data</th>
            <th scope="col">Wartość</th>
            <th scope="col">Wartość do uzycia</th>
            <th scope="col">Uzyta</th>
            <th scope="col">Zaplacona</th>
            <th scope="col">Komentarz</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(invoice, index) in invoices.slice().reverse()" v-bind:key="index">
            <td scope="row">{{ invoice.id }}</td>
            <td scope="row" v-if="invoice.buyer != null">{{invoice.buyer.name}}</td>
            <td scope="row" v-else>-----------</td>
            <td scope="row" v-if="invoice.supplier != null">{{invoice.supplier.name}}</td>
            <td scope="row" v-else>-----------</td>
            <td scope="row">{{ invoice.invoiceNumber }}</td>
            <td scope="row">{{ invoice.date }}</td>
            <td scope="row">{{ invoice.value | numeralFormat('0,0[.]00') }}</td>
            <td scope="row">{{ invoice.amountToUse | numeralFormat('0,0[.]00') }}</td>
            <td scope="row">{{ switchBooleanToString(invoice.used) }}</td>
            <td
              scope="row"
              :style="{color: checkPaid(invoice.paid)}"
            >{{ switchBooleanToString(invoice.paid) }}</td>
            <td scope="row">
              <div style="width: 1000px;">{{invoice.comment}}</div>
            </td>
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
      invoices: []
    };
  },
  created() {
    let currentDate = this.getDate();
    this.year = currentDate.slice(0, 4);
    this.month = currentDate.slice(5, 7);
    axios.get("/invoice/getMonthInvoices", {
          headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          },
          params: {
            year: this.year,
            month: this.month
          }
        })
        .then(resp => {
          this.invoices = [];
          const data = resp.data;
          for (let key in data) {
            const invoice = data[key];
            invoice.id = invoice.id;
            this.invoices.push(invoice);
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
      axios.get("/invoice/getMonthInvoices", {
          headers : {
            'Authorization': 'Bearer ' + store.state.jwt
          },
          params: {
            year: this.year,
            month: this.month
          }
        })
        .then(resp => {
          this.invoices = [];
          const data = resp.data;
          for (let key in data) {
            const invoice = data[key];
            invoice.id = invoice.id;
            this.invoices.push(invoice);
          }
        });
    },
    checkPaid(value) {
      if (value == false) {
        return "red";
      } else {
        return "green";
      }
    },
    switchBooleanToString(value) {
      if (value == false) {
        return "Nie";
      } else {
        return "Tak";
      }
    },
    getDate() {
      let currentDate = new Date();
      let currentDateWithFormat = new Date()
        .toJSON()
        .slice(0, 10)
        .replace(/-/g, "/");

      return currentDateWithFormat;
    },
    transferInvoices() {
      if (this.checkIfSureToTransfer()) {
        let date = this.getDate();
        const params = new URLSearchParams();
        params.append("localDate", date);
        axios.post("/invoice/transfer", params, {
          headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          },
        });
      }
    },
    checkIfSureToTransfer() {
      if (confirm("Czy napewno chcesz przenieść faktury na następny miesiąc?")) {
        return true;
      } else {
        return false;
      }
    }
  }
};
</script>

<style scoped>
.btn {
  background-color: green;
  border: none;
}

.btn:active {
  background-color: green;
  border: none;
  box-shadow: green;
}

table {
  width: 1900px;
}

.invoices-content {
  position: relative;
  width: 70%;
  height: 750px;
  margin-left: 400px;
  border: 1px solid black;
  border-radius: 5px;
}
</style>