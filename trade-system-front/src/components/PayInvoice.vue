<template>
    <div>
        <nav-menu></nav-menu>
        <left-menu></left-menu>
        <div class="pay-invoice-content">
            <form>
            <div class="form-group">
                <label>Id faktury</label>
                <input type="number" id="id" class="form-control" v-model="id" />
            </div>
            <router-link to="/invoices">
                <button class="btn btn-success" @click="pay">Dodaj płatność</button>
            </router-link>
            </form>
        </div>
    </div>

</template>

<script>
import axios from "../axios-auth";
import store from "../store";
import NavMenu from "../NavMenu.vue";
import LeftMenu from "../LeftMenu.vue";

export default {
  components: {
    navMenu: NavMenu,
	leftMenu: LeftMenu
  },  
  data() {
    return {
      id: 0
    };
  },
  methods: {
    pay() {
      const params = new URLSearchParams();
      params.append("id", this.id);
      axios
        .put("/invoice/payForInvoice", params, {
          headers: {
            Authorization: "Bearer " + store.state.jwt
          }
        })
        .then(resp => {
          console.log(resp);
        });
    }
  }
};
</script>

<style scoped>
.pay-invoice-content {
  position: relative;
  width: 70%;
  height: 750px;
  margin-left: 400px;
  border: 1px solid black;
  border-radius: 5px;
  padding: 10px;
}

.form-control {
  width: 70px;
  height: 25px;
}
</style>