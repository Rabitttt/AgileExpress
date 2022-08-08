<template>
  <div class="container">
    <div class="row">
      <div class="col-1"></div>
      <div class="col-7">
        <div>
          <!--
        <h5><strong>Projects</strong></h5>
          -->
        <project-card
            v-for="(project,index) in data"
            v-bind:key="index"
            :project="project"
            @handleSelectedProject="setProjectUser"
        ></project-card>
        </div>
      </div>
      <div class="col-4">
        <UserCard v-for="(data,index) in this.projectMembers"
                  v-bind:key="index"
                  :user="data"/>
        <UserCard :user="projectManager"/>
        <UserCard :user="projectTeamLeader"/>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios"
import jwtService from "@/helpers/JwtService.js";
import ProjectCard from "@/components/ProjectCard";
import UserCard from "@/components/UserCard";

  export default {
    name: 'ProjectList',
    components: {UserCard, ProjectCard},
    data: () => ({
      data: {},
      projectMembers: [],
      projectTeamLeader: {},
      projectManager: {},
    }),

    async created() {
      await this.getData();
    },

    methods: {
      async getData() {
        let queryExtension="";
        if (this.$store.state.userRole === "ROLE_Admin")
        {
          queryExtension = "getAll"
        }else {
          queryExtension = "getAllProjectsByUser"
        }

        axios.get('http://localhost:9000/project/' + queryExtension,{
          headers: {
            Authorization: "Bearer "+ jwtService.getToken(),
          }
        }).then(response => {

          this.data = response.data;
          //set first project data to initial data for users
          this.projectManager = this.data[0].projectManager;
          this.projectTeamLeader = this.data[0].teamLeader;
          this.projectMembers = this.data[0].members;
        });
      },
      setProjectUser(projectMembers,teamLeader,projectManager) {
        this.projectMembers = projectMembers;
        this.projectTeamLeader = teamLeader;
        this.projectManager = projectManager;
      },
    },
  }
</script>
