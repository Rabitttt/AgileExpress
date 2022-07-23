<template>
  <v-combobox
      clearable
      small-chips
      solo
      :items="items"
      v-model="model"
  ></v-combobox>
</template>

<script>
export default {
  name: "ComboBoxChips",
  data(){
    return {
      model: "",
      items: [
        1,
        2,
        3,
        5,
        8,
        13,
        21,
        34
      ],
    }
  }
}
</script>

<style scoped>

</style>
