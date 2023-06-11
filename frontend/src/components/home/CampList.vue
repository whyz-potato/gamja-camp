<template>
  <div class="sheet">
    <div class="camps">
      <!--
      <div class="search">

        <div class="d-flex">
          <v-combobox class="mr-2" v-model="si" dense outlined rounded placeholder="시/도"
            :items="siList" item-text="CTP_KOR_NM" return-object></v-combobox>
          <v-combobox class="mr-2" v-model="gu" dense outlined rounded placeholder="구/군"></v-combobox>
          <v-text-field v-model="inputSearch" dense outlined rounded placeholder="캠핑장"></v-text-field>
        </div>
          
        <div class="d-flex mt-n4">
          <v-menu ref="menu" v-model="menu" offset-y :close-on-content-click="false">
            <template v-slot:activator="{ on, attrs }">
              <v-text-field class="mr-2" v-model="dateRangeText" v-on="on" v-bind="attrs" 
                dense outlined rounded placeholder="기간">
              </v-text-field>
            </template>
            <v-date-picker v-model="dates" no-title range
              color="secondary" :first-day-of-week="1" :min="today" :day-format="getDay">
              <v-spacer></v-spacer>
              <v-btn class="mt-n10" text rounded
                @click="menu = false">취소</v-btn>
              <v-btn class="mt-n10" outlined rounded
                @click="$refs.menu.save(dates)">확인</v-btn>
            </v-date-picker>
          </v-menu>

          <v-combobox dense outlined rounded placeholder="인원수"></v-combobox>
        </div> 
      </div>
      -->

      <div class="camps_count">검색결과 {{ this.count }} 건</div>

      <div class="pa-3">
        <v-row>
          <v-col v-for="(list, index) in campList" :key="index" cols="12" sm="4">

            <v-hover v-slot="{ hover }">
              <v-card class="card" :elevation="hover ? 16 : 2"
                @mouseenter="mouseoverToList(index)" @mouseleave="mouseoutFromList(index)"
                @click="viewCamp(list.id)">

                <swiper class="swiper" :options="swiperOption">
                  <swiper-slide v-for="img in list.images" :key="img.index">
                    <img :src="require(`@/assets/test/${img}`)" style="width:100%;">
                  </swiper-slide>
                  <div class="swiper-pagination" slot="pagination"></div>
                  <div class="swiper-button-prev" slot="button-prev"></div>
                  <div class="swiper-button-next" slot="button-next"></div>
                </swiper>

                <!--
                <div style="display:flex;">
                  <div v-for="img in list.images" :key="img.index" class="camp_img">
                    <img :src="require(`@/assets/test/${img}`)">
                  </div>
                </div>
                -->
                
                <div class="pa-2">
                  <div class="camp_info">
                    <div class="camp_name">{{ list.name }}</div>
                    <div class="camp_rate">
                      <img :src="require('@/assets/imgs/star_icon.png')">
                      <div>{{ list.rate }}</div>
                    </div>       
                  </div>

                  <div class="camp_price">
                    <span>1박 / </span>
                    <span class="price">{{ list.OneNightPrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') }}</span>
                    <span> 원~</span>
                  </div>
                </div>
                
              </v-card>
            </v-hover>
          </v-col>
        </v-row>
      </div>  
    </div>
    
    
    <div id="map" class="map">
      <Search style="position: fixed; z-index: 1;"></Search>
      <div v-if="showCampInfo" style="width:200px;height:200px;">
        {{campInfo.name}}
      </div>
    </div>

    <v-dialog v-model="dialog" width="800">
      <camp-detail :campDetail="campDetail" :dialog="dialog"></camp-detail>
    </v-dialog>
  </div>
</template>

<script>
import camp_list from '@/assets/test/camp_list'
import si_list from '@/assets/data/si_list'
import gu_list from '@/assets/data/gu_list'
import Search from '@/components/home/Search'
import 'swiper/css/swiper.css'
import { Swiper, SwiperSlide } from 'vue-awesome-swiper'
import CampDetail from '@/components/home/CampDetail'

export default {
  components: {
    Search,
    Swiper,
    SwiperSlide,
    CampDetail
  },
  data () {
    return {
      map: null,
      neLat: null,
      neLng: null,
      swLat: null,
      swLng: null,
      count: null,
      campList: null,
      campInfo: null,
      showCampInfo: false,
      markers: [],
      dates: null,
      menu: false,
      siList: null,
      guList: null,
      si: null,
      gu: null,
      inputSearch: null,
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
      dialog: false,
      campDetail: null
    }
  },
  computed: {
    dateRangeText () {
      if(this.dates) {
        return this.dates.join(' ~ ')
      } return ''
    },
    today () {
      const today = new Date()
      const year = today.getFullYear()
      const month = ('0' + (today.getMonth() + 1)).slice(-2)
      const day = ('0' + today.getDate()).slice(-2)

      return year + '-' + month + '-' + day
    }
  },
  mounted () {
    this.count = camp_list.count
    this.campList = camp_list.camps
    this.siList = si_list
    this.guList = gu_list

    this.map = new window.naver.maps.Map('map',{
      zoom: 15
    })

    navigator.geolocation.getCurrentPosition((e) => {
      const lat = e.coords.latitude
      const lng = e.coords.longitude

      const center = new window.naver.maps.LatLng(lat, lng)
      this.map.setCenter(center)
    })

    new window.naver.maps.Event.addListener(this.map, 'idle', () => {
      const northEast = this.map.getBounds().getNE()
      const southWest = this.map.getBounds().getSW()
      
      this.neLat = northEast.lat()
      this.neLng = northEast.lng()
      this.swLat = southWest.lat()
      this.swLng = southWest.lng()

      //console.log(this.neLat,this.neLng)
    })

    for (let i = 0; i < this.campList.length; i++) {
      const coord = new window.naver.maps.LatLng(this.campList[i].lat, this.campList[i].lng)
      const price = this.campList[i].OneNightPrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
      const name = this.campList[i].name

      const marker = new window.naver.maps.Marker({
        map: this.map,
        position: coord,
        icon: { content: `<div class="marker">${price}</div>` },
        title: name
      })

      this.markers.push(marker)
    }
    this.markerEvent()
  },
  methods: {
    mouseoverToList (i) {
      const content = this.markers[i].getIcon().content.replace('marker', 'marker-mouseover')
      this.markers[i].setIcon({ content: content })
    },
    mouseoutFromList (i) {
      const content = this.markers[i].getIcon().content.replace('marker-mouseover', 'marker')
      this.markers[i].setIcon({ content: content })
    },
    markerEvent () {
      for (let i = 0; i < this.markers.length; i++) {
        window.naver.maps.Event.addListener(this.markers[i], 'click', () => {
          this.openCampInfo(i)
        })
      }
    },
    openCampInfo (i) {
      this.campInfo = this.campList[i]
      console.log(this.campInfo)
      this.showCampInfo = true
    },
    viewCamp (id) {
      // api.get(`/camps/${id}`).then(res => {
      //   this.campDetail = res.data
      // })
      this.campDetail = id
      this.dialog = true
    }
  }
}
</script>


<style scoped>
.sheet {
  display: flex;
  height: 100%; 
  background-color: white;
}
.camps {
  width: 50%; 
  height: 100%; 
}
.map {
  width: 50%; 
  height: 100%;
}
.search {
  width: 95%;
  margin: auto;
  margin-top: 10px;
}
.camps_count {
  width: 95%;
  text-align: right;
  margin-top: 20px;
  margin-bottom: 5px;
  color: #5e5e5e;
  font-size: 18px;
}
.card {
  cursor: pointer;
  border-radius: 12px;
}
.swiper {
  /* width: 230px;
  height: 200px; */
  width: 100%;
}
.swiper-container {
  --swiper-theme-color: #e4dabc;
  --swiper-pagination-color: #e4dabc;
}
.camp_list {
  width: 95%;
  margin: auto;
  margin-bottom: 5px;
  background-color: white;
  padding: 10px;
  border: 1px solid #dddddd;
  border-radius: 12px;
  cursor: pointer;
  transition: box-shadow .3s;
}
.camp_list:hover {
  border: 2px solid #dddddd;
  box-shadow: 0 14px 28px #dddddd, 0 10px 10px #dddddd;
}
.camp_img {
  width: 70px;
  margin-right: 5px;
}
.camp_img>img {
  width: 100%;
  object-fit: cover;
}
.camp_info {
  display: flex;
  margin-top: 5px;
}
.camp_name {
  font-size: 22px;
  font-weight: bold;
  color: #5e5e5e;
}
.camp_rate {
  display: flex;
  align-items: center;
  margin-left: 20px;
}
.camp_rate>img {
  width: 15px;
  height: 15px;
}
.camp_rate>div {
  color: #5e5e5e;
  margin-left: 3px;
}
.camp_price {
  color: #5e5e5e;
}
.camp_price .price {
  font-size: 18px;
  font-weight: bold;
}
</style>

<style>
.marker {
  color: #5e5e5e;
  white-space: nowrap;
  position: relative;
  background-color: white;
  line-height: 30px;
  text-align: center;
  font-weight: bold;
  border-radius: 15px;
  transition: 0.5s;
  padding: 0 8px;
  box-shadow: #dddddd 0px 0px 0px 1px,
    #dddddd 0px 1px 2px;
  overflow-y: auto;
  transform: translate(-40%, -60%);
}
.marker-mouseover {
  white-space: nowrap;
  position: relative;
  background-color: #FFBB98;
  line-height: 30px;
  text-align: center;
  font-weight: bold;
  border-radius: 15px;
  transition: 0.5s;
  padding: 0 8px;
  box-shadow: #dddddd 0px 0px 0px 1px,
    #dddddd 0px 1px 2px;
  overflow-y: auto;
  transform: translate(-40%, -60%);
  z-index: 2;
}
.marker:hover {
  white-space: nowrap;
  position: relative;
  background-color: #FFBB98;
  line-height: 30px;
  text-align: center;
  font-weight: bold;
  border-radius: 15px;
  transition: 0.5s;
  padding: 0 8px;
  box-shadow: #dddddd 0px 0px 0px 1px,
    #dddddd 0px 1px 2px;
  overflow-y: auto;
  transform: translate(-40%, -60%);
  z-index: 2;
}
</style>