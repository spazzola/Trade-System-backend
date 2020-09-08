<template>
    <div class="pay-invoice-content">
            <form>
            <div class="form-group">
                <label>Id faktury</label>
                <input type="number" id="id" class="form-control" v-model="id" min="0"/>
            </div>
            <router-link to="/invoices">
                <button class="btn btn-success btn-sm" @click="pay">Dodaj płatność</button>
            </router-link>
            </form>
    </div>
</template>

<script>
import axios from '../../axios-auth'

export default {  
  data() {
    return {
      id: null
    };
  },
  methods: {
    pay() {
      const params = new URLSearchParams();
      params.append("id", this.id);
      axios
        .put("/invoice/payForInvoice", params, {
          headers: {
            Authorization: "Bearer " + this.$store.state.jwt
          }
        })
        .then(resp => {
          console.log(resp);
        });
    }
  }
}
</script>

<style scoped>
.pay-invoice-content {
  position: relative;
  width: 100%;
  height: 100%;
  border-radius: 0px 5px 5px 5px;
  margin: 0.5%;
  float: left;
}

.form-control {
  width: 70px;
  height: 25px;
}

button {
    opacity: 1;
    display: inline;
    font-size: 0.8vw;
}
</style>