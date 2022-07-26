<template>
  <v-select
      :items="comboboxItems"
      v-model="model"
      label="Solo field"
      solo
  ></v-select>
</template>

<script>
export default {
  name: "SelectBox",
  props: {
    comboboxItems: Array,
    componentFormTitle: String,
  },
  data(){
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
