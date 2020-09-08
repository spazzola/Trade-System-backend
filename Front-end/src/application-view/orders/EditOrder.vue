<template>
    <div class="add-order-content">
       <form>
            <div class="form-group">
                <label>Nr Dodaci listu</label>
                <input type="text" id="id" class="form-control" v-model="transportNumber"/>

                <button class="btn btn-success btn-sm" @click="getOrder()">Wyszukaj wysyłkę</button>
            </div>
       </form>
       <div v-if="showOrder">
           <div>
                <p> <span>Dodaci list:</span> {{ transportNumber }} </p>
                <p> <span>Sortyment:</span> {{ product.product}}</p>
                <p> <span>Ilość m3:</span> {{ quantity | toCurrency }}</p>
                <p> <span>Kwota klienta:</span> {{ buyerSum | toCurrency }}</p>
                <p> <span>Kwota kontrahenta:</span> {{ supplierSum |toCurrency }}</p>
           </div>
            <hr>
           <div>
               <form>
                    <label>Nowy dodaci list</label>
                    <input type="text" class="form-control" v-model="order.newTransportNumber">

                    <label>Nowa ilość m3</label>
                    <input type="number" class="form-control" v-model="order.newQuantity">

                    <label>Nowa wartość sprzedaży</label>
                    <input type="number" class="form-control" v-model="order.newBuyerSum">

                    <label>Nowa wartość wykupu</label>
                    <input type="number" class="form-control" v-model="order.newSupplierSum">

                    <button class="btn btn-success btn-sm" @click="updateOrder()">Edytuj</button>
               </form>
           </div>
       </div>
    </div>
</template>

<script>
import axios from '../../axios-auth'

export default {
    data() {
        return {
            quantity: null,
            buyerSum: null,
            supplierSum: null,
            transportNumber: null,
            product: {
                product: null
            },
            showOrder: false,
            order: {
                newQuantity: null,
                newBuyerSum: null,
                newSupplierSum: null,
                newTransportNumber: null
            }
        }
    },
    methods: {
        getOrder() {
            axios.get('order/getOrderByTransportNumber', {
                headers: {
                    'Authorization': 'Bearer ' + this.$store.state.jwt
                },
                params: {
                    transportNumber: this.transportNumber
                }
                }).then(resp => {
                    this.quantity = resp.data.quantity;
                    this.buyerSum = resp.data.buyerSum;
                    this.supplierSum = resp.data.supplierSum;
                    this.product.product = resp.data.product.product;
                }).finally(this.showOrder = true)
            },
        updateOrder() {
            if (this.order.newTransportNumber = null) {
                this.order.newTransportNumber = this.transportNumber;
            }

            axios.post('order/updateOrder', this.order, {
                headers: {
                    'Authorization': 'Bearer ' + this.$store.state.jwt
                }
            }).then(resp => {console.log(resp)})
        }
    }
}
</script>

<style scoped>
.add-order-content {
  position: relative;
  width: 100%;
  height: 100%;
  border-radius: 0px 5px 5px 5px;
  margin: 0.5%;
  float: left;
}

.form-control {
  width: 10%;
  height: 1%;
  font-size: 0.9vw;
}

button {
    opacity: 1;
    font-size: 0.8vw;
}

p, span, label {
    font-size: 0.9vw;
    margin-top: 0.5%;
}

span {
    font-weight: bolder;
}

.form-control {
    width: 150px;
    height: 25px;
}

button {
    opacity: 1;
    font-size: 0.8vw;
    margin-top: 0.5%;
}
</style>
