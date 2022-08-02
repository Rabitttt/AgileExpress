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
        this.$emit("handleDeletedItem", response.data);
      });

    },
    updateTaskLog(updatedTaskLog) {
      this.item = updatedTaskLog;
    }
  }
}
</script>

<style scoped>

</style>
