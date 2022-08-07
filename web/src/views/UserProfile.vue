<template>
 <div>
   <div>
    {{userData}}
   </div>
   <div class="mt-6">
    {{projects}}
   </div>
   <div class="mt-6">
     {{tasks}}
   </div>
   <div class="mt-6">
     {{taskLogs}}
   </div>
 </div>
</template>

<script>
import axios from "axios";
import jwtService from "@/helpers/JwtService";

export default {
  name: "UserProfile",
  data() {
    return {
      userData: {},
      projects: [],
      tasks: [],
      taskLogs: [],
    }
  },
  created() {
    let id = this.$route.params.id
    axios.get("http://localhost:9000/user/profile/" + id,{
      headers: {
        Authorization: "Bearer "+ jwtService.getToken(),
      },
    }).then(response => {
      this.userData = response.data;
    });
    axios.get("http://localhost:9000/project/getProjectsByUserId/"+id,{
      headers: {
        Authorization: "Bearer "+ jwtService.getToken(),
      },
    }).then(response => {
      this.projects = response.data;
    });
    axios.get("http://localhost:9000/task/getTasksByUserId/"+id,{
      headers: {
        Authorization: "Bearer "+ jwtService.getToken(),
      },
    }).then(response => {
      this.tasks = response.data;
    });
    axios.get("http://localhost:9000/taskLog/getTaskLogsByUserId/"+id,{
      headers: {
        Authorization: "Bearer "+ jwtService.getToken(),
      },
    }).then(response => {
      this.taskLogs = response.data;
    });
  }
}
</script>

<style scoped>

</style>