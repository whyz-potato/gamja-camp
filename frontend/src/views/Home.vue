<template>
  <div style="height:100%;">
    <CampList></CampList>
  </div>
</template>

<script>
import CampList from '@/components/home/CampList'
import api from '@/api'
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'

export default {
  name: 'Home',
  components: {
    CampList
  },
  mounted() {
    api.get('/login-member').then(res => {
      console.log(res.data)
    })
    api.get('/chats/csrf').then(res => {
      console.log(res.data)
      this.connect(res.data.headerName, res.data.token)
    })
  },
  data () {
    return {

    }
  },
  methods: {
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
          this.stompClient.subscribe("/topic/1", res => {
            console.log('구독으로 받은 메시지 입니다.', res.body)
            this.recvList.push(JSON.parse(res.body))
          })
        },
        error => {
          console.log('소켓 연결 실패', error)
          this.connected = false
        } 
      )
    },
  }
}
</script>
