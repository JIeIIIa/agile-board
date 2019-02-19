<template>
  <div class="modal fade" id="cardModal" tabindex="-1" role="dialog"
       aria-labelledby="cardModalTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
      <div class="modal-content">
        <div class="modal-header bg-info">
          <h4 class="modal-title" id="cardModalTitle">
            <span v-if="card.id === ''">New card</span>
            <span v-else>
              Edit card (<span class="font-weight-bold">id = {{ card.id }}</span>)
            </span>
          </h4>
          <button type="button" class="close" data-dismiss="modal" aria-label="Close">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body bg-dark text-light">
          <div class="alert alert-danger" id="authAlert" v-if="error">
            <strong>Error!</strong> Card was not {{ operationType }}d.
            <button type="button" class="close" aria-label="Close" v-on:click="error = false">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="form-group">
            <label for="textTaskInput">Task description:</label>
            <textarea class="form-control" id="textTaskInput"
                      placeholder="Enter text" rows="5"
                      v-model="card.text"></textarea>
          </div>
          <div class="form-group">
            <label for="status">Status:</label>
            <div class="input-group">
              <select class="custom-select" id="status" v-model="card.status">
                <option disabled value="">Choose</option>
                <option>To do</option>
                <option>In progress</option>
                <option>Done</option>
              </select>
            </div>
          </div>
        </div>
        <div class="modal-footer bg-dark">
          <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
          <button type="button" class="btn btn-success" v-on:click="doOperation">Save changes</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

  import {CARD_ADD, CARD_UPDATE} from "../store/actions/card";
  import JQuery from "jquery";

  export default {
    name: "EditCard",
    props: ["bus"],
    data: function () {
      return {
        operationType: '',
        error: false,
        card: {
          id: '',
          text: '',
          status: 'To do'
        }
      }
    },
    methods: {
      save: function () {
        this.$store.dispatch(CARD_ADD, this.card)
          .then(resp => {
            JQuery("#cardModal").modal('hide')
          })
          .catch(err => {
            console.error(err)
            this.error = true
          })
      },
      update: function () {
        this.$store.dispatch(CARD_UPDATE, this.card)
          .then(resp => {
            JQuery("#cardModal").modal('hide')
          })
          .catch(err => {
            console.error(err)
            this.error = true
          })
      },
      doOperation: function () {
        if (this.operationType === "create") {
          this.save();
        } else {
          this.update()
        }
      },
      clear: function () {
        this.error = false;
        this.card = {
          id: '',
          text: '',
          status: 'To do'
        }
      },
      show: function (card) {
        if (typeof (card) == "undefined" || card == null) {
          this.clear();
          this.operationType = "create"
        } else {
          this.card.id = card.id
          this.card.text = card.text
          this.card.status = card.status
          this.operationType = "update"
        }
        JQuery("#cardModal").modal('show')
      }
    },
    mounted() {
      this.bus.$on('showCardModal', this.show)
    }
  }
</script>

<style scoped>

</style>
