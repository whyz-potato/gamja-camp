import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '@/views/Home'
import Test from '@/views/Test'
import Login from '@/views/member/Login'
import Join from '@/views/member/Join'
import Mypage from '@/views/member/Mypage'
import Community from '@/views/community/Community'
import GeneralRegister from '@/views/community/GeneralRegister'
import GeneralDetail from '@/views/community/GeneralDetail'
import GeneralUpdate from '@/views/community/GeneralUpdate'
import GatherRegister from '@/views/community/GatherRegister'
import GatherDetail from '@/views/community/GatherDetail'
import GatherUpdate from '@/views/community/GatherUpdate'
import RoomDetail from '@/views/RoomDetail'
import CampAdmin from '@/views/owner/CampAdmin'
import CampRegister from '@/views/owner/CampRegister'

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
    path: '/mypage',
    name: 'Mypage',
    component: Mypage
  },
  {
    path: '/community',
    name: 'Community',
    component: Community
  },
  {
    path: '/generalRegister',
    name: 'GeneralRegister',
    component: GeneralRegister
  },
  {
    path: '/generalDetail',
    name: 'GeneralDetail',
    component: GeneralDetail
  },
  {
    path: '/generalUpdate',
    name: 'GeneralUpdate',
    component: GeneralUpdate
  },
  {
    path: '/gatherRegister',
    name: 'GatherRegister',
    component: GatherRegister
  },
  {
    path: '/gatherDetail',
    name: 'GatherDetail',
    component: GatherDetail
  },
  {
    path: '/gatherUpdate',
    name: 'GatherUpdate',
    component: GatherUpdate
  },
  {
    path: '/roomDetail',
    name: 'RoomDetail',
    component: RoomDetail
  },
  {
    path: '/campAdmin',
    name: 'CampAdmin',
    component: CampAdmin
  },
  {
    path: '/campRegister',
    name: 'CampRegister',
    component: CampRegister
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
