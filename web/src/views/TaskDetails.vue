<template>
  <div class="container">
    <v-row>
    Hellooooo {{this.$route.params.id}}
    <p>{{task}}</p>
    </v-row>
    <v-row>
      <h4>Logs</h4>
      <button
          class="btn btn-lg btn-primary btn-block"
          v-on:click="projectDetails"
      >Project Details</button>
       {{task.taskLogs}}
    </v-row>
  </div>
</template>

<script>
import axios from "axios";
import jwtService from "@/helpers/JwtService";

export default {
  name: "TaskDetails",
  data () {
    return {
      task: {},
    }
  },
  created() {
    this.getTaskData();
  },
  methods: {
    async getTaskData() {
      await axios.get("http://localhost:9000/task/getTask/" + this.$route.params.id,{
        headers: {
          Authorization: "Bearer "+ jwtService.getToken(),
        }
      }).then(response => {
        // eslint-disable-next-line no-debugger
        debugger;
        this.task = response.data;
      });
    }
  }
}
</script>

<style scoped>

</style>
