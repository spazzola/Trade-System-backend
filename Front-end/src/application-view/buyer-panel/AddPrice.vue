<template>

    <div class="add-price-content">
        <form>
            <div class="form-group">

            <label>Wybierz sortyment</label>
            <br>
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

</template>

<script>
import axios from '../../axios-auth'

export default {
    data() {
        return {
            buyers: [],
            products: [],
            price: {
                price: 0,
                product: {
                    id: 0,
                    product: ""
                },
            },
            buyer: {
                id: 0
            },
        }
    },
    methods: {
        addBuyerPrice() {
            axios.post("/price/createBuyerPrice", this.price, {
                headers: {
                    'Authorization': 'Bearer ' + this.$store.state.jwt
                }
            }).then(resp => {
                if (resp.status == 200) {
                    alert('Dodano cenę dla klienta')
                }
            }).catch((error) => {alert(error.response.data.message)});
        },
    },
     created() {
        this.buyers = []
        axios.get("http://localhost:8080/buyer/getAll", {
        headers: {
                'Authorization': 'Bearer ' + this.$store.state.jwt
            }
        }).then(resp => {
        const data = resp.data;
        for (let key in data) {
            const buyer = data[key];
            buyer.id = buyer.id;
            this.buyers.push(buyer);
        }
        }),

      this.products = [];
      axios.get("/product/getAll", {
        headers: {
            'Authorization': 'Bearer ' + this.$store.state.jwt
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
}
</script>

<style scoped>
.add-price-content {
  position: relative;
  width: 100%;
  height: 100%;
  border-radius: 0px 5px 5px 5px;
  margin: 0.5%;
  float: left;
}
</style>