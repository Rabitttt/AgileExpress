<template>
  <div class="search-bar d-flex flex-row">
    <div>
      <input v-model="searchText" class="form-control" type="search" placeholder="Search" aria-label="Search">
      <button class="btn btn-outline-success" v-on:click="search">Search</button>
    </div>
    <div class="search-results bg-white" v-if="searchResults.length !== 0">
      <ul class="list-group" v-if="searchResults.users.length !== 0">
        <li class="list-group-item" v-for="item in searchResults.users" v-bind:key="item">
          <p>{{ item }}</p>
          <button class="btn btn-outline-success" v-on:click="changePageToProject(1)">Search</button>
        </li>
      </ul>
      <ul class="list-group" v-if="searchResults.projects.length !== 0">
        <li class="list-group-item" v-for="(item,index) in searchResults.projects" v-bind:key="index">
          <p>{{ item }}</p>
          <button class="btn btn-outline-success" v-on:click="changePageToProject(index)">Search</button>
        </li>
      </ul>
      <ul class="list-group" v-if="searchResults.tasks.length !== 0">
        <li class="list-group-item" v-for="(item,index) in searchResults.tasks" v-bind:key="index">
          <p>{{ item }}</p>
          <button class="btn btn-outline-success" v-on:click="changePageToProject(index)">Search</button>
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
        // eslint-disable-next-line no-debugger
        debugger;
        await axios.get("http://localhost:9000/search/" + this.searchText,{
          headers: {
            Authorization: "Bearer "+ jwtService.getToken(),
          }
        }).then(response => {
          // eslint-disable-next-line no-debugger
          debugger;
          console.log("Search Result = "+response.data);
          this.searchResults = response.data;
        });
    },
    changePageToProject(index) {
      this.$router.push({path:"project/management/" + this.searchResults.projects[index].id});
    }
  },
  watch: {
    searchText (){
      // eslint-disable-next-line no-debugger
      debugger;
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
  height: 250px;
  width: 250px;
  color: black;
  margin-top: 45px;
}
</style>
