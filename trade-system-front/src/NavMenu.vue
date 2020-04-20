<template>

            <ul class="nav nav-pills upper-menu">
                <router-link to="orders" tag="li" ><i class="nav-link" style="margin-left: 0px;"><span>Wysyłki</span></i></router-link>
            
                <router-link to="invoices" tag="li" ><i class="nav-link"><span>Faktury</span></i></router-link>
            
                <router-link to="/addBuyer" tag="li"><i class="nav-link">Dodaj kupca</i></router-link>
        
                <router-link to="/addSupplier" tag="li"><i class="nav-link">Dodaj sprzedawce</i></router-link>

                <router-link to="/product" tag="li"><i class="nav-link">Dodaj produkt</i></router-link>

                <router-link to="/price" tag="li"><i class="nav-link">Dodaj cenę</i></router-link>

                <router-link to="/costs" tag="li"><i class="nav-link">Dodaj koszty</i></router-link>
            
                <router-link to="/generateReport" tag="li"><i class="nav-link">Generuj raport</i></router-link>
            </ul>

</template>

<script>

export default {
    
}
</script>

<style>

i {
    border: 2px solid black;
    font-size: 20px;
}
i:hover {
    color: white;
    background-color: green;
}
.router-link-active {
    color: white;
    background-color: green;
    border-radius: 5px;
}

.upper-menu {
    margin-left: 400px;
    margin-top: 100px;
}

</style>
