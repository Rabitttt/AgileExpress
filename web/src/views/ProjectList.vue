<template>
  <div class="container">
    <div class="row">
      <div class="col-8">
        <project-card
            v-for="(project,index) in data"
            v-bind:key="index"
            :project="project"
            @handleSelectedProject="setProjectUser"
        ></project-card>
      </div>
      <div class="col-4">
        <h5>Users</h5>
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

  export default {
    name: 'ProjectList',
    components: {ProjectCard},
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
      //eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huIiwicm9sZXMiOlsiUk9MRV9BZG1pbiJdLCJpc3MiOiIvbG9naW4iLCJleHAiOjE2NTg1MjQyMTZ9.BEpuvpqBHLsbHbQx5Xnww2Gj039WK_W3DWQnVr5L8fw
      async getData() {
        axios.get('http://localhost:9000/project/getAll',{
          headers: {
            Authorization: "Bearer "+ jwtService.getToken(),
          }
        }).then(response => {
          this.data = response.data;
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
