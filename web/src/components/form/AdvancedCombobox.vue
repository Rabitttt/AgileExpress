<template>
  <v-combobox
      v-model="model"
      :filter="filter"
      :hide-no-data="!search"
      :items="items"
      :search-input.sync="search"
      hide-selected
      label="Add new Task Status"
      multiple
      small-chips
      solo
  >
    <template v-slot:no-data>
      <v-list-item>
        <span class="subheading">Create</span>
        <v-chip
            :color="`${colors[nonce - 1]} lighten-3`"
            label
            small
        >
          {{ search }}
        </v-chip>
      </v-list-item>
    </template>
    <template v-slot:selection="{ attrs, item, parent, selected }">
      <v-chip
          v-if="item === Object(item)"
          v-bind="attrs"
          :color="`${item.color} lighten-3`"
          :input-value="selected"
          label
          small
      >
          <span class="pr-2">
            {{ item.status }}
          </span>
        <v-icon
            small
            @click="parent.selectItem(item)"
        >
          $delete
        </v-icon>
      </v-chip>
    </template>
    <template v-slot:item="{ index, item }">
      <v-status-field
          v-if="editing === item"
          v-model="editing.status"
          autofocus
          flat
          background-color="transparent"
          hide-details
          solo
          @keyup.enter="edit(index, item)"
      ></v-status-field>
      <v-chip
          v-else
          :color="`${item.color} lighten-3`"
          dark
          label
          small
      >
        {{ item.status }}
      </v-chip>
      <v-spacer></v-spacer>
      <v-list-item-action @click.stop>
        <v-btn
            icon
            @click.stop.prevent="edit(index, item)"
        >
          <v-icon>{{ editing !== item ? 'mdi-pencil' : 'mdi-check' }}</v-icon>
        </v-btn>
      </v-list-item-action>
    </template>
  </v-combobox>
</template>

<script>
export default {
  name: "AdvancedCombobox",
  props: {
    componentFormTitle: null,
  },
  data: () => ({
    colors: ['green', 'purple', 'indigo', 'cyan', 'teal', 'orange'],
    editing: null,
    editingIndex: -1,
    items: [],
    nonce: 1,
    model: [],
    x: 0,
    search: null,
    y: 0,
  }),

  watch: {
    model (val, prev) {
      if (val.length === prev.length) return

      this.model = val.map(v => {
        if (typeof v === 'string') {
          v = {
            status: v,
            color: this.colors[this.nonce - 1],
          }

          this.items.push(v)

          this.nonce++
        }

        return v
      })
      this.$emit('handleFormChange',this.model,this.componentFormTitle)
    },
  },

  methods: {
    edit (index, item) {
      if (!this.editing) {
        this.editing = item
        this.editingIndex = index
      } else {
        this.editing = null
        this.editingIndex = -1
      }
    },
    filter (item, querystatus, itemstatus) {
      if (item.header) return false

      const hasValue = val => val != null ? val : ''

      const status = hasValue(itemstatus)
      const query = hasValue(querystatus)

      return status.toString()
          .toLowerCase()
          .indexOf(query.toString().toLowerCase()) > -1
    },
  },
}
</script>

<style scoped>

</style>
