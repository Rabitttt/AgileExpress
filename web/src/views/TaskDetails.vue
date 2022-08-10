<template>
  <div class="container">
    <v-row>
      <v-card class="px-7 py-7">
        <h5>Task Details</h5>
        <v-divider class="mx-6 mb-1" color="black"></v-divider>

        <v-card-title class="py-2 d-flex flex-row justify-content-between">
          {{task.taskName}}
          <div class="assignee-card" style="max-width: 600px;">
            <h6><strong> Assignee</strong></h6>
            <UserCard :user="task.assignee"></UserCard>
          </div>
        </v-card-title>
        <v-card-subtitle class="d-flex flex-column">
          <div style="text-align: start">
            <strong> Status: </strong> {{task.status}}
          </div>
          <div style="text-align: start">
            <strong> Story Point: </strong>
            <i class="far fa-gem mt-1"></i>
            {{task.storyPoint}}
          </div>
          <div style="text-align: start">
            <strong> Project: </strong>
            <router-link style="text-decoration: none" :to="'/project/management/'+project.id">{{project.name}}</router-link>
          </div>
        </v-card-subtitle>
        <v-card-text style="text-align: start">
          <strong> Description:</strong> {{task.description}}
        </v-card-text>
      </v-card>

      <UpdateTask
        v-if="$store.getters.isRoleTeamLeaderOrHigher"
        :task="this.task"
        @handleUpdateItem="onTaskUpdated"
      ></UpdateTask>
      <button
          v-if="$store.getters.isRoleTeamLeaderOrHigher"
          class="btn btn-sm btn-danger btn-block mt-2"
          v-on:click="onDeleteTask">
        Delete Task
      </button>
      <button
          v-if="$store.getters.isRoleMemberOrHigher && this.task.assignee === null"
          class="btn btn-sm btn-success btn-block mt-2"
          v-on:click="makeMyTask">
         Assign To Me
      </button>
      <button
          v-if="$store.getters.isRoleMemberOrHigher && isMyTask"
          class="btn btn-sm btn-danger btn-block mt-2"
          v-on:click="dropTaskFromMe">
        Drop Task From Me
      </button>
    </v-row>
    <v-row class="justify-content-between px-6">
      <v-col class="col-3">
        <h4>Task Logs</h4>
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
import UserCard from "@/components/UserCard";

export default {
  name: "TaskDetails",
  components: {UserCard, UpdateTask, TaskLogCard, AddLogToTask},
  data () {
    return {
      task: {},
      project: {},
      haveAccessTask: false,
    }
  },
  created() {
    this.getTaskData();
    this.haveAccessTask = this.haveUserAccessToDeleteProject();
  },
  methods: {
    async getTaskData() {
      await axios.get("http://localhost:9000/task/getTask/" + this.$route.params.id,{
        headers: {
          Authorization: "Bearer "+ jwtService.getToken(),
        }
      }).then(response => {

        this.task = response.data;
        this.getProjectByTaskId();
      });
    },
    async getProjectByTaskId() {
      await axios.get("http://localhost:9000/project/getProjectByTaskId/" + this.$route.params.id,{
        headers: {
          Authorization: "Bearer "+ jwtService.getToken(),
        }
      }).then(response => {

        this.project = response.data;
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
      if(!this.haveUserAccessToDeleteProject()) {
        this.$toast.error("Don't have permission.", {
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
        return;
      }
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
    },
    haveUserAccessToDeleteProject () {
      // eslint-disable-next-line no-debugger
      debugger;
      let users = [];
      users.push(this.project.projectManager)
      users.push(this.project.creator)
      users.push(this.project.teamLeader)
      for (let index in users) {
        if(users[index].id === this.$store.state.userId) {
          return true;
        }
      }
      if(this.$store.state.userRole === "ROLE_Admin") {
        return true;
      }
      return false;
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
