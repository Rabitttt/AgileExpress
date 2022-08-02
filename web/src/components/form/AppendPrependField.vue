<template>
  <v-text-field
      v-model="this.items[this.index]"
      class="d-flex justify-content-between"
  >
    <v-icon
        slot="append"
        color="red"
        v-on:click="handlePlus"
    >
      mdi-plus
    </v-icon>
    <v-icon
        slot="prepend"
        color="green"
        v-on:click="handleMinus"
    >
      mdi-minus
    </v-icon>
  </v-text-field>
</template>

<script>
export default {
  name: "AppendPrependField",
  props: {
    componentFormTitle: String,
    initialData: Int32Array,
  },
  data() {
    return {
      index: 0,
      items: [
        1,
        2,
        3,
        5,
        8,
        13,
        21,
        34,
      ]
    }
  },
  created() {
    // eslint-disable-next-line no-debugger
    debugger;
    this.setInitialData();
  },
  methods: {
    handleMinus() {
      this.index = this.index !== 0 ? this.index - 1 : 0;
    },
    handlePlus() {
      this.index = this.index !== 7 ? this.index + 1 : 7;
    },
    setInitialData() {
      // eslint-disable-next-line no-debugger
      debugger;
      this.items.findIndex(item => {
        if (item === this.$props.initialData) {
          this.index = this.items.indexOf(item);
        }
      });
    }
  },
  watch: {
    index () {
      // eslint-disable-next-line no-debugger
      debugger;
      let modelValue = this.items[this.index]
      this.$emit('handleFormChange',modelValue,this.componentFormTitle)
    },
  },
}
</script>

<style scoped>
v-icon {
  cursor: pointer;
}
</style>
