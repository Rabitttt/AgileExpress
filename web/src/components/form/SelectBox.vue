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
    initialData: String,
  },
  data(){
    return {
      model: "",
    }
  },
  created() {

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
