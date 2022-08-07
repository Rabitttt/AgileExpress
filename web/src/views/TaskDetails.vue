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
      <button
          v-if="$store.getters.isRoleMemberOrHigher && this.task.assignee === null"
          class="btn btn-sm btn-success btn-block"
          v-on:click="makeMyTask">
         Assign To Me
      </button>
      <button
          v-if="$store.getters.isRoleMemberOrHigher && isMyTask"
          class="btn btn-sm btn-danger btn-block"
          v-on:click="dropTaskFromMe">
        Drop Task From Me
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

        this.task = response.data;
      });
    },
    taskLogAdded(task) {
      this.task = task;
    },
    onTaskLogDeleted(taskLogList) {

      this.task.taskLogs = taskLogList;
    },
    onTaskUpdated(task) {
      this.task = task;
    },
    onDeleteTask() {
      if(confirm(`Do you really want to delete Task '${this.task.taskName}' ?`)) {
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
                  if(response.status === 200) {
                    this.$router.push("/project/management/" + this.$store.state.selectedProject.id);
                    this.$toast.error("Task deleted successfully.", {
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
                  else {
                    this.$toast.error("Operation Failed.", {
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
              console.log(c)
            });
      }
    },
    async makeMyTask() {
      await axios.post("http://localhost:9000/task/makeMyTask/",{},{
        params: {
          taskId: this.task.id,
        },
        headers: {
          Authorization: "Bearer "+ jwtService.getToken(),
        }
      }).then(response => {
          if(response.data !== "") {
            this.task = response.data;
            this.$toast.success("Task assignee updated.", {
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
          else {
            this.$toast.error("Operation Failed.", {
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
    async dropTaskFromMe() {
      await axios.post("http://localhost:9000/task/dropTaskFromMe/",{},{
        params: {
          taskId: this.task.id,
        },
        headers: {
          Authorization: "Bearer "+ jwtService.getToken(),
        }
      }).then(response => {
        if(response.data !== "") {
          this.task = response.data;
          this.$toast.success("Task assignee updated.", {
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
        else {
          this.$toast.error("Operation Failed.", {
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
    }
  },
  computed: {
    isMyTask() {
      if(this.task.assignee !== null){
        return this.task.assignee.id === this.$store.state.userId
      }
      return false
    }
  }
}
</script>

<style scoped>

</style>
