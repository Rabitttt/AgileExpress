<template>
  <div>
    <v-row style="height: 90vh">
      <!-- BACKLOGS -->
      <v-col
          class="col-3">
        <span style="display: none">{{this.$store.state.selectedProject}}</span>
        <div class="d-flex flex-row justify-content-between px-6 mt-5">
          <div class="d-flex justify-content-center align-center">
            <h5><strong>Backlogs</strong></h5>
          </div>
          <CreateBacklog
              v-if="this.$store.getters.isRoleTeamLeaderOrHigher"
              :members="memberUsernames"
          >
          </CreateBacklog>
        </div>
        <div class="drop-zone backlog-drop-zone"
             style="height: 80vh; overflow-y: auto"
             @drop="onDrop($event, 'backlog')"
             @dragover.prevent
             @dragenter.prevent
        >
          <div
              v-for="(item,index) in selectedProject.backlogTasks"
              v-bind:key="index"
              class="drag-el"
              draggable
              @dragstart="startDrag($event, item,'backlog')"
          >
            <span v-if="item.status === 'backlog'">
              <TaskCard :is-disabled="false" :task="item"></TaskCard>
            </span>
          </div>
        </div>
      </v-col>
      <!-- Task Management -->
      <v-col
          class="col-6 d-flex flex-row"
          style="overflow-x: auto; height: 93vh;"
      >
        <div
            style="min-width: 300px; height: 89vh; border: 1px solid lightgrey"
            v-for="(taskStatus,index) in selectedProject.taskStatus"
            v-bind:key="index"
        >
          <h3>{{taskStatus.status}}</h3>
          <div class="drop-zone"
               v-bind:key="index"
               @drop="onDrop($event, taskStatus.status)"
               @dragover.prevent
               @dragenter.prevent
               style="overflow-y: auto"
          >
            <!-- iterate over task component -->
            <span v-for="(task,index) in selectedProject.sprints[sprintIndex].tasks"
                  v-bind:key="index">
              <div
                  class="drag-el"
                  draggable
                  @dragstart="startDrag($event, task,'sprints')"
                  v-if="taskStatus.status === task.status"
              >
                <span v-if="taskStatus.status === task.status">
                  <TaskCard :is-disabled="isCardDisabled" :task="task"></TaskCard>
                </span>
              </div>
            </span>
          </div>
        </div>
      </v-col>
      <!-- Sprints -->

      <v-col class="col-3 d-flex flex-column justify-content-between" style="height: 93vh;">
        <div>
          <div class="d-flex flex-row justify-content-between px-6">
            <div class="d-flex flex-column justify-content-center align-center">
              <h5><strong>Sprints</strong></h5>
            </div>
            <CreateSprint v-if="this.$store.getters.isRoleProjectManagerOrHigher">
            </CreateSprint>
          </div>
          <v-tabs
              v-model="tab"
              background-color="transparent"
              grow
          >
            <v-tab
                v-for="item in items"
                :key="item"
            >
              {{ item }}
            </v-tab>
          </v-tabs>
          <v-tabs-items v-model="tab">
            <v-tab-item
                v-for="item in items"
                :key="item"
            >
              <div
                  style="background-color: Rgb(250,250,250)"
                  v-for="(item,index) in activeSprints"
                  v-bind:key="index"
              >
                <SprintCard :sprint="item"
                            @handleClick="onSprintChanged"
                            style="margin: 30px;">
                </SprintCard>
              </div>
            </v-tab-item>
          </v-tabs-items>
        </div>
      </v-col>

    </v-row>
  </div>
</template>

