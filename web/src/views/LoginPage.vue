<template>
  <div class="container" style="max-width: 17vw">
    <img alt="App_Logo" src="../assets/logo.png" style="width:250px; height:250px;">
    <form class="form-signin" method="post" action="/login">
      <h2 class="form-signin-heading">Please sign in</h2>
      <p>
        <label for="username" class="sr-only">Username</label>
        <input type="text" v-model="username" id="username" name="username" class="form-control" placeholder="Username" required autofocus>
      </p>
      <p>
        <label for="password" class="sr-only">Password</label>
        <input type="password" v-model="password" id="password" name="password" class="form-control" placeholder="Password" required>
      </p>
    </form>
    <button class="btn btn-lg btn-primary btn-block" v-on:click="login">Sign in</button>

    <h2 class="form-signin-heading">Login with OAuth 2.0</h2>
    <table class="table table-striped">
      <tr>
        <td><a href="/oauth2/authorization/google">AgileExpress Google Client</a></td>
      </tr>
    </table>
  </div>
</template>

<script>
import axios from "axios";
import jwtService from "@/helpers/JwtService.js";
import jwt_decode from "jwt-decode";

export default {
  name: "LoginPage",
  data() {
    return {
      username: '',
      password: '',
      token:"",
    }
  },
  methods: {
    async login() {

      // eslint-disable-next-line no-debugger
      debugger
      // eslint-disable-next-line no-debugger
      //debugger
      await axios.post("http://localhost:9000/login", {}, {
        params: {
          username: this.username,
          password: this.password,
        },
        headers: {
          "Accept-Encoding": "gzip, deflate, br",
          "Accept": "*/*",
          "Connection": "keep-alive",
        }
      })
      .then( response => {
            // eslint-disable-next-line no-debugger
            debugger;
            jwtService.destroyToken();
            this.token = response.data.access_token;
            jwtService.saveToken(this.token);

            this.$store.commit("setPayload",jwt_decode(this.token));
            this.$store.commit("setAuthUserStatus",true);
            this.$router.push({ path: '/' })
          },
      )
      .catch(c => {
        console.log(c)
      });

    }
  },

}
</script>

<style scoped>

</style>
