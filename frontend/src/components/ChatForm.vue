<template>
  <div>
    <v-dialog v-model="dialog" width="400px"
      transition="dialog-bottom-transition">
      <template v-slot:activator="{ on }">
        <v-btn v-on="on" fab top right absolute>
          <v-icon>mdi-plus</v-icon>
        </v-btn>
      </template>
      
      <v-card class="card" height="600">
        <v-card-title>{{currentTitle}}
          <v-spacer></v-spacer>
          <v-btn v-if="sheet==1" class="mr-n3" @click="dialog=false" icon>
            <v-icon>mdi-close</v-icon>
          </v-btn>
          <v-btn v-else class="mr-n3" @click="sheet=1" icon>
            <v-icon>mdi-arrow-left</v-icon>
          </v-btn>
        </v-card-title>
        <v-divider class="mx-2"></v-divider>
        <v-window v-model="sheet">
          <v-window-item :value="1">

            <!-- 테스트용 -->
            <div v-if="chatRoomList.length == 0">
              방없음
              <v-btn @click="makeSingleRoom">채팅방개설</v-btn>
            </div>

            <div class="pa-1">
              <v-list rounded flat>
                <v-list-item-group>
                  <v-list-item v-for="list in chatRoomList" :key="list.index" 
                    @click="showChats(list.roomId, list.title, list.lastMessage.id)">
                    <v-list-item-avatar>
                      <img :src="list.lastMessage.from.picture">
                    </v-list-item-avatar>
                    <v-list-item-content>
                      <v-list-item-title>{{list.title}}
                        <span v-if="list.numParticipants>2"
                          class="grey--text text--lighten-1 ml-1">{{list.numParticipants}}</span>
                      </v-list-item-title>
                      <v-list-item-subtitle>{{list.lastMessage.content}}</v-list-item-subtitle>
                      <v-spacer></v-spacer>
                    </v-list-item-content>
                    <v-list-item-action>
                      <v-list-item-action-text>{{list.lastMessage.date.toString().slice(0,10)}}</v-list-item-action-text>
                      <div v-if="list.numUnreadMessages!=0" class="message-count">{{list.numUnreadMessages}}</div>
                    </v-list-item-action>   
                  </v-list-item>
                </v-list-item-group>
              </v-list>
            </div>
          </v-window-item>

          <v-window-item :value="2">
            <div class="pa-3">
              <div class="message-list" ref="chatRef" v-scroll.self="onScroll">
                <div v-for="(list, index) in messageList" :key="index">
                  <div v-if="userId == list.from.id" class="my-message">
                    <p class="message-time" v-if="sameTime(index)" >
                      {{list.time.toString().slice(0,5)}}
                    </p>
                    <p class="my-message-paragraph">{{list.content}}</p>
                  </div>

                  <div v-else class="your-message">
                    <div class="your-message-avartar">
                      <img :src="list.from.picture" class="your-message-img"
                         v-if="sameUser(index)">
                    </div>
                    <div>
                      <p class="your-message-user" v-if="sameUser(index)">
                        {{list.from.username}}
                      </p>
                      <div class="your-message-p">
                        <p class="your-message-paragraph">{{list.content}}</p>
                        <p class="message-time" v-if="sameTime(index)">
                          {{list.time.toString().slice(0,5)}}
                        </p>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="d-flex">
                <textarea v-model="message" cols="40" rows="1" name="text" 
                  class="input-chat" @keydown.enter="send($event)"></textarea>
                <v-btn @click="send" class="ml-2 mt-5" color="grey" text rounded>전송</v-btn>
              </div>
            </div>          
          </v-window-item>
        </v-window>
      </v-card>        
    </v-dialog>   
  </div>
</template>


<script>
import api from '@/api'
import Stomp from 'webstomp-client'
import SockJS from 'sockjs-client'

