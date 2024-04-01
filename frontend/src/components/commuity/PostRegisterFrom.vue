<template>
  <div>
    <v-card flat>
      <v-card class="d-flex" flat>
        <v-card-title>게시글 등록</v-card-title>
        <v-btn @click="postRegister">등록</v-btn>
      </v-card>
      
      <v-divider></v-divider>
      
      <v-card class="pa-3" flat>
        <v-text-field v-model="title" label="제목" solo></v-text-field>
        <v-file-input class="mt-3" v-model="files" @change="selectImg"
          chips multiple required solo ></v-file-input>
        <v-card class="mx-10" flat>
          <v-img v-for="url in urls" :key="url.index" class="ml-10" :src="url" width="50%"/>
        </v-card>
        <v-textarea v-model="content" solo height="500"></v-textarea>
      </v-card>
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
      urls: []
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
    postRegister () {
      let frm = new FormData()

      const obj = { title: this.title, content: this.content }; 
      const json = JSON.stringify(obj); 
      const blob = new Blob([json], { type: 'application/json' });
      frm.append('request', blob)

      for (let i = 0; i < this.files.length; i++) {
				frm.append('images', this.files[i])
        console.log(this.files[i])
			}

      api.post(`/post/general/new`, frm, { 
        headers: {
          'X-XSRF-TOKEN': this.token,
          'Content-Type': 'multipart/form-data',
          accept: 'application/json'
        }
      }).then(() => {
        console.log('게시글등록완')
        this.$router.push({ name: 'Community' })
      })
    }
  }
}
</script>