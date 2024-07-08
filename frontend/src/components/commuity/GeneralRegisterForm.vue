<template>
  <div>
    <v-card flat>
      <v-card class="border d-flex" flat>
        <v-card-title class="border flex-fill">게시글 등록</v-card-title>
        <!-- <div class="border">
          <v-btn class="mt-4" @click="postRegister" rounded text color="grey">등록</v-btn>
        </div> -->
      </v-card>
      
      <v-divider></v-divider>
      
      <v-card class="pa-3" flat>
        <v-text-field v-model="title" label="제목" solo></v-text-field>
        <v-file-input class="mt-3" v-model="files" @change="selectImg"
          chips multiple required solo ></v-file-input>
        <div class="mx-10 mb-10">
          <v-img v-for="url in urls" :key="url.index" class="ml-10" :src="url" width="50%"/>
        </div>
        <v-textarea v-model="content" solo rows="40" auto-grow></v-textarea>
        <!-- <ckeditor :editor="editor" v-model="editorData" :config="editorConfig"></ckeditor> -->
      </v-card>

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn @click="postRegister" rounded text large>저장하기</v-btn>
      </v-card-actions>
    </v-card>
    
  </div>
</template>


<script>
import api from '@/api' 
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';

export default {
  data () {
    return {
      token: null,
      title: '',
      content: '',
      files: [],
      urls: [],
      editor: ClassicEditor,
      editorData: 'ttt',
      editorConfig: {
        toolbar: ['heading', '|','bold', 'italic', 'bulletedList', 'numberedList', '|', 
          'link', 'insertTable', '|', 'imageUpload', '|', 'undo', 'redo'],
        //language: 'Ko'
      }
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
      }).then(res => {
        console.log(res.data)
        console.log('게시글등록완')
        this.$router.push({ name: 'GeneralDetail', query: { 'postId': res.data.id }})
      })
    }
  }
}
</script>