<template>
  <div style="background-color:white; height:5vh; width:100%; border-bottom:1px solid rgba(0, 0, 0, 0.1);">
    <v-container class="d-flex justify-content-between align-center">
        <v-col
            cols="4"
            class="text-center">
            <span style="cursor:pointer;" v-on:click="changeRoute('/')">
              <strong>𝘼𝙜𝙞𝙡𝙚 𝙀𝙭𝙥𝙧𝙚𝙨𝙨</strong>
            </span>
        </v-col>
        <v-col
            cols="6"
            class="text-center">
          <span
              class="nav-item"
              v-on:click="changeRoute('/')">
            Projects
          </span>
          <span
              class="nav-item"
              v-on:click="changeRoute('/user/profile/')">
            Profile
          </span>
          <span
              class="nav-item"
              v-on:click="changeRoute('/project/create')"
              v-if="this.$store.getters.isRoleProjectManagerOrHigher"
          >
            Create Project
          </span>


          <span v-if="this.$store.state.isAuthenticated" class="nav-item" v-on:click="logout()">Logout</span>
          <span v-if="!this.$store.state.isAuthenticated" class="nav-item" v-on:click="changeRoute('/.login')">Login</span>

        </v-col>
    </v-container>
  </div>
</template>

<script>
import jwtService from "@/helpers/JwtService";

export default {
  name: "Navbar",
  methods: {
    changeRoute(newRoute) {
      this.$router.push({path: newRoute.toString()})
    },
    logout() {
      jwtService.destroyToken();
      window.localStorage.removeItem("user_id");
      this.$store.commit("setAuthUserStatus", false);
      this.$router.push({path: "/login"})
    },
  },
}
</script>

<style scoped>
.col {
  padding: 0;
}
.nav-item {
  padding-left: 14px;
  padding-right: 14px;
  font-size: 0.9rem;
  cursor: pointer;
}
.nav-item:hover {
  color: black;
}
</style>
