<template>
  <v-app>
    <div id="app">
      <Navbar></Navbar>
      <router-view/>
    </div>
  </v-app>
</template>

<style lang="scss">

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  background-color: Rgb(250, 250, 250);
  min-height: 100vh;
}

#nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}
</style>
<script>
import Navbar from "@/components/Navbar";
export default {
  components: {Navbar}
}
</script>
