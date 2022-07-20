<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <div class="social-login">
      <a class="btn btn-block social-btn google" :href="uri" target="_blank">
        <img src={googleLogo}  alt="Google" /> Log in with Google
      </a>
    </div>
  </div>
</template>

<script>
import {GOOGLE_AUTH_URL} from "@/constants";

export default {
  name: 'HelloWorld',
  props: {
    msg: String,
  },
  computed: {
    uri() {
      return GOOGLE_AUTH_URL
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
