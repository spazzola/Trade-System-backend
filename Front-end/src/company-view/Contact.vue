<template>
    <div>
        <left-menu></left-menu>
        <div class="main">
            <div class="rectangle">
                <div id="title"> Forest Industry S.A.</div>
                <div id="borderBottom"></div>
                </div>

                <div class="contactTile" style="left: 259px; margin-top: 10px;" >
                <h2><strong>Prezes firmy</strong></h2>
                Adam Szczotka <br>
                tel.: +48 602 597 988 <br>
                adamdalibor2019@gmail.com <br> <br>
            </div>

            <div class="contactTile"  style="float: left;">
                <h2>Dyrektor handlowy</h2>
                Dalibor Leher <br>
                tel.: +48 797 860 707 <br>
                tel.: +420 123 456 789
                adamdalibor2019@gmail.com <br> <br>
            </div>
        
            <div class="contactTile" style="float: right; right: 10px;">
                <h2>Technik administracji</h2>
                Bartosz Wisiński <br>
                tel.: +48 505 605 469 <br>
                adamdalibor2019@gmail.com <br> <br>
            </div>
            <div style="clear: both;"></div>
                
            <div class="contactTile" style="left: 210px; width: 350px; bottom: 25px;">
            <h2>Dane firmy</h2>
            Rycerka Górna 283C<br>
            34-370 Rajcza <br>
            NIP: 553 100 62 39 <br>
            IBAN: PL18 1020 1390 0000 6702 0642 7589 <br>
            SWIFT: BPKOPLPW
            </div>
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
.contactTile
{
    width: 250px;
    font-size: 15px;
    margin-top: 20px;
    position: relative;
}

h2
{
    font-size: 20px;
    line-height: 15px;
    color: #ffffff;
    letter-spacing: 1px;
}
</style>