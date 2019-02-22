<template>
  <div class="container-fluid">
    <div class="row justify-content-center">
      <div class="col-12 col-sm-10 col-md-8 text-center">
        <div class="card border-info">
          <div class="card-header bg-info">
            <h2>Authorization</h2>
          </div>
          <div class="card-body text-left">
            <div class="alert alert-danger" id="authAlert" v-if="error">
              <strong>Authorization error!</strong> Login/password is incorrect.
              <button type="button" class="close" aria-label="Close" v-on:click="error = false">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div class="form-group">
              <label for="loginInput">Login</label>
              <input required type="text" class="form-control"
                     id="loginInput" placeholder="Enter login"
                     v-model="login">
            </div>
            <div class="form-group">
              <label for="passwordInput">Password</label>
              <input required type="password" class="form-control"
                     id="passwordInput" placeholder="Password"
                     v-model="password">
            </div>
          </div>
          <div class="card-footer">
            <button type="submit" class="col-12 col-sm-10 col-md-7 btn btn-primary form-control" v-on:click="doLogin">Login</button>
            <div class="my-2 my-sm-3">OR</div>
            <router-link to="/registration" class="col-12 col-sm-10 col-md-7 btn btn-warning form-control">
              Register new account
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {AUTH_REQUEST} from '@/store/actions/auth'

  export default {
    name: "Login",
    data() {
      return {
        login: '',
        password: '',
        error: false
      }
    },
    methods: {
      doLogin: function () {
        const {login, password} = this
        this.$store.dispatch(AUTH_REQUEST, {login, password})
          .then(() => {
            this.$router.push('/board')
          })
          .catch(err => {
            console.error('Auth error: ' + err);
            this.error = true
          })
      }
    },
  }
</script>

<style scoped>

</style>
