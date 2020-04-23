<template>
  <div>
        <nav-menu></nav-menu>
        <left-menu></left-menu>
         <div class="price-content">
    <button class="btn btn-primary btn-success btn-sm" @click="forBuyer">Dla klienta</button>
    <button class="btn btn-primary btn-success btn-sm" @click="forSupplier">Dla kontrahenta</button>
    <button
      class="btn btn-primary btn-success btn-sm"
      style="margin-left: 100px;"
      @click="editBuyer"
    >Edytuj cenę klienta</button>
    <button class="btn btn-primary btn-success btn-sm" @click="editSupplier">Edytuj cenę kontrahenta</button>

    <div v-if="isBuyer">
      <form>
        <div class="form-group">
          <label>Wybierz produkt</label>
          <br />
          <select v-model="price.product">
            <option
              v-bind:value="{id: product.id, name: product.product}"
              v-for="product in products"
              v-bind:key="product.id"
            >{{ product.product }}</option>
          </select>
        </div>

        <div class="form-group">
          <label>Podaj cenę</label>
          <input
            type="number"
            id="price"
            class="form-control"
            style="width: 100px; height: 25px;"
            step="0.01"
            v-model="price.price"
          />
        </div>

        <div class="form-group">
          <label>Wybierz klienta</label>
          <br />
          <select v-model="price.buyer">
            <option
              v-bind:value="{id: buyer.id}"
              v-for="buyer in buyers"
              v-bind:key="buyer.id"
            >{{ buyer.name }}</option>
          </select>
        </div>

        <button class="btn btn-success btn-primary btn-sm" @click="addBuyerPrice">Dodaj</button>
      </form>
    </div>

    <div v-if="isSupplier">
      <form>
        <div class="form-group">
          <label>Wybierz produkt</label>
          <br />
          <select v-model="price.product">
            <option
              v-bind:value="{id: product.id, name: product.product}"
              v-for="product in products"
              v-bind:key="product.id"
            >{{ product.product }}</option>
          </select>
        </div>

        <div class="form-group">
          <label>Podaj cenę</label>
          <input
            type="number"
            id="price"
            class="form-control"
            style="width: 100px; height: 25px;"
            step="0.01"
            v-model="price.price"
          />
        </div>

        <div class="form-group">
          <label>Wybierz kontrahenta</label>
          <br />
          <select v-model="price.supplier">
            <option
              v-bind:value="{id: supplier.id}"
              v-for="supplier in suppliers"
              v-bind:key="supplier.id"
            >{{ supplier.name }}</option>
          </select>
        </div>

        <button class="btn btn-success btn-primary btn-sm" @click="addSupplierPrice">Dodaj</button>
      </form>
    </div>

    <div v-if="isEditBuyer" class="edit-price-content">
      <form>
        <div class="form-group">
          <label>Wybierz klienta</label>
          <select v-model="selectedBuyer.buyer" @change="loadBuyerProducts">
            <option
              v-bind:value="{id: buyer.id, name: buyer.name}"
              v-for="buyer in buyers"
              v-bind:key="buyer.id"
            >{{ buyer.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>Wybierz kontrahenta</label>
          <select v-model="selectedProduct">
            <option
              v-bind:value="{name: product.product}"
              v-for="product in products"
              v-bind:key="product.id"
            >{{ product.product.product }} - {{ product.price }}zł</option>
          </select>
        </div>

        <div class="form-group">
          <label>Podaj nową cenę</label>
          <input
            type="number"
            value="newPriceValue"
            v-model="newPriceValue"
            class="form-control"
            style="width: 100px; height: 25px;"
            step="0.01"
          />
        </div>
        <button class="btn btn-success btn-primary btn-sm" @click="updateBuyerPrice">Zmień cenę</button>
      </form>
    </div>

    <div v-if="isEditSupplier" class="edit-price-content">
      <form>
        <div class="form-group">
          <label>Wybierz kontrahenta</label>
          <select v-model="selectedSupplier.supplier" @change="loadSupplierProducts">
            <option
              v-bind:value="{id: supplier.id, name: supplier.name}"
              v-for="supplier in suppliers"
              v-bind:key="supplier.id"
            >{{ supplier.name }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>Wybierz product</label>
          <select v-model="selectedProduct">
            <option
              v-bind:value="{name: product.product}"
              v-for="product in products"
              v-bind:key="product.id"
            >{{ product.product.product }} - {{ product.price }}zł</option>
          </select>
        </div>

        <div class="form-group">
          <label>Podaj nową cenę</label>
          <input
            type="number"
            value="newPriceValue"
            v-model="newPriceValue"
            class="form-control"
            style="width: 100px; height: 25px;"
            step="0.01"
          />
        </div>
        <button class="btn btn-success btn-primary btn-sm" @click="updateSupplierPrice">Zmień cenę</button>
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
      isBuyer: false,
      isSupplier: false,
      isEditBuyer: false,
      isEditSupplier: false,
      buyers: [],
      suppliers: [],
      products: [],
      newPriceValue: 0,
      selectedProduct: {
        product: {
          id: 0
        }
      },
      selectedBuyer: {
        buyer: {
          id: 0
        }
      },
      selectedSupplier: {
        supplier: {
          id: 0
        }
      },
      price: {
        price: 0,
        product: {
          id: 0,
          product: ""
        },
        buyer: {
          id: 0
        },
        supplier: {
          id: 0
        }
      }
    };
  },
  beforeCreate() {
    axios.get("http://localhost:8080/buyer/getAll", {
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
      axios.get("http://localhost:8080/supplier/getAll", {
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
    forBuyer() {
      this.loadAllProducts();
      this.isBuyer = !this.isBuyer;
      this.isSupplier = false;
      this.isEditSupplier = false;
      this.isEditBuyer = false;
    },
    forSupplier() {
      this.loadAllProducts();
      this.isSupplier = !this.isSupplier;
      this.isBuyer = false;
      this.isEditSupplier = false;
      this.isEditBuyer = false;
    },
    editBuyer() {
      this.isEditBuyer = true;
      this.isEditSupplier = false;
      this.isSupplier = false;
      this.isBuyer = false;
    },
    editSupplier() {
      this.isEditSupplier = true;
      this.isEditBuyer = false;
      this.isSupplier = false;
      this.isBuyer = false;
    },
    addBuyerPrice() {
      axios.post("/price/createBuyerPrice", this.price, {
        headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          }
      }).then(this.isBuyer = !this.isBuyer);
    },
    addSupplierPrice() {
            console.log(this.price)
      axios.post("/price/createSupplierPrice", this.price, {
        headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          }
      }).then(this.isSupplier = !this.isSupplier);
    },
    loadBuyerProducts() {
      this.products = [];
      axios.get("/buyer/getBuyerProducts", {
          headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          },
          params: {
            id: this.selectedBuyer.buyer.id
          }
        })
        .then(resp => {
          const data = resp.data;
          for (let key in data) {
            const product = data[key];
            product.id = product.id;
            this.products.push(product);
          }
        });
    },
    loadAllProducts() {
      this.products = [];
      axios.get("/product/getAll", {
        headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          },
      }).then(resp => {
        const data = resp.data;
        for (let key in data) {
          const product = data[key];
          product.id = product.id;
          this.products.push(product);
        }
      });
    },
    loadSupplierProducts() {
      this.products = [];
      axios
        .get("/supplier/getSupplierProducts", {
          headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          },
          params: {
            id: this.selectedSupplier.supplier.id
          }
        })
        .then(resp => {
          const data = resp.data;
          for (let key in data) {
            const product = data[key];
            product.id = product.id;
            this.products.push(product);
          }
        });
    },
    updateBuyerPrice() {
      let params = new URLSearchParams();
      params.append("buyerId", this.selectedBuyer.buyer.id);
      params.append("productId", this.selectedProduct.name.id);
      params.append("value", this.newPriceValue);
      axios.put("/price/editBuyerPrice", params, {
        headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          },
      }).catch((error) => {console.log(error.response.data.message)});
      this.newPriceValue = 0;
      this.isEditBuyer = false;
      this.loadBuyerProducts();
    },
    updateSupplierPrice() {
      let params = new URLSearchParams();
      params.append("supplierId", this.selectedSupplier.supplier.id);
      params.append("productId", this.selectedProduct.name.id);
      params.append("value", this.newPriceValue);
      axios.put("/price/editSupplierPrice", params, {
        headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          },
      });
      this.newPriceValue = 0;
      this.isEditSupplier = false;
      this.loadSupplierProducts();
    }
  }
};
</script>

<style scoped>
.price-content {
  position: relative;
  width: 70%;
  height: 750px;
  margin-left: 400px;
  border: 1px solid black;
  border-radius: 5px;
  overflow: scroll;
  padding: 5px;
}

.edit-price-content {
  margin-top: 15px;
  margin-left: 290px;
}
.btn {
  background-color: green;
  border: none;
}

.btn:active {
  background-color: green;
  border: none;
  box-shadow: green;
}

.form-group {
  margin-top: 15px;
}

.form-control {
  width: 150px;
  height: 25px;
}
</style>