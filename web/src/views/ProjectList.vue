<template>
  <div class="container">
    <div class="row">
      <div class="col-8">
        <div>
          <SearchBar></SearchBar>
        </div>
        <div>
        <h5><strong>Projects</strong></h5>
        <project-card
            v-for="(project,index) in data"
            v-bind:key="index"
            :project="project"
            @handleSelectedProject="setProjectUser"
        ></project-card>
        </div>
      </div>
      <div class="col-4">
        <h5><strong>Users</strong></h5>
        <div>{{this.projectManager.username}}</div>
        <div>{{this.projectTeamLeader.username}}</div>
        <div v-for="(user,index) in projectMembers" v-bind:key="index">{{user.username}}</div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios"
import jwtService from "@/helpers/JwtService.js";
import ProjectCard from "@/components/ProjectCard";
import SearchBar from "@/components/SearchBar";

  export default {
    name: 'ProjectList',
    components: {SearchBar, ProjectCard},
    data: () => ({
      data: {},
      projectMembers: [],
      projectTeamLeader: "",
      projectManager: "",
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
        // eslint-disable-next-line no-debugger
        debugger;
        this.projectMembers = projectMembers;
        this.projectTeamLeader = teamLeader;
        this.projectManager = projectManager;
      },
    },
  }
</script>
