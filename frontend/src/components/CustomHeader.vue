<template>
  <q-header reveal class="bg-primary text-white">
    <q-toolbar>
      <q-btn dense flat round icon="menu" @click="toggleLeftDrawer" />

      <q-toolbar-title>
        <q-avatar>
          <img src="https://cdn.quasar.dev/logo-v2/svg/logo-mono-white.svg">
        </q-avatar>
        Incident Management
      </q-toolbar-title>
      <q-space />
      <q-btn-dropdown flat rounded dense dropdown-icon="account_circle" no-icon-animation>
        <div class="items-center column no-wrap q-pa-md">
          <q-avatar color="primary" text-color="white">{{user.initial}}</q-avatar>
          <div class="text-subtitle1 q-mt-xs">{{user.name}}</div>
          <q-btn class="full-width q-mt-xs" color="primary" text-color="white" size="sm" to="/user/profile"
            label="Meu Perfil" v-close-popup />
          <q-btn class="full-width q-mt-xs" color="primary" text-color="white" label="Logout" size="sm"
            @click="logout()" v-close-popup />
        </div>
      </q-btn-dropdown>
    </q-toolbar>
  </q-header>

  <q-drawer show-if-above v-model="leftDrawerOpen" side="left" bordered>

    <q-scroll-area class="fit">
      <q-list>

        <template v-for="(menuItem, index) in menuList" :key="index">
          <q-item clickable v-ripple :to="menuItem.route">
            <q-item-section avatar>
              <q-icon :name="menuItem.icon" />
            </q-item-section>
            <q-item-section>
              {{ menuItem.label }}
            </q-item-section>
          </q-item>
          <q-separator :key="'sep' + index" v-if="menuItem.separator" />
        </template>

      </q-list>
    </q-scroll-area>

  </q-drawer>
</template>

<script>
import { ref } from 'vue'
import authService from '@/services/auth.service';
import localforage from 'localforage';
import { USER_SERVICE_SESSION_INFO } from '@/services/constants/user.constants';

const menuList = [
  {
    icon: 'home',
    label: 'Home',
    separator: false,
    route: '/'
  },
  {
    icon: 'person_add',
    label: 'Novo Usuário',
    separator: false,
    route: '/user/create'
  },
  {
    icon: 'people',
    label: 'Lista de Usuários',
    separator: false,
    route: '/user/list'
  },
  {
    icon: 'assignment',
    label: 'Novo Chamado',
    separator: false,
    route: '/ticket/create'
  },
  {
    icon: 'view_list',
    label: 'Lista de Chamados',
    separator: false,
    route: '/ticket/list'
  }
]

export default {
  methods: {
    logout() {
      if (authService.loggedIn()) {
        authService.logout();
      }
    }
  },
  setup() {
    const leftDrawerOpen = ref(false);

    let user = {
      name: null,
      initial: null
    }

    // Carrega o nome do usuário e inicial do nome
    localforage.getItem(USER_SERVICE_SESSION_INFO).then(data =>{
      user.name = data.name;
      user.initial = data.name.charAt(0);
    })

    return {
      user,
      menuList,
      leftDrawerOpen,
      toggleLeftDrawer() {
        leftDrawerOpen.value = !leftDrawerOpen.value
      }
    }
  }
}
</script>