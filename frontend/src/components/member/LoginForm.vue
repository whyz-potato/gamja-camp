<template>
  <div class="form">
    <!--
    <div class="header">로그인</div>
    -->
    <div class="tabs">
      <ul>
        <li class="tab">
          <input type="radio" id="tab1" name="tab"
            v-model="tabValue" value="개인" checked>
          <label for="tab1">개인회원</label>
        </li>
        <li class="tab">
          <input type="radio" id="tab2" name="tab"
            v-model="tabValue" value="기업">
          <label for="tab2">기업회원</label>
        </li>
      </ul>
    </div>

    <div class="line"></div>

    <div>
      <div class="button" @click="login('kakao')">
        <img class="logo" :src="require('@/assets/imgs/k-logo.png')"/>
        <a href="http://localhost:8080/oauth2/authorization/kakao" class="button-text">카카오 로그인</a>
      </div>
      <div class="button" @click="login('naver')">
        <img class="logo" :src="require('@/assets/imgs/n-logo.png')"/>
        <a href="http://localhost:8080/oauth2/authorization/naver" class="button-text">네이버 로그인</a>
      </div>
      <div class="button" @click="login('google')">
        <img class="logo" :src="require('@/assets/imgs/g-logo.png')"/>
        <a href="http://localhost:8080/oauth2/authorization/google" class="button-text">구글 로그인</a>
      </div>
    </div>
  </div>
</template>


<script>
import api from '@/api/index'

export default {
  data (){
    return {
      tabValue: '개인'
    }
  },
  methods: {
    login () {
      // console.log(this.tabValue)
      // console.log(social)
      const type = 'c'
      api.get(`/login`,{ params: { type } }).then(res => {
        //this.openPopup(res.data[0])
        api.get(`${res.data[2]}`)
      })
      //this.$router.push({ name: 'Join' })
      //const ret = window.open('http://localhost:8080/login?type=c', '_blank', 'width=300,height=500')
      // if (ret != null) {
      //   console.log(ret)
        
      //   window.open('http://localhost:8080/oauth2/authorization/naver', '_blank', 'width=300,height=500')
      //   //ret.close()
      // }
    },
    openPopup () {
      window.open('http://localhost:8080/oauth2/authorization/google', '_self', 'width=300,height=500')
    }
  }
}
</script>


<style scoped>
.form {
  width: 400px;
  margin: auto;
}
/* .header {
  font-size: 30px;
  font-weight: bold;
  text-align: center;
  margin-top: 10px;
  margin-bottom: 20px;
} */
.tabs {
  margin-top: 10px;
  margin-bottom: 50px;
}
ul {
  list-style: none;
  padding-left: 0;
}
ul li {
  display: inline-block;
  width: 50%; 
  text-align: center; 
  line-height: 50px;
}
label {
  display: block;
  width: 100%; 
  height: 50px;
  line-height: 50px;
  border-radius: 50px;
  cursor: pointer;
  color: gray;
  transition: 0.5s;
  letter-spacing: 3px;
  /* transition: 0.6s cubic-bezier(0.55, 0.055, 0.675, 0.19); */
}
label:hover {
  background:#f2f2f2;
}
input {
  display: none;
}
input:checked ~ label{
  background:#f2f2f2;
  color: black;
}
/* .line {
  border-top: 1px solid #dddddd;
  margin: auto;
  margin-bottom: 30px;
  width: 350px;
} */
.button {
  margin-bottom: 15px;
  height: 50px;
  border: 1px solid #dddddd;
  border-radius: 50px;
  cursor: pointer;
  align-items: center;
  display: flex;
}
.button:hover {
  background-color: #f2f2f2;
  transition: 0.6s;
}
.logo {
  height: 50%;
  margin-left: 40px;
}
.button-text {
  /* width: 200px; */
  margin: auto;
  text-align: center;
  letter-spacing: 3px;
}
a:link {
  color: black;
  text-decoration: none;
}
a:visited {
  color: black;
  text-decoration: none;
}
</style>