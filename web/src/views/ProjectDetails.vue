<template>
  <div>
    heloooo
    {{data}}
  </div>
</template>

<script>
import axios from "axios";
import jwtService from "@/helpers/JwtService";

export default {
  name: "ProjectDetails",
  data () {
    return {
      data: {},
    }
  },
  created() {
    let id = this.$route.params.id
    axios.get("http://localhost:9000/project/getProject/" + id,{
      headers: {
        Authorization: "Bearer "+ jwtService.getToken(),
      }
    }).then(response => {


      this.data = response.data;
      console.log(this.data)
    });
  }

}
</script>

<style scoped>

</style>
