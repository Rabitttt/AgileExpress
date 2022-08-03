<template>
  <div class="d-flex flex-column align-center show-user-card">
    <img alt="default_user_image" src="../assets/defaultUser.png" style="width:40px; height:40px;">
    <p class="justify-center">{{user.username}}</p>
    <button
        class="btn btn-danger btn-sm"
        v-on:click="handleDeletedItem"
    >Delete</button>
  </div>
</template>

<script>
export default {
  name: "ShowUserInProject",
  props: {
    user: {},
  },
  methods:{
    handleDeletedItem(){
      this.$emit("handleDelete", this.user);
    }
  }
}
</script>

<style scoped>
.show-user-card{
  width: 110px;
}
</style>
