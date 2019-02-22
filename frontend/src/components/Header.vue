<template>
  <nav class="navbar navbar-expand-sm navbar-dark bg-dark fixed-top">
    <ul class="nav navbar-nav">

      <li>
        <router-link to="/" class="navbar-brand">AgileBoard</router-link>
      </li>

      <li class="nav-item my-auto">
        <h5 class="text-info m-0">{{ name }}</h5></li>
    </ul>

    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarToggleExternalContent"
            aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse justify-content-end" id="navbarNavDropdown">
      <ul class="nav navbar-nav d-inline-block d-sm-flex">
        <li class="nav-item mr-3">
          <router-link to="/board" class="nav-link"
                       exact-active-class="active"
                       v-if="isAuthenticated">
            Board
          </router-link>
        </li>
        <li class="nav-item mr-3">
          <router-link to="/about" class="nav-link"
                       exact-active-class="active">
            About
          </router-link>
        </li>
        <li class="nav-item">
          <router-link to="/login"
                       class="btn btn-success px-4" exact-active-class="active"
                       v-if="!isAuthenticated && isNotLoginPage">
            Login
          </router-link>
          <a @click="logout"
             class="btn btn-warning px-4"
             v-if="isAuthenticated">
            Logout
          </a>
        </li>
      </ul>
    </div>
  </nav>
</template>

<script>
  import {mapGetters, mapState} from 'vuex'
  import {AUTH_LOGOUT} from '@/store/actions/auth'

  export default {
    name: "Header",
    computed: {
      ...mapGetters(['isAuthenticated']),
      ...mapState({
        name: state => state.user.profile.login,
      }),
      isNotLoginPage: function () {
        return this.$route.path !== "/login"
      }
    },
    methods: {
      logout: function () {
        this.$store.dispatch(AUTH_LOGOUT).then(() => this.$router.push('/login'))
      }
    }
  }
</script>

<style scoped lang="scss">
  .nav.navbar-nav {
    flex-direction: row;
  }
</style>
