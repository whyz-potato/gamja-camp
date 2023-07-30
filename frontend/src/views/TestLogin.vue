<template>
  <div>
    <v-btn @click="loginTest">카카오로그인테스트</v-btn>
  </div>
</template>


<script>
import api from '@/api'

export default {
  data () {
    return {
      code: null
    }
  },
  created () {
    console.log(this.$route.query.code)
    this.code = this.$route.query.code
  },
  methods: {
    loginTest () {
      api.get(`/oauth/callback/kakao?code=${this.code}`).then(() => {
        console.log('성공')
      })
    }
  }
}
</script>