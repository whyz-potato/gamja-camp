<template>
  <div>
    test
    <!--채팅 테스트-->
    <v-container>
      uid : 
      <input v-model="uid" type="text" >

      message : 
      <input v-model="message" type="text" @keyup="sendMessage">

      <div class="mt-2" v-for="(item, idx) in recvList" :key="idx">
        <v-card class="mt-2 mb-2" color="teal lighten-3" dark max-width="400">
          <v-card-text>
            <div>uid : {{ item.user.id }}</div>
            <div>{{ item.message }}</div>
            <div>{{ item.sendAt }}</div>
          </v-card-text>
        </v-card>
      </div>
    </v-container>

    <div>
      <a href="http://localhost:8080/oauth2/authorization/kakao">카카오로그인</a>
    </div>
    <div>
      <a href="http://localhost:8080/oauth2/authorization/google">google</a>
    </div>
    <div>
      <a href="http://localhost:8080/oauth2/authorization/naver">naver</a>
    </div>
  </div>
</template>

<script>
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'
import api from '@/api'

export default {
  data () {
    return {
      uid: '',
      message: '',
      recvList: [],
      token: ''
    }
  },
  created () {
    this.connect()
  },
  methods: {
    sendMessage(e) {
      if(e.keyCode === 13 && this.uid !== '' && this.message !== '') {
        this.send()
        this.message = ''
      }
    },
    send() {
      console.log("Send message:" + this.message)
      if (this.stompClient && this.stompClient.connected) {
        const msg = { 
        user: {
            id: this.uid
        },
        message: this.message,
        sendAt: Date.now(),
        isRequest: false,
        }
        this.stompClient.send("/receive", JSON.stringify(msg), {})
      }
    }, 
    connect() {
      api.get('/chats/csrf').then(res => {
        console.log(res.data)
        this.token = res.data.token
      })
      
      let socket = new SockJS('http://localhost:8080/chats')
      let headers = { 'X-CSRF-TOKEN': this.token }
      this.stompClient = Stomp.over(socket)

      console.log(`소켓 연결을 시도합니다`)

      this.stompClient.connect(
        headers,
        frame => {
          this.connected = true
          console.log('소켓 연결 성공', frame)
          // this.stompClient.subscribe("/send", res => {
          //   console.log('구독으로 받은 메시지 입니다.', res.body)
          //   this.recvList.push(JSON.parse(res.body))
          // })
        },
        error => {
          console.log('소켓 연결 실패', error)
          this.connected = false
        } 
      )
    }
  }
}
</script>