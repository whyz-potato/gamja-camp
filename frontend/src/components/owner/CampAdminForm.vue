<template>
  <div>
    <div class="register-btn" @click="campRegister">
      캠핑장 추가
    </div>
  </div>
</template>


<script>
import api from '@/api/index'

export default {
  data () {
    return {

    }
  },
  mounted () {
    api.get('/owners/my-camp').then(res => {
      console.log(res.data)
    })
  
  },
  methods: {
    campRegister () {
      this.$router.push({ name: 'CampRegister' })
    }
  }
}
</script>


<style scoped>
.register-btn {
  width: 90%;
  margin: auto;
  height: 250px;
  text-align: center;
  cursor: pointer;
}
</style>