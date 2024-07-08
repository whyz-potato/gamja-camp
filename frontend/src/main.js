import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import VueGeolocation from 'vue-geolocation-api'
import 'vue2-daterange-picker/dist/vue2-daterange-picker.css'
import 'material-icons/iconfont/material-icons.css'
import VueCookies from 'vue-cookies'
import 'vue-datepicker-ui/lib/vuedatepickerui.css'
import CKEditor from '@ckeditor/ckeditor5-vue2'

Vue.config.productionTip = false

Vue.use(VueGeolocation)
Vue.use(CKEditor)
Vue.use(VueCookies)
Vue.$cookies.config('1d')


window.Kakao.init('')

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')
