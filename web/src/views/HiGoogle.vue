<template>
  <div>
   {{token}}
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "HiGoogle",
  data() {
    return {
      token: "",
    }
  },
  async created() {
    await axios.get("http://localhost:9000/hiGoogle",{
      headers: {
        Authorization: "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJCdXJhayIsInJvbGVzIjpbIlRlYW1NZW1iZXIiXSwiaXNzIjoiL2xvZ2luL29hdXRoMi9jb2RlL2dvb2dsZSIsImV4cCI6MTY2MDIxMDI0N30.DY54HGx8c4ARfvdFJ9xGU2uXQwDYMEAOG63abFcvE6E",
      }
    }).then(response => {
      // eslint-disable-next-line no-debugger
      debugger;
      let bearerToken = response.config.headers.Authorization;
      let token = bearerToken.substring(7, bearerToken.length);
      console.log(response,bearerToken,token)
    });
  }
}
</script>

<style scoped>

</style>