<template>
        <div class="leftmenu">
            <div class="tile"> <router-link to="/offer" class="tilelink"><span><br>Oferta</span></router-link></div>
            <div class="tile"> <router-link to="/about" class="tilelink"><span><br>O firmie</span></router-link></div>
            <div class="tile"> <router-link to="/gallery" class="tilelink"><span><br>Galeria</span></router-link></div>
            <div class="tile"> <router-link to="/contact" class="tilelink"><span><br>Kontakt</span></router-link></div>
         </div>
</template>

<script>
export default {
    
}
</script>

<style scoped>

.leftmenu
{
    margin-top: 75px;
    width: 200px;
    height: 510px;
    float: left;  
    margin-left: 20%;

}

.tile
{   
    font-family: 'Libre Baskerville', serif;
    background: rgba(2, 153, 40, 0.6);
    border: 1px solid black;
    margin: 10px;
    width: 180px;
    height: 111px;
    font-size: 25px;
    letter-spacing: 1px;
    color: #ffffff;
    text-align: center;
    border-radius: 20%;


}

.tile:hover
{
    background-color: #007000;
}

.tileFirst:hover
{
    background-color: #007000;
}

a.tilelink
{
    color: #ffffff;
    text-decoration: none;
    display: block;
    width: 180px;
    height: 111px;
    border-radius: 20%;
}

.router-link-active {
    color: white;
    background-color: green;
    max-height: 100%;
    max-width: 100%;
}

</style>