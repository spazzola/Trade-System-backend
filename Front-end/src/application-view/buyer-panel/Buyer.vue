<template>

    <div>
        <spazz-left-menu></spazz-left-menu>
        <spazz-nav-menu></spazz-nav-menu>

            <div class="buyer-content">
                <button class="btn btn-success btn-sm" style="float: left;" @click="changeAddBuyer()">Dodaj klienta</button>
                <button class="btn btn-success btn-sm" style="float: left; margin-left: 0.5%;" @click="changeAddPrice()">Dodaj cenę</button>
                 <button class="btn btn-success btn-sm" style="float: left; margin-left: 0.5%;" @click="changeEditPrice()">Edytuj cenę</button>

                <div v-if="showAddBuyerContent">
                    <spazz-add-buyer></spazz-add-buyer>
                </div>

                <div v-if="showAddPriceContent">
                    <spazz-add-price></spazz-add-price>
                </div>

                <div v-if="showEditPriceContent">
                    <spazz-edit-price></spazz-edit-price>
                </div>
            </div>
    </div>
    
</template>

<script>
import LeftMenu from '../LeftMenu'
import NavMenu from '../NavMenu'
import AddBuyer from './AddBuyer'
import AddPrice from './AddPrice'
import EditPrice from './EditPrice'

export default {
    components: {
        spazzLeftMenu: LeftMenu,
        spazzNavMenu: NavMenu,
        spazzAddBuyer: AddBuyer,
        spazzAddPrice: AddPrice,
        spazzEditPrice: EditPrice
    },
    data() {
        return {
            showAddBuyerContent: false,
            showAddPriceContent: false,
            showEditPriceContent: false
        }
    },
    methods: {
        changeAddBuyer() {
            this.showAddBuyerContent = true
            this.showAddPriceContent = false
            this.showEditPriceContent = false
        },
        changeAddPrice() {
            this.showAddPriceContent = true
            this.showAddBuyerContent = false
            this.showEditPriceContent = false
        },
        changeEditPrice() {
            this.showEditPriceContent = true
            this.showAddBuyerContent = false
            this.showAddPriceContent = false
        }
    }
}
</script>

<style scoped>
.buyer-content {
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