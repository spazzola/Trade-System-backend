<template>
    <div>
        <nav-menu></nav-menu>
        <left-menu></left-menu>        
        <div class="product-content">
            <div class="form-group">
                <label>Nazwa produktu</label>
                <input type="text" id="date" class="form-control" v-model="product.product" />
                
                <router-link to="/product">
                    <button 
                    class="btn btn-success btn-sm" 
                    style="margin-top: 10px;"
                    @click="create">Dodaj</button>
                </router-link>
        
            </div>

            <div class="products">
                <form>
                    <button 
                    class="btn btn-success btn-primary" 
                    style="margin-left: 80px;"
                    @click="getAllProducts">Wyświetl wszystkie produkty</button>
                    <ul class="products-list">
                        <li 
                        v-for="(product, index) in products" 
                        v-bind:key="index"
                        style="max-width: 200px; margin-left: 180px;"
                        class="list-group-item">{{ product.product }}</li>
                    </ul>
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
    data () {
        return {
            products: [],
            product: {
                id: null,
                product: ''
            }
        }
    },
    methods: {
        create() {
            axios.post("/product/create", this.product, {
                headers: {
                    'Authorization': 'Bearer ' + store.state.jwt
          },
            })
        },
        getAllProducts() {
            this.products = [];
            axios.get("/product/getAll", {
                headers: {
                    'Authorization': 'Bearer ' + store.state.jwt
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
}
</script>

<style scoped>

.product-content {
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

.form-group {
    float: left;
}

.products {
    margin-left: 50px;

}

.products-list {
    overflow: auto;
    overflow-x: auto;
    height: 700px; 
    position: relative; 
    right: 100px;
}

.list-group-item {
    width: 300px;
    overflow-x: auto;
}
</style>