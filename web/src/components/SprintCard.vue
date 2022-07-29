<template>
  <div v-on:click="handleClick">
    {{sprint}}
    <MakeActiveSprint :sprint-id="sprint.id"></MakeActiveSprint>
  </div>
</template>

<script>
import MakeActiveSprint from "@/components/modal/MakeActiveSprint";
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
  },
}
</script>

<style scoped>

</style>
