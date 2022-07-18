<template>
  <q-card class="q-ma-md">
    <q-card-section>
      <h4 class="text-h5 text-black q-my-none">Atualizar Informações do Usuário</h4>
    </q-card-section>
    <q-card-section>
      <div class="row q-my-sm">
        <div class="col q-mx-sm">
          <q-input label="Nome">
            <template v-slot:prepend>
              <q-icon name="account_circle" />
            </template>
          </q-input>
        </div>
        <div class="col">
          <q-input label="CPF">
            <template v-slot:prepend>
              <q-icon name="123" />
            </template>
          </q-input>
        </div>
      </div>
      <div class="row q-my-sm">
        <div class="col q-mx-sm">
          <q-input label="Telefone">
            <template v-slot:prepend>
              <q-icon name="phone" />
            </template>
          </q-input>
        </div>
        <div class="col">
          <q-input label="Perfil de Acesso">
            <template v-slot:prepend>
              <q-icon name="lock" />
            </template>
          </q-input>
        </div>
      </div>
      <div class="row q-my-sm">
        <div class="col q-mx-sm">
          <q-input disable label="Email">
            <template v-slot:prepend>
              <q-icon name="email" />
            </template>
          </q-input>
        </div>
        <div class="col">
          <q-input label="Senha">
            <template v-slot:prepend>
              <q-icon name="key" />
            </template>
          </q-input>
        </div>
      </div>
    </q-card-section>
      <q-card-actions class="row justify-end">
        <q-btn outline size="md" color="primary" label="Limpar" />
        <q-btn outline size="md" color="red" class="text-white" label="Excluir" />
        <q-btn unelevated size="md" color="primary" class="text-white" label="Salvar" />
      </q-card-actions>
  </q-card>
</template>

<script>
export default {
  setup() {
    return {
    }
  }
}
</script>