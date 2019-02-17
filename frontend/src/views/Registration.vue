<template>
  <div class="container-fluid">
    <div class="row justify-content-center">
      <div class="col-12 col-sm-10 col-md-8 text-center">
        <div class="card border-info">
          <div class="card-header bg-info">
            <h2>Registration</h2>
          </div>
          <div class="card-body text-left">
            <div class="alert alert-danger" id="authAlert" v-if="error">
              <strong>Registration error!</strong> Something went wrong.
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
            <div class="form-group">
              <label for="passwordConfirmationInput">Confirm</label>
              <input required type="password" class="form-control"
                     id="passwordConfirmationInput" placeholder="Confirm password"
                     v-model="passwordConfirmation">
            </div>
          </div>
          <div class="card-footer">
            <button type="submit" class="col-7 btn btn-primary form-control"
                    v-on:click="doRegister"
                    v-bind:class="{'disabled': !isPasswordConfirmationCorrect}">
              Register
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import {AUTH_NEW_ACCOUNT} from '@/store/actions/auth'
  import {USER_REQUEST} from "../store/actions/user";

  export default {
    name: "Registration",
    data() {
      return {
        login: 'admin',
        password: 'password',
        passwordConfirmation: 'password',
        error: false
      }
    },
    computed: {
      isPasswordConfirmationCorrect: function () {
        return this.password === this.passwordConfirmation
      }
    },
    methods: {
      doRegister: function () {
        if (this.password !== this.passwordConfirmation) {
          return
        }
        const {login, password, passwordConfirmation} = this

        this.$store.dispatch(AUTH_NEW_ACCOUNT, {login, password, passwordConfirmation})
          .then(() => {
            this.doAutoLogin()
          })
          .catch(err => {
            console.error('Registration error: ' + err);
            this.error = true
          })
      },
      doAutoLogin: function () {
        console.info("Registration success")
        this.$store.dispatch(USER_REQUEST)
          .then(() => {
            console.info("USER_REQUEST is success")
            this.$router.push('/board')
          })
          .catch(() => {
            console.error("USER_REQUEST is failure")
            this.$router.push('/login')
          })
      }
    },
  }
</script>

<style scoped>

</style>
