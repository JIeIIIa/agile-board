<template>
  <div class="row">
    <div class="col p-2" v-for="status in statuses">
      <CardsGroup v-bind:group-name="status.title" v-bind:items="filterItems(status.title)"
                  v-bind:class-name="status.class"
                  v-on:change-card-group="changeCard"
                  v-on:alert-card-group="onAlert"
      />
      <div class="modal fade" id="alertModal" tabindex="-1" role="dialog" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered" role="document">
          <div class="modal-content">
            <div class="modal-header" v-bind:class="'bg-' + alert.type">
              <h4>
                {{ alert.caption }}
              </h4>
              <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="modal-body bg-dark text-light">
              {{ alert.msg }}
            </div>
            <div class="modal-footer bg-dark">
              <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {mapGetters, mapState} from 'vuex'
  import JQuery from 'jquery'
  import CardsGroup from "./CardsGroup";


  export default {
    name: "CardsTable",
    components: {CardsGroup},
    data: function () {
      return {
        statuses: [
          {title: 'To do', class: "todo-group"},
          {title: 'In progress', class: "in-progress-group"},
          {title: 'Done', class: "done-group"}
        ],
        alert: {
          msg: '',
          type: ''
        }
      }
    },
    computed: {
      ...mapGetters(['cardsList']),
      ...mapState({
        // name: state => state.user.profile.login,
      })
    },
    methods: {
      filterItems: function (groupName) {
        return this.cardsList
          .filter(i => i.status === groupName)
          .sort((a, b) => (a["id"] > b["id"]) ? 1 : (a["id"] < b["id"]) ? -1 : 0
          )
      },
      changeCard: function (item) {
        this.$emit('change-card-agile', item)
      },
      onAlert: function (alert) {
        this.alert = alert
        this.alert.type = alert.type || 'info'
        JQuery("#alertModal").modal('show')
      }
    }
  }
</script>

<style scoped>

</style>
