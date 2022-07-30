<template>
  <div v-on:click="handleClick" class="sprint-card">
    {{sprint.id}}
    {{sprint.name}}
    <MakeActiveSprint v-if="sprint.sprintState !== 'active'" :sprint-id="sprint.id"></MakeActiveSprint>
    <button
        class="btn btn-sm btn-secondary btn-block"
        v-if="sprint.sprintState !== 'planned'"
        v-on:click="handleStateChange('planned')">
      Set as Planned
    </button>
    <button
        class="btn btn-sm btn-success btn-block"
        v-if="sprint.sprintState !== 'completed'"
        v-on:click="handleStateChange('completed')">
    Set as Completed
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
      // eslint-disable-next-line no-debugger
      debugger;
      //this.$store.commit("setSelectedSprintId", this.sprint.id);
      this.$emit("handleClick",this.sprint.id)
    },
    handleStateChange(newState) {
      // eslint-disable-next-line no-debugger
      debugger;
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
                // eslint-disable-next-line no-debugger
                debugger;
                this.$store.commit("setSprintStateChange", response.data);
              },
          )
          .catch(c => {
            console.log(c)
          });
    }
  },
}
</script>

<style scoped>
.sprint-card:hover {
  cursor: pointer;
}
</style>
