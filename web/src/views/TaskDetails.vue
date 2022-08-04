<template>
  <div class="container">
    <v-row>
    Hellooooo {{this.$route.params.id}}
    <p>{{task}}</p>
      <UpdateTask
        v-if="$store.getters.isRoleTeamLeaderOrHigher"
        :task="this.task"
        @handleUpdateItem="onTaskUpdated"
      ></UpdateTask>
      <button
          v-if="$store.getters.isRoleTeamLeaderOrHigher"
          class="btn btn-sm btn-primary btn-block"
          v-on:click="onDeleteTask">
        Delete Task
      </button>
    </v-row>
    <v-row class="justify-content-between">
      <v-col class="col-3">
        <h4>Logs</h4>
      </v-col>
      <v-col class="col-2">
        <AddLogToTask
            v-if="$store.getters.isRoleMemberOrHigher"
            :task-id="this.task.id"
            @taskLogCreateHandler="taskLogAdded"></AddLogToTask>
      </v-col>
    </v-row>
    <v-row>
      <div v-for="(item,index) in task.taskLogs" v-bind:key="index">
        <TaskLogCard
            :item="item"
            :task-id="task.id"
            @handleDeletedItem="onTaskLogDeleted"
        ></TaskLogCard>
      </div>
    </v-row>
  </div>
</template>

<script>
import axios from "axios";
import jwtService from "@/helpers/JwtService";
import AddLogToTask from "@/components/modal/AddLogToTask";
import TaskLogCard from "@/components/TaskLogCard";
import UpdateTask from "@/components/modal/UpdateTask";

export default {
  name: "TaskDetails",
  components: {UpdateTask, TaskLogCard, AddLogToTask},
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
    taskLogAdded(task) {
      this.task = task;
    },
    onTaskLogDeleted(taskLogList) {
      // eslint-disable-next-line no-debugger
      debugger;
      this.task.taskLogs = taskLogList;
    },
    onTaskUpdated(task) {
      this.task = task;
    },
    onDeleteTask() {
      // eslint-disable-next-line no-debugger
      debugger;
      axios.post("http://localhost:9000/task/delete", {},
          {
            params: {
              taskId: this.task.id,
              projectId: this.$store.state.selectedProject.id,
              sprintId: this.$store.state.selectedSprintId,
            },
            headers: {
              Authorization: `Bearer ${jwtService.getToken()}`,
              "Accept-Encoding": "gzip, deflate, br",
              "Accept": "*/*",
              "Connection": "keep-alive",},
          },
      )
          .then( response => {
                // eslint-disable-next-line no-debugger
                debugger;
                if(response.status === 200) {
                  this.$router.push("/project/management/" + this.$store.state.selectedProject.id);
                }
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
