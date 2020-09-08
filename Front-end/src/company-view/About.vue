<template>
    <div>
        <left-menu></left-menu>
        <div class="main">
            <div class="rectangle">
                <div id="title"> Forest Industry S.A. </div>
                <div id="borderBottom"></div>
            </div>
            <br>
            Działamy na rynku leśnym od 1997 roku. Do 2020 roku pracując w Polsce firma była znana pod nazwą Gospodarka Leśna S.A, obecnie jesteśmy rozpoznawalni jako Forest Industry S.A. <br>
            Przez cały ten czas nawiązaliśmy szereg różnorodnych kontaktów, dzięki którym weszliśmy na rynek europejski zachowując zaufanie i dobrą opinię klientów. <br> <br>
            Zajmujemy się wszelkimi pracami związanymi z gospodarką leśną oraz handlem drewna na terenie Europy. <br>
            Nasze składnice znajdują się głównie w Czechach oraz na Słowacji. <br>
            Posiadamy certyfikaty PEFC oraz FSC&reg;.
        
        <div style="clear: both;"></div>
      </div>
      <spazz-footer></spazz-footer>
    </div>
</template>

<script>
import LeftMenu from './LeftMenu.vue';
export default {
    components: {
        LeftMenu
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