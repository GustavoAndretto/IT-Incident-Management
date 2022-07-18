<template>
  <div class="fullscreen bg-blue text-white text-center q-pa-md flex flex-center">
    <q-card class="shadow-24" style="width:400px;">
      <q-card-section>
        <h4 class="text-h5 text-black q-my-sm">IT Incident Management</h4>
      </q-card-section>
      <q-card-section>
        <q-form ref="loginForm" class="q-gutter-md">
          <q-input v-model="email" lazy-rules :rules="[this.required, this.isEmail]" type="email" label="Email">
            <template v-slot:prepend>
              <q-icon name="email" />
            </template>
          </q-input>
          <q-input v-model="password" lazy-rules :rules="[this.required]" type="password" label="Password">
            <template v-slot:prepend>
              <q-icon name="lock" />
            </template>
          </q-input>
        </q-form>
      </q-card-section>
      <q-card-section>
        <q-btn unelevated size="lg" color="primary" @click="submit" class="full-width text-white" label="Login" />
      </q-card-section>
    </q-card>
  </div>
</template>

<script>
import authService from '@/services/auth.service'
import userService from '@/services/user.service'
import { LANG_INPUT_REQUIRED, LANG_INPUT_EMAIL_INVALID } from '../lang/lang.ptbr'
import { USER_SERVICE_SESSION_INFO } from '../services/constants/user.constants'
import router from '@/router'
import localforage from 'localforage'

export default {
  data: () => ({
    email: null,
    password: null
  }),
  methods: {
    submit() {
      this.validateLogin().then(success => {
        if (success) {
          authService.login(this.getCredentials()).then(session => {
            if (session) {
              // Carrega o perfil do usuário
              userService.load(session).then(response => {
                if (response) {
                  // Armazena os dados do usuário em cache
                  localforage.setItem(USER_SERVICE_SESSION_INFO, response.data);

                  // Direciona para a home page após autenticação
                  router.go({ name: 'home' });
                }
              })
            }
          })
        }
      })
    },
    getCredentials() {
      return {
        email: this.email,
        password: this.password
      }
    },
    validateLogin() {
      return this.$refs.loginForm.validate();
    },
    required(val) {
      return (val && val.length > 0 || LANG_INPUT_REQUIRED)
    },
    isEmail(val) {
      const emailPattern = /^(?=[a-zA-Z0-9@._%+-]{6,254}$)[a-zA-Z0-9._%+-]{1,64}@(?:[a-zA-Z0-9-]{1,63}\.){1,8}[a-zA-Z]{2,63}$/
      return (emailPattern.test(val) || LANG_INPUT_EMAIL_INVALID)
    }
  },
  setup() {
    return {}
  }
}
</script>