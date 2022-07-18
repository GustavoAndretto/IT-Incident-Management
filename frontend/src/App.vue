<template>
  <q-layout view="hhh LpR fFf">

    <CustomHeader v-if="loggedIn"/>

    <q-page-container>
      <router-view />
    </q-page-container>

  </q-layout>
</template>

<script>
import CustomHeader from '@/components/CustomHeader.vue';
import authService from './services/auth.service';

export default {
  components: {
    CustomHeader
  },
  setup() {
    // Desabilita o header no caso da sessão não ter sido criada ainda
    let loggedIn = authService.loggedIn();

    return {
      loggedIn
    }
  }
}
</script>