<template>
  <v-text-field
      label="Solo"
      solo
      v-model="model"
  ></v-text-field>
</template>

<script>
export default {
  name: "TextField",
  props: {
    componentFormTitle: String,
  },
  data() {
    return {
      model: "",
    }
  },
  watch: {
    model () {
      this.$emit('handleFormChange',this.model,this.componentFormTitle)
    },
  },
}
</script>

<style scoped>

</style>
