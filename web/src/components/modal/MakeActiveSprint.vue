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
          Make Active Sprint
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
              <label class="required fs-5 fw-bold mb-2">Select End Date</label>
              <DateRangePicker @handleFormChange="onFormChanged" componentFormTitle="endDate"></DateRangePicker>
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
import axios from "axios";
import jwtService from "@/helpers/JwtService";
import DateRangePicker from "@/components/form/DateRangePicker";

export default {
  name: "MakeActiveSprint",
  props: {
    sprintId: String,
  },
  components: {DateRangePicker},
  data: () => ({
    dialog: false,
    form: {
      endDate: "",
    },
  }),
  methods: {
    onFormChanged(model, componentFormTitle) {
      // eslint-disable-next-line no-debugger
      debugger;
      this.form[componentFormTitle] = model
    },
    createTaskHandler() {
      // eslint-disable-next-line no-debugger
      debugger;
      axios.post("http://localhost:9000/sprint/makeActiveSprint", {},
          {
            params: {
              sprintId: this.$props.sprintId,
              endDate: this.form.endDate,
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
                window.alert(response.data)
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
