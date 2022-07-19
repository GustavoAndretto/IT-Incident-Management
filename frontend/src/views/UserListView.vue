<template>
  <q-card v-if="editMode" class="q-ma-md">
    <q-card-section>
      <div class="row">
        <div class="col">
          <h4 class="text-h5 text-black q-my-none">Editar Usuário</h4>
        </div>
      </div>
    </q-card-section>
    <q-card-section>
      <q-form ref="userForm">
        <div class="row q-my-sm">
          <div class="col q-mx-sm">
            <q-input dense v-model="user.name" type="text" label="Nome" lazy-rules :rules="[this.required]">
              <template v-slot:prepend>
                <q-icon name="account_circle" />
              </template>
            </q-input>
          </div>
          <div class="col">
            <q-input dense v-model="user.cpf" label="CPF" mask="###.###.###-##" lazy-rules
              :rules="[this.required]">
              <template v-slot:prepend>
                <q-icon name="123" />
              </template>
            </q-input>
          </div>
        </div>
        <div class="row q-my-sm">
          <div class="col q-mx-sm">
            <q-input dense v-model="user.phone" mask="(##) #####-####" label="Telefone" lazy-rules
              :rules="[this.required]">
              <template v-slot:prepend>
                <q-icon name="phone" />
              </template>
            </q-input>
          </div>
          <div class="col">
            <q-select dense v-model="user.role.value" :options="user.role.options" lazy-rules :rules="[this.required]"
              label="Perfil de Acesso">
              <template v-slot:prepend>
                <q-icon name="lock" />
              </template>
            </q-select>
          </div>
        </div>
        <div class="row q-my-sm">
          <div class="col q-mx-sm">
            <q-input dense v-model="user.email" type="email" lazy-rules :rules="[this.required, this.isEmail]"
              label="Email">
              <template v-slot:prepend>
                <q-icon name="email" />
              </template>
            </q-input>
          </div>
          <div class="col">
            <q-input dense v-model="user.password" type="password" label="Senha">
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
      <q-btn outline size="md" color="primary" @click="editMode = false" label="Cancelar" />
      <q-btn unelevated size="md" color="primary" class="text-white" @click="submit" label="Salvar" />
    </q-card-actions>
  </q-card>

  <q-card class="q-ma-md">
    <q-card-section>
      <div class="row">
        <div class="col">
          <h4 class="text-h5 text-black q-my-none">Lista de Usuários</h4>
        </div>
        <div class="col flex justify-end align-center">
          <q-btn round size="sm" color="primary" icon="refresh" @click="updateUserList" />
        </div>
      </div>
    </q-card-section>
    <q-card-section>
      <div class="row q-mb-sm">
        <div class="col">
          <q-input v-model="filter" label="Filtrar por número, cpf, nome, email, telefone ..." dense>
            <template v-slot:append>
              <q-icon name="search" />
            </template>
          </q-input>
        </div>
      </div>
      <q-table flat :rows="rows" :visible-columns="visibleColumns()" :columns="columns" :loading="loading"
        :pagination="pagination" :filter="filter" row-key="name">
        <template v-slot:no-data>
          <div class="full-width row flex-center">
            <span>
              Nenhum registro encontrado
            </span>
          </div>
        </template>
        <template v-if="role > 0" v-slot:body-cell-actions="props">
          <q-td :props="props">
            <q-btn dense round flat color="grey" @click="editRow(props)" icon="edit"></q-btn>
            <q-btn dense round flat color="grey" @click="deleteRow(props)" icon="delete"></q-btn>
          </q-td>
        </template>
      </q-table>
    </q-card-section>
  </q-card>

  <q-dialog v-model="dialogDelete" persistent>
    <q-card>
      <q-card-section>
        <div class="text-h6">Deseja realmente excluir o usuário?</div>
      </q-card-section>

      <q-card-actions class="bg-white flex justify-end">
        <q-btn color="primary" flat label="Cancelar" v-close-popup />
        <q-btn flat color="primary" label="Confirmar" @click="confirmDelete" v-close-popup />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script>
import localforage from 'localforage';
import { AUTH_CONST_JWT_SESSION } from '@/services/constants/auth.constants';
import { LANG_INPUT_EMAIL_INVALID, LANG_INPUT_REQUIRED } from '@/lang/lang.ptbr';
import { useQuasar } from 'quasar';
import userService from '@/services/user.service';
import { USER_SERVICE_SESSION_INFO } from '@/services/constants/user.constants';

var columns = [
  { name: 'id', field: 'id', label: 'Número', align: 'center', sortable: true },
  { name: 'cpf', field: 'cpf', label: 'CPF', align: 'center', sortable: true },
  { name: 'email', field: 'email', label: 'Email', align: 'center' },
  { name: 'name', field: 'name', label: 'Nome', align: 'center' },
  { name: 'phone', field: 'phone', label: 'Telefone', align: 'center' },
  { name: 'enabled', field: 'enabled', label: 'Ativo', align: 'center' },
  { name: 'role', field: 'role', label: 'Permissão', align: 'center' },
  { name: 'passwordExpired', field: 'passwordExpired', label: 'Senha Expirada', align: 'center' },
  { name: 'createdIn', field: 'createdIn', label: 'Criado em', align: 'center', sortable: true },
  { name: 'modifiedIn', field: 'modifiedIn', label: 'Atualizado em', align: 'center', sortable: true },
  { name: 'actions', field: 'actions', label: 'Ação', align: 'center', sortable: false, hide: 'permitView()' }
]

