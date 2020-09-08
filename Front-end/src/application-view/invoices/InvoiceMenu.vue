<template>
    <div class="invoice-menu">
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

         <button
            class="btn btn-success btn-sm"
            style="margin-top: -0.5%;"
            @click="transferInvoices"
        >Przenieś faktury na następny miesiąc</button>

    </div>
</template>

<script>
export default {
    data() {
        return {
            month: "",
            year: ""
        }
    },
    methods: {
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
        transferInvoices() {
            if (this.checkIfSureToTransfer()) {
                let date = this.getDate();
                const params = new URLSearchParams();
                params.append("localDate", date);
                axios.post("/invoice/transfer", params, {
                headers: {
                    'Authorization': 'Bearer ' + store.state.jwt
                },
            });
        }
        },
        checkIfSureToTransfer() {
            if (confirm("Czy napewno chcesz przenieść faktury na następny miesiąc?")) {
                return true;
            } else {
                return false;
            }
        }
    }
}
</script>

<style scoped>
.invoice-menu {
    height: 3.9%;
    float: left;
    width: 70%;
    margin-left: 1%;
    margin-top: 0.3%;
    font-size: 0.9vw;
}

.sort-menu {
    float: left;
    width: 70%;
    margin-left: 1%;
    margin-top: 0.3%;
    font-size: 0.9vw;
}

button {
    opacity: 1;
    display: inline;
    font-size: 0.8vw;
}
</style>