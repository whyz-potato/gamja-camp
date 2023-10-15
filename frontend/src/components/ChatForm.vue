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
                    @click="getMessageList(list.roomId, list.title, list.lastMessage.id)">
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
                      <v-list-item-action-text>{{lastMessageDateTime(list.lastMessage.date, list.lastMessage.time)}}</v-list-item-action-text>
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
                    <p class="message-time" v-if="sameTime(null, index)" >
                      {{list.time.toString().slice(0,5)}}
                    </p>
                    <p class="my-message-paragraph">{{list.content}}</p>
                  </div>

                  <div v-else class="your-message">
                    <div class="your-message-avartar">
                      <img :src="list.from.picture" class="your-message-img"
                         v-if="sameUser(null, index)">
                    </div>
                    <div>
                      <p class="your-message-user" v-if="sameUser(null, index)">
                        {{list.from.username}}
                      </p>
                      <div class="your-message-p">
                        <p class="your-message-paragraph">{{list.content}}</p>
                        <p class="message-time" v-if="sameTime(null, index)">
                          {{list.time.toString().slice(0,5)}}
                        </p>
                      </div>
                    </div>
                  </div>
                </div>

                <div v-if="subMessageList.length != 0">
                  <div v-for="(list, index) in subMessageList" :key="index">
                    <div v-if="userId == list.from.id" class="my-message">
                      <p class="message-time" v-if="sameTime('sub', index)" >
                        {{list.time.toString().slice(0,5)}}
                      </p>
                      <p class="my-message-paragraph">{{list.content}}</p>
                    </div>

                    <div v-else class="your-message">
                      <div class="your-message-avartar">
                        <img :src="list.from.picture" class="your-message-img"
                          v-if="sameUser('sub', index)">
                      </div>
                      <div>
                        <p class="your-message-user" v-if="sameUser('sub', index)">
                          {{list.from.username}}
                        </p>
                        <div class="your-message-p">
                          <p class="your-message-paragraph">{{list.content}}</p>
                          <p class="message-time" v-if="sameTime('sub', index)">
                            {{list.time.toString().slice(0,5)}}
                          </p>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                
                <div class="new-message" v-if="!bottom && recentMessageUserName != ''" @click="scrollToBottom">
                  <div class="username">{{recentMessageUserName}}</div>
                  <div class="content">{{recentMessageContent}}</div>
                  <div>
                    <v-icon color="white" class="mr-1">mdi-chevron-down</v-icon>
                  </div>
                </div>

                <div class="scrolldown-btn" v-if="!bottom && recentMessageUserName == ''" @click="scrollToBottom">
                  <v-icon large>mdi-chevron-down</v-icon>
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
      roomId: null,
      roomTitle: null,
      sheet: 1,
      messageList: [],
      userId: null,
      message: '',
      subMessageList: [],
      beforMessageList: [],
      showPrevMessage: false,
      beforScrollHeight: 0,
      bottom: false,
      recentMessageUserName: '',
      recentMessageContent: '',
      recentMessageUserId: ''
    }
  },
  watch: {
    dialog () {
      console.log(this.dialog)
      if (this.dialog) {
        api.get('/chats').then(res => {
          this.chatRoomList = res.data.chats
          console.log(this.chatRoomList)
        })
        this.userId = this.$cookies.get('id')
        //console.log(this.userId)
      } else {
        this.sheet = 1
        this.subMessageList = []
        this.roomId = null
      }
    },
    sheet () {
      console.log(this.sheet)
      if (this.sheet == 1) {
        api.get('/chats').then(res => {
          this.chatRoomList = res.data.chats
        })
        this.subMessageList = []
        this.roomId = null

        // this.stompClient.unsubscribe(`/topic/3`, res => {
        //   console.log(res.data)
        // })
      }
    },
    messageList () {
      this.$nextTick(() => {
        if (this.messageList.length <= 10) {
          this.$refs.chatRef.scrollTo({ top: this.$refs.chatRef.scrollHeight, behavior: 'smooth' })
          this.bottom = true
        } else {
          this.$refs.chatRef.scrollTop = this.$refs.chatRef.scrollHeight - this.beforScrollHeight
        }
      })
    },
    subMessageList () {
      this.$nextTick(() => {
        if (this.sheet == 2 && this.bottom == true) {
          this.$refs.chatRef.scrollTo({ top: this.$refs.chatRef.scrollHeight, behavior: 'smooth' })
        } else if (this.sheet == 2 && this.bottom == false && this.recentMessageUserId == this.userId) {
          this.$refs.chatRef.scrollTo({ top: this.$refs.chatRef.scrollHeight, behavior: 'smooth' })
        } 
        // if (this.subMessageList.length != 0) {
        //   //const recentMessageUserId = this.subMessageList[this.subMessageList.length - 1].from.id

          
        //   // else if (this.sheet == 2 && this.bottom == false && recentMessageUserId != this.userId) {
        //   //   this.recentMessageUserName = this.subMessageList[this.subMessageList.length - 1].from.username
        //   //   this.recentMessageContent = this.subMessageList[this.subMessageList.length - 1].content
        //   // }
        // }
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
    },
    lastMessageDateTime () {
      return (date, time) => {
        const today = new Date()
        const stringToday = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate()
        
        if (stringToday == date) {
          return time.toString().slice(0, 5)
        }

        return date
      }
      
    }
  },
  mounted () {
    api.get('/chats/csrf').then(res => {
      this.connect(res.data.headerName, res.data.token)
    })
  },
  methods: {
    getMessageList (roomId, title, messageId) {
      this.roomId = roomId
      this.roomTitle = title
      this.sheet = 2

      api.get(`/chats/${this.roomId}`).then(res => {
        //console.log(res.data.messages)
        this.messageList = res.data.messages
      })
      api.post(`/chats/${this.roomId}/last-read/${messageId}`).then(()=> {
      })

      // this.stompClient.subscribe(`/topic/3`, res => {
      //   if (this.dialog && this.sheet == 2) {
      //     this.recentMessageUserId = JSON.parse(res.body).from.id

      //     if (this.bottom == false && this.recentMessageUserId != this.userId) {
      //       this.recentMessageUserName = JSON.parse(res.body).from.username
      //       this.recentMessageContent = JSON.parse(res.body).content
      //     }

      //     this.subMessageList.push(JSON.parse(res.body))
      //     console.log('subscribe', this.subMessageList)

      //     const recentMessageId = JSON.parse(res.body).id
      //     api.post(`/chats/${this.roomId}/last-read/${recentMessageId}`).then(()=> {
      //     })
      //   }
      // })
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
          this.stompClient.subscribe(`/topic/3`, res => {
            if (this.dialog && this.sheet == 2) {
              this.recentMessageUserId = JSON.parse(res.body).from.id

              if (this.bottom == false && this.recentMessageUserId != this.userId) {
                this.recentMessageUserName = JSON.parse(res.body).from.username
                this.recentMessageContent = JSON.parse(res.body).content
              }

              this.subMessageList.push(JSON.parse(res.body))
              console.log('subscribe', this.subMessageList)

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
    sameTime (type, index) {
      let list = []

      if (type == 'sub') {
        list = this.subMessageList
      } else list = this.messageList

      if (index == list.length-1) return true
      else if (list[index].time.toString().slice(0,5) != list[index+1].time.toString().slice(0,5)
        || list[index].from.id != list[index+1].from.id
      ) return true

      return false
    },
    sameUser (type, index) {
      let list = []

      if (type == 'sub') {
        list = this.subMessageList
      } else list = this.messageList

      if (index == 0) return true
      else if (
        list[index-1].time.toString().slice(0,5) != list[index].time.toString().slice(0,5)
        || list[index-1].from.id != list[index].from.id
      ) return true

      return false
    },
    onScroll (e) {
      const scrollTop = e.target.scrollTop
      const scrollHeight = e.target.scrollHeight
      const clientHeight = this.$refs.chatRef.clientHeight
      
      //console.log('scrollTop', scrollTop, 'scrollHeight', scrollHeight, 'clientHeight', clientHeight )

      if (scrollTop == 0) {
        this.beforScrollHeight = scrollHeight

        const messageId = this.messageList[0].id

        api.get(`/chats/${this.roomId}?start=${messageId}`).then(res => {
          //console.log(res.data.messages)
          this.beforMessageList = res.data.messages

          if (this.beforMessageList.length != 0) {
            this.messageList = this.beforMessageList.concat(this.messageList)           
          }
        })
      }

      if (scrollTop + clientHeight + 10 >= scrollHeight) {
        this.bottom = true
        this.recentMessageUserName = ''
        this.recentMessageContent = ''
      } else if(scrollTop + clientHeight < scrollHeight + 100) {
        this.bottom = false
      }
      
    },
    scrollToBottom () {
      this.$refs.chatRef.scrollTo({ top: this.$refs.chatRef.scrollHeight, behavior: 'smooth' })
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
  position: relative;
  z-index: 1;
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
.new-message {
  position: fixed;
  z-index: 100;
  height: 30px;
  width: 250px;
  line-height : 30px;
  border-radius: 50px;
  background: #bdbdbd;
  opacity: 0.6;
  left: 50%;
  top: 70%;
  margin-left: -125px;
  display: flex;
  font-size: 14px;
  color: white;
  cursor: pointer;
}
.new-message > .username {
  width: 50px;
  margin-left: 10px;
  margin-right: 10px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.new-message > .content {
  width: 160px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.scrolldown-btn {
  position: fixed;
  z-index: 100;
  height: 40px;
  width: 40px;
  line-height : 30px;
  border-radius: 50px;
  left: 50%;
  top: 70%;
  margin-left: -20px;
  cursor: pointer;
}
</style>