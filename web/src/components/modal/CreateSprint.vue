<template>
    <v-dialog
        v-model="dialog"
        max-width="600px"
        justify="center"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn
            style="max-width: 150px;"
            color="primary btn-sm"
            dark
            v-bind="attrs"
            v-on="on"
        >
          Add Sprint
        </v-btn>
      </template>
      <v-card>
        <v-card-title class="justify-center">
          <h4 class="text-h5">Create New Sprint</h4>
        </v-card-title>
        <v-card-text>
          <form>
            <div class="form-element">
              <label class="required fs-5 fw-bold mb-2">Sprint Name</label>
              <TextField @handleFormChange="onFormChanged" componentFormTitle="name"></TextField>
            </div>
            <div class="form-element">
              <label class="required fs-5 fw-bold mb-2">Description</label>
              <TextArea @handleFormChange="onFormChanged" componentFormTitle="description"></TextArea>
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
              @click="createSprintHandler"
          >
            Save
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
</template>

<script>
import TextField from "@/components/form/TextField";
import TextArea from "@/components/form/TextArea";

export default {
  name: "CreateSprint",
  components: {TextField, TextArea},
  data: () => ({
    dialog: false,
    form: {
      name: "",
      description: "",
    },
  }),
  methods: {
    onFormChanged(model, componentFormTitle) {
      this.form[componentFormTitle] = model
    },
    createSprintHandler() {
      this.$store.dispatch("createSprint", this.form)
    }
  },
}
</script>

<style scoped>
.col {
  min-height: 100%;
}
.form-element {
  text-align: start;
}
</style>
