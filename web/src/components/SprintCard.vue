<template>
  <v-card v-on:click="handleClick">
    <v-card-title>
      <span class="text-h5">{{sprint.name}}</span>

      <v-spacer></v-spacer>

      <v-menu
          bottom
          left
      >
        <template v-slot:activator="{ on, attrs }">
          <v-btn
              dark
              icon
              v-bind="attrs"
              v-on="on"
          >
            <v-icon color="black">mdi-dots-vertical</v-icon>
          </v-btn>
        </template>

        <v-list class="d-flex flex-column justify-content-between">
          <h6>Change State</h6>
          <v-list-item v-if="sprint.sprintState !== 'active' && $store.getters.isRoleProjectManagerOrHigher">
            <MakeActiveSprint
                :sprint-id="sprint.id"></MakeActiveSprint>
          </v-list-item>
          <v-list-item
              class="px-1"
              v-if="sprint.sprintState !== 'planned' && $store.getters.isRoleProjectManagerOrHigher"
          >

          <button
              style="min-width: 100%"
                class="btn btn-sm btn-secondary btn-block"
                v-on:click="handleStateChange('planned')">
            <i class="fa-solid fa-list-ul"></i>
              Set as Planned
            </button>
          </v-list-item>
          <v-list-item
              class="px-1"
              v-if="sprint.sprintState !== 'completed' && $store.getters.isRoleProjectManagerOrHigher"
          >

          <button
              style="min-width: 100%"
                class="btn btn-sm btn-success btn-block"
                v-on:click="handleStateChange('completed')">
            <i class="fa-solid fa-check"></i>
            Set as Completed
            </button>
          </v-list-item>
          <v-list-item
              class="px-1"
              v-if="$store.getters.isRoleProjectManagerOrHigher"
          >
            <button
                style="min-width: 100%"
                class="btn btn-sm btn-danger btn-block"
                v-on:click="deleteSprint">
              <i class="fa-solid fa-trash"></i>
              Delete Sprints
            </button>
          </v-list-item>
        </v-list>
      </v-menu>
    </v-card-title>
    <v-card-subtitle class="d-flex justify-content-between" >
      <div>
        {{sprint.description}}
      </div>
      <div class="sprint-card">
        {{sprint.sprintState}}
      </div>
    </v-card-subtitle>
    <v-card-text>
      <div v-if="sprint.sprintState ==='active'">
        Sprint Start Date: {{sprint.startDate}}
      </div>
      <div v-if="sprint.sprintState ==='active'">
        Sprint Start Date: {{sprint.endDate}}
      </div>
    </v-card-text>
  </v-card>
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
  data() {
    return {
      items: [
        { title: 'Click Me' },
        { title: 'Click Me' },
        { title: 'Click Me' },
        { title: 'Click Me 2' },
      ],
    }
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
