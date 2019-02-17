<template>
  <div class="home">
    <div class="row">
      <div class="col-12 text-center">
        <h1>Welcome</h1>
        <h1>to</h1>
        <h1>Agile Board</h1>
      </div>
    </div>
  </div>
</template>

<script>
  // @ is an alias to /src
  import HelloWorld from '@/components/HelloWorld.vue'

  export default {
    name: 'home',
    components: {
      HelloWorld
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
