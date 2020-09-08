<template>

    <div class="edit-price-content">
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
                <label>Wybierz sortyment</label>
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

</template>

<script>
import axios from '../../axios-auth'

export default {
    data() {
        return {
            buyers: this.$store.state.buyers,
            products: [],
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
            newPriceValue: null
        }
    },
    methods: {
        updateBuyerPrice() {
            let params = new URLSearchParams();
            params.append("buyerId", this.selectedBuyer.buyer.id);
            params.append("productId", this.selectedProduct.name.id);
            params.append("value", this.newPriceValue);
            axios.put("/price/editBuyerPrice", params, {
                headers: {
                    'Authorization': 'Bearer ' + this.$store.state.jwt
                },
            }).then(resp => {
                if (resp.status == 200) {
                    alert('Zaktualizowano cenę klienta')
                }
            })
            .catch((error) => {console.log(error.response.data.message)});
            this.newPriceValue = 0;
            this.isEditBuyer = false;
            this.loadBuyerProducts();
        },
        loadBuyerProducts() {
            this.products = [];
            axios.get("/buyer/getBuyerProducts", {
                headers: {
                    'Authorization': 'Bearer ' + this.$store.state.jwt
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
        }
    }
}
</script>

<style scoped>
.edit-price-content {
  position: relative;
  width: 100%;
  height: 100%;
  border-radius: 0px 5px 5px 5px;
  margin: 0.5%;
  float: left;
}

.form-control {
  width: 150px;
  height: 25px;
}

button {
    opacity: 1;
    display: inline;
    font-size: 0.8vw;
}
</style>