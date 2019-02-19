<template>
  <div class="card border-dark" v-bind:class="className">
    <div class="card-header text-center">
      <h4>{{ groupName }}</h4>
    </div>
    <div class="card-body">
      <div class="col my-2 px-0" v-for="item in items">
        <Card v-bind:item="item"
              v-on:change-card="changeCard"
              v-on:alert-card="onAlert"/>
      </div>
    </div>
  </div>
</template>

<script>
  import Card from "./Card";

  export default {
    name: "CardsGroup",
    components: {Card},
    props: ["groupName", "className", "items"],
    methods: {
      changeCard: function (item) {
        this.$emit('change-card-group', item)
      },
      onAlert: function (alert) {
        this.$emit('alert-card-group', alert)
      }
    }
  }
</script>

<style scoped lang="scss">
  .todo-group {
    .card-header {
      background: #6d45ff;
    }

    .card-body {
      background: #d5dae9;
    }
  }

  .in-progress-group {
    & .card-header {
      background: #ff855b;
    }

    .card-body {
      background: #fff0de;
    }
  }

  .done-group {
    & .card-header {
      background: #7dff51;
    }

    .card-body {
      background: #dcffd1;
    }
  }
</style>
