<template>
  <v-card
      style="text-align: start;"
  >
     <v-row>
       <v-col cols="2 pl-4">
         <div class="m-0 p-0">
           <img class="d-flex justify-content-center align-center text-center" alt="default_user_image" src="../assets/defaultUser.png" style="width:50px; height:50px;">
         </div>
       </v-col>
       <v-col cols="6" class="d-flex flex-column justify-content-around">
         <v-card-title class="m-0 p-0">
           <strong>
             {{user.username}}
           </strong>
         </v-card-title>
          <v-card-subtitle class="m-0 p-0" style="color: rgba(0, 0, 0, 0.6)">
            {{user.email}}
          </v-card-subtitle>
       </v-col>
       <v-col cols="4" class="px-4 d-flex justify-content-end align-center">
           <v-btn
               color="primary btn-sm"
               dark
               small
               text
               v-on:click="openUserProfile"
           >
             Profile
           </v-btn>
       </v-col>
     </v-row>
  </v-card>
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