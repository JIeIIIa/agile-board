<template>
  <div class="home">
    <div class="row">
      <div class="col-12 text-center">
        <h1>Welcome</h1>
        <h1>to</h1>
        <h1>Agile Board</h1>
      </div>
      <div class="col-6 offset-5 col-sm-4 offset-sm-6 col-lg-2 offset-lg-7 col-xl-2 offset-xl-6">
        <router-link to="/board" class="btn btn-primary btn-lg form-control">
          View board
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'home',
    components: {
    }
  }
</script>

<style lang="scss" scoped>
  .row {
    padding-top: 10rem;
  }

  h1 {
    font-size: 6rem;
    font-weight: bold;
    color: #112d37;
  }
</style>
