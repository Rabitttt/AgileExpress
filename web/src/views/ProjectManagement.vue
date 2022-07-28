<template>
  <div >
    <v-row>
      <v-col
          class="col-3">
        {{this.selectedProject.sprints[sprintIndex].id}}

        <h5><strong>Backlogs</strong></h5>
        <div class="drop-zone"
             @drop="onDrop($event, 'backlog')"
             @dragover.prevent
             @dragenter.prevent
        >
          <div
              v-for="(item,index) in selectedProject.backlogTasks"
              v-bind:key="index"
              style="border-bottom:1px solid black"
              class="drag-el"
              draggable
              @dragstart="startDrag($event, item,'backlog')"
          >
            <span v-if="item.status === 'backlog'">
              <TaskCard :task="item"></TaskCard>
            </span>
          </div>
        </div>

        <CreateBacklog
            :members="memberUsernames"
        >
        </CreateBacklog>
      </v-col>
      <v-col
          class="col-6 d-flex flex-row">
        <div
            style="width: 350px; height:70vh; border: 1px solid lightgrey"
            v-for="(taskStatus,index) in selectedProject.taskStatus"
            v-bind:key="index"
        >
          <h3>{{taskStatus.status}}</h3>
          <div class="drop-zone"
               v-bind:key="index"
               @drop="onDrop($event, taskStatus.status)"
               @dragover.prevent
               @dragenter.prevent
          >
            <!-- iterate over task component -->
            <div
                v-for="(task,index) in selectedProject.sprints[sprintIndex].tasks"
                v-bind:key="index"
                class="drag-el"
                draggable
                @dragstart="startDrag($event, task,'sprints')"
            >
              <span v-if="taskStatus.status === task.status" >
                <TaskCard :task="task"></TaskCard>
              </span>
            </div>
          </div>
        </div>
      </v-col>
      <v-col
          class="col-3">
        <h5><strong>Sprints</strong></h5>
        <div
            v-for="(item,index) in selectedProject.sprints"
            v-bind:key="index"
            style="border-bottom:1px solid black"
        >
          <SprintCard :sprint="item" @handleClick="onSprintChanged"></SprintCard>
        </div>
        <CreateSprint>
        </CreateSprint>
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
    }
  },
  created() {
    this.$store.dispatch("setSelectedProject",this.$route.params.id)
    this.setUsernameArray();
  },
  methods: {
    createSprint() {
      console.log("Create Sprint");
    },
    createBacklog() {
      console.log("Create Backlog");
    },
    startDrag(evt, item,itemFrom) {
      // eslint-disable-next-line no-debugger
      debugger;
      evt.dataTransfer.dropEffect = 'move'
      evt.dataTransfer.effectAllowed = 'move'
      evt.dataTransfer.setData('itemID', item.id)
      evt.dataTransfer.setData('itemFrom', itemFrom)
    },
    async onDrop(evt, taskStatus) {
      // eslint-disable-next-line no-debugger
      debugger;
      const itemID = evt.dataTransfer.getData('itemID')
      const itemFrom = evt.dataTransfer.getData('itemFrom')


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
                  // eslint-disable-next-line no-debugger
                  debugger;
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
                  // eslint-disable-next-line no-debugger
                  debugger;
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
                  // eslint-disable-next-line no-debugger
                  debugger;
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
      // eslint-disable-next-line no-debugger
      debugger;
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
      // eslint-disable-next-line no-debugger
      debugger
      return this.$store.state.selectedProject.members.map(member  => member.username);
    }
  }
}
</script>

<style scoped>
.drop-zone {
  background-color: #eee;
  margin-bottom: 10px;
  padding: 10px;
  height: 30vh;
}
.drag-el {
  background-color: #fff;
  margin-bottom: 10px;
  padding: 5px;
}
</style>