function getSession() {
  return localforage.getItem(AUTH_CONST_JWT_SESSION);
}

export default {
  methods: {
    submit() {
      getSession().then(session => {
        if (session) {
          let user = {
            id: this.user.id,
            name: this.user.name,
            cpf: this.user.cpf,
            phone: this.user.phone,
            enabled: this.user.enabled,
            email: this.user.email,
            passwordExpired: this.user.passwordExpired,
            role: this.getIdFromRoleName(this.user.role.value),
          }

          userService.update(user, session).then(response => {
            if (response.status == 200) {
              this.notifyOk("Usuário atualizado com sucesso!");
              this.updateUserList();
              this.editMode = false;
            }
            else {
              this.notifyError("Ocorreu um erro na atualização do usuário.");
            }
          })
        }
      })
    },
    confirmDelete() {
      getSession().then(session => {
        if (session) {
          userService.delete(this.user.email, session).then(response => {
            if (response.status == 200) {
              this.notifyOk("Usuário excluído com sucesso!");
              this.updateUserList();
            }
            else {
              this.notifyError("Ocorreu um erro na exclusão do usuário.");
            }
          })
        }
      })
    },
    updateUserList() {
      this.rows = [];
      this.loading = true;

      getSession().then(session => {
        if (session) {
          userService.list(session).then(response => {
            if (response.status == 200) {
              response.data.forEach(obj => {
                let row = {
                  id: obj.id,
                  email: obj.email,
                  cpf: obj.cpf,
                  name: obj.name,
                  phone: obj.phone,
                  role: this.getRoleNameFromId(obj.role),
                  password: obj.password,
                  passwordExpired: obj.passwordExpired,
                  enabled: obj.enabled,
                  createdIn: obj.createdIn,
                  modifiedIn: obj.modifiedIn
                }

                this.rows.push(row);
              })

              this.loading = false;
            }
          })
        }
      })
    },
    // Converte de string para inteiro(id) o status
    getRoleNameFromId(id) {
      switch (id) {
        case 0: return 'Usuário';
        case 1: return 'Técnico';
        case 2: return 'Administrador';
      }
    },
    // Converte de string para inteiro(id) o status
    getIdFromRoleName(name) {
      switch (name) {
        case 'Usuário': return 0;
        case 'Técnico': return 1;
        case 'Administrador': return 2;
      }
    },
    isEmail(val) {
      const emailPattern = /^(?=[a-zA-Z0-9@._%+-]{6,254}$)[a-zA-Z0-9._%+-]{1,64}@(?:[a-zA-Z0-9-]{1,63}\.){1,8}[a-zA-Z]{2,63}$/
      return (emailPattern.test(val) || LANG_INPUT_EMAIL_INVALID);
    },
    required(val) {
      return (val && val.length > 0 || LANG_INPUT_REQUIRED);
    },
    editRow(props) {
      this.fillDataTicketFromProps(props);
      this.editMode = true;
    },
    deleteRow(props) {
      this.fillDataTicketFromProps(props);
      this.dialogDelete = true;
    },
    fillDataTicketFromProps(props) {

      this.user.id = props.row.id;
      this.user.cpf = props.row.cpf;
      this.user.email = props.row.email;
      this.user.name = props.row.name;
      this.user.phone = props.row.phone;
      this.user.role.value = props.row.role;
      this.user.enabled = props.row.enabled;
      this.user.passwordExpired = props.row.passwordExpired;
    },
    visibleColumns() {
      if (this.role > 0) {
        return ['id', 'cpf', 'email', 'name', 'phone', 'enabled', 'role', 'passwordExpired', 'createdIn', 'actions'];
      }
      else {
        return ['id', 'cpf', 'email', 'name', 'phone', 'enabled', 'role', 'passwordExpired', 'createdIn'];
      }
    }
  },

  data() {
    return {
      user: {
        id: null,
        email: null,
        name: null,
        cpf: null,
        phone: null,
        password: null,
        passwordExpired: false,
        enabled: false,
        role: {
          value: 'Usuário',
          options: ['Usuário', 'Técnico', 'Administrador']
        }
      },
      role: 0,
      dialogDelete: false,
      editMode: false,
      rows: [],
      filter: '',
      loading: false,
      columns: columns,
      pagination: {
        rowsPerPage: 10
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
    this.updateUserList();

    localforage.getItem(USER_SERVICE_SESSION_INFO).then(data => {
      return this.role = data.role;
    }).then(role => {
      if (role > 0) {
        this.role = role;
      }
    })
  }
}
</script>