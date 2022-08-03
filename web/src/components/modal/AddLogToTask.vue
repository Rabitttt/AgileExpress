<template>
  <v-row justify="center">
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
          Add Task Log
        </v-btn>
      </template>
      <v-card>
        <v-card-title>
          <span class="text-h5">Create New Task</span>
        </v-card-title>
        <v-card-text>
          {{this.form}}
          <form>
            <div class="form-element">
              <label class="required fs-5 fw-bold mb-2">Description</label>
              <TextArea @handleFormChange="onFormChanged" componentFormTitle="description"></TextArea>
            </div>

            <div class="form-element">
              <label class="required fs-5 fw-bold mb-2">Select Work Time</label>
              <TimeRangePicker  @handleFormChange="onFormChanged"></TimeRangePicker>
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
              @click="createTaskHandler"
          >
            Save
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>


<script>
import TextArea from "@/components/form/TextArea";
import TimeRangePicker from "@/components/form/TimeRangePicker";
import axios from "axios";
import jwtService from "@/helpers/JwtService";



export default {
  name: "AddLogToTask",
  components: {TimeRangePicker, TextArea},
  props: {
    taskId: String,
  },
  data: () => ({
    dialog: false,
    form: {
      description: "",
      startClock: "",
      endClock: "",
    },
  }),
  methods: {
    onFormChanged(model, componentFormTitle) {
      // eslint-disable-next-line no-debugger
      debugger;
      this.form[componentFormTitle] = model
    },
    createTaskHandler() {
       axios.post("http://localhost:9000/taskLog/create/"+this.$props.taskId,
          {
            ...this.form,
          },
          {
            params: {
              userId: this.$store.state.userId,
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
                this.$emit("taskLogCreateHandler",response.data)
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
