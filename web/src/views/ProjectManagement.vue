<template>
  <div >
    <v-row>
      <v-col
        class="col-3">
        <h5><strong>Backlogs</strong></h5>
        <div
            v-for="(item,index) in this.data.backlogTasks"
            v-bind:key="index"
        >
          {{item}}
        </div>
        <CreateBacklog>
        </CreateBacklog>
      </v-col>
      <v-col
        class="col-6 d-flex flex-row">
        <div
            style="width: 350px; height:70vh; border: 1px solid lightgrey"
            v-for="(item,index) in this.data.taskStatus"
            v-bind:key="index"
        >
          <h3>{{item.status}}</h3>
        </div>
      </v-col>
      <v-col
        class="col-3">
        <h5><strong>Sprints</strong></h5>
        <div
            v-for="(item,index) in this.data.sprints"
            v-bind:key="index"
        >
          {{item}}
        </div>
        <button class="btn btn-sm btn-primary btn-block"
                v-on:click="createSprint">
            Create Sprint

        </button>
      </v-col>
    </v-row>
  </div>
</template>

<script>
import axios from "axios";
import jwtService from "@/helpers/JwtService";
import CreateBacklog from "@/components/modal/CreateBacklog";

export default {
  name: "ProjectManagement",
  components: {CreateBacklog},
  data () {
    return {
      data: {},
    }
  },
  created() {
    let id = this.$route.params.id
    axios.get("http://localhost:9000/project/getProject/" + id,{
      headers: {
        Authorization: "Bearer "+ jwtService.getToken(),
      }
    }).then(response => {
      // eslint-disable-next-line no-debugger
      debugger;
      this.data = response.data;
      console.log(this.data)
    });
  },
  methods: {
    createSprint() {
      console.log("Create Sprint");
    },
    createBacklog() {
      console.log("Create Backlog");
    },
  }
}
</script>

<style scoped>

</style>
