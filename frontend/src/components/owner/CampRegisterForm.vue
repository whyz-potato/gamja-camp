<template>
  <div>
    캠핑장등록
    <v-btn @click="register">캠핑장등록</v-btn>
    <v-file-input class="mt-3" v-model="files" @change="selectImg"
      chips multiple required solo ></v-file-input>
    <v-btn @click="roomRegister">객실등록</v-btn>
    <v-btn @click="campModify">캠핑장수정</v-btn>
    <v-btn @click="checkCampList">캠핑장목록</v-btn>
  </div>
</template>


<script>
import api from '@/api/index'

export default {
  data () {
    return {
      files: [],
      token: null
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
    register () {
      let frm = new FormData()

      const obj = { name: '테스트캠핑장1', address: '서울시 광진구 동일로218', phone: '010-0000-0000',
        checkInTime: '15:00', checkOutTime: '11:00', introduction: 'test'}
      const json = JSON.stringify(obj)
      const blob = new Blob([json], { type: 'application/json' })
      frm.append('request', blob)

      for (let i = 0; i < this.files.length; i++) {
				frm.append('images', this.files[i])
        console.log(this.files[i])
			}

      api.post(`/camps/new/4`, frm, { 
        headers: {
          'Content-Type': 'multipart/form-data',
          accept: 'application/json'
        }
      }).then(res => {
        console.log(res.data)
      })
    },
    roomRegister () {
      api.post(`/owner/rooms/14`, {name: '방1', cnt: 3, capacity: 2, weekPrice: 15000,weekendPrice:20000}).then(res => {
        console.log('등록완')
        console.log(res.data)

      })
    },
    campModify () {
      //let frm = new FormData()

      const obj = { name: '테스트변경', address: '서울시 광진구 동일로218', phone: '010-1234-0000',
        checkInTime: '15:00', checkOutTime: '11:00', introduction: 'test'}
      // const json = JSON.stringify(obj)
      // const blob = new Blob([json], { type: 'application/json' })
      // frm.append('request', blob)

      api.put(`/camps/update/100/14`, obj).then(res => {
        console.log(res.data)
      })
    },
    checkCampList () {
      api.get(`/owner/camps`,{ 
        headers: {
          'X-XSRF-TOKEN': this.token
        }
      }).then(res => {

        console.log(res.data)

      })
    }
  }
}
</script>