<template>
  <v-app>
    <div id="app">
      <Navbar v-if="this.$store.state.isAuthenticated"></Navbar>
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
import AuthService from "@/helpers/AuthService";
import jwt_decode from "jwt-decode";
import JwtService from "@/helpers/JwtService";
export default {
  components: {Navbar},
  created() {
    // eslint-disable-next-line no-debugger
    debugger;
    if(AuthService.isAuthenticated()) {
      this.$store.commit("setPayload",jwt_decode(JwtService.getToken()));
      this.$store.commit("setUserId", window.localStorage.getItem("user_id"));
    }
  }
}
</script>
