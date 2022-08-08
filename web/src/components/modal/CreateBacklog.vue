<template>
  <v-row justify="center">
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
          Add Task
        </v-btn>
      </template>
      <v-card>
        <v-card-title>
          <span class="text-h5">Create New Task</span>
        </v-card-title>
        <v-card-text>
          <form>
            <div class="form-element">
              <label class="required fs-5 fw-bold mb-2">Project Name</label>
              <TextField @handleFormChange="onFormChanged" componentFormTitle="taskName"></TextField>
            </div>
            <div class="form-element">
              <label class="required fs-5 fw-bold mb-2">Description</label>
              <TextArea @handleFormChange="onFormChanged" componentFormTitle="description"></TextArea>
            </div>
            <div class="form-element">
              <label class="required fs-5 fw-bold mb-2">Story Point</label>
              <AppendPrependField @handleFormChange="onFormChanged" componentFormTitle="storyPoint"></AppendPrependField>
            </div>
            <div class="form-element">
              <label class="required fs-5 fw-bold mb-2">Select Assignee</label>
              <SelectBox @handleFormChange="onFormChanged"
                         :comboboxItems="members"
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
import TextField from "@/components/form/TextField";
import TextArea from "@/components/form/TextArea";
import AppendPrependField from "@/components/form/AppendPrependField";
import SelectBox from "@/components/form/SelectBox";

export default {
  name: "CreateBacklog",
  components: {AppendPrependField, TextField, TextArea,SelectBox},
  props: {
    members: Array,
  },
  data: () => ({
    dialog: false,
    memberUsernames: [],
    form: {
      taskName: "",
      description: "",
      storyPoint: 1,
      assignee: "",
    },
  }),
  methods: {
    onFormChanged(model, componentFormTitle) {
      this.form[componentFormTitle] = model
    },
    createTaskHandler() {
      this.$store.dispatch("createTask", this.form)
    }
  },
}
</script>

<style scoped>

</style>
