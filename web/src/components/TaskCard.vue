<template>
  <div>
    {{task}}
    <button
        class="btn btn-sm btn-primary btn-block"
        v-on:click="handleProjectDetails"
    >Project Details</button>
  </div>
</template>

<script>
export default {
  name: "TaskCard",
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
