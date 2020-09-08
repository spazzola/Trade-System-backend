<template>
  <div id="app">
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'app',
}
</script>

<style>
body 
{
  font-family: 'Libre Baskerville', serif;
    height: 100%;
    padding: 0;
    margin: 0;
    background-color: #228B22;
    background: url("./assets/logo.jpg") no-repeat center center fixed; 
    -webkit-background-size: contain;
    -moz-background-size: contain;
    -o-background-size: contain;
    background-size: cover;

}
</style>
