<template>
  <div>
    <img alt="default_user_image" src="../assets/defaultUser.png" style="width:40px; height:40px;">
    <p>{{user.username}}</p>
  </div>
</template>

<script>
export default {
  name: "ShowUserInProject",
  props: {
    user: {}
  },
}
</script>

<style scoped>

</style>
