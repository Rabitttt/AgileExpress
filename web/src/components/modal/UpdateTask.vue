<template>
    <v-dialog
        v-model="dialog"
        max-width="600px"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn
            color="primary btn-sm"
            dark
            v-bind="attrs"
            v-on="on"
        >
          Update Task
        </v-btn>
      </template>
      <v-card>
        <v-card-title>
          <span class="text-h5">Update Task</span>
        </v-card-title>
        <v-card-text>
          {{this.form}}
          <form>
            <div class="form-element">
              <label class="required fs-5 fw-bold mb-2">Project Name</label>
              <TextField :initial-data="task.taskName"
                         @handleFormChange="onFormChanged"
                         componentFormTitle="taskName"
              ></TextField>
            </div>
            <div class="form-element">
              <label class="required fs-5 fw-bold mb-2">Description</label>
              <TextArea
                  :initial-data="task.description"
                  @handleFormChange="onFormChanged"
                  componentFormTitle="description"></TextArea>
            </div>
            <div class="form-element">
              <label class="required fs-5 fw-bold mb-2">Story Point</label>
              <AppendPrependField
                  :initial-data="task.storyPoint"
                  @handleFormChange="onFormChanged"
                  componentFormTitle="storyPoint"
              ></AppendPrependField>
            </div>
            <div class="form-element">
              <label class="required fs-5 fw-bold mb-2">Select Assignee</label>
              <SelectBox
                  :initial-data="task.assignee.username"
                  @handleFormChange="onFormChanged"
                  :comboboxItems="memberUsernames"
                  componentFormTitle="assignee"
              ></SelectBox>
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
import AppendPrependField from "@/components/form/AppendPrependField";
import TextField from "@/components/form/TextField";
import TextArea from "@/components/form/TextArea";
import SelectBox from "@/components/form/SelectBox";
import axios from "axios";
import jwtService from "@/helpers/JwtService";

export default {
  name: "UpdateTask",
  components: {AppendPrependField, TextField, TextArea,SelectBox},
  props: {
    task: Object,
  },
  data: () => ({
    dialog: false,
    form: {
      taskName: "",
      description: "",
      storyPoint: 1,
      assignee: "",
    },
    members: [],
  }),
  created() {

    /*
    this.form.taskName = this.$props.task.taskName;
    this.form.description = this.$props.task.description;
    this.form.storyPoint = this.$props.task.storyPoint;
    this.form.assignee = this.$props.task.assignee;
     */
    //this.getTeamMembers();
  },
  methods: {
    onFormChanged(model, componentFormTitle) {
      this.form[componentFormTitle] = model
    },
    async updateTaskHandler() {
      await axios.post("http://localhost:9000/task/update/",
          {
            ...this.form,
          },
          {
            params: {
              taskId: this.$props.task.id,
            },
            headers: {
              Authorization: `Bearer ${jwtService.getToken()}`,
              "Accept-Encoding": "gzip, deflate, br",
              "Accept": "*/*",
              "Connection": "keep-alive",},
          },
      )
          .then( response => {

                if(response.data !== "") {
                  this.dialog = false;
                  this.$emit("handleUpdateItem",response.data)
                  this.$toast.success("Task successfully updated.", {
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
              },
          )
          .catch(c => {
            console.log(c)
          });
    },
    /*
    async getTeamMembers() {

      let id = this.$store.state.selectedProject.id;
      await axios.get("http://localhost:9000/project/getTeamMembers/" + id,{
        headers: {
          Authorization: "Bearer "+ jwtService.getToken(),
        }
      }).then(response => {

        this.members = response.data;
      });
    }

     */
  },
  /*
  computed: {
    memberUsernames() {

      return this.members.map(member  => member.username);
    },
  }
   */

  computed: {
    memberUsernames() {

      return this.$store.state.selectedProject.members.map(member  => member.username);
    },
  }

}
</script>

<style scoped>

</style>
