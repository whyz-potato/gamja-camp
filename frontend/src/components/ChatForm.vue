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
        <v-card-title>채팅목록
          <v-spacer></v-spacer>
          <v-btn class="mr-n3" @click="dialog=false" icon>
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>
        <div class="pa-1">
          <v-list rounded>
            <v-list-item-group>
              <v-list-item>
                <v-list-item-avatar>
                  <v-img :src="require(`@/assets/test/test1.jpg`)"></v-img>
                </v-list-item-avatar>
                <v-list-item-content>
                  <v-list-item-title>감자방 <span class="grey--text text--lighten-1">3</span></v-list-item-title>
                  <v-list-item-subtitle>안녕!!</v-list-item-subtitle>
                  <v-spacer></v-spacer>
                </v-list-item-content>
                <v-list-item-action>
                  <v-list-item-action-text>2023-08-20</v-list-item-action-text>
                  <div class="message-count">3</div>
                </v-list-item-action>   
              </v-list-item>
            </v-list-item-group>
            <v-list-item-group>
              <v-list-item>
                <v-list-item-avatar>
                  <v-img :src="require(`@/assets/test/test2.jpg`)"></v-img>
                </v-list-item-avatar>
                <v-list-item-content>
                  <v-list-item-title>서영</v-list-item-title>
                  <v-list-item-subtitle>우와</v-list-item-subtitle>
                  <v-spacer></v-spacer>
                </v-list-item-content>
                <v-list-item-action>
                  <v-list-item-action-text>2023-08-20</v-list-item-action-text>
                  <div class="message-count">2</div>
                </v-list-item-action>  
              </v-list-item>
            </v-list-item-group>
            <v-list-item-group>
              <v-list-item>
                <v-list-item-avatar>
                  <v-img :src="require(`@/assets/test/test3.jpg`)"></v-img>
                </v-list-item-avatar>
                <v-list-item-content>
                  <v-list-item-title>다슬</v-list-item-title>
                  <v-list-item-subtitle>ㅋㅋㅋ</v-list-item-subtitle>
                  <v-spacer></v-spacer>
                </v-list-item-content>
                <v-list-item-action>
                  <v-list-item-action-text>2023-08-20</v-list-item-action-text>
                  <div class="message-count">19</div>
                </v-list-item-action>  
              </v-list-item>
            </v-list-item-group>
          </v-list>
        </div>
        
      </v-card>
    </v-dialog>
    
  </div>
</template>


<script>
import api from '@/api'

export default {
  data () {
    return {
      dialog: false,
      chatList: null
    }
  },
  watch: {
    dialog () {
      console.log(this.dialog)
      if (this.dialog) {
        api.get('/chats').then(res => {
          console.log('채팅목록')
          console.log(res.data)
          this.chatList = res.data
        })
      }
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
</style>