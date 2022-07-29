<template>
  <v-col>
    <v-date-picker
        v-model="model"
        elevation="1"
    ></v-date-picker>
  </v-col>
</template>

<script>
export default {
  name: "DatePicker",
  props: {
    componentFormTitle: String,
  },
  data () {
    return {
      model: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
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
