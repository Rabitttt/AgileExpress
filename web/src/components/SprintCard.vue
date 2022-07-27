<template>
  <div v-on:click="handleClick">
    {{sprint}}
  </div>
</template>

<script>
export default {
  name: "SprintCard",
  props: {
    sprint: {},
  },
  methods: {
    handleClick() {
      // eslint-disable-next-line no-debugger
      debugger;
      this.$store.commit("setSelectedSprintId", this.sprint.id);
    },
  },
}
</script>

<style scoped>

</style>
