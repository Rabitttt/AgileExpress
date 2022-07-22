<template>
    <v-textarea
        solo
        name="input-7-4"
        label="Solo textarea"
        v-model="model"
    ></v-textarea>
</template>

<script>
export default {
  name: "TextArea",
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
