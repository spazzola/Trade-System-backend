<template>
    <div>
        <left-menu></left-menu>
        <div class="main">
            <div class="rectangle">
                <div id="title"> Forest Industry S.A. </div>
                <div id="borderBottom"></div>
            </div>
                <br>Oferujemy drewno iglaste w wymiarach od 2.5m do 14m. <br>
                 <div style="text-align: left; margin-left: 20px; margin-top: 15px;">
                    Posiadamy w ofercie cenne gatunki drewna takie jak: <br>
                    </div>
                    <ul style="text-align: left;">
                    <li>Dąb</li>
                    <li>Buk</li>
                    <li>Modrzew</li>
                    <li>Jesion</li>
                    <li>Jawor</li>
                    <li>Olcha</li>
                    </ul> <br>
                    Ceny ustalane są indiwudalnie, na podstawie ilości zamówionego sortymentu. <br>
                    Dla stałych klientów gwarancja dostawy każdej ilości drewna w ciągu całego roku. <br> <br>
                    Handlujemy drewnem wyłącznie posiadającym certyfikaty PEFC oraz FSC&reg;.
                
                <div style="clear: both;"></div>
        </div>
        <spazz-footer></spazz-footer>
    </div>
</template>

<script>
import LeftMenu from './LeftMenu.vue';
import Footter from './Footer.vue';

export default {
    components: {
        LeftMenu,
        Footter
    }
}
</script>

<style scoped>
.main
{   
    margin-top: 75px;
    width: 780px;
    height: 510px;
    font-size: 14.5px;
    text-align: center;
    color: #ffffff;
    background: rgba(2, 153, 40, 0.6);
    border: 1px solid black;
    border-radius: 5%;
    padding: 5px;
    float: left;
    margin-left: 10px;
}

#borderBottom
{
    border-top: 4px solid rgb(7, 94, 7);
    position: relative;
    bottom: 0px;
    margin-left: 50px;
    margin-right: 50px;
    border-radius: 50%;
    padding: 1px;
}

.rectangle
{
    width: 770px;
}

#title
{
    font-family: 'Libre Baskerville', serif;
    margin-top: 3px;
    font-size: 30px;
    padding: 1px;
    letter-spacing: 3px;
    color: #ffffff;
    text-align: center;
}
</style>