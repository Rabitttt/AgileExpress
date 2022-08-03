import Vue from 'vue'
import Vuex from 'vuex'
import axios from "axios";
import jwtService from "@/helpers/JwtService";
import AuthService from "@/helpers/AuthService";

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    // User
    isAuthenticated : AuthService.isAuthenticated(),
    username: "",
    userId: "",
    userRole: "",


    // Projects
    selectedProject:  {},
    //sprints
    selectedSprintId: "",

    allProjects: []
  },

  mutations: {
    setUserId (state,userId) {
        // eslint-disable-next-line no-debugger
        debugger;
        state.userId = userId;
        window.localStorage.setItem("user_id", userId);
    },
    setAuthUserStatus (state,status) {
        // eslint-disable-next-line no-debugger
        debugger;
      state.isAuthenticated = status;
    },
    setPayload (state,payload) {
      // eslint-disable-next-line no-debugger
      debugger;
      state.username = payload.sub;
      state.userRole = payload.roles[0];
    },
    setSelectedProject (state,project) {
      // eslint-disable-next-line no-debugger
      debugger;
      state.selectedProject = project;
    },
    setCreatedTask (state,task) {
      // eslint-disable-next-line no-debugger
      debugger;
      state.selectedProject.backlogTasks.push(task);
    },
    setCreatedSprint (state,sprint) {
      // eslint-disable-next-line no-debugger
      debugger;
      state.selectedProject.sprints.push(sprint);
    },
    setSelectedSprintId (state,sprintId) {
        state.selectedSprintId = sprintId;
    },
    setSprintStateChange (state,sprint) {
        // eslint-disable-next-line no-debugger
        debugger;
        let index = state.selectedProject.sprints.findIndex(searchSprint => searchSprint.id === sprint.id)
        state.selectedProject.sprints[index] = sprint;
    },

  },
  actions: {
    async setSelectedProject (context,id) {
      // eslint-disable-next-line no-debugger
      debugger;
       await axios.get("http://localhost:9000/project/getProject/" + id,{
        headers: {
          Authorization: "Bearer "+ jwtService.getToken(),
        }
      }).then(response => {
        // eslint-disable-next-line no-debugger
        debugger;
        context.commit("setSelectedProject",response.data);
        context.commit("setSelectedSprintId",response.data.sprints[0].id);
      });
    },
    async createTask(context,form) {
      // eslint-disable-next-line no-debugger
      debugger;
      await axios.post("http://localhost:9000/task/create/" + context.state.selectedProject.id,
          {
            ...form,
          },
          {
            headers: {
              Authorization: `Bearer ${jwtService.getToken()}`,
              "Accept-Encoding": "gzip, deflate, br",
              "Accept": "*/*",
              "Connection": "keep-alive",},
          },
      )
          .then( response => {
                // eslint-disable-next-line no-debugger
                debugger;
                let newTask = response.data;
                console.log(newTask)
                context.commit("setCreatedTask",newTask);
              },
          )
          .catch(c => {
            console.log(c)
          });
      this.dialog = false
    },
    async createSprint(context,form) {
      // eslint-disable-next-line no-debugger
      debugger;
      await axios.post("http://localhost:9000/sprint/create/" + context.state.selectedProject.id,
          {
            ...form,
          },
          {
            headers: {
              Authorization: `Bearer ${jwtService.getToken()}`,
              "Accept-Encoding": "gzip, deflate, br",
              "Accept": "*/*",
              "Connection": "keep-alive",},
          },
      )
          .then( response => {
                // eslint-disable-next-line no-debugger
                debugger;
                let newTask = response.data;
                console.log(newTask)
                context.commit("setCreatedSprint",newTask);
              },
          )
          .catch(c => {
            console.log(c)
          });
      this.dialog = false
    },
  },
  modules: {
  }
})
