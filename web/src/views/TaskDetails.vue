<template>
  <div class="container">
    <v-row>
    Hellooooo {{this.$route.params.id}}
    <p>{{task}}</p>
    </v-row>
    <v-row class="justify-content-between">
      <v-col class="col-3">
        <h4>Logs</h4>
      </v-col>
      <v-col class="col-2">
        <AddLogToTask :task-id="this.task.id"></AddLogToTask>
      </v-col>
    </v-row>
    <v-row>
      {{task.taskLogs}}
    </v-row>
  </div>
</template>

<script>
import axios from "axios";
import jwtService from "@/helpers/JwtService";
import AddLogToTask from "@/components/modal/AddLogToTask";

export default {
  name: "TaskDetails",
  components: {AddLogToTask},
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
    },
  }
}
</script>

<style scoped>

</style>
