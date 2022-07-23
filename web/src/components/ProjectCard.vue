<template>
  <div style="background-color: lightblue; margin: 30px;" class="d-flex flex-column" v-on:click="handleClick">
    <p>name = {{project.name}}</p>
    <p>description = {{project.description}}</p>
    <p>createdDate = {{project.createdDate}}</p>
    <p>creator = {{project.creator.username}}</p>
  </div>
</template>

<script>
export default {
  name: "ProjectCard",
  props: {
    project: {},
  },
  methods: {
    handleClick() {
      // eslint-disable-next-line no-debugger
      debugger;
      this.$emit("handleSelectedProject", this.project.members,this.project.teamLeader,this.project.projectManager);
    },
  },
}
</script>

<style scoped>

</style>
