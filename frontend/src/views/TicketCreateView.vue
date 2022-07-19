<template>
  <q-card class="q-ma-md">
    <q-card-section>
      <h4 class="text-h5 text-black q-my-none">Novo Chamado</h4>
    </q-card-section>
    <q-card-section>
      <q-form ref="ticketForm">
        <div class="row q-my-sm">
          <div class="col q-mx-sm">
            <q-input v-model="ticket.requester" lazy-rules :rules="[this.required]" label="Solicitante">
            </q-input>
          </div>
          <div class="col">
            <q-input v-model="ticket.technician" lazy-rules :rules="[this.required]" label="Técnico">
            </q-input>
          </div>
        </div>
        <div class="row q-my-sm">
          <div class="col q-mx-sm">
            <q-select v-model="ticket.priority.value" :options="ticket.priority.options" lazy-rules
              :rules="[this.required]" label="Prioridade">
            </q-select>
          </div>
          <div class="col">
            <q-select v-model="ticket.status.value" :options="ticket.status.options" lazy-rules :rules="[this.required]"
              label="Status">
            </q-select>
          </div>
        </div>
        <div class="row q-my-sm">
          <div class="col q-mx-sm">
            <q-input v-model="ticket.title" lazy-rules :rules="[this.required]" type="text" label="Título">
            </q-input>
          </div>
        </div>
        <div class="row qmy-sm">
          <div class="col q-mx-sm">
            <q-input v-model="ticket.description" lazy-rules :rules="[this.required]" type="textarea" label="Descrição">
            </q-input>
          </div>
        </div>
        <div class="row qmy-sm">
          <div class="col q-mx-sm">
            <q-input v-model="ticket.notes" type="textarea" label="Observações">
            </q-input>
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
import { useQuasar } from 'quasar';
import { LANG_INPUT_REQUIRED } from '@/lang/lang.ptbr';
import ticketService from '@/services/ticket.service';
import localforage from 'localforage';
import { AUTH_CONST_JWT_SESSION } from '@/services/constants/auth.constants';

export default {
  methods: {
    submit() {
      this.validateForm().then(success => {
        if (success) {
          let ticket = {
            requester: this.ticket.requester,
            technician: this.ticket.technician,
            title: this.ticket.title,
            description: this.ticket.description,
            notes: this.ticket.notes,
            priority: this.getPriorityFromPriorityName(this.ticket.priority.value),
            status: this.getStatusFromStatusName(this.ticket.status.value),
          }

          // Cria o ticket conforme os dados inseridos no formulário
          this.getSession().then(session => {
            ticketService.save(ticket, session).then(response => {
              if (response.status == 200) {
                this.notifyOk("Chamado aberto com sucesso!");
                this.clear();
              }
              else {
                this.notifyError("Ocorreu um erro na abertura do chamado.");
              }
            })
          });
        }
      });
    },
    reset() {
      this.ticket.requester = null;
      this.ticket.technician = null;
      this.ticket.title = null;
      this.ticket.description = null;
      this.ticket.notes = null;
      this.ticket.priority.value = 'Baixa';
      this.ticket.status.value = 'Aberto';
    },
    clear() {
      this.reset();
      this.$refs.ticketForm.resetValidation();
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
    getSession() {
      return localforage.getItem(AUTH_CONST_JWT_SESSION);
    },
    validateForm() {
      return this.$refs.ticketForm.validate();
    },
    required(val) {
      return (val && val.length > 0 || LANG_INPUT_REQUIRED);
    }
  },
  data() {
    return {
      ticket: {
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

  }
}
</script>