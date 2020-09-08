<template>
    <div class="add-invoice-content">
            <form>
                <div class="form-group">
                    <label for="invoiceNumber">Numer faktury</label>
                    <input
                    type="text"
                    name="invoiceNumber"
                    class="form-control"
                    v-model="invoice.invoiceNumber"
                    />
                </div>

                <div class="form-group">
                    <label>Data</label>
                    <input type="date" id="date" class="form-control" v-model="invoice.date" />
                </div>

                <div class="form-group">
                    <label>Wartość</label>
                    <input type="number" id="value" class="form-control" v-model="invoice.value" />
                </div>

                <div class="form-group">
                    <label>Zapłacona</label> <br>
                    <select v-model="isPaid">
                    <option
                        v-for="paidOption in paidOptions"
                        v-bind:key="paidOption.index"
                    >{{ switchBooleanToString(paidOption) }}</option>
                    </select>
                </div>

                <div class="form-group">
                    <label>Komentarz</label>
                    <input type="text" id="comment" class="form-control" v-model="invoice.comment" />
                </div>

                <div class="form-group">
                    <label>Wybierz klienta</label> <br>
                    <select v-model="selectedBuyer.buyer">
                    <option
                        v-bind:value="{id: buyer.id, name: buyer.name}"
                        v-for="buyer in buyers"
                        v-bind:key="buyer.id"
                    >{{ buyer.name }}</option>
                    </select>
                </div>

                <p style="display: block; margin-left: 50px; font-weight: bold;">lub</p>

                <div class="form-group">
                    <label>Wybierz kontrahenta</label> <br>
                    <select v-model="selectedSupplier.supplier">
                    <option
                        v-bind:value="{id: supplier.id, name: supplier.name}"
                        v-for="supplier in suppliers"
                        v-bind:key="supplier.id"
                    >{{ supplier.name }}</option>
                    </select>
                </div>
                <router-link to="/invoices">
                    <button class="btn btn-success btn-sm" @click="addInvoice">Dodaj</button>
                </router-link>
            </form>
    </div>
</template>

<script>
import axios from '../../axios-auth'

export default {

    data() {
        return {
        isPaid: null,
        paidOptions: [false, true],
        invoice: {
            invoiceNumber: "",
            date: "",
            value: null,
            amountToUse: 0,
            paid: false,
            comment: "",
            buyer: {
            id: null
            },
            supplier: {
            id: null
            }
        },
        buyers: this.$store.state.buyers,
        suppliers: this.$store.state.suppliers,

        selectedBuyer: {
            buyer: {
            id: null
            }
        },
        selectedSupplier: {
            supplier: {
            id: null
            }
        }
        };
    },
    methods: {
        addInvoice() {
        this.switchPayValueToBoolean()
        this.invoice.paid = this.isPaid;
        this.invoice.amountToUse = this.invoice.value;

        if (this.selectedBuyer.buyer.id != null) {
            this.invoice.buyer.id = this.selectedBuyer.buyer.id;
            this.invoice.supplier = null;
            axios.post("/invoice/create", this.invoice, {
            headers: {
                'Authorization': 'Bearer ' + this.$store.state.jwt
            }
            }).then(resp => {
                if (resp.status == 200) {
                    alert('Dodano fakturę')
                }
            }).catch((error) => {alert(error.response.data.message)});
        } else {
            this.invoice.supplier.id = this.selectedSupplier.supplier.id;
            this.invoice.buyer = null;
            axios.post("/invoice/create", this.invoice, {
            headers: {
                'Authorization': 'Bearer ' + this.$store.state.jwt
            }
            }).then(resp => {
                if (resp.status == 200) {
                    alert('Dodano fakture')
                }
            }).catch((error) => {alert(error.response.data.message)})
        }
        },
        getDate() {
        var currentDate = new Date();
        var currentDateWithFormat = new Date()
            .toJSON()
            .slice(0, 10)
            .replace(/-/g, "/");

        return currentDateWithFormat;
        },
        switchBooleanToString(value) {
        if (value == false) {
            return "Nie";
        } else {
            return "Tak";
        }
        },

        switchPayValueToBoolean() {
        if (this.isPaid == 'Nie') {
            this.isPaid = false
        }
        if (this.isPaid == 'Tak') {
            this.isPaid = true
        }
        }
    }
}
</script>

<style scoped>
.add-invoice-content {
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
</style>