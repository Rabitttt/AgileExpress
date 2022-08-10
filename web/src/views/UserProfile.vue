<template>
 <v-container>
   <v-row>
     <v-col cols="1"></v-col>
     <v-col cols="3">
       <img alt="default_user_image" src="../assets/user-image.jpeg" style="width:250px; height:250px; border-radius: 50%;">
       <h4 class="mt-3"><strong>{{this.userData.username}}</strong></h4>
       <div>{{this.userData.roles[0]}}</div>
       <div>{{this.userData.email}}</div>
     </v-col>
     <v-col cols="8">
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
       <v-tabs-items v-model="tab" class="mt-5">
         <v-tab-item v-for="item in items"
                     :key="item"
         >

           <div>
             <span v-if="items[tab]==='Projects'">
               <span v-if="projects.length === 0" class="d-flex flex-row">
                 <strong>
                  {{noAvailableText}}
                 </strong>
                  <img
                      alt="default_user_image"
                      src="../assets/hard-worker-cartoon.jpg"
                      style="width:250px; height:250px;">
               </span>
               <ProjectCard
                   v-for="(project,index) in projects"
                   v-bind:key="index"
                   :project="project">
               </ProjectCard>
             </span>
             <span v-if="items[tab]==='Tasks'">
               <span v-if="tasks.length === 0">
                 <strong>
                  {{noAvailableText}}
                 </strong>
                  <img
                      alt="default_user_image"
                      src="../assets/hard-worker-cartoon.jpg"
                      style="width:250px; height:250px;">
               </span>
               <TaskCard
                   style="margin-bottom: 25px;"
                   v-for="(task,index) in tasks"
                   v-bind:key="index"
                   :task="task"
                   :is-disabled="false">
               </TaskCard>
             </span>
             <span v-if="items[tab]==='Task Logs'">
               <span v-if="taskLogs.length === 0">
                 <strong>
                  {{noAvailableText}}
                 </strong>
                   <img
                       alt="default_user_image"
                       src="../assets/hard-worker-cartoon.jpg"
                       style="width:250px; height:250px;">
               </span>
               <TaskLogCard
                   v-for="(taskLog,index) in taskLogs"
                   v-bind:key="index"
                   :item="taskLog">
               </TaskLogCard>
             </span>
           </div>
         </v-tab-item>
       </v-tabs-items>
     </v-col>
   </v-row>
 </v-container>
</template>

<script>
import axios from "axios";
import jwtService from "@/helpers/JwtService";
import ProjectCard from "@/components/ProjectCard";
import TaskCard from "@/components/TaskCard";
import TaskLogCard from "@/components/TaskLogCard";

export default {
  name: "UserProfile",
  components: {TaskLogCard, ProjectCard,TaskCard},
  data() {
    return {
      tab: null,
      items: ['Projects','Tasks','Task Logs'],
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
  },
  computed: {
    noAvailableText() {
      return `There are no available ${this.items[this.tab]} for ${this.userData.username}.`
    }
  }
}
</script>

<style>
.v-window-item {
  background-color: rgb(250,250,250);
}
</style>