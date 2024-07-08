<template>
  <div>
    <div class="sheet">
      <div class="mypage-title">회원정보</div>
      <div class="d-flex mt-3">
        <div>
          <img class="user-picture" :src="userInfo.picture">
        </div>
        <div class="user-info">
          <div>
            {{ userInfo.name }}
          </div>
          <div>
            {{ userInfo.account }}
          </div>
        </div>
      </div>
    </div>

    <div class="sheet">
      <div class="mypage-title">캠핑장 예약내역</div>
      <v-row class="mt-3 mx-1 mb-3" v-if="reservation != null && reservation.length != 0" >
        <v-col v-for="list in reservation" :key="list.index" cols="12" sm="4">
          <v-card class="reservation-card pa-3">
            <div class="mx-1">{{ list.camp.name }}</div> 
            <v-divider class="my-2"></v-divider>
            <div class="mx-1">
              <div class="reservation-info">
                <div class="reservation-info-title">객실</div> 
                <div class="reservation-info-content">{{ list.room.name }}</div>
              </div>
              <div class="reservation-info">
                <div class="reservation-info-title">체크인</div> 
                <div class="reservation-info-content">{{ list.reservation.stayStarts }}</div>
              </div>
              <div class="reservation-info">
                <div class="reservation-info-title">체크아웃</div> 
                <div class="reservation-info-content">{{ list.reservation.stayEnds }}</div>
              </div>
              <div class="reservation-info">
                <div class="reservation-info-title">결제금액</div> 
                <div class="reservation-info-content">{{ list.reservation.price }}</div>
              </div>
              <div class="reservation-info">
                <div class="reservation-info-title">상태</div> 
                <div class="reservation-info-content">{{ list.reservation.status }}</div>
              </div>
            </div>
            
          </v-card>
        </v-col>
        <v-col cols="12" sm="4">
          <!-- <div class="more-reservation">더보기</div>
          <v-btn></v-btn> -->
        </v-col>
      </v-row>
      <div v-else class="grey--text text--darken-1 ma-3">
        예약내역이 없습니다.
      </div>
      <!-- <div v-for="list in reservation" :key="list.index">
        <span>{{ list.camp.name }}</span> <span>{{ list.reservation.stayStarts}}</span>
      </div> -->
    </div>

    <div class="sheet">
      <div class="mypage-title">
        게시판 이용내역
      </div>
      <div class="d-flex mt-3">
        <div class="posts">
          <div class="mx-3 text-center grey lighten-3 rounded-xl" 
            style="height:35px;line-height:35px;">
            자유게시판
          </div>
          <div class="mx-2" v-if="generalPosts != null && generalPosts.length != 0" >
            <v-list v-for="list in generalPosts" :key="list.index">
              <v-list-item
                  @click="generalPostDetail(list.id)">
                <div class="d-flex">
                  <div class="img-wrapper mr-5">
                    <img v-if="list.image" :src="list.image">
                    <div v-else class="grey lighten-5"></div>
                  </div>
                  
                  <v-list-item-content>
                    <v-list-item-title>{{list.title}}</v-list-item-title>
                    <v-list-item-subtitle class="list-content">{{list.content}}</v-list-item-subtitle>
                    <v-list-item-subtitle class="caption">
                      {{list.date}} &#183; {{list.time.toString().slice(0,5)}}
                    </v-list-item-subtitle>
                  </v-list-item-content>
                </div>
              </v-list-item>
            </v-list>
          </div>
          <div v-else class="grey--text text--darken-1 ma-4">
            이용내역이 없습니다.
          </div>
        </div>
        <v-divider vertical></v-divider>
        <div class="posts">
          <div class="mx-3 text-center grey lighten-3 rounded-xl" 
            style="height:35px;line-height:35px;">
            모집게시판
          </div>
          <div class="mx-2" v-if="gatherPosts != null && gatherPosts.length != 0" >
            <v-list v-for="list in gatherPosts" :key="list.index">
              <v-list-item
                  @click="gatherPostDetail(list.id)">
                <div class="d-flex">
                  <div class="img-wrapper mr-5">
                    <img v-if="list.image" :src="list.image">
                    <div v-else class="grey lighten-5"></div>
                  </div>
                  
                  <v-list-item-content>
                    <v-list-item-title>{{list.title}}</v-list-item-title>
                    <v-list-item-subtitle class="list-content">{{list.content}}</v-list-item-subtitle>
                    <v-list-item-subtitle class="caption">
                      {{list.date}} &#183; {{list.time.toString().slice(0,5)}}
                    </v-list-item-subtitle>
                  </v-list-item-content>
                </div>
              </v-list-item>
            </v-list>
          </div>
          <div v-else class="grey--text text--darken-1 ma-4">
            이용내역이 없습니다.
          </div>
        </div>
      </div>
      
    </div>
  </div>
  
</template>


<script>
import api from '@/api'

export default {
  data() {
    return {
      token: null,
      userInfo: null,
      reservation: null,
      generalPosts: null,
      gatherPosts: null
    }
  },
  mounted() {
    api.get('/chats/csrf').then(res => {
      this.token = res.data.token
    })
    api.get('/login-member').then(res => {
      this.userInfo = res.data
      //console.log(this.userInfo)
    })
     api.get('/customer/reservations/my?size=5').then(res => {
      this.reservation = res.data.content
      //console.log(this.reservation)
    })
    api.get('/post/general/list/my?lastPostId=1000', {
      header: {
        'X-XSRF-TOKEN': this.token 
      }
    }).then(res => {
      this.generalPosts = res.data
      console.log(res.data)
    })
    api.get('/post/gather/list/my?lastPostId=1000', {
      header: {
        'X-XSRF-TOKEN': this.token 
      }
    }).then(res => {
      this.gatherPosts = res.data
      console.log(res.data)
    })
  },
  methods: {
    generalPostDetail (postId) {
      this.$router.push({ name: 'GeneralDetail', query: { 'postId': postId }})
    },
    gatherPostDetail (postId) {
      this.$router.push({ name: 'GatherDetail', query: { 'postId': postId }})
    }
  }
}
</script>


<style scoped>
.sheet {
  /* background-color: #fceae1; */
  width: 80%;
  margin: auto;
  margin-top: 30px;
  padding: 12px;
  border-radius: 12px;
  border: 1px solid #dddddd;

}
.mypage-title {
  font-size: 18px;
  margin-left: 7px;
  margin-top: 7px;
}
.user-picture {
  border-radius: 100%;
  margin-left: 30px;
}
.user-info {
  margin-left: 30px;
  margin-top: 30px;
}
.reservation-card {
  border-radius: 12px;
  cursor: pointer;
}
.reservation-info {
  display: flex;
}
.reservation-info-title {
  width: 40%;
  font-size: 14px;
  color: rgb(142, 142, 142);

}
.reservation-info-content{
  width: 60%;
  font-size: 14px;
  text-align: center;
}
.more-reservation {

}
.posts {
  width: 50%;
}
.img-wrapper {
  position: relative;
  width: 80px;
  height: 80px;
}
.img-wrapper img{
  /* position: absolute;
  top: 0;
  left: 0;
  transform: translate(50, 50); */
  width: 100%;
  height: 100%;
  object-fit: cover;
  margin: auto;
}
</style>