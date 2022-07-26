<template>
  <div >
    <v-row>
      <v-col
        class="col-3">
        <h5><strong>Backlogs</strong></h5>
        <div
            v-for="(item,index) in selectedProject.backlogTasks"
            v-bind:key="index"
            style="border-bottom:1px solid black"
        >
          {{item}}
        </div>
        <CreateBacklog
          :members="memberUsernames"
        >
        </CreateBacklog>
      </v-col>
      <v-col
        class="col-6 d-flex flex-row">
        <div
            style="width: 350px; height:70vh; border: 1px solid lightgrey"
            v-for="(item,index) in selectedProject.taskStatus"
            v-bind:key="index"
        >
          <h3>{{item.status}}</h3>
        </div>
      </v-col>
      <v-col
        class="col-3">
        <h5><strong>Sprints</strong></h5>
        <div
            v-for="(item,index) in selectedProject.sprints"
            v-bind:key="index"
            style="border-bottom:1px solid black"
        >
          {{item}}
        </div>
        <CreateSprint>
        </CreateSprint>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import CreateBacklog from "@/components/modal/CreateBacklog";
import CreateSprint from "@/components/modal/CreateSprint";

export default {
  name: "ProjectManagement",
  components: {CreateBacklog,CreateSprint},
  data () {
    return {
      //memberUsernames: [],
    }
  },
  created() {
    this.$store.dispatch("setSelectedProject",this.$route.params.id)
    this.setUsernameArray();
  },
  methods: {
    createSprint() {
      console.log("Create Sprint");
    },
    createBacklog() {
      console.log("Create Backlog");
    },
  },
  computed: {
    selectedProject() {
        return this.$store.state.selectedProject;
    },
    memberUsernames() {
      // eslint-disable-next-line no-debugger
      debugger
      return this.$store.state.selectedProject.members.map(member  => member.username);
    }
  }
}
</script>

<style scoped>

</style>
