<template>
  <div class="row">
    <div class="col-12">
      <div class="row my-2 mx-2">
        <div class="col-2">
          <button type="button" class="btn btn-success"
                  v-on:click="createTask">New card
          </button>
          <CardModal v-bind:bus="bus"/>
        </div>
        <div class="col-8 text-center">
          <h2>Board</h2>
        </div>
      </div>
      <div class="row mx-2">
        <div class="col-12">
          <AgileTable v-on:change-card-agile="changeCard"/>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import Vue from "vue"
  import AgileTable from "../components/AgileTable";
  import CardModal from "../components/CardModal";
  import {AUTH_AUTO_LOGIN} from "../store/actions/auth";
  import {CARD_REQUEST} from "../store/actions/card";


  export default {
    name: "Board",
    data: function () {
      return {
        bus: new Vue()
      }
    },
    components: {CardModal, AgileTable},
    methods: {
      createTask: function () {
        this.bus.$emit('showCardModal')
      },
      changeCard: function (item) {
        this.bus.$emit('showCardModal', item)
      }
    },
    mounted() {
      console.log("Board: from mounted")

      this.$store.dispatch(AUTH_AUTO_LOGIN)
        .then(() => {
          console.log('Load cards')
          return this.$store.dispatch(CARD_REQUEST)})
        .catch(() => this.$router.push("/login"))
    }
  }
</script>

<style scoped>

</style>
