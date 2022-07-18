<template>
  <q-card class="q-ma-md">
    <q-card-section>
      <h4 class="text-h5 text-black q-my-none">Novo Usuário</h4>
    </q-card-section>
    <q-card-section>
      <q-form ref="userForm">
        <div class="row q-my-sm">
          <div class="col q-mx-sm">
            <q-input v-model="user.name" type="text" label="Nome" lazy-rules :rules="[this.required]">
              <template v-slot:prepend>
                <q-icon name="account_circle" />
              </template>
            </q-input>
          </div>
          <div class="col">
            <q-input v-model="user.cpf" type="number" label="CPF" lazy-rules :rules="[this.required]">
              <template v-slot:prepend>
                <q-icon name="123" />
              </template>
            </q-input>
          </div>
        </div>
        <div class="row q-my-sm">
          <div class="col q-mx-sm">
            <q-input v-model="user.phone" type="phone" label="Telefone" lazy-rules :rules="[this.required]">
              <template v-slot:prepend>
                <q-icon name="phone" />
              </template>
            </q-input>
          </div>
          <div class="col">
            <q-select v-model="user.profile.value" :options="user.profile.options" lazy-rules :rules="[this.required]"
              label="Perfil de Acesso">
              <template v-slot:prepend>
                <q-icon name="lock" />
              </template>
            </q-select>
          </div>
        </div>
        <div class="row q-my-sm">
          <div class="col q-mx-sm">
            <q-input v-model="user.email" type="email" lazy-rules :rules="[this.required, this.isEmail]" label="Email">
              <template v-slot:prepend>
                <q-icon name="email" />
              </template>
            </q-input>
          </div>
          <div class="col">
            <q-input v-model="user.password" type="password" lazy-rules :rules="[this.required]" label="Senha">
              <template v-slot:prepend>
                <q-icon name="key" />
              </template>
            </q-input>
          </div>
        </div>
        <div class="row q-my-sm">
          <div class="col">
            <q-checkbox v-model="user.enabled" label="Ativado" />
          </div>
          <div class="col">
            <q-checkbox v-model="user.passwordExpired" label="Senha Expirada" />
          </div>
        </div>
      </q-form>
    </q-card-section>
    <q-card-actions class="row justify-end">
      <q-btn outline size="md" color="primary" @click="clear" label="Limpar" />
      <q-btn unelevated size="md" color="primary" class="text-white" @click="submit" label="Salvar" />
    </q-card-actions>
  </q-card>
</template>

<script>
import { LANG_INPUT_EMAIL_INVALID, LANG_INPUT_REQUIRED } from '@/lang/lang.ptbr';
import localforage from 'localforage';
import { AUTH_CONST_JWT_SESSION } from '@/services/constants/auth.constants';
import userService from '@/services/user.service';
import { useQuasar } from 'quasar';

export default {
  methods: {
    submit() {
      // Valida o formulário
      this.validateForm().then(success => {
        if (success) {
          let user = {
            email: this.user.email,
            name: this.user.name,
            cpf: this.user.cpf,
            phone: this.user.phone,
            role: this.getRoleFromRoleName(this.user.profile.value),
            password: this.user.password,
            passwordExpired: this.user.passwordExpired,
            enabled: this.user.enabled
          }

          // Cria o usuário conforme os dados inseridos no formulário
          this.getSession().then(session => {
            userService.save(user, session).then(response => {
              if (response.status == 200) {
                this.notifyOk("Novo usuário criado com sucesso!");
                this.clear();
              }
              else {
                this.notifyError("Ocorreu um erro na criação do usuário.");
              }
            })
          });
        }
      })
    },
    // Realiza o reset dos campos do formulário
    reset() {
      this.user.email = null;
      this.user.name = null;
      this.user.cpf = null;
      this.user.phone = null;
      this.user.profile.value = 'Usuário';
      this.user.password = null;
      this.user.enabled = false;
      this.user.passwordExpired = false;
    },
    // Reseta o formulário e também os erros dos campos
    clear() {
      this.reset();
      this.$refs.userForm.resetValidation();
    },
    // Converte a role de string para inteiro
    getRoleFromRoleName(name) {
      switch (name) {
        case 'Usuário': return 0;
        case 'Técnico': return 1;
        case 'Administrador': return 2;
      }
    },
    validateForm() {
      return this.$refs.userForm.validate();
    },
    // Validador de campo [campo não pode ser nulo]
    required(val) {
      return (val && val.length > 0 || LANG_INPUT_REQUIRED);
    },
    // Validador de email [regex, email deve estar no formato aaaaaa@aa.aa]
    isEmail(val) {
      const emailPattern = /^(?=[a-zA-Z0-9@._%+-]{6,254}$)[a-zA-Z0-9._%+-]{1,64}@(?:[a-zA-Z0-9-]{1,63}\.){1,8}[a-zA-Z]{2,63}$/
      return (emailPattern.test(val) || LANG_INPUT_EMAIL_INVALID);
    },
    // Recebe os dados da sessão armazenados em cache
    getSession() {
      return localforage.getItem(AUTH_CONST_JWT_SESSION);
    }
  },
  data() {
    return {
      user: {
        email: null,
        name: null,
        cpf: null,
        phone: null,
        role: null,
        password: null,
        passwordExpired: false,
        enabled: false,
        profile: {
          value: 'Usuário',
          options: ['Usuário', 'Técnico', 'Admnistrador']
        }
      }
    }
  },
  setup() {
    const $q = useQuasar();

    return {
      notifyOk(msg) {
        $q.notify({
          type: 'positive',
          timeout: 800,
          message: msg
        })
      },

      notifyError(msg) {
        $q.notify({
          type: 'negative',
          timeout: 2000,
          message: msg
        })
      }
    }
  }
}
</script>