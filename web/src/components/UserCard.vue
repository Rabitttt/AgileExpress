<template>
  <div>
    {{user.username}}
    <button class="btn btn-primary btn-block btn-sm" v-on:click="openUserProfile">Profile</button>
  </div>
</template>

<script>
export default {
  name: "UserCard",
  props: {
    user: Object,
  },
  methods: {
    openUserProfile () {
      this.$router.push({path: `/user/profile/${this.$props.user.id}`})
    }
  }
}
</script>

<style scoped>

</style>