import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '@/views/Home'
import Test from '@/views/Test'
import Login from '@/views/member/Login'
import Join from '@/views/member/Join'
import RoomDetail from '@/views/RoomDetail'
import TestLoginKakao from '@/views/TestLoginKakao'
import TestLoginGoogle from '@/views/TestLoginGoogle'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/test',
    name: 'Test',
    component: Test
  },
  {
    path: '/login/oauth2/code/kakao',
    name: 'TestLoginKakao',
    component: TestLoginKakao,
    props: {
      default: true
    }
  },
  {
    path: '/login/oauth2/code/google',
    name: 'TestLoginGoogle',
    component: TestLoginGoogle,
    props: {
      default: true
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/join',
    name: 'Join',
    component: Join
  },
  {
    path: '/roomDetail',
    name: 'RoomDetail',
    component: RoomDetail
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
