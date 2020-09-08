<template>

    <div>
        <spazz-left-menu></spazz-left-menu>
        <spazz-nav-menu></spazz-nav-menu>

        <div class="product-content">
            
            <button class="btn btn-success btn-sm" style="float: left;" @click="changeAddProduct()">Dodaj sortyment</button>
            <button class="btn btn-success btn-sm" style="float: left; margin-left: 0.5%;" @click="changeEditProduct()">Edytuj sortyment</button>
            <button class="btn btn-success btn-sm" style="float: left; margin-left: 0.5%;" @click="changeAllProducts()">Wyświetl wszystkie sortymenty</button>
            
            <div v-if="showAddProductContent">
                <spazz-add-product></spazz-add-product>
            </div>

            <div v-if="showEditProductContent">
                <spazz-edit-product></spazz-edit-product>
            </div>

            <div v-if="showAllProductsContent">
                <spazz-all-products></spazz-all-products>
            </div>

        </div>

    </div>

</template>

<script>
import LeftMenu from '../LeftMenu'
import NavMenu from '../NavMenu'
import AddProduct from './AddProduct'
import EditProduct from './EditProduct'
import AllProducts from './AllProducts'

export default {
    components: {
        spazzLeftMenu: LeftMenu,
        spazzNavMenu: NavMenu,
        spazzAddProduct: AddProduct,
        spazzEditProduct: EditProduct,
        spazzAllProducts: AllProducts
    },
    data() {
        return {
            showAddProductContent: false,
            showEditProductContent: false,
            showAllProductsContent: false
        }
    },
    methods: {
        changeAddProduct() {
            this.showAddProductContent = true;
            this.showEditProductContent = false;
        },
        changeEditProduct() {
            this.showEditProductContent = true;
            this.showAddProductContent = false;
        },
        changeAllProducts() {
            this.showAllProductsContent = !this.showAllProductsContent
        }
    }
}
</script>

<style scoped>
.product-content {
  position: relative;
  width: 70%;
  height: 790px;
  border: 1px solid black;
  border-radius: 0px 5px 5px 5px;
  margin-left: 2%;
  background: rgb(255, 255, 255, 0.8);
  float: left;
}

button {
    opacity: 1;
    display: inline;
    font-size: 0.8vw;
}
</style>