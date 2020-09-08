<template>

            <ul class="nav nav-pills upper-menu">
                <div class="tile">
                    <router-link to="orders" tag="li"><i class="nav-link" style="margin-left: 0px;"><span>Wysyłki</span></i></router-link>
                </div>

                <div class="tile">
                    <router-link to="invoices" tag="li" ><i class="nav-link"><span>Faktury</span></i></router-link>
                </div>

                <div class="tile">
                    <router-link to="/buyer" tag="li"><i class="nav-link">Panel klienta</i></router-link>
                </div>
        
                <div class="tile">
                    <router-link to="/supplier" tag="li"><i class="nav-link">Panel kontrahenta</i></router-link>
                </div>

                <div class="tile">
                    <router-link to="/product" tag="li"><i class="nav-link">Panel sortymentu</i></router-link>
                </div>
            
                <div class="tile">
                    <router-link to="/report" tag="li"><i class="nav-link">Panel raportu</i></router-link>
                </div>

            </ul>

</template>

<script>

export default {
    
}
</script>

<style>

i {
    border: 1px solid black;
    font-size: 1.1vw;

}
i:hover {
    color: white;
    background-color: green;
}
.router-link-active {
    color: black;
    background-color: green;
    border-radius: 5px;
}

.nav-pills .nav-link {
    width: auto;
    border-radius: 0.25em 0.25em 0em 0em;
}

.upper-menu {
    margin-top: 3%;
    margin-left: 27%;

    border-radius: 5px;
    opacity: 0.8;
}

.tile {
    background-color: white;
    border-radius: 5px;
}
</style>
