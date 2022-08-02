<template>
    <v-textarea
        solo
        label="Solo textarea"
        v-model="model"
    ></v-textarea>
</template>

<script>
export default {
  name: "TextArea",
  props: {
    initialData: String,
    componentFormTitle: String,
  },
  data() {
    return {
      model: "",
    }
  },
  created() {
    // eslint-disable-next-line no-debugger
    debugger;
    this.model = this.initialData;
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
