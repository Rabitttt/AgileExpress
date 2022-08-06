<template>
  <div class="create-project container">
    <v-row>
      <!--
      <v-col
          cols="3"
          class="create-backlog-tasks"
          style="border-left:1px solid rgba(0, 0, 0, 0.1); border-right:1px solid rgba(0, 0, 0, 0.1);"
      >
        <h5>Create Backlog</h5>
        <form>
          <div class="form-element">
            <label class="required fs-5 fw-bold mb-2">Task Name</label>
            <TextField componentFormTitle="taskname"></TextField>
          </div>
          <div class="form-element">
            <label class="required fs-5 fw-bold mb-2">Task Description</label>
            <TextArea componentFormTitle="description"></TextArea>
          </div>
          <div class="form-element">
            <label class="required fs-5 fw-bold mb-2">Story Point</label>
            <ComboBoxChips componentFormTitle="storyPoint"></ComboBoxChips>
          </div>
        </form>
      </v-col>
      -->
      <v-col cols="2"></v-col>
      <v-col
          cols="6"
          class="create-project-form"
      >
          <h3>Create New Project</h3>
          <form>
            <div class="form-element">
              <label class="required fs-5 fw-bold mb-2">Project Name</label>
              <TextField @handleFormChange="onFormChanged" componentFormTitle="name"></TextField>
            </div>
            <div class="form-element">
              <label class="required fs-5 fw-bold mb-2">Description</label>
              <TextArea @handleFormChange="onFormChanged" componentFormTitle="description"></TextArea>
            </div>
            <div class="form-element">
              <label class="required fs-5 fw-bold mb-2">Set Task Status</label>
              <AdvancedCombobox @handleFormChange="onFormChanged" componentFormTitle="taskStatus"></AdvancedCombobox>
            </div>

            <div class="form-element" v-if="this.projectManager !== null">
              <label class="required fs-5 fw-bold mb-2">Project Manager</label>
              <ShowUserInProject
                  :user="this.projectManager"
                  @handleDelete="deleteUserFromProject"
              ></ShowUserInProject>
            </div>
            <div class="form-element" v-if="this.teamLeader !== null">
              <label class="required fs-5 fw-bold mb-2">Team Leader</label>
              <ShowUserInProject
                  :user="this.teamLeader"
                  @handleDelete="deleteUserFromProject"
              ></ShowUserInProject>
            </div>
            <div class="form-element" v-if="this.teamMembers !== []">
              <label class="required fs-5 fw-bold mb-2">Team Members</label>
              <div class="d-flex">
                <ShowUserInProject
                    v-for="(user,index) in this.teamMembers"
                    :key="index"
                    :user="user"
                    style="width: 110px;"
                    @handleDelete="deleteUserFromProject"
                ></ShowUserInProject>
              </div>
            </div>
          </form>
        <button
            class="btn btn-lg btn-primary btn-block"
            v-on:click="createProject"
        >Create Project</button>
      </v-col>
      <v-col
          cols="3"
          class="users"
          style="border-left:1px solid rgba(0, 0, 0, 0.1); border-right:1px solid rgba(0, 0, 0, 0.1);"
      >
        <h5>Add User</h5>
        <div>
          <AddUserToProject
              v-for="(user,index) in this.users"
              v-bind:key="index"
              @handleClick="addUserToProject"
              :user="user"
              style="border-bottom:1px solid rgba(0, 0, 0, 0.1);"
          >
          </AddUserToProject>
        </div>
        <!--
        <div v-if="this.form.projectManager === ''">
          <h6><strong>Project Managers</strong></h6>
          <AddUserToProject
              v-for="(user,index) in this.projectManagers"
              v-bind:key="index"
              @handleClick="addUserToProject"
              :user="user"
          >
          </AddUserToProject>
        </div>
        <div v-if="this.form.teamLeader === ''">
          <h6><strong>Team Leaders</strong></h6>
          <AddUserToProject
              v-for="(user,index) in this.users"
              v-bind:key="index"
              @handleClick="addUserToProject"
              :user="user"
          >
          </AddUserToProject>
        </div>
        <div>
          <h6><strong>Team Members</strong></h6>
          <AddUserToProject
              v-for="(user,index) in this.teamMembers"
              v-bind:key="index"
              @handleClick="addUserToProject"
              :user="user"
          >
          </AddUserToProject>
        </div>
        -->
      </v-col>
    </v-row>
  </div>
</template>

