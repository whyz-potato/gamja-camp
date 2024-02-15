<template>
  <div class="pa-3">
    <v-card-title>{{ roomDetail.camp.name }} {{ '>' }}
       {{ roomDetail.room.name}}</v-card-title>

    <v-divider></v-divider>

    <!--
    <swiper class="swiper" :options="swiperOption">
      <swiper-slide v-for="img in roomDetail.room.images" :key="img.index">
        <img :src="require(`@/assets/test/${img}`)" style="width:100%;">
      </swiper-slide>
      <div class="swiper-pagination" slot="pagination"></div>
      <div class="swiper-button-prev" slot="button-prev"></div>
      <div class="swiper-button-next" slot="button-next"></div>
    </swiper>
    -->

    <v-card-title class="mt-3">객실 예약</v-card-title>

    <div class="ml-3">
      <v-chip class="mr-2" outlined>
        <v-icon class="material-icons mr-2">calendar_month</v-icon> 
        체크인 {{ roomDetail.checkIn }}
      </v-chip>
      <v-chip class="mr-2" outlined>
        <v-icon class="material-icons mr-2">calendar_month</v-icon> 
        체크아웃 {{ roomDetail.checkOut }}
      </v-chip>
      <v-chip class="mr-2" outlined>
        <v-icon class="material-icons mr-2">person</v-icon> 
        숙박인원 {{ 2 }} 명
      </v-chip>
    </div>
      
    <v-card class="card ml-3 mt-3 pa-3" width="500" flat>
      <div>요금 상세 내역</div>
      <v-divider></v-divider>
      <div class="d-flex">
        <div>
          기본요금 <br>
          {{ roomDetail.checkIn }}
        </div>
        <div class="price">
          {{ roomDetail.room.price.minOneNightPrice }}원
          <div v-for="price in roomDetail.room.price.dailyPrice" :key="price.index">
            {{ price }}원
          </div>
        </div>
        
        
      </div>
    </v-card>
   
    <div class="ml-3 mt-3">
      <v-chip class="mr-2" outlined>
        <v-icon class="material-icons mr-2">alarm</v-icon> 
        체크인 {{ roomDetail.camp.checkInTime }}
      </v-chip>
      <v-chip outlined>
        <v-icon class="material-icons mr-2">alarm</v-icon> 
        체크아웃 {{ roomDetail.camp.checkOutTime }}
      </v-chip>
    </div>

  </div>
</template>


<script>
import room_detail from '@/assets/test/room_detail'
import 'swiper/css/swiper.css'
// import { Swiper, SwiperSlide } from 'vue-awesome-swiper'

export default {
  components: {
    // Swiper,
    // SwiperSlide,
  },
  data () {
    return {
      roomDetail: {},
      swiperOption: {
        loop: true,
        slidesPerView: 1,
        spaceBetween: 30,
        pagination: {
          el: '.swiper-pagination',
          clickable: true
        },
        navigation: {
          nextEl: '.swiper-button-next',
          prevEl: '.swiper-button-prev',
        }
      },
      dates: []
    }
  },
  mounted () {
    this.roomDetail = room_detail
    const checkInDate = new Date(this.roomDetail.checkIn)
    const checkOutDate = new Date(this.roomDetail.checkOut)
    console.log((checkOutDate - checkInDate)/(1000 * 60 * 60 * 24))
    
    this.dates.push(checkInDate.toLocaleDateString())
    console.log(this.dates)
  },
  
}
</script>


<style scoped>
.swiper {
  /* width: 230px;
  height: 200px; */
  width: 90%;
  margin-top: 50px;
}
.swiper-container {
  --swiper-theme-color: #e4dabc;
  --swiper-pagination-color: #e4dabc;
}
/* .calendar {
  border-radius: 12px;
  border: 1px solid #dddddd;
} */
.card {
  border-radius: 12px;
  border: 1px solid #dddddd;
}
.price {
  margin-right: 0;
}
</style>