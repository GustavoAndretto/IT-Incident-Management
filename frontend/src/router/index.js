import { createRouter, createWebHistory } from 'vue-router'

import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import ErrorView from '../views/PageNotFoundView.vue'
import TicketCreatetView from '../views/TicketCreateView.vue'
import TicketListView from '../views/TicketListView.vue'
import UserCreateView from '../views/UserCreateView.vue'
import UserProfileView from '../views/UserProfileView.vue'
import UserListView from '../views/UserListView.vue'

import authService from '@/services/auth.service'

// Configuração das rotas do endpoint do cliente
const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
    meta: {
      // Define se a rota precisará de autenticação para ser acessado
      requiresAuth: true,
    }
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView,
    meta: {
      // Define se a rota ficará inacessível no caso de a sessão já ter sido criada
      hideForAuth: true
    }
  },
  {
    path: '/user/create',
    name: 'user',
    component: UserCreateView,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/user/profile',
    name: 'user-profile',
    component: UserProfileView,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/user/list',
    name: 'user-list',
    component: UserListView,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/ticket/create',
    name: 'ticket',
    component: TicketCreatetView,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/ticket/list',
    name: 'ticket-list',
    component: TicketListView,
    meta: {
      requiresAuth: true
    }
  },
  {
    // Rotas inválidas serão direcionadas para a página de erro PageNotFound
    path: '/:pathMatch(.*)*',
    name: 'error',
    component: ErrorView
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  // Verifica se o endpoint necessita de autenticação
  if (to.matched.some(record => record.meta.requiresAuth)) {
    // Verifica se já está autenticado
    if (!authService.loggedIn()) {
      next({ name: 'login' });
    }
    else {
      next();
    }
  }
  // Redireciona para a home page caso já esteja autenticado
  else if (to.matched.some(record => record.meta.hideForAuth)) {
    if (authService.loggedIn()) {
      next({ name: 'home' });
    }
    else {
      next();
    }
  }
  // Redireciona para o endpoint solicitado sem verificação adicional
  else {
    next();
  }
})

export default router
