<template>
  <v-card class="card pa-3">
    <!--
    <v-toolbar flat>
      <v-toolbar-title>{{ campDetail.camp.name }}</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn v-if="showRoomDetail" @click="showRoomDetail = false" icon>
        <v-icon class="material-icons ml-2">arrow_back_ios</v-icon>
      </v-btn>
    </v-toolbar>
    -->
    <v-card-title>{{ campDetail.name }}
      <!--
      <v-btn class="ml-auto" fab text>
        <v-icon class="material-icons ml-2">arrow_back_ios</v-icon>
      </v-btn>
      -->
    </v-card-title>

    <v-divider></v-divider>

    <v-hover v-slot="{ hover }" v-if="campDetail.images">
      <v-card class="d-flex pa-2" flat>
        <div class="ma-1 mb-5" style="width:50%;">
          <img :src="require(`@/assets/test/${campDetail.images[0]}`)" 
            class="img_radius1" style="width:100%; height: 100%;">
        </div>
        <div class="ma-1" style="width:25%;">
          <img :src="require(`@/assets/test/${campDetail.images[1]}`)"
            style="width:100%;">
          <img :src="require(`@/assets/test/${campDetail.images[2]}`)" 
            style="width:100%;">
        </div>
        <div class="ma-1" style="width:25%;">
          <img :src="require(`@/assets/test/${campDetail.images[3]}`)"
            class="img_radius2" style="width:100%;">
          <img :src="require(`@/assets/test/${campDetail.images[4]}`)"
            class="img_radius3" style="width:100%;">
        </div>

        <button v-if="hover">사진 더보기</button>
      </v-card>
    </v-hover>

    <v-card v-else>
      이미지 없음
    </v-card>
    

    <v-card-title class="mt-3">캠핑장 정보</v-card-title>
    <div class="ml-6"> {{ campDetail.introduction }} </div>
    <div class="ml-6 mt-3">
      <v-chip class="mr-2" outlined>
        <v-icon class="material-icons mr-2">alarm</v-icon> 
        체크인 {{ campDetail.checkInTime }}
      </v-chip>
      <v-chip outlined>
        <v-icon class="material-icons mr-2">alarm</v-icon> 
        체크아웃 {{ campDetail.checkOutTime }}
      </v-chip>
    </div>

    <v-card-title class="mt-7">캠핑장 객실</v-card-title>
    <div class="d-flex ml-6" >
      <div v-for="room in campRooms" :key="room.index">
        <v-hover  v-slot="{ hover }">
          <v-card :elevation="hover ? 16 : 2" @click="roomDetail(room.id)"
            class="card mr-5 d-flex" width="220">
            <img v-if="room.images" :src="require(`@/assets/test/${room.images}`)"
              class="room_image" style="width:100px;">
            <img v-else :src="require(`@/assets/test/test1.jpg`)"
              class="room_image" style="width:100px;">
            <div>
              <v-card-subtitle>{{ room.name }}</v-card-subtitle>
              <p class="subtitle-2 text-right">{{ room.price.totalPrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') }} / 2박</p>
            </div>
          </v-card>
        </v-hover>
      </div>
    </div>


    <v-card-title class="mt-7">캠핑장 위치</v-card-title>
    <div class="d-flex ml-6">
      <div id="detail_map" class="detail_map" style="height: 300px; width: 400px"></div>
      <div class="ml-5">
        주소 <br>
        {{ campDetail.address }} <br><br>
        전화번호 <br>
        {{ campDetail.contact }}
      </div>
    </div>
  </v-card>
</template>


<script>
// import 'swiper/css/swiper.css'
// import { Swiper, SwiperSlide } from 'vue-awesome-swiper'
//import camp_detail from '@/assets/test/camp_detail'
//import camp_rooms from '@/assets/test/camp_rooms'
import api from '@/api'

export default {
  components: {
    // Swiper,
    // SwiperSlide,
  },
  props: {
    campDetail: {

    },
    // dialog: Boolean
  },
  data () {
    return {
      //campDetail: {},
      map: null,
      campRooms: [],
    }
  },
  watch: {
    campDetail () {
      console.log(this.campDetail.id)
      api.get(`/rooms?camp=${this.campDetail.id}`).then(res => {
        console.log(res.data)
        this.campRooms = res.data.rooms
      })
    }
  },
  mounted () {
    //this.campDetail = camp_detail
    //this.campRooms = camp_rooms
    setTimeout(() => 
      this.initMap(), 500)
    //console.log(this.campDetail)
    
  },
  methods: {
    initMap () {
      const coord = new window.naver.maps.LatLng(this.campDetail.latitude, this.campDetail.longitude)
      this.map = new window.naver.maps.Map('detail_map',{
        center: coord,
        zoom: 13
      })

      new window.naver.maps.Marker({
        map: this.map,
        position: coord
      })
    },
    roomDetail (roomId) {
      console.log(roomId)
      const newWindow = this.$router.resolve({ name: 'RoomDetail', query: { 'id': roomId }})
      window.open(newWindow.href, '_blank')
    }
  }
}
</script>

<style scoped>
.card {
  border-radius: 12px;
}
.image {
  border-radius: 12px;
  margin-top: 5px;
  width: 100%;
}
.btn {
  height: 40px;
  line-height: 40px;
  border-radius: 50px;
  background: #e4dabc;
}
.room_image {
  border-top-right-radius: 0 !important;
  border-bottom-left-radius: 12px;
}
.detail_map {
  border-radius: 12px;
}
.img_radius1 {
  border-top-left-radius: 12px;
  border-bottom-left-radius: 12px;
}
.img_radius2 {
  border-top-right-radius: 12px;
}
.img_radius3 {
  border-bottom-right-radius: 12px;
}
button {
  position: absolute; 
  z-index: 1;
  /* left: 80%;
  transform: translate(-80%, 0%); */
  
}
</style>