<script>
import CreateBacklog from "@/components/modal/CreateBacklog";
import CreateSprint from "@/components/modal/CreateSprint";
import axios from "axios";
import jwtService from "@/helpers/JwtService";
import SprintCard from "@/components/SprintCard";
import TaskCard from "@/components/TaskCard";
export default {
  name: "ProjectManagement",
  components: {TaskCard, SprintCard, CreateBacklog,CreateSprint},
  data () {
    return {
      //memberUsernames: [],
      sprintIndex: 0,
      tab: null,
      items: [
        'active', 'planned', 'completed',
      ],
      text: 'quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.',
    }
  },
  created() {
    this.$store.dispatch("setSelectedProject",this.$route.params.id)
    this.tab = 0; //items array index
  },
  methods: {
    startDrag(evt, item,itemFrom) {
      evt.dataTransfer.dropEffect = 'move'
      evt.dataTransfer.effectAllowed = 'move'
      evt.dataTransfer.setData('itemID', item.id)
      evt.dataTransfer.setData('itemFrom', itemFrom)
      evt.dataTransfer.setData('taskAssigneeId',item.assignee.id)
    },
    async onDrop(evt, taskStatus) {
      if(this.selectedProject.sprints[this.sprintIndex].sprintState === 'completed' && taskStatus!=="backlog") {
        //Sprint is Completed
        this.$toast.warning("Sprint is Completed.", {
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
        return null;
      }
      const itemID = evt.dataTransfer.getData('itemID')
      const itemFrom = evt.dataTransfer.getData('itemFrom')
      const taskAssigneeId = evt.dataTransfer.getData('taskAssigneeId')

      // eslint-disable-next-line no-debugger
      debugger;
      if(taskAssigneeId !== this.$store.state.userId && !this.$store.getters.isRoleTeamLeaderOrHigher) {
        this.$toast.warning("Can't change state if task is not yours", {
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
        return null;
      }

      console.log(itemID, itemFrom,taskStatus);
      if (itemFrom === 'sprints' && taskStatus !== 'backlog') {
        await axios.post("http://localhost:9000/task/changeStatus", {}, {
          params: {
            id: itemID,
            sprintId: this.selectedProject.sprints[this.sprintIndex].id,
            newStatus: taskStatus,
          },
          headers: {
            Authorization: "Bearer "+ jwtService.getToken(),
            "Accept-Encoding": "gzip, deflate, br",
            "Accept": "*/*",
            "Connection": "keep-alive",
          }
        })
            .then( response => {
                  this.$store.state.selectedProject.sprints[this.sprintIndex].tasks = response.data;
                },
            )
            .catch(c => {
              console.log(c)
            });
      }
      if (itemFrom === 'backlog' && taskStatus !== 'backlog') {
        await axios.post("http://localhost:9000/task/backlogToSprint", {}, {
          params: {
            id: itemID,
            sprintId: this.selectedProject.sprints[this.sprintIndex].id,
            newStatus: taskStatus,
            projectId: this.selectedProject.id,
          },
          headers: {
            Authorization: "Bearer "+ jwtService.getToken(),
            "Accept-Encoding": "gzip, deflate, br",
            "Accept": "*/*",
            "Connection": "keep-alive",
          }
        })
            .then( response => {

                  this.$store.state.selectedProject = response.data;
                },
            )
            .catch(c => {
              console.log(c)
            });
      }
      if (itemFrom === 'sprints' && taskStatus === 'backlog') {
        await axios.post("http://localhost:9000/task/sprintToBacklog", {}, {
          params: {
            id: itemID,
            sprintId: this.selectedProject.sprints[this.sprintIndex].id,
            projectId: this.selectedProject.id,
          },
          headers: {
            Authorization: "Bearer "+ jwtService.getToken(),
            "Accept-Encoding": "gzip, deflate, br",
            "Accept": "*/*",
            "Connection": "keep-alive",
          }
        })
            .then( response => {
                  this.$store.state.selectedProject = response.data;
                },
            )
            .catch(c => {
              console.log(c)
            });
      }

      /*
      if(itemFrom === 'sprints') {
        let selectedTask = this.selectedProject.sprints[0].tasks.find((item) => item.id === itemID)
        delete this.selectedProject.sprints[0].tasks[selectedTask.id];

        if(taskStatus === 'backlog') {
          selectedTask.status = 'backlog'
          this.selectedProject.backlogTasks.push(selectedTask);
        }
        else {
          selectedTask.status = taskStatus.status;
          this.selectedProject.sprints[0].tasks.push(selectedTask);
        }
      }
      if(itemFrom === 'backlog') {
        let selectedBacklog = this.selectedProject.backlogTasks.find((item) => item.id === itemID)
        delete this.selectedProject.backlogTasks[selectedBacklog];
        selectedBacklog.status = taskStatus.status;
        this.selectedProject.sprints[0].tasks.push(selectedBacklog);
      }
       */
    },
    /*
    onDropFromBacklog(evt,taskStatus) {

      const itemID = evt.dataTransfer.getData('itemID')
      let selectedBacklog = this.selectedProject.backlogTasks.find((item) => item.id === itemID);
      selectedBacklog.status = taskStatus.status;
      this.selectedProject.sprints[0].tasks.push(selectedBacklog);
    }
     */
    onSprintChanged(sprintId) {
      this.sprintIndex = this.selectedProject.sprints.findIndex((item) => item.id === sprintId);
    },
  },
  computed: {
    selectedProject() {
      return this.$store.state.selectedProject;
    },
    memberUsernames() {
      return this.$store.state.selectedProject.members.map(member  => member.username);
    },
    //Split sprints from sprintStates, Active , Planned ,
    activeSprints() {
      let state = this.items[this.tab];
      return  this.selectedProject.sprints.filter((item) => item.sprintState === state);
    },
    isCardDisabled() {
      return this.selectedProject.sprints[this.sprintIndex].sprintState === 'completed'
    }
  }
}
</script>

<style scoped>
.drop-zone {
  margin-bottom: 10px;
  padding: 10px;
  height: 85vh;
}
.drag-el {
  margin-bottom: 10px;
  padding: 5px;
}
.v-window {
  background-color: Rgb(250, 250, 250) !important;
}
h5 {
  margin: 0;
}
.col-3 {
  height: 85vh;
}
.backlog-drop-zone {
  height: 70vh !important;
}
</style>
