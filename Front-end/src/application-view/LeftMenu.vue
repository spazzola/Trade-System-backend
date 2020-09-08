<template>
  <div class="left-menu">
    <h1>Balanse</h1>
    <hr style="margin-top: -3%;" />

    <h2 
      @mouseover="hoverBuyers = true, getBuyersTotalPositiveBalance(), getBuyersTotalNegativeBalance()"
      @mouseleave="hoverBuyers = false"
    >Klienci</h2>

    <div class="hoveredBalances">
        <p 
          v-if="hoverBuyers" 
          :style="{color:checkAmount(buyersPositiveBalance)}"
          >{{ buyersPositiveBalance| toCurrency }} 
        </p>
        
        <p v-if="hoverBuyers">|</p>
        
        <p 
          v-if="hoverBuyers" 
          :style="{color:checkAmount(buyersNegativeBalance)}"
          >{{ buyersNegativeBalance| toCurrency }}
        </p>
    </div>  

    <ul>
      <li
        v-for="(buyer, index) in buyers"
        v-bind:key="index"
  
        :style="{color: checkAmount(buyer.currentBalance)}">
        {{buyer.name}} : {{buyer.currentBalance | toCurrency }} &emsp; | &emsp; {{ buyer.currentBalance * 0.23 + buyer.currentBalance  | toCurrency}}</li>
    </ul>

    <h2 
      style="margin-top: 20px;"       
      @mouseover="hoverSuppliers = true, getSuppliersTotalPositiveBalance(), getSuppliersTotalNegativeBalance()"
      @mouseleave="hoverSuppliers = false"
    >Kontrahenci</h2>
    
    <div class="hoveredBalances">
        <p 
          v-if="hoverSuppliers" 
          :style="{color:checkAmount(suppliersPositiveBalance)}"
          >{{ suppliersPositiveBalance| toCurrency }} 
        </p>
        
        <p v-if="hoverSuppliers">|</p>
        
        <p 
          v-if="hoverSuppliers" 
          :style="{color:checkAmount(suppliersNegativeBalance)}"
          >{{ suppliersNegativeBalance| toCurrency }}
        </p>
    
    </div>  
    <ul>
      <li
        v-for="(supplier, index) in suppliers"
        v-bind:key="index"
        :style="{color: checkAmount(supplier.currentBalance)}">
        {{supplier.name}} : {{supplier.currentBalance | toCurrency }}</li>
    </ul>
  </div>
</template>

<script>
import store from '../store'
import axios from '../axios-auth'

 export default {
   data() {
     return {
       buyersPositiveBalance: 0,
       buyersNegativeBalance: 0,
       suppliersPositiveBalance: 0,
       suppliersNegativeBalance: 0,
       buyers: this.$store.state.buyers,
       suppliers: this.$store.state.suppliers,
       hoverBuyers: null,
       hoverSuppliers: null
     }
   },
    beforeCreate() {
     this.$store.commit('getAllBuyers')
     this.$store.commit('getAllSuppliers')
   },
   methods: {
     show() {
       console.log("ehaa")
     },
    checkAmount(value) {
      if (value < 0) {
        return "red";
      } else {
        return "green";
      }
    },
    getBuyersTotalPositiveBalance() {
      axios.get("/invoice/getBuyersPositiveBalance", {
        headers: {
          'Authorization': 'Bearer ' + this.$store.state.jwt
        }
      }).then(resp => this.buyersPositiveBalance = resp.data)
    },
    getBuyersTotalNegativeBalance() {
      axios.get("/invoice/getBuyersNegativeBalance", {
        headers: {
          'Authorization': 'Bearer ' + this.$store.state.jwt
        }
      }).then(resp => this.buyersNegativeBalance = resp.data)
    },
    getSuppliersTotalPositiveBalance() {
      axios.get("/invoice/getSuppliersPositiveBalance", {
        headers: {
          'Authorization': 'Bearer ' + this.$store.state.jwt
        }
      }).then(resp => this.suppliersPositiveBalance = resp.data)
    },
    getSuppliersTotalNegativeBalance() {
      axios.get("/invoice/getSuppliersNegativeBalance", {
        headers: {
          'Authorization': 'Bearer ' + this.$store.state.jwt
        }
      }).then(resp => this.suppliersNegativeBalance = resp.data)
    }
  }
}
</script>

<style scoped>
.left-menu {
  float: left;
  width: 23%;
  height: 835px;
  border: 1px solid black;
  border-radius: 5px;
  margin-left: 2%;
  position: relative;
  overflow: scroll;

  background-color: white;
  opacity: 0.8;
}

h1 {
  font-weight: bold;
  text-align: center;
  font-size: 1.3vw;
  padding: 15px;
}

h2 {
  text-align: center;
  font-size: 1vw;
  font-weight: bold;
}

li {
  margin: 10px;
  border-bottom: 1px solid black;
  font-size: 0.9vw;
}

ul {
  margin-left: -10%;
  list-style-type: none;
}

.hoveredBalances {
  text-align: center;
}

p {
  display: inline-block;
  margin-left: 5%;
}
</style>