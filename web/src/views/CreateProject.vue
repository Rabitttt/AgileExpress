<template>
  <div class="create-project container">
    <v-row>
      <v-col
          cols="3"
          class="create-backlog-tasks"
      >
        <h5>Create Backlog</h5>

      </v-col>
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

          </form>
        <button
            class="btn btn-lg btn-primary btn-block"
            v-on:click="createProject"
        >Create Project</button>
      </v-col>
      <v-col
          cols="3"
          class="users"
      >
        <h5>Add User</h5>
        {{this.form.description}}
        <div v-for="(item,index) in this.users" v-bind:key="index">
          <img alt="default_user_image" src="../assets/defaultUser.png" style="width:40px; height:40px;">
          <p>{{item.username}}</p>
          <button
              class="btn btn-lg btn-block"
              :class="isHoveringToUser ? 'btn-primary' : 'btn-danger'"
              @mouseover="isHoveringToUser = true"
              @mouseout="isHoveringToUser = false"
              v-on:click="createProject">Add to Projec
            t</button>
        </div>
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
export default {
  name: "CreateProject",
  components: {TextField, TextArea, AdvancedCombobox},
  data() {
    return {
      isHoveringToUser: false,
      users: {},
      form: {
        name: "Project 0",
        description: "This is project 1",
        expiryDate: null,
        creator: "62d87cc053d2624bcabdc07e",
        teamLeader: "62d87cc053d2624bcabdc082",
        projectManager: "62d87cc053d2624bcabdc084",
        members: [
          "62d87cc053d2624bcabdc07f"
        ],
        backlogTasks: [],
        taskStatus: [
          "deneme",
          "status2",
          "status10"
        ]
      }
    }
  },
  mounted() {
    //TODO: Get All users and set user data
    axios.get("http://localhost:9000/user/getAll",{
      headers: {
        Authorization: "Bearer "+ jwtService.getToken(),
      }
    }).then(response => {
      // eslint-disable-next-line no-debugger
      debugger
      this.users = response.data;
      console.log(this.users)
    });
  },
  methods: {
    onFormChanged(model,componentFormTitle) {
      this.form[componentFormTitle] = model
    },

    async createProject() {
      await axios.post("/project/create",
          {
            ...this.form
          },
          {
        headers: {
          "Accept-Encoding": "gzip, deflate, br",
          "Accept": "*/*",
          "Connection": "keep-alive",
          Authorization: "Bearer "+jwtService.getToken(),
        }
      })
          .then( response => {
                console.log(response)
              },
          )
          .catch(c => {
            console.log(c)
          });
    },
    isUserAdded () {
      // TODO: Check if user is added to project
    }
  }
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
