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
        <td>
          <v-btn
              color="blue darken-1"
              text
              @click="fakeGoogleLogin"
          >
            AgileExpress Google Client
          </v-btn>
        </td>
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
  //http://localhost:9000/login/oauth2/code/google
  methods: {
    async login() {
      await axios.post("http://localhost:9000/login", {}, {
        params: {
          username: this.username,
          password: this.password,
        },
        headers: {
          "Accept-Encoding": "gzip, deflate, br",
          "Accept": "*/*",
          "Connection": "keep-alive",
          "Content-Type": "application/x-www-form-urlencoded",
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
            if(response.data.access_token !== "") {
              this.$toast(`Wellcome ${this.username}.`, {
                timeout: 3000,
                closeOnClick: true,
                pauseOnFocusLoss: true,
                pauseOnHover: true,
                draggable: true,
                draggablePercent: 0.6,
                showCloseButtonOnHover: false,
                hideProgressBar: true,
                icon: true,
                rtl: false
              });
            }
          },
      )
      .catch(c => {
        if(c.status !== 401) {
          this.$toast.error(`Username or Password mismatch.`, {
            timeout: 3000,
            closeOnClick: true,
            pauseOnFocusLoss: true,
            pauseOnHover: true,
            draggable: true,
            draggablePercent: 0.6,
            showCloseButtonOnHover: false,
            hideProgressBar: true,
            icon: true,
            rtl: false
          });
        }
      });
      await axios.get("http://localhost:9000/user/getUserDetails/" + this.$store.state.username , {}, {
        headers: {
          Authorization: `Bearer ${jwtService.getToken()}`,
        }
      })
          .then( response => {
            // eslint-disable-next-line no-debugger
            debugger;
                this.$store.commit("setUserId",response.data.id);
                this.$router.push({ path: '/' })
              },
          )
          .catch(c => {
            console.log(c)
          });

    },
    loginWithGoogle() {
      // eslint-disable-next-line no-debugger
      debugger;
      axios.post("https://accounts.google.com/o/oauth2/v2/auth/oauthchooseaccount?response_type=code&client_id=948966529463-0fp3bem7dlm83f4r4dp1ouq4u0c9club.apps.googleusercontent.com&scope=openid%20profile%20email&state=zjni6VX60AnU4HA7_BmuMDJ8CpVi7A3940GhkpCicEo%3D&redirect_uri=http%3A%2F%2Flocalhost%3A9000%2Flogin%2Foauth2%2Fcode%2Fgoogle&nonce=yLLEU7PpP9yqUKiLc_GH8kj1srUdU_FrQGQOdnVWfzI&flowName=GeneralOAuthFlow",{}, {
        headers: {
          Authorization: "Bearer "+ jwtService.getToken(),
        },
          }
      ).then(response => {
        // eslint-disable-next-line no-debugger
        debugger;
        jwtService.destroyToken();
        let bearerToken = response.config.headers.Authorization;
        this.token = bearerToken.substring(7, bearerToken.length);
        jwtService.saveToken(this.token);
        this.$store.commit("setPayload",jwt_decode(this.token));
        this.$store.commit("setAuthUserStatus",true);
        if(response.data.access_token !== "") {
          this.$toast(`Wellcome ${this.username}.`, {
            timeout: 3000,
            closeOnClick: true,
            pauseOnFocusLoss: true,
            pauseOnHover: true,
            draggable: true,
            draggablePercent: 0.6,
            showCloseButtonOnHover: false,
            hideProgressBar: true,
            icon: true,
            rtl: false
          });
        }
      });
    },
    async fakeGoogleLogin() {
      // eslint-disable-next-line no-debugger
      debugger;
      let fakeToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCdXJhayIsInJvbGVzIjpbIlJPTEVfQWRtaW4iXSwiaXNzIjoiL2xvZ2luL29hdXRoMi9jb2RlL2dvb2dsZSIsImV4cCI6MTY2MDI0NDkzOH0.7DtcrrXgIJFYAvAX27FJy80wiKiQbyL8_Sxl3nGUw3Y"
      jwtService.destroyToken();
      this.token = fakeToken;
      jwtService.saveToken(this.token);
      this.$store.commit("setPayload",jwt_decode(this.token));
      this.$store.commit("setAuthUserStatus",true);
      if(fakeToken !== "") {
        this.$toast(`Wellcome ${this.$store.state.username}.`, {
          timeout: 3000,
          closeOnClick: true,
          pauseOnFocusLoss: true,
          pauseOnHover: true,
          draggable: true,
          draggablePercent: 0.6,
          showCloseButtonOnHover: false,
          hideProgressBar: true,
          icon: true,
          rtl: false
        });
      }

      await axios.get("http://localhost:9000/user/getUserDetails/" + this.$store.state.username , {}, {
        headers: {
          Authorization: `Bearer ${jwtService.getToken()}`,
        }
      })
          .then( response => {
                // eslint-disable-next-line no-debugger
                debugger;
                this.$store.commit("setUserId",response.data.id);
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
