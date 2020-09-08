<template>
    <div>
        <spazz-left-menu></spazz-left-menu>
        <spazz-nav-menu></spazz-nav-menu>
        <div class="orders-content">
            <div v-if="isChanged">
                <button class="btn btn-success btn-sm" style="float: left; margin-right: 0.5%;" @click="changeOrdersContent()">Wstecz</button>
            </div>
            <button class="btn btn-success btn-sm" style="float: left;" @click="changeAddOrder()">Dodaj wysyłke</button>
            <button class="btn btn-success btn-sm" style="float: left; margin-left: 0.5%;" @click="changeEditOrder()">Edytuj wysyłkę</button>
            <div class="sort-menu">
            <div>
                    <label>Sortuj według</label>
                    <select 
                    @change="onSelectedMerhant($event)">
                        <option>Wszyscy</option>
                        <option>Klienta</option>
                        <option>Kontrahenta</option>
                    </select>
                
            
                <div v-if="selectedMerhant.buyer" class="merhant-content">
                    <select v-model="selectedBuyer.buyer">
                        <option
                            v-bind:value="{ id: buyer.id, name: buyer.name }"
                            v-for="buyer in buyers"
                            v-bind:key="buyer.id"
                        >{{ buyer.name }}</option>
                    </select>
                </div>

                <div v-if="selectedMerhant.supplier" class="merhant-content">
                    <select v-model="selectedSupplier.supplier">
                        <option
                            v-bind:value="{ id: supplier.id, name: supplier.name }"
                            v-for="supplier in suppliers"
                            v-bind:key="supplier.id"
                        >{{ supplier.name }}</option>
                    </select>
                </div>

                <select
                    style="margin-left: 3%;"
                    id="year"
                    v-model="selectedYear"
                    @change="onChangeYear($event)"
                    :selected:="year"
                    >
                    <option value="2020">2020</option>
                    <option value="2021">2021</option>
                    <option value="2022">2022</option>
                    <option value="2023">2023</option>
                    <option value="2024">2024</option>
                    <option value="2025">2025</option>
                    <option value="2026">2026</option>
                    <option value="2027">2027</option>
                    <option value="2028">2028</option>
                    <option value="2029">2029</option>
                    <option value="2030">2030</option>
                </select>

                <select 
                    style="" 
                    id="month" 
                    v-model="selectedMonth" 
                    @change="onChangeMonth($event)">
                    <option value="01">Styczeń</option>
                    <option value="02">Luty</option>
                    <option value="03">Marzec</option>
                    <option value="04">Kwiecień</option>
                    <option value="05">Maj</option>
                    <option value="06">Czerwiec</option>
                    <option value="07">Lipiec</option>
                    <option value="08">Sierpień</option>
                    <option value="09">Wrzesien</option>
                    <option value="10">Październik</option>
                    <option value="11">Listopad</option>
                    <option value="12">Grudzień</option>
                </select>
                <button class="btn btn-success btn-sm" style="margin-top: -0.5%;" @click="sort()">Sortuj</button>
            </div>
            
        </div>
            
            <table v-if="showOrderContent" class="table table-striped">
                <thead>
                    <tr>
                    <th scope="col">Data</th>
                    <th scope="col">Dodaci list</th>
                    <th scope="col">Sortyment</th>
                    <th scope="col">Ilość m3</th>
                    <th scope="col">Klient</th>
                    <th scope="col">Klient suma</th>
                    <th scope="col">Kontrahent</th>
                    <th scope="col">Kontrahent suma</th>
                    <th scope="col">Komentarz systemu</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="(order, index) in orders" v-bind:key="index">
                        <td scope="row">{{ order.date }}</td>
                        <td scope="row">{{ order.orderDetails[0].transportNumber }}</td>
                        <td scope="row">{{ order.orderDetails[0].product.product}}</td>
                        <td scope="row">{{ order.orderDetails[0].quantity | toCurrency }}</td>
                        <td scope="row">{{ order.buyer.name }}</td>
                        <td scope="row">{{ order.orderDetails[0].buyerSum | toCurrency }}</td>
                        <td scope="row">{{ order.supplier.name }}</td>
                        <td scope="row">{{ order.orderDetails[0].supplierSum |toCurrency }}</td>
                        <td scope="row">{{ order.orderDetails[0].orderComment.systemComment}}</td>           
                    </tr>
                </tbody>
            </table>

            <div v-if="showAddOrder">
                <spazz-add-order></spazz-add-order>
            </div>

            <div v-if="showEditOrder">
                   <spazz-edit-order></spazz-edit-order>
            </div>
        </div>
        
    </div>
</template>

<script>
import LeftMenu from '../LeftMenu'
import NavMenu from '../NavMenu'
import AddOrder from './AddOrder'
import EditOrder from './EditOrder'
import axios from '../../axios-auth'

