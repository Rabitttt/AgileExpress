<template>
  <div class="search-bar d-flex flex-row">
    <div>
      <input v-model="searchText" class="form-control" type="search" placeholder="Search" aria-label="Search">
    </div>
    <div class="search-results" v-if="searchResults.length !== 0">
      <ul class="list-group" v-if="searchResults.users.length !== 0">
        <li class="list-group-item" v-for="(item,index) in searchResults.users" v-bind:key="item">
          <v-row>
            <v-col cols="2 pl-4">
              <div class="m-0 p-0">
                <img class="d-flex justify-content-center align-center text-center" alt="default_user_image" src="../assets/defaultUser.png" style="width:50px; height:50px;">
              </div>
            </v-col>
            <v-col cols="6" class="d-flex flex-column justify-content-around">
              <v-card-title class="m-0 p-0">
                <strong>
                  {{item.username}}
                </strong>
              </v-card-title>
              <v-card-subtitle class="m-0 p-0" style="color: rgba(0, 0, 0, 0.6); text-align: start">
                {{item.email}}
              </v-card-subtitle>
            </v-col>
            <v-col cols="4" class="px-4 d-flex justify-content-end align-center">
              <button class="btn btn-outline-primary" v-on:click="changePageToUserProfile(index)">User</button>
            </v-col>
          </v-row>
        </li>
      </ul>
      <ul class="list-group" v-if="searchResults.projects.length !== 0">
        <li class="list-group-item" v-for="(item,index) in searchResults.projects" v-bind:key="index">
          <v-card elevation="0">
            <v-card-title class="d-flex flex-row justify-content-between">
              {{ item.name }}
              <button class="btn btn-outline-success" v-on:click="changePageToProject(index)">Project</button>
            </v-card-title>
            <v-card-subtitle style="text-align: start">
              Project
            </v-card-subtitle>
          </v-card>
        </li>
      </ul>
      <ul class="list-group" v-if="searchResults.tasks.length !== 0">
        <li class="list-group-item" v-for="(item,index) in searchResults.tasks" v-bind:key="index">
          <v-card elevation="0">
            <v-card-title class="d-flex flex-row justify-content-between">
              {{item.taskName}}
              <button class="btn btn-outline-warning" v-on:click="changePageToTaskDetails(index)">Task Details</button>
            </v-card-title>
            <v-card-subtitle>
              <div style="text-align: start">
                <strong> Status: </strong> {{item.status}}
              </div>
              <div style="text-align: start">
                <strong> Story Point: </strong>
                <i class="far fa-gem mt-1"></i>
                {{item.storyPoint}}
              </div>
              <div style="text-align: start">
                <strong>Description:</strong> {{item.description}}
              </div>
            </v-card-subtitle>
          </v-card>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import jwtService from "@/helpers/JwtService";

export default {
  name: "SearchBar",
  data () {
    return {
      searchText: "",
      searchResults: [],
    }
  },
  methods: {
    async search() {

        await axios.get("http://localhost:9000/search/" + this.searchText,{
          headers: {
            Authorization: "Bearer "+ jwtService.getToken(),
          }
        }).then(response => {

          console.log("Search Result = "+response.data);
          this.searchResults = response.data;
        });
    },
    changePageToProject(index) {
      // eslint-disable-next-line no-debugger
      debugger
      let id = this.searchResults.projects[index].id
      this.$router.push({path:"/project/management/" + id});
      this.searchText = "";
    },
    changePageToUserProfile(index) {
      // eslint-disable-next-line no-debugger
      debugger
      let id = this.searchResults.users[index].id
      this.$router.push({path:"/user/profile/" + id });
      this.searchText = "";
    },
    changePageToTaskDetails(index) {
      // eslint-disable-next-line no-debugger
      debugger
      let id = this.searchResults.tasks[index].id
      this.$router.push({path:"/task/details/" + id});
      this.searchText = "";
    }
  },
  watch: {
    searchText (){

      if(this.searchText === "") {
        this.searchResults = [];
      }
      else {
        this.search();
      }
    }
  }
}
</script>

<style scoped>
.search-results {
  z-index: 9999;
  display: block;
  position: absolute;
  width: 400px;
  color: black;
  margin-top: 45px;
  margin-left: -100px;
}
</style>
