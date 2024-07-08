<template>
  <div>
    <v-card flat>
      <v-card class="d-flex" flat>
        <v-card-title>게시글 등록</v-card-title>
        <!-- <v-btn @click="gatherRegister">등록</v-btn> -->
      </v-card>
      
      <v-divider></v-divider>
      
      <v-card class="pa-3" flat>
        <div class="d-flex">
          <v-text-field v-model="title" label="제목" solo></v-text-field>
          <v-autocomplete v-model="capacity" class="input-capacity ml-5" :items="items" solo label="모집인원수"></v-autocomplete>
        </div>
        <v-file-input class="mt-3" v-model="files" @change="selectImg"
          chips multiple required solo ></v-file-input>
        <div class="mx-10 mb-10">
          <v-img v-for="url in urls" :key="url.index" class="ml-10" :src="url" width="50%"/>
        </div>
        <v-textarea v-model="content" solo rows="40" auto-grow></v-textarea>
      </v-card>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn @click="gatherRegister" rounded text large>저장하기</v-btn>
      </v-card-actions>
    </v-card>
    
  </div>
</template>


<script>
import api from '@/api' 

export default {
  data () {
    return {
      token: null,
      title: '',
      content: '',
      files: [],
      urls: [],
      capacity: 3,
      items: [3, 4, 5, 6, 7, 8, 9, 10]
    }
  },
  mounted () {
    api.get('/chats/csrf').then(res => {
      this.token = res.data.token
    })

  },
  methods: {
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
    gatherRegister () {
      let frm = new FormData()

      const obj = { title: this.title, content: this.content }; 
      const json = JSON.stringify(obj); 
      const blob = new Blob([json], { type: 'application/json' });
      frm.append('request', blob)

      for (let i = 0; i < this.files.length; i++) {
				frm.append('images', this.files[i])
        console.log(this.files[i])
			}

      api.post(`/post/gather`, frm, { 
        headers: {
          'X-XSRF-TOKEN': this.token,
          'Content-Type': 'multipart/form-data',
          accept: 'application/json'
        }
      }).then(res => {
        console.log(res.data)
        console.log('게시글등록완')

        api.post('/chats/group', {postId: res.data.id, capacity: this.capacity}).then(res => {
          console.log(res.data)
        })
        
        this.$router.push({ name: 'GatherDetail', query: { 'postId': res.data.id }})
      })
    }
  }
}
</script>


<style scoped>
.input-capacity {
  width: 30px;
}
</style>