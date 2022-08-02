<template>
    <v-dialog
        v-model="dialog"
        max-width="900px"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn
            color="primary btn-sm"
            dark
            v-bind="attrs"
            v-on="on"
        >
          Update Task Log
        </v-btn>
      </template>
      <v-card>
        <v-card-title>
          <span class="text-h5">Update Task Log</span>
        </v-card-title>
        <v-card-text>
          {{this.form}}
          <form>
            <div class="form-element">
              <label class="required fs-5 fw-bold mb-2">Description</label>
              <TextArea :initialData="taskLog.description" @handleFormChange="onFormChanged" componentFormTitle="description"></TextArea>
            </div>
            <div class="form-element">
              <label class="required fs-5 fw-bold mb-2">Select Work Time</label>
              <TimeRangePicker
                  :initial-end="taskLog.endClock"
                  :initial-start="taskLog.startClock"
                  @handleFormChange="onFormChanged"
              ></TimeRangePicker>
            </div>
          </form>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
              color="blue darken-1"
              text
              @click="dialog = false"
          >
            Close
          </v-btn>
          <v-btn
              color="blue darken-1"
              text
              @click="updateTaskHandler"
          >
            Save
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>




<script>
import TimeRangePicker from "@/components/form/TimeRangePicker";
import TextArea from "@/components/form/TextArea";
import axios from "axios";
import jwtService from "@/helpers/JwtService";

export default {
  name: "UpdateTaskLog",
  components: {TimeRangePicker, TextArea},
  props: {
    taskId: String,
    taskLog: Object,
  },
  data: () => ({
    dialog: false,
    form: {
      description: "",
      startClock: "",
      endClock: "",
    },
  }),
  created() {
    this.form.description = this.$props.taskLog.description;
    this.form.startClock = this.$props.taskLog.startClock;
    this.form.endClock = this.$props.taskLog.endClock;
  },
  methods: {
    onFormChanged(model, componentFormTitle) {
      // eslint-disable-next-line no-debugger
      debugger;
      this.form[componentFormTitle] = model
    },
    updateTaskHandler() {
      axios.post("http://localhost:9000/taskLog/update/",
          {
            ...this.form,
          },
          {
            params: {
              taskLogId: this.$props.taskLog.id,
            },
            headers: {
              Authorization: `Bearer ${jwtService.getToken()}`,
              "Accept-Encoding": "gzip, deflate, br",
              "Accept": "*/*",
              "Connection": "keep-alive",},
          },
      )
          .then( response => {
                // eslint-disable-next-line no-debugger
                debugger;
                this.$emit("taskLogUpdateHandler",response.data)
                this.dialog = false;
              },
          )
          .catch(c => {
            console.log(c)
          });
    },
  },
}
</script>

<style scoped>

</style>

