<template>
  <q-card v-if="editMode" class="q-ma-md">
    <q-card-section>
      <div class="row">
        <div class="col">
          <h4 class="text-h5 text-black q-my-none">Editar Chamado</h4>
        </div>
      </div>
    </q-card-section>
    <q-card-section>
      <q-form ref="ticketForm">
        <div class="row q-my-sm">
          <div class="col q-mx-sm">
            <q-input dense v-model="ticket.requester" lazy-rules :rules="[this.required]" label="Solicitante">
            </q-input>
          </div>
          <div class="col">
            <q-input dense v-model="ticket.technician" lazy-rules :rules="[this.required]" label="Técnico">
            </q-input>
          </div>
        </div>
        <div class="row q-my-sm">
          <div class="col q-mx-sm">
            <q-select dense v-model="ticket.priority.value" :options="ticket.priority.options" lazy-rules
              :rules="[this.required]" label="Prioridade">
            </q-select>
          </div>
          <div class="col">
            <q-select dense v-model="ticket.status.value" :options="ticket.status.options" lazy-rules
              :rules="[this.required]" label="Status">
            </q-select>
          </div>
        </div>
        <div class="row q-my-sm">
          <div class="col q-mx-sm">
            <q-input dense v-model="ticket.title" lazy-rules :rules="[this.required]" type="text" label="Título">
            </q-input>
          </div>
        </div>
        <div class="row qmy-sm">
          <div class="col q-mx-sm">
            <q-input dense v-model="ticket.description" lazy-rules :rules="[this.required]" type="textarea"
              label="Descrição">
            </q-input>
          </div>
        </div>
        <div class="row qmy-sm">
          <div class="col q-mx-sm">
            <q-input dense v-model="ticket.notes" type="textarea" label="Observações">
            </q-input>
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
          <h4 class="text-h5 text-black q-my-none">Lista de Chamados</h4>
        </div>
        <div class="col flex justify-end align-center">
          <q-btn round size="sm" color="primary" icon="refresh" @click="updateTicketList" />
        </div>
      </div>
    </q-card-section>
    <q-card-section>
      <div class="row q-mb-sm">
        <div class="col">
          <q-input v-model="filter" label="Filtrar por número, solicitante, título, prioridade, ..." dense>
            <template v-slot:append>
              <q-icon name="search" />
            </template>
          </q-input>
        </div>
      </div>
      <q-table flat :rows="rows" :columns="columns" :loading="loading" :pagination="pagination" :filter="filter"
        row-key="name">
        <template v-slot:no-data>
          <div class="full-width row flex-center">
            <span>
              Nenhum registro encontrado
            </span>
          </div>
        </template>
        <template v-slot:body-cell-actions="props">
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
        <div class="text-h6">Deseja realmente excluir o chamado?</div>
      </q-card-section>

      <q-card-actions class="bg-white flex justify-end">
        <q-btn color="primary" flat label="Cancelar" v-close-popup />
        <q-btn flat color="primary" label="Confirmar" @click="confirmDelete" v-close-popup />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script>
import ticketService from '@/services/ticket.service';
import localforage from 'localforage';
import { AUTH_CONST_JWT_SESSION } from '@/services/constants/auth.constants';
import { LANG_INPUT_REQUIRED } from '@/lang/lang.ptbr';
import { useQuasar } from 'quasar';

const columns = [
  { name: 'id', field: 'id', label: 'Número', align: 'center', sortable: true },
  { name: 'requester', field: 'requester', label: 'Solicitante', align: 'center', sortable: true },
  { name: 'title', field: 'title', label: 'Título', align: 'center' },
  { name: 'priority', field: 'priority', label: 'Prioridade', align: 'center' },
  { name: 'status', field: 'status', label: 'Status', align: 'center' },
  { name: 'createdIn', field: 'createdIn', label: 'Criado em', align: 'center', sortable: true },
  { name: 'closedIn', field: 'closedIn', label: 'Encerrado em', align: 'center', sortable: true },
  { name: 'actions', field: 'actions', label: 'Ação', align: 'center', sortable: false }
]

function getSession() {
  return localforage.getItem(AUTH_CONST_JWT_SESSION);
}

export default {
  methods: {
    submit() {
      getSession().then(session => {
        if (session) {
          let ticket = {
            id: this.ticket.id,
            requester: this.ticket.requester,
            technician: this.ticket.technician,
            title: this.ticket.title,
            description: this.ticket.description,
            notes: this.ticket.notes,
            priority: this.getPriorityFromPriorityName(this.ticket.priority.value),
            status: this.getStatusFromStatusName(this.ticket.status.value),
          }

          ticketService.update(ticket, session).then(response => {
            if (response.status == 200) {
              this.notifyOk("Chamado atualizado com sucesso!");
              this.updateTicketList();
              this.editMode = false;
            }
            else {
              this.notifyError("Ocorreu um erro na atualização do chamado.");
            }
          })
        }
      })
    },
    confirmDelete() {
      getSession().then(session => {
        if (session) {
          ticketService.delete(this.ticket.id, session).then(response => {
            if (response.status == 200) {
              this.notifyOk("Chamado excluído com sucesso!");
              this.updateTicketList();
            }
            else {
              this.notifyError("Ocorreu um erro na exclusão do chamado.");
            }
          })
        }
      })
    },
    updateTicketList() {
      this.rows = [];
      this.loading = true;

      getSession().then(session => {
        if (session) {
          ticketService.list(session).then(response => {
            if (response.status == 200) {
              response.data.forEach(obj => {
                let row = {
                  id: obj.id,
                  requester: obj.requester,
                  title: obj.title,
                  description: obj.description,
                  notes: obj.notes,
                  priority: this.getPriorityNameFromPriority(obj.priority),
                  status: this.getStatusNameFromStatus(obj.status),
                  createdIn: obj.createdIn,
                  closedIn: obj.closedIn
                }

                this.rows.push(row);
              })

              this.loading = false;
            }
          })
        }
      })
    },
    getPriorityNameFromPriority(id) {
      switch (id) {
        case 0: return 'Baixa';
        case 1: return 'Média';
        case 2: return 'Alta';
      }
    },
    // Converte de string para inteiro(id) o status
    getStatusNameFromStatus(id) {
      switch (id) {
        case 0: return 'Encerrado';
        case 1: return 'Aberto';
        case 2: return 'Em Atendimento';
      }
    },
    // Converte de string para inteiro(id) a prioridade
    getPriorityFromPriorityName(name) {
      switch (name) {
        case 'Baixa': return 0;
        case 'Média': return 1;
        case 'Alta': return 2;
      }
    },
    // Converte de string para inteiro(id) o status
    getStatusFromStatusName(name) {
      switch (name) {
        case 'Encerrado': return 0;
        case 'Aberto': return 1;
        case 'Em Atendimento': return 2;
      }
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
      this.ticket.id = props.row.id;
      this.ticket.requester = props.row.requester;
      this.ticket.technician = props.row.technician;
      this.ticket.title = props.row.title;
      this.ticket.description = props.row.description;
      this.ticket.notes = props.row.notes;
      this.ticket.priority.value = props.row.priority;
      this.ticket.status.value = props.row.status;
    }
  },

  data() {
    return {
      ticket: {
        id: null,
        requester: null,
        technician: null,
        title: null,
        description: null,
        notes: null,
        priority: {
          value: 'Baixa',
          options: ['Baixa', 'Média', 'Alta']
        },
        status: {
          value: 'Aberto',
          options: ['Encerrado', 'Aberto', 'Em Atendimento']
        }
      },
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
    this.updateTicketList();
  }
}
</script>