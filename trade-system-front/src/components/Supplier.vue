<template>
    <div>
        <nav-menu></nav-menu>
        <left-menu></left-menu>
        <div class="add-supplier-content">
        <div class="form-group">
            <label>Nazwa sprzedawcy</label>
            <input type="text" id="date" class="form-control" v-model="supplier.name" />
            
            <router-link to="/orders">
                <button 
                class="btn btn-success btn-sm" 
                style="margin-top: 10px;"
                @click="create">Dodaj</button>
            </router-link>
       
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
            supplier : {
                name: ''
            }
        }
    },
    methods: {
        create() {
            axios.post("/supplier/create", this.supplier, {
                headers: {
                    'Authorization': 'Bearer ' + store.state.jwt
          }
            })
        }
    }
}
</script>

<style scoped>

.add-supplier-content {
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