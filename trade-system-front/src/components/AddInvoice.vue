<template>
  <div>
    <nav-menu></nav-menu>
    <left-menu></left-menu>
  <div class="add-invoice-content">
    <form>
      <div class="form-group">
        <label for="invoiceNumber">Numer faktury</label>
        <input
          type="text"
          name="invoiceNumber"
          class="form-control"
          v-model="invoice.invoiceNumber"
        />
      </div>

      <div class="form-group">
        <label>Data</label>
        <input type="date" id="date" class="form-control" v-model="invoice.date" />
      </div>

      <div class="form-group">
        <label>Wartość</label>
        <input type="number" id="value" class="form-control" v-model="invoice.value" />
      </div>

      <div class="form-group">
        <label>Zapłacona</label> <br>
        <select v-model="isPaid">
          <option
            v-for="paidOption in paidOptions"
            v-bind:key="paidOption.index"
          >{{ switchBooleanToString(paidOption) }}</option>
        </select>
      </div>

      <div class="form-group">
        <label>Komentarz</label>
        <input type="text" id="comment" class="form-control" v-model="invoice.comment" />
      </div>

      <div class="form-group">
        <label>Wybierz klienta</label> <br>
        <select v-model="selectedBuyer.buyer">
          <option
            v-bind:value="{id: buyer.id, name: buyer.name}"
            v-for="buyer in buyers"
            v-bind:key="buyer.id"
          >{{ buyer.name }}</option>
        </select>
      </div>

      <p style="display: block; margin-left: 50px; font-weight: bold;">lub</p>
      <br />

      <div class="form-group">
        <label>Wybierz kontrahenta</label> <br>
        <select v-model="selectedSupplier.supplier">
          <option
            v-bind:value="{id: supplier.id, name: supplier.name}"
            v-for="supplier in suppliers"
            v-bind:key="supplier.id"
          >{{ supplier.name }}</option>
        </select>
      </div>
      <router-link to="/invoices">
        <button class="btn btn-success btn-sm" @click="addInvoice">Dodaj</button>
      </router-link>
    </form>
  </div>
  </div>
  
</template>

<script>
import axios from '../axios-auth';
import store from '../store';
import NavMenu from '../NavMenu.vue';
import LeftMenu from '../LeftMenu.vue';

export default {
  components: {
    navMenu: NavMenu,
	  leftMenu: LeftMenu
  },
  data() {
    return {
      isPaid: null,
      paidOptions: [false, true],
      invoice: {
        invoiceNumber: "",
        date: "",
        value: 0,
        amountToUse: 0,
        paid: false,
        comment: "",
        buyer: {
          id: null
        },
        supplier: {
          id: null
        }
      },
      buyers: [],
      suppliers: [],

      selectedBuyer: {
        buyer: {
          id: null
        }
      },
      selectedSupplier: {
        supplier: {
          id: null
        }
      }
    };
  },
  beforeCreate() {
    axios.get("/buyer/getAll", {
      headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          }
    }).then(resp => {
      const data = resp.data;
      for (let key in data) {
        const buyer = data[key];
        buyer.id = buyer.id;
        this.buyers.push(buyer);
      }
    }),
      axios.get("/supplier/getAll", { 
        headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          }
      }).then(resp => {
        const data = resp.data;
        for (let key in data) {
          const supplier = data[key];
          supplier.id = supplier.id;
          this.suppliers.push(supplier);
        }
      });
  },
  methods: {
    addInvoice() {
      this.switchPayValueToBoolean()
      this.invoice.paid = this.isPaid;
      this.invoice.amountToUse = this.invoice.value;

      if (this.selectedBuyer.buyer.id != null) {
        this.invoice.buyer.id = this.selectedBuyer.buyer.id;
        this.invoice.supplier = null;
        axios.post("/invoice/create", this.invoice, {
          headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          }
        });
      } else {
        this.invoice.supplier.id = this.selectedSupplier.supplier.id;
        this.invoice.buyer = null;
        axios.post("/invoice/create", this.invoice, {
          headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          }
        })
        .catch((error) => {alert(error.response.data.message)})
      }
    },
    getDate() {
      var currentDate = new Date();
      var currentDateWithFormat = new Date()
        .toJSON()
        .slice(0, 10)
        .replace(/-/g, "/");

      return currentDateWithFormat;
    },
    switchBooleanToString(value) {
      if (value == false) {
        return "Nie";
      } else {
        return "Tak";
      }
    },

    switchPayValueToBoolean() {
      if (this.isPaid == 'Nie') {
        this.isPaid = false
      }
      if (this.isPaid == 'Tak') {
        this.isPaid = true
      }
    }
  }
};
</script>

<style scoped>
.add-invoice-content {
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