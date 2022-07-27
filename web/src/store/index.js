import Vue from 'vue'
import Vuex from 'vuex'
import axios from "axios";
import jwtService from "@/helpers/JwtService";

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    // User
    token: "",
    isAuthenticated : false,
    username: "",
    userRole: "",


    // Projects
    selectedProject:  {},
    //sprints
    selectedSprintId: "",


    allProjects: [
      {
        id: "62d87f48bba99a3092358c12",
        name: "Project 0",
        description: "This is project 1",
        createdDate: "2022-07-20T22:18:48.260+00:00",
        expiryDate: null,
        creator: {
          id: "62d87cc053d2624bcabdc07e",
          username: "John Travolta",
          email: "john@gmail.com",
          password: "$2a$10$SxZpPcaQc52mH/s5NWq1D.qU5/DQVG.H/ocEYaq2/sAckKRv5HfPG",
          roles: [
            "Admin"
          ],
          target: {
            id: "62d87cc053d2624bcabdc07e",
            username: "John Travolta",
            email: "john@gmail.com",
            password: "$2a$10$SxZpPcaQc52mH/s5NWq1D.qU5/DQVG.H/ocEYaq2/sAckKRv5HfPG",
            roles: [
              "Admin"
            ]
          },
          source: {
            timestamp: 1658354880,
            date: "2022-07-20T22:08:00.000+00:00"
          }
        },
        teamLeader: {
          id: "62d87cc053d2624bcabdc082",
          username: "Jim Carry",
          email: "jim@gmail.com",
          password: "$2a$10$cQOrmQOHxJCusZFk63qr8eUMr.DxxBvRs94heVkqR.wSZ3iWGxJra",
          roles: [
            "TeamLeader"
          ],
          target: {
            id: "62d87cc053d2624bcabdc082",
            username: "Jim Carry",
            email: "jim@gmail.com",
            password: "$2a$10$cQOrmQOHxJCusZFk63qr8eUMr.DxxBvRs94heVkqR.wSZ3iWGxJra",
            roles: [
              "TeamLeader"
            ]
          },
          source: {
            timestamp: 1658354880,
            date: "2022-07-20T22:08:00.000+00:00"
          }
        },
        projectManager: {
          id: "62d87cc053d2624bcabdc084",
          username: "Arnold Schwarzenegger",
          email: "arnold@gmail.com",
          password: "$2a$10$Xo6CvcjrL6ez.YhJ5LsbCe4z/ruLVPFqdUn1ZlNu6sM2.BJS1lkUS",
          roles: [
            "ProjectManager"
          ],
          target: {
            id: "62d87cc053d2624bcabdc084",
            username: "Arnold Schwarzenegger",
            email: "arnold@gmail.com",
            password: "$2a$10$Xo6CvcjrL6ez.YhJ5LsbCe4z/ruLVPFqdUn1ZlNu6sM2.BJS1lkUS",
            roles: [
              "ProjectManager"
            ]
          },
          source: {
            timestamp: 1658354880,
            date: "2022-07-20T22:08:00.000+00:00"
          }
        },
        members: [
          {
            id: "62d87cc053d2624bcabdc07f",
            username: "Will Smith",
            email: "will@gmail.com",
            password: "$2a$10$duYBTZu3vAa9vQgRaApKX.INafDw0HE8wUsgQIgoT/8kwNurn3qHi",
            roles: [
              "TeamMember"
            ]
          },
          {
            id: "62d87cc053d2624bcabdc080",
            username: "Great Gatsby",
            email: "great@gmail.com",
            password: "$2a$10$XrINAVQamn3OXuF0qsf54eLQoJN15O.XyEiT1JIoCdnb8gCUnhjG6",
            roles: [
              "TeamMember"
            ]
          },
          {
            id: "62d87cc053d2624bcabdc080",
            username: "Great Gatsby",
            email: "great@gmail.com",
            password: "$2a$10$XrINAVQamn3OXuF0qsf54eLQoJN15O.XyEiT1JIoCdnb8gCUnhjG6",
            roles: [
              "TeamMember"
            ]
          }
        ],
        tasks: [
          "deneme",
          "heloloooo",
          "task3"
        ],
        taskStatus: [
          "status1",
          "status2",
          "status3"
        ]
      }
    ]
  },

  mutations: {
    setUserToken (state,token) {
      // eslint-disable-next-line no-debugger
      debugger;
      state.token = token;
    },
    setAuthUserStatus (state,status) {
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
    }

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
      await axios.post("http://localhost:9000/task/create",
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
      await axios.post("http://localhost:9000/sprint/create",
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
