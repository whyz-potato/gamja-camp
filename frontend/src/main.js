import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import VueGeolocation from 'vue-geolocation-api'
import DateRangePicker from 'vue2-daterange-picker'
import 'vue2-daterange-picker/dist/vue2-daterange-picker.css'
import 'material-icons/iconfont/material-icons.css';

Vue.config.productionTip = false

Vue.use(VueGeolocation)
Vue.use(DateRangePicker)

window.Kakao.init(process.env.VUE_APP_KAKAO_KEY)

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')
