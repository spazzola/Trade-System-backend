<template>
    <div class="add-buyer-content">
        <div class="form-group">
            <label>Nazwa klienta</label>
            <input type="text" id="date" class="form-control" v-model="buyer.name"/>
            <button 
                    class="btn btn-success btn-sm" 
                    style="margin-top: 0.5%;"
                    @click="createBuyer">Dodaj
            </button>
        </div>
    </div>
</template>

<script>
import axios from '../../axios-auth'


export default {
    data() {
        return {
            buyer : {
                name: ''
            }
        }
    },
    methods: {
        createBuyer() {
            axios.post("/buyer/create", this.buyer, {
                headers: {
                    'Authorization': 'Bearer ' + this.$store.state.jwt
          }
            }).catch((error) => {alert(error.response.data.message)})
            .then(this.$store.commit('getAllBuyers'))
            .then(resp => {
                if (resp.status == 200) {
                    alert('Dodano klienta')
                }
            }).catch((error) => {alert(error.response.data.message)});
            setTimeout(()=>{
                this.buyer.name = ''
                },100);
        }
    }
}
</script>

<style scoped>
.add-buyer-content {
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