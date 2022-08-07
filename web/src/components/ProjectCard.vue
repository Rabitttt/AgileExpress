<template>
  <div
      style="border-bottom: 1px solid rgba(0,0,0,1); margin: 30px;"
      class="d-flex flex-column"
      v-on:click="handleClick">
    <p>name = {{project.name}}</p>
    <p>description = {{project.description}}</p>
    <p>createdDate = {{project.createdDate}}</p>
    <p>creator = {{project.creator.username}}</p>
    <button
        class="btn btn-sm btn-primary btn-block"
        v-on:click="projectDetails"
    >Project Details</button>
    <button
        v-if="this.$store.getters.isRoleProjectManagerOrHigher"
        class="btn btn-sm btn-danger btn-block"
        v-on:click="deleteProject"
    >Delete Project</button>
  </div>
</template>

<script>
import axios from "axios";
import jwtService from "@/helpers/JwtService";

export default {
  name: "ProjectCard",
  props: {
    project: {},
  },
  methods: {
    handleClick() {

      this.$emit("handleSelectedProject", this.project.members,this.project.teamLeader,this.project.projectManager);
    },
    projectDetails() {
      this.$router.push("project/management/" + this.project.id);
    },
    async deleteProject() {

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
                    this.$toast.error("Project deleted successfully.", {
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
    }
  },
}
</script>

<style scoped>

</style>
