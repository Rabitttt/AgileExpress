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
        class="btn btn-lg btn-primary btn-block"
        v-on:click="projectDetails"
    >Project Details</button>
    <button
        class="btn btn-lg btn-primary btn-block"
        v-on:click="deleteProject"
    >Delete Project</button>
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
    projectDetails() {
      this.$router.push("project/management/" + this.project.id);
    },
    deleteProject() {

    }
  },
}
</script>

<style scoped>

</style>
