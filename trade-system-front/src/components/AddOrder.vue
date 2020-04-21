<template>

  <div>
    <nav-menu></nav-menu>
  <left-menu></left-menu>
    <div class="add-order-content">
        <form>

            <div class="form-group">
                <label>Data</label>
                <input type="date" id="date" class="form-control" v-model="order.date" style="width: 160px;" required/>
            </div>
      
            <div class="form-group">
                <label>Wybierz kupca</label> <br>
                <select v-model="selectedBuyer.buyer" @change="loadBuyerProducts">
                <option
                    v-bind:value="{id: buyer.id, name: buyer.name}"
                    v-for="buyer in buyers"
                    v-bind:key="buyer.id"
                >{{ buyer.name }}</option>
                </select>
            </div>

            <div class="form-group">
                <label>Ilość (m3)</label>
                <input type="number" id="quantity" class="form-control" v-model="order.orderDetails[0].quantity"/>
            </div>

            <div class="form-group">
                <label>Dodaci list</label>
                <input type="text" id="transportNumber" class="form-control" v-model="order.orderDetails[0].transportNumber"/>
            </div>

            <button class="btn btn-primary btn-success btn-sm" @click.prevent="setIndividualPrice">Dodaj indywidualną cenę</button>
            <div class="form-group" v-if="individualPrice">
                <label style="margin-top: 1%;">Podaj cenę</label>
                <input type="number" id="typedPrice" class="form-control" v-model="order.orderDetails[0].typedPrice"/>
            </div>

            <div class="form-group" style="margin-top: 1%;">
                <label>Wybierz product</label> <br>
                <select v-model="selectedProduct">
                <option
                    v-bind:value="{name: product.product}"
                    v-for="product in products"
                    v-bind:key="product.id"
                    >{{ product.product.product }} - {{ product.price }}zł</option>
                </select>
            </div>

            <div class="form-group">
                <label>Wybierz dostawcę</label> <br>
                <select v-model="selectedSupplier.supplier">
                <option
                    v-bind:value="{id: supplier.id, name: supplier.name}"
                    v-for="supplier in suppliers"
                    v-bind:key="supplier.id"
                    > {{ supplier.name }}</option>
                </select>
            </div>

            <router-link to="/orders">
                <button class="btn btn-success btn-sm" @click="addOrder">Dodaj</button>
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
        individualPrice: false,
        selectedBuyer: {
        buyer: {
          id: null
        }
      },
      selectedSupplier: {
        supplier: {
          id: null
        }
      },
      selectedProduct: {
          product: {
              id: null
          }
      },
      buyers: [],
      suppliers: [],
      products: [],

      order: {
          date: '',
          buyerId: 0,
          supplierId: 0,
          orderDetails: [
              {
                  quantity: 0,
                  typedPrice: 0,
                  transportNumber: '',
                  product: {
                      id: 0
                  }
              }
          ]
      }
        }
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
      addOrder() {
        this.order.buyerId = this.selectedBuyer.buyer.id;
        this.order.supplierId = this.selectedSupplier.supplier.id;
        this.order.orderDetails[0].product.id = this.selectedProduct.name.id;
        axios.post("/order/create", this.order, {
          headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          }
        })
        .catch((error) => {alert(error.response.data.message)}
        )},
      loadBuyerProducts() {
        this.products = []
        axios.get("/buyer/getBuyerProducts", {
          headers: {
            'Authorization': 'Bearer ' + store.state.jwt
          },
          params: {
            id: this.selectedBuyer.buyer.id
          }
        }).then(resp => {
          const data = resp.data;
          for (let key in data) {
            const product = data[key];
            product.id = product.id;
            this.products.push(product);
        }
        
      })
      },
      setIndividualPrice() {
        this.individualPrice = !this.individualPrice;
      }

  }
}
</script>

<style scoped>

.add-order-content {
  position: relative;
  width: 70%;
  height: 750px;
  margin-left: 400px;
  border: 1px solid black;
  border-radius: 5px;
  overflow: scroll;
  padding: 5px;
}

.form-control {
  width: 150px;
  height: 25px;
}
</style>