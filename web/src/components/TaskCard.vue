<template>
  <v-card
    :disabled="isDisabled"
    :style="usersOwnTask"
  >
    <v-card-title class="py-2 d-flex flex-row justify-content-between task-card-title">
      {{task.taskName}}
      <div style="font-size: small">
        <router-link style="text-decoration: none" :to="`/user/profile/${task.assignee.id}`"> {{task.assignee.username}}</router-link>
      </div>
    </v-card-title>
    <v-card-subtitle class="d-flex flex-row justify-content-between">
      <div>
        {{task.status}}
      </div>
      <div>
        <i class="far fa-gem mt-1"></i>
        {{task.storyPoint}}
      </div>
    </v-card-subtitle>
    <v-card-text style="text-align: start">
      {{task.description}}
    </v-card-text>
    <v-card-actions class="justify-content-end">
      <v-btn
          color="primary dark lighten-1"
          btn
          @click="handleProjectDetails"
      >
        Task Details
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  name: "TaskCard",
  props: {
    task: {},
    isDisabled: Boolean,
  },
  methods: {
    handleProjectDetails() {
      this.$router.push("/task/details/" + this.$props.task.id)
    }
  },
  computed: {
    // eslint-disable-next-line vue/return-in-computed-property
    usersOwnTask() {
      // eslint-disable-next-line no-debugger
      debugger;
      if( this.$store.state.userId === this.task.assignee.id){
        if(this.task.status === "backlog") {
          return "border: 1px solid #001a00"
        }
        for (let index in this.$store.state.selectedProject.taskStatus) {
          if(this.$store.state.selectedProject.taskStatus[index].status === this.task.status) {
            return "border: 1px solid " + this.$store.state.selectedProject.taskStatus[index].color.split(' ')[0];
          }
        }
      }
      else "";
    }
  }
}
</script>

<style scoped>
</style>
