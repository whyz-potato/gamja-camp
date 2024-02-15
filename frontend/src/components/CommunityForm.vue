<template>
  <div>
    <div class="post">
      <div class="search">
        <v-text-field placeholder="검색" filled rounded color="black"></v-text-field>
        <v-btn @click="post">등록</v-btn>
        <v-btn @click="test">test</v-btn>
      </div>
    </div>
    <div>
      
    </div>
    
  </div>
</template>


<script>
import api from '@/api'

export default {
  data () {
    return {
      token: null
    }
  },
  mounted () {
    api.get('/chats/csrf').then(res => {
      this.token = res.data.token
    })
  },
  methods: {
    post () {
      let request = new FormData()
      request.append('title', '테스트')
      request.append('content', '내용')

      // const formData = {
      //   title: '테스트',
      //   content: '내용'
      // }

      api.post(`/post/general/new`, request, {
        headers: { 
          'X-XSRF-TOKEN': this.token,
          'Content-Type': 'multipart/form-data'
        }
      }).then(res => {
        console.log(res.data)
        
      })
    },
    test () {
      // api.get('/users/me').then(res => {
      //   console.log(res.data)
      // })
      // api.get('/post/general/search?lastPostId=100&keyword=test').then(res => {
      //   console.log(res.data)
      // })
 
      let test = {
        campId:2,
        roomId:11,
        checkIn:"2024-02-05",
        checkOut:"2024-02-06",
        guest:{id:1,name:"customer",email:"a"},
        reservation:{numGuest:2,dailyPrice:[15000]}
      }
      api.get(`/customer/reservations`, test, {
        headers: { 
          'X-CSRF-TOKEN': this.token,
         
        }
      }).then(res => {
        console.log(res.data)
        
     })
    }
  }
}
</script>


<style scoped>
.search {
  width: 400px;
  margin: auto;
  margin-top: 50px;
}
.post {
  width: 80%;
  height: 1000px;
  margin: auto;
  margin-top: 50px;
  border-radius: 12px;
  border: 1px solid #dddddd;
}
</style>