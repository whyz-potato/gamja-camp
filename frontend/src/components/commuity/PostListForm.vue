<template>
  <div>
    <div class="post">
      <div class="search">
        <v-text-field placeholder="검색" filled rounded color="black" full-width></v-text-field>
        <v-btn @click="search">검색</v-btn>
      </div>
      <div>
        <v-btn @click="post">등록</v-btn>
        <v-btn @click="postUpdate">수정</v-btn>
        <v-btn @click="postDelete">삭제</v-btn>
        <v-btn @click="getPostList">조회</v-btn>
        <v-btn @click="test">예약</v-btn>
      </div>
      <div>
        <v-list>
          <v-list-item-group>
            <v-list-item v-for="list in postList" :key="list.index"
               @click="postDetail(list.id)">
              <div class="d-flex">
                <v-img :src="list.image" width="30"></v-img>
                <v-list-item-content>
                  <v-list-item-title>{{list.title}}</v-list-item-title>
                  <v-list-item-subtitle>{{list.content}}</v-list-item-subtitle>
                </v-list-item-content>
              </div>
              
            </v-list-item>
          </v-list-item-group>
          
          
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
    }
  },
  mounted () {
    api.get('/chats/csrf').then(res => {
      this.token = res.data.token
    })
    api.get(`/post/general/list?lastPostId=30`).then(res => {
      this.postList = res.data
    })

  },
  methods: {
    search () {
      api.get('/post/general/search?lastPostId=100&keyword=test').then(res => {
      console.log(res.data)
    })
    },
    selectImg () {
			try {
				this.urls = []

				for (let i = 0; i < this.files.length; i++) {
					this.urls.push(URL.createObjectURL(this.files[i]))
				}

			}catch(e) {
				this.urls = []
			}
		},
    // post () {
      
    //   let frm = new FormData()

    //   const obj = { title: "test title", content: "test content" }; 
    //   const json = JSON.stringify(obj); 
    //   const blob = new Blob([json], { type: 'application/json' });
    //   frm.append('request', blob)

    //   for (let i = 0; i < this.files.length; i++) {
		// 		frm.append('images', this.files[i])
    //     console.log(this.files[i])
		// 	}


    //   api.post(`/post/general/new`, frm, { 
    //     headers: {
    //       'X-XSRF-TOKEN': this.token,
    //       'Content-Type': 'multipart/form-data',
    //       accept: 'application/json'
    //     }
    //   }).then(res => {
    //     console.log(res.data)
    //   })
    // },
    post () {
      this.$router.push({ name: 'PostRegister' })
    },
    postUpdate () {
      let frm = new FormData()

      const obj = { title: "update title", content: "update content" }; 
      const json = JSON.stringify(obj); 
      const blob = new Blob([json], { type: 'application/json' });
      frm.append('request', blob)

      api.put(`/post/general/update/2`, frm, { 
        headers: {
          'Content-Type': 'multipart/form-data',
          accept: 'application/json'
        }
      }).then(res => {
        console.log(res.data)
      })
    },
    postDelete () {
      api.delete(`/post/general/delete/2`).then(() => {
        console.log('삭제 완료')
      })
    }, 
    getPostList () {
      api.get(`/post/general/list?lastPostId=20`).then(res => {
        console.log(res.data)
      })
    },
    postDetail (postId) {
      // api.get(`/post/general/${postId}`).then(res => {
      //   console.log(res.data)
      // })
      this.$router.push({ name: 'PostDetail', query: { 'postId': postId }})
    },
    test () {
      let test = {
        campId:2,
        roomId:11,
        checkIn:"2024-02-05",
        checkOut:"2024-02-06",
        guest:{id:1,name:"customer",email:"a"},
        reservation:{numGuest:2,dailyPrice:[15000]}
      }
      api.post(`/customer/reservations`, test, {
        headers: { 
          'X-XSRF-TOKEN': this.token,
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
  width: 600px;
  margin: auto;
  margin-top: 50px;
  display: flex;
}
.post {
  height: 1000px;
  margin: auto;
  margin-top: 50px;

}
</style>