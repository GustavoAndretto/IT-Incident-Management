<template>
  <q-card class="q-ma-md">
    <q-card-section>
      <h4 class="text-h5 text-black q-my-none">Perfil do Usuário</h4>
    </q-card-section>
    <q-card-section>
      <div class="row q-my-sm">
        <div class="col q-mx-sm">
          <q-input v-model="user.name" label="Nome">
            <template v-slot:prepend>
              <q-icon name="account_circle" />
            </template>
          </q-input>
        </div>
        <div class="col">
          <q-input v-model="user.cpf" label="CPF">
            <template v-slot:prepend>
              <q-icon name="123" />
            </template>
          </q-input>
        </div>
      </div>
      <div class="row q-my-sm">
        <div class="col q-mx-sm">
          <q-input v-model="user.phone" label="Telefone">
            <template v-slot:prepend>
              <q-icon name="phone" />
            </template>
          </q-input>
        </div>
        <div class="col">
          <q-select v-model="profile.value" :options="profile.options" :disable="disableIfAuthorityIsUnderOperator()"
            label="Perfil de Acesso">
            <template v-slot:prepend>
              <q-icon name="lock" />
            </template>
          </q-select>
        </div>
      </div>
      <div class="row q-my-sm">
        <div class="col q-mx-sm">
          <q-input v-model="user.email" disable label="Email">
            <template v-slot:prepend>
              <q-icon name="email" />
            </template>
          </q-input>
        </div>
        <div class="col">
          <q-input type="password" v-model="user.password" label="Senha">
            <template v-slot:prepend>
              <q-icon name="key" />
            </template>
          </q-input>
        </div>
      </div>
    </q-card-section>
    <q-card-actions class="row justify-end">
      <q-btn outline size="md" color="primary" @click="clear" label="Limpar" />
      <q-btn unelevated size="md" color="primary" class="text-white" @click="submit" label="Salvar" />
    </q-card-actions>
  </q-card>
</template>

<script>
import localforage from 'localforage'
import { USER_SERVICE_SESSION_INFO } from '@/services/constants/user.constants'
import userService from '@/services/user.service'
import { AUTH_CONST_JWT_SESSION } from '@/services/constants/auth.constants'
import { useQuasar } from 'quasar'

export default {
  methods: {
    submit() {
      let user = {
        email: this.user.email,
        name: this.user.name,
        cpf: this.user.cpf,
        phone: this.user.phone,
        role: this.getRoleFromRoleName(this.profile.value),
        password: this.user.password
      }

      this.getSession().then(session => {
        userService.update(user, session).then(response => {
          if (response.status == 200) {
            localforage.setItem(USER_SERVICE_SESSION_INFO, response.data);

            this.notifyOk("Perfil atualizado com sucesso!");
          }
          else {
            this.notifyError("Ocorreu um erro ao atualizar o perfil.");
          }
        })
      })
    },
    clear() {
    },
    getSession() {
      return localforage.getItem(AUTH_CONST_JWT_SESSION)
    },
    getRoleFromRoleName(name) {
      switch (name) {
        case 'Usuário': return 0;
        case 'Técnico': return 1;
        case 'Administrador': return 2;
      }
    },
    // Desativa um determinado elemento com base no nível de permissão
    disableIfAuthorityIsUnderOperator() {
      return this.user.role > 0 === false;
    },
    // Insere as informações do perfil
    async getProfileInfo() {
      const res = await localforage.getItem(USER_SERVICE_SESSION_INFO);

      this.user.name = res.name;
      this.user.email = res.email;
      this.user.phone = res.phone;
      this.user.cpf = res.cpf;
      this.user.role = res.role;
      this.profile.value = this.profile.options[res.role];
    }
  },
  data() {
    return {
      profile: {
        value: null,
        options: ['Usuário', 'Técnico', 'Admnistrador']
      },
      user: {
        name: null,
        cpf: null,
        phone: null,
        role: null,
        email: null,
        password: null
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
  },
  mounted() {
    this.getProfileInfo();
  }
}
</script>