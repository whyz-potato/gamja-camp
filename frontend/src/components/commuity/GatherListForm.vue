<template>
  <div>
    <div class="post">
      <div class="search">
        <v-text-field v-model="searchWord" placeholder="검색" filled rounded 
          color="black" full-width @keyup.enter="search"></v-text-field>
        <v-btn class="mt-2 ml-3" @click="search" icon>
          <v-icon>mdi-magnify</v-icon>
        </v-btn>
      </div>
      <div class="text-right">
        <v-btn @click="post" text rounded>글쓰기</v-btn>
        <!-- <v-btn @click="test">예약</v-btn> -->
      </div>
      <div class="ma-10">
        <v-list v-for="list in postList" :key="list.index">
          <v-list-item
              @click="postDetail(list.id)">
            <div class="d-flex">
              <div class="img-wrapper mr-10">
                <img v-if="list.image" :src="list.image">
                <div v-else class="grey lighten-5"></div>
              </div>
              
              <v-list-item-content>
                <v-list-item-title>{{list.title}}</v-list-item-title>
                <v-list-item-subtitle class="list-content mt-2">{{list.content}}</v-list-item-subtitle>
                <v-list-item-subtitle class="caption mt-4">
                  {{list.writer.username}} &#183; {{list.date}} &#183; {{list.time.toString().slice(0,5)}}
                </v-list-item-subtitle>
              </v-list-item-content>
            </div>
            
          </v-list-item>
        </v-list>
      </div>
    </div>
    
  </div>
</template>


<script>
import api from '@/api'

export default {
  data () {
    return {
      token: null,
      postList: [],
      files: [],
      urls: [],
      searchWord: null
    }
  },
  mounted () {
    api.get('/chats/csrf').then(res => {
      this.token = res.data.token
    })
    api.get(`/post/gather/list?lastPostId=100`).then(res => {
      this.postList = res.data
      console.log(this.postList)
    })

  },
  methods: {
    search () {
      api.get(`/post/gather/search?lastPostId=100&keyword=${this.searchWord}`).then(res => {
        console.log(res.data)
        this.postList = res.data
      })
    },
    post () {
      this.$router.push({ name: 'GatherRegister' })
    },
    postDetail (postId) {
      this.$router.push({ name: 'GatherDetail', query: { 'postId': postId }})
    },
  }
}
</script>


<style scoped>
.search {
  width: 600px;
  margin: auto;
  margin-top: 50px;
  display: flex;
}
.post {
  min-height: 1000px;
  margin: auto;
  margin-top: 50px;
}
.img-wrapper {
  position: relative;
  width: 100px;
  height: 100px;
}
.img-wrapper img{
  /* position: absolute;
  top: 0;
  left: 0;
  transform: translate(50, 50); */
  width: 100%;
  height: 100%;
  object-fit: cover;
  margin: auto;
}
.img-wrapper div {
  width: 100%;
  height: 100%;
}
.list-content {
  width: 100px;
  overflow: hidden; 
  text-overflow: ellipsis;  
  white-space: nowrap; 	
  word-break:break-all
}
</style>