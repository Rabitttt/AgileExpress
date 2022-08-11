<template>
  <v-row justify="center">
    <v-dialog
        v-model="dialog"
        max-width="900px"
    >
      <template v-slot:activator="{ on, attrs }">
        <button
            style="min-width: 100%"
            class="btn btn-sm btn-dark btn-block"
            v-bind="attrs"
            v-on="on"
        >
          <i class="fa-solid fa-chart-line"></i>
          Make Active Sprint
        </button>
      </template>
      <v-card>
        <v-card-title>
          <span class="text-h5">Make Active Sprint</span>
        </v-card-title>
        <v-card-text>
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
              @click="changeToActive"
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
      this.form[componentFormTitle] = model
    },
    changeToActive() {
      let activeSprint = this.$store.state.selectedProject.sprints.filter((item) => item.sprintState === "active");
      if(activeSprint.length > 0) {
        this.$toast.warning("There is already an active sprint", {
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
        axios.post("http://localhost:9000/sprint/makeActiveSprint", {},
            {
              params: {
                sprintId: this.$props.sprintId,
                endDate: this.form.endDate,
                projectId: this.$store.state.selectedProject.id
              },
              headers: {
                Authorization: `Bearer ${jwtService.getToken()}`,
                "Accept-Encoding": "gzip, deflate, br",
                "Accept": "*/*",
                "Connection": "keep-alive",},
            },
          )
            .then( response => {
                  this.$store.commit("setSprintStateChange", response.data);
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
                  this.dialog = false;
                },
            )
            .catch(c => {
              console.log(c)
            });

      }
    },
  },
}
</script>

<style scoped>

</style>
