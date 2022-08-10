<template>
  <v-card
      style="text-align: start; margin: 30px;"
      v-on:click="handleClick">

    <v-card-title class="mx-4 mb-1 d-flex justify-content-between">
      <strong>{{project.name}}</strong>
      <i :class="isOpenIcon"></i>
    </v-card-title>
    <v-card-subtitle class="mx-4 mb-1 d-flex justify-content-between">
      <strong>{{project.creator.username}}</strong>
      <strong>{{project.createdDate}}</strong>
    </v-card-subtitle>

    <v-card-text class="mx-4 mb-1 d-flex flex-column">
      <div>
      {{project.description}}
      </div>
    </v-card-text>
    <span v-if="isOpen">
      <v-divider class="mx-6 mb-1" color="black"></v-divider>
      <v-card-title class="mx-4 mb-1">Task States</v-card-title>
      <div class="px-4 mx-4 mb-1">
        <v-chip-group v-model="selection">
          <v-chip
              label
              v-for="(item,index) in project.taskStatus"
              v-bind:key="index"
              :color="item.color"
              class="px-5"
          >
            {{item.status}}
          </v-chip>
        </v-chip-group>
      </div>
      <v-divider class="mx-6 mb-1" color="black"></v-divider>
      <v-card-actions class="flex justify-content-end mx-4 mb-1">
      <v-btn
          color="primary dark lighten-2"
          text
          @click="projectDetails"
      >
        Project Details
      </v-btn>
      <v-btn
          v-if="this.$store.getters.isRoleProjectManagerOrHigher"
          color="error dark lighten-2"
          text
          @click="deleteProject"
      >
        Delete Project
      </v-btn>
    </v-card-actions>
    </span>
  </v-card>
</template>

<script>
import axios from "axios";
import jwtService from "@/helpers/JwtService";

export default {
  name: "ProjectCard",
  props: {
    project: {},
  },
  data() {
    return {
      isOpen: false,
    }
  },
  methods: {
    handleClick() {
      this.$emit("handleSelectedProject", this.project.members,this.project.teamLeader,this.project.projectManager);
      this.changeOpenState();
    },
    projectDetails() {
        this.$router.push("project/management/" + this.project.id);
    },
    async deleteProject() {
// eslint-disable-next-line no-debugger
      debugger;
      if(!this.haveUserAccessToDeleteProject()) {
        this.$toast.error("Don't have permission.", {
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
        return;
      }

      if(confirm(`Do you really want to delete Project '${this.project.name}' ?`)){
        await axios.post("http://localhost:9000/project/delete", {},
            {
              params: {
                projectId: this.project.id,
              },
              headers: {
                Authorization: `Bearer ${jwtService.getToken()}`,
                "Accept-Encoding": "gzip, deflate, br",
                "Accept": "*/*",
                "Connection": "keep-alive",},
            },
        )
            .then( response => {

                  if(response.status === 200) {
                    this.$toast.success("Project deleted successfully.", {
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
                    this.$router.push("/");
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
      }
    },
    changeOpenState() {
      this.isOpen = !this.isOpen;
    },
    //this is necessary when looking other people's projects
    haveUserAccessToDeleteProject () {
      // eslint-disable-next-line no-debugger
      debugger;
      let users = [];
      users.push(this.project.projectManager)
      users.push(this.project.creator)
      for (let user in users) {
        if(user.id === this.$store.state.userId) {
          return true;
        }
      }
      if(this.$store.state.userRole === "ROLE_Admin") {
        return true;
      }
      return false;
    }
  },
  computed: {
    isOpenIcon() {
      return this.isOpen ? "fa-solid fa-angle-up" : "fa-solid fa-angle-down";
    }
  }
}
</script>

<style scoped>

</style>
