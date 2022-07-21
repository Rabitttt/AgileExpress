<template>
  <div>
    {{this.data}}
  </div>
</template>

<script>
import axios from "axios"

  export default {
    name: 'HelloWorld',

    data: () => ({
      data: {}
    }),

    async created() {
      await this.getData();
    },

    methods: {
      async getData() {
        axios.get('http://localhost:9000/project/getAll').then(response => {
          this.data = response.data;
        });
      }
    },
  }
</script>
