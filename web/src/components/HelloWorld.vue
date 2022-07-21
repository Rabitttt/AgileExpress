<template>
  <div>
    {{this.data}}
  </div>
</template>

<script>
import axios from "axios"
import jwtService from "@/helpers/JwtService.ts";

  export default {
    name: 'HelloWorld',

    data: () => ({
      data: {}
    }),

    async created() {
      await this.getData();
    },

    methods: {
      //eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huIiwicm9sZXMiOlsiUk9MRV9BZG1pbiJdLCJpc3MiOiIvbG9naW4iLCJleHAiOjE2NTg1MjQyMTZ9.BEpuvpqBHLsbHbQx5Xnww2Gj039WK_W3DWQnVr5L8fw
      async getData() {
        let token = jwtService.getToken();
        axios.get('http://localhost:9000/project/getAll',{
          headers: {
            Authorization: "Bearer "+token,
          }
        }).then(response => {
          this.data = response.data;
        });
      }
    },
  }
</script>
