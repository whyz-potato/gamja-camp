<template>
  <div>
    test
    <!--BE 연결테스트-->
    <v-btn @click="test">test</v-btn>

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

    <!--카카오로그인-->
    <div>
      <v-btn @click="kakaoLogin">카카오로그인</v-btn>
    </div>
    <div>
      <v-btn @click="googleLogin">구글로그인</v-btn>
    </div>
    <div>
      <v-btn @click="login">왜안돼?</v-btn>
    </div>
  </div>
</template>

<script>
import api from '@/api'
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'

export default {
  data () {
    return {
      uid: '',
      message: '',
      recvList: []
    }
  },
  created () {
    //this.connect()
  },
  methods: {
    test () {
      api.get('/test').then(res => {
        console.log(res.data)
      })
    },
    sendMessage(e) {
      if(e.keyCode === 13 && this.uid !== '' && this.message !== '') {
        this.send();
        this.message = '';
      }
    },
    send() {
      console.log("Send message:" + this.message);
      if (this.stompClient && this.stompClient.connected) {
        const msg = { 
        user: {
            id: this.uid
        },
        message: this.message,
        sendAt: Date.now(),
        isRequest: false,
        };
        this.stompClient.send("/receive", JSON.stringify(msg), {});
      }
    }, 
    connect() {
      const serverURL = "http://localhost:8080"
      let socket = new SockJS(serverURL);
      this.stompClient = Stomp.over(socket);
      console.log(`소켓 연결을 시도합니다. 서버 주소: ${serverURL}`)
      this.stompClient.connect(
        {},
        frame => {
          this.connected = true;
          console.log('소켓 연결 성공', frame);
          this.stompClient.subscribe("/send", res => {
            console.log('구독으로 받은 메시지 입니다.', res.body);
            this.recvList.push(JSON.parse(res.body))
          });
        },
        error => {
          console.log('소켓 연결 실패', error);
          this.connected = false;
        } 
      );   
    },
    kakaoLogin () {
      const params = {
        redirectUri: 'http://localhost:7777/login/oauth2/code/kakao'
      }
      window.Kakao.Auth.authorize(params)
    },
    googleLogin () {
      const clientId = '934503312540-qdiapmg4doo3p5fmie1p34mnfnj9bcd8.apps.googleusercontent.com'
      const redirectUri = 'http://localhost:7777/login/oauth2/code/google'
      
      const url = 'https://accounts.google.com/o/oauth2/v2/auth?client_id=' + clientId +
      '&redirect_uri=' + redirectUri +
      '&response_type=code' + '&scope=email profile'

      window.open(url, '_blank', 'width=400,height=600')
    },
    login () {
      // api.post(`/oauth2/authorization/kakao?redirect_uri=http://localhost:7777/oauth/kakao`).then(() => {
      //   console.log('성공')

      // })
      const url = 'http://localhost:8080/oauth2/authorization/kakao'
      window.open(url, '_blank', 'width=400,height=600')

    }
  }
}
</script>