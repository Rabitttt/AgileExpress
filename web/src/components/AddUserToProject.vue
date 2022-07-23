<template>
  <div v-if="user.roles[0] !== 'Admin'" class="py-5">
    <img alt="default_user_image" src="../assets/defaultUser.png" style="width:40px; height:40px;">
    <p style="margin: 2px;">{{user.username}}</p>
    <button
        class="btn btn-sm btn-block"
        :class="(isHoveringToUser) ? 'btn-success' : 'btn-dark'"
        @mouseover="isHoveringToUser = true"
        @mouseout="isHoveringToUser = false"
        v-on:click="handleClick"
    >Add to Project</button>
  </div>
</template>

<script>
export default {
  name: "AddUserToProject",
  props: {
    user: {}
  },
  data() {
    return {
      isHoveringToUser: false,
    }
  },
  methods: {
    handleClick() {
      // eslint-disable-next-line no-debugger
      debugger;
      this.$emit('handleClick',this.user)
    },
  },
}
</script>

<style scoped>

</style>