export default {
    components: {
        spazzLeftMenu: LeftMenu,
        spazzNavMenu: NavMenu,
        spazzAddOrder: AddOrder,
        spazzEditOrder: EditOrder
    },
    data() {
        return {
            orders: [],
            showOrderContent: true,
            showAddOrder: false,
            showEditOrder: false,
            isChanged: false,
            buyers: this.$store.state.buyers,
            suppliers: this.$store.state.suppliers,
            selectedMerhant: {
                buyer: false,
                supplier: false,
                all: true
            },
            selectedBuyer: {
                buyer: {
                id: null
                }
            },
            selectedSupplier: {
                supplier: {
                id: null
                }
            },
            selectedMonth: this.getMonth(),
            selectedYear: this.getYear(),
            month: "",
            year: "",
            merhants: []
        }
    },
    methods: {
        changeAddOrder() {
            this.showAddOrder = true;
            this.showOrderContent = false;
            this.showEditOrder = false;
            this.isChanged = true;
        },
        changeEditOrder() {
            this.showEditOrder = true;
            this.showOrderContent = false;
            this.showAddOrder = false;
            this.isChanged = true;
        },
        changeOrdersContent() {
            this.showOrderContent = true;
            this.showAddOrder = false;
            this.showEditOrder = false;
            this.isChanged = false;
        },
        getDate() {
            let currentDate = new Date();
            let currentDateWithFormat = new Date()
                .toJSON()
                .slice(0, 10)
                .replace(/-/g, "/");

            return currentDateWithFormat;
        },
        getMonth() {
            let currentDate = this.getDate();
            return currentDate.slice(5, 7);
        },
        getYear() {
            let currentDate = this.getDate();
            return currentDate.slice(0, 4);
        },
        onChangeMonth(event) {
            this.month = event.target.value;
        },
        onChangeYear(event) {
            this.year = event.target.value;
        },
        sort() {
            if (this.selectedMerhant.all == true) {
                console.log('month all')
                this.getMonthOrders();
            }
            if (this.selectedMerhant.buyer == true) {
                this.getBuyerMonthOrders();
            }
            if (this.selectedMerhant.supplier == true) {
                this.getSupplierMonthOrders();
            }
        },
        onSelectedMerhant(event) {
            if (event.target.value === "Klienta") {
                this.selectedMerhant.buyer = true;
                this.selectedMerhant.supplier = false;
                this.onSelectedMerhant.all = false;
            } 
            else if (event.target.value === "Kontrahenta") {
                this.selectedMerhant.buyer = false;
                this.selectedMerhant.supplier = true;
                this.onSelectedMerhant.all = false;
            }
            else {
                this.selectedMerhant.buyer = false;
                this.selectedMerhant.supplier = false;
                this.selectedMerhant.all = true;
            }
        },
        getBuyerMonthOrders() {
            this.orders = [];
            axios.get("orders/getBuyerMonthOrders", {
                headers: {
                    'Authorization': 'Bearer ' + this.$store.state.jwt
                },
                params: {
                    year: this.year,
                    month: this.month,
                    id: this.selectedBuyer.buyer.id
                }
            }).then(resp => {
                const data = resp.data;
                for (let key in data) {
                    const order = data[key];
                    order.id = order.id;
                    this.orders.push(order);
                }
            });
        },
        getSupplierMonthOrders() {
            this.orders = [];
            axios.get("orders/getSupplierMonthOrders", {
                headers: {
                    'Authorization': 'Bearer ' + this.$store.state.jwt
                },
                params: {
                    year: this.year,
                    month: this.month,
                    id: this.selectedSupplier.supplier.id
                }
            }).then(resp => {
                const data = resp.data;
                for (let key in data) {
                    const order = data[key];
                    order.id = order.id;
                    this.orders.push(order);
                }
            });
        },
        getMonthOrders() {
            this.orders = [];
            axios.get("/order/getMonthOrders", {
                headers: {
                    'Authorization': 'Bearer ' + this.$store.state.jwt
                },
                params: {
                    year: this.year,
                    month: this.month
                }
                })
                .then(resp => {
                    console.log(resp)
                const data = resp.data;
                for (let key in data) {
                    const order = data[key];
                    order.id = order.id;
                    this.orders.push(order);
                }
            });
        }
    },
    created() {
        let currentDate = this.getDate();
        this.year = currentDate.slice(0, 4);
        this.month = currentDate.slice(5, 7);
        this.getMonthOrders();
    }
}
</script>

<style scoped>

.orders-content {
  position: relative;
  width: 70%;
  height: 790px;
  border: 1px solid black;
  border-radius: 0px 5px 5px 5px;
  margin-left: 2%;
  background: rgb(255, 255, 255, 0.8);
  float: left;
  overflow-x: auto;

}

td {
  white-space: nowrap;
}

table {
  width: 2700px;
  font-size: 0.9vw;
}

li {
  width: 2500px;

  overflow: auto;
  position: relative;
  border: 1px solid black;
}

button {
    opacity: 1;
    display: inline;
    font-size: 0.8vw;
}

.sort-menu {
    float: left;
    width: 70%;
    margin-left: 1%;
    margin-top: 0.3%;
    font-size: 0.9vw;
}

.merhant-content {
    display: inline;
    position: relative;
}
</style>