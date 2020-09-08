<template>
    <div>
        <left-menu></left-menu>
            <div class="main">
                <div class="rectangle">
                <div id="title"> Forest Industry S.A. </div>
                <div id="borderBottom"></div>

                    <div class="container">
                        <div class="top">
                            <ul>
                                <li><a href="#img_1"><img src="../assets/1.jpg"></a></li>
                                <li><a href="#img_2"><img src="../assets/2.jpg"></a></li>
                                <li><a href="#img_3"><img src="../assets/3.jpg"></a></li>
                                <li><a href="#img_4"><img src="../assets/4.jpg"></a></li>
                                <li><a href="#img_5"><img src="../assets/5.jpg"></a></li>
                                <li><a href="#img_6"><img src="../assets/6.jpg"></a></li>
                                <li><a href="#img_7"><img src="../assets/7.jpg"></a></li>
                                
                            </ul>
                            <a href="#_1" class="lightbox trans" id="img_1"><img src="../assets/1.jpg"></a>
                            <a href="#_2" class="lightbox trans" id="img_2"><img src="../assets/2.jpg"></a>
                            <a href="#_3" class="lightbox trans" id="img_3"><img src="../assets/3.jpg"></a>
                            <a href="#_4" class="lightbox trans" id="img_4"><img src="../assets/4.jpg"></a>
                            <a href="#_5" class="lightbox trans" id="img_5"><img src="../assets/5.jpg"></a>
                            <a href="#_6" class="lightbox trans" id="img_6"><img src="../assets/6.jpg"></a>
                            <a href="#_7" class="lightbox trans" id="img_7"><img src="../assets/7.jpg"></a>
                            
                        </div>
                    </div>
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



.container
{
	width: 100%;
	height: 100%;
	margin-right: auto;
	margin-left: auto;
}
.trans
{
	transition: all 1s ease;
	-moz-transition: all 1s ease;
	-ms-transition: all 1s ease;
	-o-transition: all 1s ease;
	-webkit-transition: all 1s ease;
}
.top
{
	position: relative;
	display: flex;
	width: 770px;
    height: 400px;
	margin-top: 50px;
	right: 20px;
	margin-bottom: 10vh;
	margin-right: auto;
	margin-left: auto;

}
.top ul
{
	list-style: none;
	width: 100%;
	height: 100%;
	z-index: 1;
	box-sizing: border-box;
}
.top ul li
{
	/* pojedyńcze zdjęcia*/
	position: relative;
	float: left;
	width: 25%;
	height: 25%;
	overflow: hidden;
}

.top ul li::before
{
	position: absolute;
	top: 0;
	left: 0px;
	width: 100%;
	height: 100%;
	background-color: #000;
	content: '';
	color: white;
	opacity: 0.4;
	text-align: center;
	box-sizing: border-box;
	pointer-events: none;
	transition: all 0.5s ease;
	-moz-transition: all 0.5s ease;
	-ms-transition: all 0.5s ease;
	-o-transition: all 0.5s ease;
    -webkit-transition: all 0.5s ease;
}
.top ul li:hover::before
{
	opacity: 0;
	background-color: rgba(0,0,0,0.90);
}
.top ul li img
{
	width: 100%;
	height: auto;
    overflow: hidden;
}
.lightbox
{
	position: fixed;
	width: 100%;
	height: 100%;
	text-align: center;
	top: 0;
	left: 0;

	z-index: 999;
	opacity: 0;
    pointer-events: none;
}
.lightbox img
{
	max-width: 90%;
	max-height: 80%;
	position: relative;
	top: -100%;
	/* Transition */
	transition: all 1s ease;
	-moz-transition: all 1s ease;
	-ms-transition: all 1s ease;
	-o-transition: all 1s ease;
	-webkit-transition: all 1s ease;
}
.lightbox:target
{
	outline: none;
	top: 0;
	opacity: 1;
	pointer-events: auto;
	transition: all 1.2s ease;
	-moz-transition: all 1.2s ease;
	-ms-transition: all 1.2s ease;
	-o-transition: all 1.2s ease;
	-webkit-transition: all 1.2s ease;
}
.lightbox:target img
{
	top: 0;
	top: 50%;
	transform: translateY(-50%);
	-moz-transform: translateY(-50%);
	-ms-transform: translateY(-50%);
	-o-transform: translateY(-50%);
	-webkit-transform: translateY(-50%);
}
</style>