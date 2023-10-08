<template>
  <div>
    test
    <!--채팅 테스트-->
    <v-container>
      message : 
      <input v-model="message" type="text">
      <v-btn @click="send">보내기</v-btn>

      <v-btn @click="chatSingleRoom">채팅방개설</v-btn>
      <v-btn @click="showMessage">메세지확인</v-btn>
    </v-container>
  </div>
</template>

<script>
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'
import api from '@/api'

export default {
  data () {
    return {
      message: '',
      recvList: [],
    }
  },
  created () {
    api.get('/chats/csrf').then(res => {
      console.log(res.data)
      this.connect(res.data.headerName, res.data.token)
    })
    api.get('/chats').then(res => {
      console.log('채팅목록')
      console.log(res.data)
    })
    
  },
  methods: {
    // sendMessage(e) {
    //   if(e.keyCode === 13 && this.uid !== '' && this.message !== '') {
    //     this.send()
    //     this.message = ''
    //   }
    // },
    send() {
      console.log("Send message:" + this.message)
      if (this.stompClient && this.stompClient.connected) {
        const msg = { 
          id: 1,
          content: this.message,
        }
        this.stompClient.send("/app/3", JSON.stringify(msg), {})
      }
    }, 
    connect(headerName, token) {
      let socket = new SockJS('http://localhost:8080/socket')
      let headers = { [headerName]: token }
      this.stompClient = Stomp.over(socket)

      console.log(`소켓 연결을 시도합니다`)

      this.stompClient.connect(
        headers,
        frame => {
          this.connected = true
          console.log('소켓 연결 성공', frame)
          this.stompClient.subscribe("/topic/3", res => {
            //console.log('구독으로 받은 메시지 입니다.', res.body)
            this.recvList.push(JSON.parse(res.body))
            //console.log(this.recvList)
          })
        },
        error => {
          console.log('소켓 연결 실패', error)
          this.connected = false
        } 
      )
    },
    chatSingleRoom () {
      api.post('/chats/single', {to: 1}).then(res => {
        console.log(res.data)
      })
      // this.stompClient.subscribe("/topic/1", res => {
      //   console.log('구독', res.body)
      //   this.recvList.push(JSON.parse(res.body))
      //   console.log(this.recvList)
      // })
    },
    showMessage () {
      api.get('/chats').then(res => {
        console.log(res.data)
      })
      api.get('/chats/6').then(res => {
        console.log(res.data)
      })
    }
  }
}
</script>