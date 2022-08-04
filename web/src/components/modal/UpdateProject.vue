<template>
  <v-dialog
      v-model="dialog"
      max-width="900px"
  >
    <template v-slot:activator="{ on, attrs }">
      <button
          class="btn btn-primary btn-sm btn-block"
          v-bind="attrs"
          v-on="on"
      >
        Update Project
      </button>
    </template>
    <v-card>
      <v-card-title class="justify-center">
        <h4 class="text-h5">Update Project</h4>
      </v-card-title>
      <v-card-text>
        {{this.form}}
        <v-row>
          <v-col>
            <form>
              <div class="form-element">
                <label class="required fs-5 fw-bold mb-2">Project Name</label>
                <TextField
                    :initial-data="project.name"
                    @handleFormChange="onFormChanged"
                    componentFormTitle="name"></TextField>
              </div>
              <div class="form-element">
                <label class="required fs-5 fw-bold mb-2">Description</label>
                <TextArea
                    :initial-data="project.description"
                    @handleFormChange="onFormChanged"
                    componentFormTitle="description"></TextArea>
              </div>

              <div class="form-element" v-if="this.projectManager !== null">
                <label class="required fs-5 fw-bold mb-2">Project Manager</label>
                <div class="d-flex">
                  <ShowUserInProject
                      :user="this.projectManager"
                      @handleDelete="deleteUserFromProject"
                  ></ShowUserInProject>
                </div>
              </div>
              <div class="form-element" v-if="this.teamLeader !== null">
                <label class="required fs-5 fw-bold mb-2">Team Leader</label>
                <div class="d-flex">
                  <ShowUserInProject
                      :user="this.teamLeader"
                      @handleDelete="deleteUserFromProject"
                  ></ShowUserInProject>
                </div>
              </div>
              <div class="form-element" v-if="this.teamMembers !== []">
                <label class="required fs-5 fw-bold mb-2">Team Members</label>
                <div class="d-flex">
                  <ShowUserInProject
                      v-for="(user,index) in this.teamMembers"
                      :key="index"
                      :user="user"
                      @handleDelete="deleteUserFromProject"
                  ></ShowUserInProject>
                </div>
              </div>
            </form>
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
      </v-card-text>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn
            color="blue darken-1"
            text
            @click="dialog = false"
        >
          Close
        </v-btn>
        <v-btn
            color="blue darken-1"
            text
            @click="updateProject"
        >
          Save
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
import TextField from "@/components/form/TextField";
import TextArea from "@/components/form/TextArea";
import ShowUserInProject from "@/components/ShowUserInProject";
import AddUserToProject from "@/components/AddUserToProject";
import axios from "axios";
import jwtService from "@/helpers/JwtService";

export default {
  name: "UpdateProject",
  components: {AddUserToProject, ShowUserInProject, TextField, TextArea},
  props: {
    project: Object,
  },
  data: () => ({
    dialog: false,
    projectManager: null,
    teamLeader: null,
    teamMembers: [],
    users: [],
    form: {
      id: "",
      name: "",
      description: "",
      teamLeader: "",
      projectManager: "",
      members: [],
    },
  }),
  created() {
    //TODO: Get All users and set user data
    axios.get("http://localhost:9000/user/getAll",{
      headers: {
        Authorization: "Bearer "+ jwtService.getToken(),
      }
    }).then(response => {
      // eslint-disable-next-line no-debugger
      debugger
      let allUsers = response.data;
      this.users = this.deleteFormMembersInAllUsers(allUsers);

    });
    this.setInitialProjectMembers();
  },
  methods: {
    onFormChanged(model, componentFormTitle) {
      this.form[componentFormTitle] = model
    },
    updateProject() {
      // eslint-disable-next-line no-debugger
      debugger;
      this.form.teamLeader = this.teamLeader.id;
      this.form.projectManager = this.projectManager.id;
      this.teamMembers.forEach(user => {
        this.form.members.push(user.id);
      });
      axios.post("http://localhost:9000/project/update",
          {
            ...this.form
          },{
        params: {
          projectId: this.$props.project.id,
        },
        headers: {
          Authorization: "Bearer "+ jwtService.getToken(),
        }
      }).then(response => {
        this.dialog = false;
        this.$emit("updateProject", response.data);
      });
    },
    addUserToProject (user) {
      // eslint-disable-next-line no-debugger
      debugger;
      if(user.roles[0] === "ProjectManager") {
        this.form.projectManager = user.id;
        this.projectManager = user;
      }
      if(user.roles[0] === "TeamLeader") {
        this.form.teamLeader = user.id;
        this.teamLeader = user;
      }
      if(user.roles[0] === "TeamMember") {
        this.form.members.push(user.id);
        this.teamMembers.push(user);
      }
      //delete from users
      this.users.splice(this.users.indexOf(member => member.id === user.id), 1);
    },
    setInitialProjectMembers() {
      this.projectManager = this.$props.project.projectManager;
      this.teamLeader = this.$props.project.teamLeader;
      this.$props.project.members.forEach(member => {
        this.teamMembers.push(member);
      });
    },
    deleteUserFromProject(user) {
      // eslint-disable-next-line no-debugger
      debugger;
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
    deleteFormMembersInAllUsers(allUsers) {
      // eslint-disable-next-line no-debugger
      debugger;
      //project users
      let projectUsers = [];
      for (let index in this.teamMembers) {
        projectUsers.push(this.teamMembers[index]);
      }
      projectUsers.push(this.teamLeader);
      projectUsers.push(this.projectManager);

      projectUsers.forEach(user => {
        //members
        let index = allUsers.findIndex(member => member.id === user.id);
        allUsers.splice(index, 1);
      });
      return allUsers;
    },
  },
}
</script>

<style scoped>
.form-element {
  text-align: start;
}
</style>