export default {
  data () {
    return {
      dialog: false,
      chatRoomList: [],
      roomTitle: null,
      sheet: 1,
      dbMessageList: [],
      messageList: [],
      userId: null,
      message: '',
      recvMessageList: [],
      prevMessageList: [],
      showPrevMessage: false,
      firstMessage: false,
      prevScrollHeight: 0,
    }
  },
  watch: {
    dialog () {
      console.log(this.dialog)
      if (this.dialog) {
        api.get('/chats').then(res => {
          this.chatRoomList = res.data.chats
        })
        this.userId = this.$cookies.get('id')
        //console.log(this.userId)
      } else {
        this.sheet = 1
        this.recvMessageList = []
      }
    },
    sheet () {
      console.log(this.sheet)
      if (this.sheet == 1) {
        api.get('/chats').then(res => {
          this.chatRoomList = res.data.chats
        })
        this.recvMessageList = []
      }
    },
    messageList () {
      this.$nextTick(() => {
        if (this.messageList.length <= 10) {
          this.$refs.chatRef.scrollTo({ top: this.$refs.chatRef.scrollHeight, behavior: 'smooth' })
        } else {
          this.$refs.chatRef.scrollTop = this.$refs.chatRef.scrollHeight - this.prevScrollHeight
        }
      })
    },
    recvMessageList () {
      this.$nextTick(() => {
        if (this.sheet == 2) {
          this.$refs.chatRef.scrollTo({ top: this.$refs.chatRef.scrollHeight, behavior: 'smooth' })
        }
      })
    }
  },
  computed: {
    currentTitle () {
      switch (this.sheet) {
        case 1: return '채팅목록'
        case 2: return this.roomTitle
        default: return '채팅목록'
      }
    }
  },
  mounted () {
    api.get('/chats/csrf').then(res => {
      console.log(res.data)
      this.connect(res.data.headerName, res.data.token)
    })
  },
  methods: {
    showChats (roomId, title, messageId) {
      this.roomId = roomId
      this.roomTitle = title
      this.sheet = 2

      api.get(`/chats/${this.roomId}`).then(res => {
        //console.log(res.data.messages)
        this.dbMessageList = res.data.messages
        this.messageList = res.data.messages
      })
      api.post(`/chats/${this.roomId}/last-read/${messageId}`).then(()=> {
      })
    },
    connect(headerName, token) {
      let socket = new SockJS('http://localhost:8080/socket')
      let headers = { [headerName]: token }
      this.stompClient = Stomp.over(socket)

      this.stompClient.connect(
        headers,
        frame => {
          this.connected = true
          console.log('소켓 연결 성공', frame)
          this.stompClient.subscribe("/topic/3", res => {
            if (this.dialog && this.sheet == 2) {
              this.recvMessageList.push(JSON.parse(res.body))
              console.log('recv', this.recvMessageList)

              this.messageList = this.dbMessageList.concat(this.recvMessageList)

              const recentMessageId = JSON.parse(res.body).id
              api.post(`/chats/${this.roomId}/last-read/${recentMessageId}`).then(()=> {
              })
            }
          })
        },
        error => {
          console.log('소켓 연결 실패', error)
          this.connected = false
        } 
      )
    },
    send(e) {
      e.preventDefault()
      //console.log("Send message:" + this.message)
      if (this.stompClient && this.stompClient.connected && this.message!='') {
        const msg = { 
          id: this.userId,
          content: this.message,
        }
        this.stompClient.send("/app/3", JSON.stringify(msg), {})
      }

      this.message = ''
    }, 
    sameTime (index) {
      if (index == this.messageList.length-1) return true
      else if (this.messageList[index].time.toString().slice(0,5) != this.messageList[index+1].time.toString().slice(0,5)
        || this.messageList[index].from.id != this.messageList[index+1].from.id
      ) return true

      return false
    },
    sameUser (index) {
      if (index == 0) return true
      else if (
        this.messageList[index-1].time.toString().slice(0,5) != this.messageList[index].time.toString().slice(0,5)
        || this.messageList[index-1].from.id != this.messageList[index].from.id
      ) return true

      return false
    },
    onScroll (e) {
      if (e.target.scrollTop == 0) {
        this.prevScrollHeight = e.target.scrollHeight

        const messageId = this.messageList[0].id

        api.get(`/chats/${this.roomId}?start=${messageId}`).then(res => {
          //console.log(res.data.messages)
          this.prevMessageList = res.data.messages

          if (this.prevMessageList.length != 0) {
            this.messageList = this.prevMessageList.concat(this.messageList)           
          }
        })
      }
    },
    makeSingleRoom () {
      api.post('/chats/single', {to: 1}).then(res => {
        console.log(res.data)
        this.roomTitle = res.data.title
        this.sheet = 2
      })
    }
  }
}
</script>


<style scoped>
.card {
  border-radius: 12px;
}
.message-count {
  width: 18px;
  height: 18px;
  background-color: #e4dabc;
  border-radius: 50px;
  font-size: 12px;
  font-weight: bold;
  color: white;
  text-align: center;

}
.message-list {
  height: 420px;
  padding-left: 10px;
  padding-right: 10px;
  overflow: scroll;
}
.my-message {
  display: flex;
  justify-content: right;
  align-items: flex-end;
  margin: 0;
  min-height: 40px;
  line-break: anywhere;
}
.my-message-paragraph {
  margin: 0.4rem 0 0 0.5rem;
  border-radius: 20px 20px 0px 20px;
  max-width: 180px;
  background-color: #e4dabc;
  color: #ffffff;
  padding: 0.8rem;
  font-size: 14px;
}
.your-message {
  display: flex;
  /* margin-bottom: 10px; */
}
.your-message-avartar {
  width: 40px;
  margin-right: 1rem;
  margin-top: 10px;
}
.your-message-img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}
.your-message-user {
  font-size: 14px;
  color: #292929;
  margin-top: 15px;
  margin-block-end: 0rem;
}
.your-message-p {
  display: flex;
  align-items: flex-end;
  line-break: anywhere;
}
.your-message-paragraph {
  margin: 0.4rem 0.5rem 0 0;
  border-radius: 0px 20px 20px 20px;
  background-color: #f3f3f3;
  max-width: 180px;
  color: #414141;
  padding: 0.8rem;
  font-size: 14px;
}
.message-time {
  margin: 0;
  font-size: 12px;
  color: #9c9c9c;
}
.input-chat {
  resize: none; 
  height: 90px;
  border: 1px solid #9c9c9c;
  border-radius: 12px;
  padding: 5px;
}
.input-chat:focus {
  outline: none;
  border: 3px solid #e4dabc;
  border-radius: 12px;
}
/* .v-list-item--active {
  background-color: white;
} */
</style>