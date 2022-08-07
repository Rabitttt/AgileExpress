<template>
  <div>
    {{item}}
    <button class="btn btn-sm btn-primary btn-block" v-on:click="deleteTaskLog">Delete Card</button>
    <UpdateTaskLog
        :task-id="taskId"
        :task-log="item"
        @taskLogUpdateHandler="updateTaskLog"
    ></UpdateTaskLog>
  </div>
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