<script>
import AdvancedCombobox from "@/components/form/AdvancedCombobox";
import axios from "axios";
import jwtService from "@/helpers/JwtService";
import TextArea from "@/components/form/TextArea";
import TextField from "@/components/form/TextField";
import AddUserToProject from "@/components/AddUserToProject";
import ShowUserInProject from "@/components/ShowUserInProject";
//import ComboBoxChips from "@/components/form/ComboBoxChips";

export default {
  name: "CreateProject",
  components: { ShowUserInProject, AddUserToProject, TextField, TextArea, AdvancedCombobox},
  data() {
    return {
      //teamMembers: [],
      //projectManagers: [],
      //teamLeaders: [],
      projectManager: null,
      teamLeader: null,
      teamMembers: [],
      users: [],
      form: {
        id: "",
        name: "",
        createdDate: "",
        description: "",
        creator: "",
        teamLeader: "",
        projectManager: "",
        members: [],
        backlogTasks: [],
        taskStatus: [],
        sprints: [],
      },
    }
  },
  created() {
    //TODO: Get All users and set user data (solved in Update Project)
    axios.get("http://localhost:9000/user/getAll",{
      headers: {
        Authorization: "Bearer "+ jwtService.getToken(),
      }
    }).then(response => {

      this.users = response.data;
      console.log(this.users)
      //this.seperateUsers();
    });
  },
  methods: {
    onFormChanged(model,componentFormTitle) {
      this.form[componentFormTitle] = model
    },

    async createProject() {

      this.form.creator = this.$store.state.userId;
      await axios.post("http://localhost:9000/project/create",
          {
            ...this.form
          },
          {
        headers: {
          Authorization: "Bearer "+ jwtService.getToken(),
          "Accept-Encoding": "gzip, deflate, br",
          "Accept": "*/*",
          "Connection": "keep-alive",
        }
      })
          .then( response => {
                console.log(response)
                this.$router.push("/");
              },
          )
          .catch(c => {
            console.log(c)
          });
    },
    addUserToProject (user) {

      if(user.roles[0] === "ProjectManager") {
        if(this.projectManager !== null) {
          this.$toast.error("Only one Project Manager allowed.", {
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
          return null;
        }
        this.form.projectManager = user.id;
        this.projectManager = user;
      }
      if(user.roles[0] === "TeamLeader") {
        if(this.teamLeader !== null) {
          this.$toast.error("Only one Team Leader allowed.", {
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
          return null;
        }
        this.form.teamLeader = user.id;
        this.teamLeader = user;
      }
      if(user.roles[0] === "TeamMember") {
        this.form.members.push(user.id);
        this.teamMembers.push(user);
      }
      this.users.splice(this.users.indexOf(user), 1);
    },
    deleteUserFromProject(user) {

      if(user.roles[0] === "ProjectManager") {
        this.form.projectManager = "";
        this.projectManager = null;
      }
      if(user.roles[0] === "TeamLeader") {
        this.form.teamLeader = "";
        this.teamLeader = null;
      }
      if(user.roles[0] === "TeamMember") {
        //delete from array
        let index = this.teamMembers.findIndex(member => member.id === user.id);
        this.teamMembers.splice(index, 1);
        //delete from array on form data
        let formIndex = this.form.members.findIndex(member => member === user.id)
        this.form.members.splice(formIndex, 1);
      }
      //add users
      this.users.push(user);
    },
    /*
    addUserToProject (user) {

      if(user.roles[0] === "ProjectManager") {
        this.form.projectManager = user.id;
        this.projectManagers.splice(this.projectManagers.indexOf(user), 1);
      }
      if(user.roles[0] === "TeamLeader") {
        this.form.TeamLeader = user.id;
        this.teamLeaders.splice(this.teamLeaders.indexOf(user), 1);
      }
      if(user.roles[0] === "TeamMember") {
        this.form.members.push(user.id);
      }

    },
     */

    /*
    seperateUsers () {

      this.users.forEach(user => {
        if (user.roles[0] === "TeamLeader") {
          this.teamLeaders.push(user);
        }
        if (user.roles[0] === "ProjectManager") {
          this.projectManagers.push(user);
        }
        if (user.roles[0] === "TeamMember") {
          this.teamMembers.push(user);
        }
      })
    }
     */
  },
}

</script>

<style scoped>
.create-project {
  min-height: 80vh;
  background-color: white;
}
.row {
  min-height: 95vh;
}
.col {
  min-height: 100%;
}
.form-element {
  text-align: start;
}
</style>
