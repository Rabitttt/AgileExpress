<template>
  <v-card
      style="text-align: start"
      class="mb-4"
      elevation="4"
  >
    <v-card-title class="d-flex justify-content-between" >
      <div style="font-size: 0.9em">
        {{item.description}}
      </div>
      <div style="font-size: small" class="p-0">
        <div>
          <strong> {{item.startClock}} </strong> <i class="fa-solid fa-arrow-right"></i> <strong> {{item.endClock}} </strong>
        </div>
        <div>
          <router-link style="text-decoration: none" :to="`/user/profile/${item.creator.id}`">
            <strong>
              {{item.creator.username}}
            </strong>
          </router-link>
        </div>
      </div>
    </v-card-title>
    <v-card-actions class="flex-row-reverse justify-content-end">
      <v-btn
          color="error btn-sm"
          dark
          text
          @click="deleteTaskLog"
      >
        Update Task
      </v-btn>
      <UpdateTaskLog
          :task-id="taskId"
          :task-log="item"
          @taskLogUpdateHandler="updateTaskLog"
      ></UpdateTaskLog>
    </v-card-actions>
  </v-card>
</template>

<script>
import axios from "axios";
import jwtService from "@/helpers/JwtService";
import UpdateTaskLog from "@/components/modal/UpdateTaskLog";

export default {
  name: "TaskLogCard",
  components: {UpdateTaskLog},
  props: {
   item: Object,
   taskId: String,
  },
  methods: {
    deleteTaskLog() {
      if(confirm("Do you really want to delete Log ?")) {
        axios.post("http://localhost:9000/taskLog/deleteTaskLog/",{},
            {
              params: {
                taskId: this.taskId,
                taskLogId: this.item.id,
              },
              headers: {
                Authorization: `Bearer ${jwtService.getToken()}`,
                "Accept-Encoding": "gzip, deflate, br",
                "Accept": "*/*",
                "Connection": "keep-alive",},
            },
        ).then(response => {
          if(response.data !== "") {
            this.$emit("handleDeletedItem", response.data);
            this.$toast.success("Task Log successfully deleted.", {
              timeout: 3000,
              closeOnClick: true,
              pauseOnFocusLoss: true,
              pauseOnHover: true,
              draggable: true,
              draggablePercent: 0.6,
              showCloseButtonOnHover: false,
              hideProgressBar: true,
              icon: true,
              rtl: false
            });
          }
          else {
            this.$toast.error("Operation Failed.", {
              timeout: 3000,
              closeOnClick: true,
              pauseOnFocusLoss: true,
              pauseOnHover: true,
              draggable: true,
              draggablePercent: 0.6,
              showCloseButtonOnHover: false,
              hideProgressBar: true,
              icon: true,
              rtl: false
            });
          }
        });
      }

    },
    updateTaskLog(updatedTaskLog) {
      this.item = updatedTaskLog;
    }
  }
}
</script>

<style scoped>

</style>
