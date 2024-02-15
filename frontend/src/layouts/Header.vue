<template>
  <div>
    <div class="header">
      <router-link :to="{ name: 'Home' }" class="home">
        <!--감자 사진 임시로...-->
        <img :src="require('@/assets/imgs/gamjacamp_logo.png')" width="250px" />
      </router-link>
      <div>
        <router-link :to="{ name: 'Home' }">전국캠핑장</router-link>
        <router-link :to="{ name: 'Community' }">커뮤니티</router-link>
      </div>
      <div class="login">
        <router-link :to="{ name: 'Login' }" v-if="!isLogin">로그인</router-link>
        <router-link :to="{ name: 'Test' }" v-else>마이페이지</router-link>
        <a href="http://localhost:8080/logout" v-if="isLogin" @click="logout">로그아웃</a>
        <!-- <div class="icon">
          <v-icon>mdi-menu</v-icon>
          <img class="image" :src="require(`@/assets/imgs/gamja.png`)" >
        </div> -->
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
export default {
  data () {
    return {
      
    }
  },
  computed: {
    ...mapState(['isLogin'])
  },
  mounted () {
    if (this.$cookies.isKey('id')) {
      this.$store.state.isLogin = true
    }
  },
  methods: {
    goHome () {
      this.$router.push('/')
    },
    logout () {
      this.$cookies.remove('id')
      this.$store.state.isLogin = false
    }
  }
}
</script>


<style scoped>
.header {
  height: 70px;
  display: flex;
  align-items: center;
  background-color: white;
  letter-spacing: 3px;
  border-bottom: 1px solid #dddddd; 
}
a:link {
  text-decoration: none;
  margin-right: 20px;
  color: black;
}
a:visited {
  color: black;
}
/* a:hover {
  font-size: 17px;
  transition: 0.4s;
} */
a:hover {
  color: #999999;
  transition: 0.2s;
}
.home {
  font-size: 30px;
  display: flex;
  cursor: pointer;
  margin-left: 40px;
  margin-right: 30px;
  font-weight: bolder;
}
.login {
  margin-left: auto;
  margin-right: 40px;
  display: flex;
}
/* .login > .icon {
  display: flex;
  width: 80px;
  height: 40px;
  border-radius: 50px;
  border: 1px solid #9c9c9c;
  padding: 5px;

}
.image {
  width: 30px;
  height: 30px;
  border-radius: 50px;

} */
</style>