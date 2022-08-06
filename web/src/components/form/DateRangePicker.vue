<template>
  <v-row>
    <v-col
        cols="12"
        sm="6"
    >
      <v-date-picker
          v-model="dates"
          range
          :min="minDate"
      ></v-date-picker>
    </v-col>
    <v-col
        cols="12"
        sm="6"
    >
      <v-text-field
          v-model="dateRangeText"
          label="Date range"
          prepend-icon="mdi-calendar"
          readonly
      ></v-text-field>
    </v-col>
  </v-row>
</template>

<script>
export default {
  name: "DateRangePicker",
  props: {
    componentFormTitle: String,
  },
  data: () => ({
    //dates: ['2019-09-10', '2019-09-20'],
    dates: [],
  }),
  created() {
    this.dates[0] = this.minDate;
  },
  watch: {
    dates () {
      this.dates[0] = this.minDate;
      this.$emit("handleFormChange", this.dates[1], this.$props.componentFormTitle);
    },
  },
  computed: {
    dateRangeText () {
      return this.dates.join(' ~ ')
    },
    //today
    minDate () {
      let today = new Date();
      let dd = String(today.getDate()).padStart(2, '0');
      let mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
      let yyyy = today.getFullYear();

      today = yyyy + '-' + mm + '-' + dd;
      return today;
    }
  },
}
</script>

<style scoped>

</style>
