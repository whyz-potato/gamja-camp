<template>
  <div style="height:100%;">
    <CampList></CampList>
  </div>
</template>

<script>
import CampList from '@/components/home/CampList'
import api from '@/api'


export default {
  name: 'Home',
  components: {
    CampList
  },
  mounted() {
    api.get('/login-member').then(res => {
      console.log(res.data)
      this.$cookies.set('id', res.data.id)
    })
  },
  data () {
    return {

    }
  }
}
</script>
