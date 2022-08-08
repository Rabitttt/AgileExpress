<template>
  <v-card>
    <v-card-title class="py-2 d-flex flex-row justify-content-between">
      {{task.taskName}}
      <div style="font-size: small">
        {{task.assignee.username}}
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
  name: "",
  props: {
    task: {},
  },
  methods: {
    handleProjectDetails() {
      this.$router.push("/task/details/" + this.$props.task.id)
    }
  }
}
</script>

<style scoped>

</style>
