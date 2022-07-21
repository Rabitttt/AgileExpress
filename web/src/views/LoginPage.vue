<template>
  <div class="container">
    <h3>Token = {{this.token}}</h3>
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
      <button class="btn btn-lg btn-primary btn-block" v-on:click="login">Sign in</button>
    </form>
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
import jwtService from "@/helpers/JwtService.ts";

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
            console.log(response)
            this.token = response.data.access_token;
            jwtService.saveToken(this.token);
            console.log("deneme");
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
