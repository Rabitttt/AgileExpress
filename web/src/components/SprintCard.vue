<template>
  <div v-on:click="handleClick" class="sprint-card">
    {{sprint.id}}
    {{sprint.name}}
    <MakeActiveSprint
        v-if="sprint.sprintState !== 'active' && $store.getters.isRoleProjectManagerOrHigher"
        :sprint-id="sprint.id"></MakeActiveSprint>
    <button
        class="btn btn-sm btn-secondary btn-block"
        v-if="sprint.sprintState !== 'planned' && $store.getters.isRoleProjectManagerOrHigher"
        v-on:click="handleStateChange('planned')">
      Set as Planned
    </button>
    <button
        class="btn btn-sm btn-success btn-block"
        v-if="sprint.sprintState !== 'completed' && $store.getters.isRoleProjectManagerOrHigher"
        v-on:click="handleStateChange('completed')">
    Set as Completed
    </button>
    <button
        v-if="$store.getters.isRoleProjectManagerOrHigher"
        class="btn btn-sm btn-danger btn-block"
        v-on:click="deleteSprint">
    Delete Sprints
    </button>
  </div>
</template>

<script>
import MakeActiveSprint from "@/components/modal/MakeActiveSprint";
import axios from "axios";
import jwtService from "@/helpers/JwtService";
export default {
  name: "SprintCard",
  components: {MakeActiveSprint},
  props: {
    sprint: {},
  },
  methods: {
    handleClick() {

      //this.$store.commit("setSelectedSprintId", this.sprint.id);
      this.$emit("handleClick",this.sprint.id)
    },
    handleStateChange(newState) {

      axios.post("http://localhost:9000/sprint/changeSprintState", {},
          {
            params: {
              sprintId: this.sprint.id,
              state: newState,
            },
            headers: {
              Authorization: `Bearer ${jwtService.getToken()}`,
              "Accept-Encoding": "gzip, deflate, br",
              "Accept": "*/*",
              "Connection": "keep-alive",},
          },
      )
          .then( response => {
                if(response.data !== "") {
                  this.$store.commit("setSprintStateChange", response.data);
                  this.$toast.success("Task successfully updated.", {
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
    },
    async deleteSprint () {
      if(confirm(`Do you really want to delete Sprint '${this.sprint.name}' ?`)) {
        await axios.post("http://localhost:9000/sprint/delete", {},
            {
              params: {
                sprintId: this.sprint.id,
                projectId: this.$store.state.selectedProject.id,
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
                    this.$toast.success("Sprint successfully deleted.", {
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
.sprint-card:hover {
  cursor: pointer;
}
</style>
