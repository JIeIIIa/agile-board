<template>
  <div class="card border-dark">
    <div class="card-header p-1">

      <pre>{{ item.text }}</pre>
    </div>
    <div class="card-footer p-1 bg-dark text-light">
      <div class="row">
        <div class="col-6 m-auto text-center">
          <small>id: {{ item.id }}</small>

        </div>
        <div class="col-6 d-flex justify-content-end">
          <button class="btn btn-sm btn-info btn-menu" v-on:click="onUpdate">Edit</button>
          <button class="btn btn-sm btn-danger btn-menu" v-on:click="onDelete">Del</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {CARD_DELETE} from "../store/actions/card";

  export default {
    name: "Card",
    props: ["item"],
    methods: {
      onUpdate: function () {
        this.$emit('change-card', this.item)
      },
      onDelete: function () {
        this.$store.dispatch(CARD_DELETE, this.item.id)
          .catch(err => {
            console.error(err)
            let alert = {
              msg: "Error! Can't delete the card with id = " + this.item.id,
              type: "danger",
              caption: "Error"
            }
            this.$emit('alert-card', alert)
          })
      }
    }
  }
</script>

<style scoped lang="scss">
  .btn-menu {
    width: 3em;
  }

  .card {
    & &-header {
      min-height: 6em;
      background: #ffe293;
    }
  }

</style>
