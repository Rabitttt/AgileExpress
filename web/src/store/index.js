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
  getters: {
      isRoleAdmin(state) {
          return state.userRole === "ROLE_Admin";
      },
      isRoleProjectManagerOrHigher(state) {
          return state.userRole === "ROLE_Admin" || state.userRole === "ROLE_ProjectManager";
      },
      isRoleTeamLeaderOrHigher(state) {
          return state.userRole === "ROLE_Admin" || state.userRole === "ROLE_ProjectManager" || state.userRole === "ROLE_TeamLeader";
      },
      isRoleMemberOrHigher(state) {
          return state.userRole === "ROLE_Admin" || state.userRole === "ROLE_ProjectManager" || state.userRole === "ROLE_TeamLeader"  || state.userRole === "ROLE_TeamMember";
      }
  },
  mutations: {
    setUserId (state,userId) {

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

      state.selectedProject = project;
    },
    setCreatedTask (state,task) {

      state.selectedProject.backlogTasks.push(task);
    },
    setCreatedSprint (state,sprint) {

      state.selectedProject.sprints.push(sprint);
    },
    setSelectedSprintId (state,sprintId) {
        state.selectedSprintId = sprintId;
    },
    setSprintStateChange (state,sprint) {

        let index = state.selectedProject.sprints.findIndex(searchSprint => searchSprint.id === sprint.id)
        state.selectedProject.sprints[index] = sprint;
    },
  },
  actions: {
    async setSelectedProject (context,id) {

       await axios.get("http://localhost:9000/project/getProject/" + id,{
        headers: {
          Authorization: "Bearer "+ jwtService.getToken(),
        }
      }).then(response => {

        context.commit("setSelectedProject",response.data);
        context.commit("setSelectedSprintId",response.data.sprints[0].id);
      });
    },
    async createTask(context,form) {

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

                let newTask = response.data;
                console.log(newTask)
                if(response.data !== "") {
                    context.commit("setCreatedTask",newTask);
                    this._vm.$toast.success("Task successfully created.", {
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
                }
                else {
                    this.$toast.error("Operation Failed.", {
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
                }
              },
          )
          .catch(c => {
            console.log(c)
          });
      this.dialog = false
    },
    async createSprint(context,form) {

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
              let newTask = response.data;
              console.log(newTask)
              if(response.data !== "") {
                  context.commit("setCreatedSprint",newTask);
                  this._vm.$toast.success("Sprint successfully created.", {
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
              }
              else {
                  this.$toast.error("Operation Failed.", {
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
              }
            })
          .catch(c => {
            console.log(c)
          });
      this.dialog = false
    },
  },
  modules: {
  }
})
