<template>
  <div>
    <v-row
        justify="space-around"
        align="center"
    >
      <v-col style="width: 350px; flex: 0 1 auto;">
        <h2>Start:</h2>
        <v-time-picker
            format="24hr"
            elevation="1"
            v-model="start"
            :max="end"
        ></v-time-picker>
      </v-col>
      <v-col style="width: 350px; flex: 0 1 auto;">
        <h2>End:</h2>
        <v-time-picker
            format="24hr"
            elevation="1"
            v-model="end"
            :min="start"
        ></v-time-picker>
      </v-col>
    </v-row>
  </div>
</template>

<script>
export default {
  name: "TimeRangePicker",
  data () {
    return {
      start: null,
      end: null,
    }
  },
  watch: {
    start () {
      this.$emit('handleFormChange',this.start,"startClock")
    },
    end () {
      this.$emit('handleFormChange',this.end,"endClock")
    },
  }
}
</script>

<style scoped>

</style>
