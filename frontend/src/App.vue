
<template>
  <div id="app">
    <Header/>

    <div class="container-fluid">
      <div class="row">
        <div class="col-12 bg-info py-1"></div>
      </div>
    </div>

    <router-view/>
  </div>
</template>

<script>
  import Header from '@/components/Header.vue'

  export default {
    components: {Header}
  }
</script>

<style lang="scss">
  html, body {
    min-height: 100%;
    min-width: 300px;
  }

  body {
    padding-top: 3.4rem;
    background-image: linear-gradient(to top, #d5d4d0 0%, #d5d4d0 1%, #eeeeec 31%, #efeeec 75%, #e9e9e7 100%);
  }
</style>
