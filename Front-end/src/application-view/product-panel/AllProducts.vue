<template>
    <div class="all-products-content">
        <form>
          <ul class="products-list">
                <li 
                v-for="(product, index) in products" 
                v-bind:key="index"
                style="max-width: 200px;"
                class="list-group-item">{{ product.product }}</li>
            </ul>
        </form>
    </div>
</template>

<script>
import axios from '../../axios-auth'

export default {
    data() {
        return {
            products: []
        }
    },
    beforeCreate() {
        this.products = [];
        axios.get("/product/getAll", {
            headers: {
                'Authorization': 'Bearer ' + this.$store.state.jwt
            }
        }).then(resp => {
            const data = resp.data;
            for (let key in data) {
                const product = data[key];
                product.id = product.id;
                this.products.push(product);
            }
        });
    }
}
</script>

<style scoped>
.all-products-content {
  position: relative;
  width: 100%;
  height: 100%;
  border-radius: 0px 5px 5px 5px;
  margin: 0.5%;
  float: left;
  overflow-y: auto;
  font-size: 0.9vw;
}

.products-list {
    height: 500px;
    overflow: auto;
    overflow-x: auto;
    position: relative;
    float: left;
    padding: 0px;
}
</style>