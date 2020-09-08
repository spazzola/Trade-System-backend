<template>
    <div class="order-menu">
       
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

                <select style="" id="month" @change="onChangeMonth($event)">
                    <option value="1">Styczeń</option>
                    <option value="2">Luty</option>
                    <option value="3">Marzec</option>
                    <option value="4">Kwiecień</option>
                    <option value="5">Maj</option>
                    <option value="6">Czerwiec</option>
                    <option value="7">Lipiec</option>
                    <option value="8">Sierpień</option>
                    <option value="9">Wrzesien</option>
                    <option value="10">Październik</option>
                    <option value="11">Listopad</option>
                    <option value="12">Grudzień</option>
                </select>
                <button class="btn btn-success btn-sm" style="margin-top: -0.5%;" @click="sort">Sortuj</button>
            </div>
            
        </div>

    </div>
</template>

<script>
export default {
    data() {
        return {
            buyers: [],
            suppliers: [],
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
            month: "",
            year: "",
            merhants: []
        }
    },
    methods: {
        loadBuyers() {
            this.buyers.push(this.$store.getters.getAllBuyers);
        },
        loadSuppliers() {
            this.suppliers.push(this.$store.getters.getAllSuppliers);
        },
        onSelectedMerhant(event) {
            if (event.target.value === "Klienta") {
                this.selectedMerhant.buyer = true;
                this.selectedMerhant.supplier = false;
            } 
            else if (event.target.value === "Kontrahenta") {
                this.selectedMerhant.buyer = false;
                this.selectedMerhant.supplier = true;
            }
            else {
                this.selectedMerhant.buyer = false;
                this.selectedMerhant.supplier = false;
                this.selectedMerhant.all = true;
            }
        },
        onChangeMonth(event) {
            this.month = event.target.value;
        },
        onChangeYear(event) {
            this.year = event.target.value;
        },
        sort() {

        },
        getDate() {
            let currentDate = new Date();
            let currentDateWithFormat = new Date()
                .toJSON()
                .slice(0, 10)
                .replace(/-/g, "/");

            return currentDateWithFormat;
        },
        addOrder() {
            this.$store.state.isAddingOrder = !this.$store.state.isAddingOrder
            this.$root.$emit(this.$store.state.isAddingOrder)
            console.log(this.$store.state.isAddingOrder)
        }
    },
    mounted() {
        let currentDate = this.getDate();
        this.year = currentDate.slice(0, 4);
        this.month = currentDate.slice(5, 7);

        this.buyers = this.$store.state.buyers;
        this.suppliers = this.$store.state.suppliers;
    },
    
}
</script>

<style scoped>

.order-menu {
    height: 3.9%;
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