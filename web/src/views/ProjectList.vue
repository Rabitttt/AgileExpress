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
          <h5><strong>Users</strong></h5>
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
              <div v-for="(data,index) in parsedUsers"
                   v-bind:key="index">
                <UserCard :user="data"/>
              </div>
            </v-tab-item>

          </v-tabs-items>

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
      tab: null,
      items: ['Manager','Leader','Members'],
      data: {},
      projectMembers: [],
      projectTeamLeader: {},
      projectManager: {},
    }),

    async created() {
      await this.getData();
      this.tab = 0;
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
    computed: {
      parsedUsers() {
        let array = [];
        if(this.tab===0){
          array.push(this.projectManager)
          return array;
        }
        if(this.tab === 1) {
          array.push(this.projectTeamLeader)
        }
        if(this.tab===2) {
          return this.projectMembers;
        }
        return [];
      }
    }
  }
</script>
