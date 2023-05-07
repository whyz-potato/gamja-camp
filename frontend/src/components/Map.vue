<template>
  <div class="sheet">
    <div class="camps">
      <div class="search">
          <input type="text" placeholder="지역 또는 캠핑장">

        <div>
          <v-menu ref="menu" v-model="menu" offset-y :close-on-content-click="false">
            <template v-slot:activator="{ on, attrs }">
              <input class="search_date" v-model="dateRangeText"
                v-on="on" v-bind="attrs" placeholder="기간"/>
            </template>
            <v-date-picker v-model="dates" no-title range
              color="grey" :first-day-of-week="1" :min="today" :day-format="getDay">
              <v-spacer></v-spacer>
              <v-btn text @click="menu = false">취소</v-btn>
              <v-btn text @click="$refs.menu.save(dates)">확인</v-btn>
            </v-date-picker>
          </v-menu>

          <div class="search_guests">
            <!--
            <div class="arrow_icon">
              <img :src="require(`@/assets/imgs/arrow_icon.png`)">
            </div>
            -->
            <select name="guests" class="select_box">
              <option disabled selected class="select_option">인원</option>
              <option v-for="index in 10" :key="index"
                class="select_option">{{ index }}</option>
            </select>
          </div>
        </div>
          
      </div>
      
      <div class="camps_count">검색결과 {{ this.count }} 건</div>

      <div v-for="(list, index) in campList" :key="index" class="camp_list" 
        @mouseenter="mouseoverToList(index)" @mouseleave="mouseoutToList(index)">
        <div style="display:flex;">
          <div v-for="img in list.images" :key="img.index" class="camp_img">
            <img :src="require(`@/assets/test/${img}`)">
          </div>
        </div>

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
    </div>
    
    <div id="map" class="map"></div>
  </div>
</template>

<script>
import camp_list from '@/assets/test/camp_list'

export default {
  data () {
    return {
      map: null,
      neLat: null,
      neLng: null,
      swLat: null,
      swLng: null,
      count: null,
      campList: null,
      markers: [],
      dates: null,
      menu: false
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
    },
    getDay (date) {
      const daysOfWeek = ['일', '월', '화', '수', '목', '금', '토']
      let i = new Date(date).getDay(date)
      return daysOfWeek[i]
    }
  },
  mounted () {
    this.count = camp_list.count
    this.campList = camp_list.camps
    //console.log(this.campList)

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

      // console.log(this.neLat,this.neLng)
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
  },
  methods: {
    mouseoverToList (i) {
      const content = this.markers[i].getIcon().content.replace('marker', 'marker-mouseover')
      this.markers[i].setIcon({ content: content })
    },
    mouseoutToList (i) {
      const content = this.markers[i].getIcon().content.replace('marker-mouseover', 'marker')
      this.markers[i].setIcon({ content: content })
    }
  }
}
</script>


<style scoped>
.sheet {
  display: flex;
  height:100%; 
  background-color: #f7f7f7;
}
.camps {
  width:25%; 
  height:100%; 
}
.map {
  width:75%; 
  height:100%;
}
.search {
  width: 95%;
  margin: auto;
  margin-top: 10px;
}
input {
  width: 100%;
  height: 40px;
  border: 1px solid #dddddd;
  border-radius: 50px;
  cursor: pointer;
  align-items: center;
  display: flex;
  background-color: white;
  outline-color: #dddddd;
  padding-left: 10px;
  text-align: center;
  margin-bottom: 5px;
}
input[type=text] {
  background-image: url('@/assets/imgs/search_icon.png');
  background-repeat: no-repeat;
  background-size: 18px;
  background-position: 13px center;
}
.search>div {
  display: flex;
}
.search_date {
  width: 70%;
  background-image: url('@/assets/imgs/date_icon.png');
  background-repeat: no-repeat;
  background-size: 18px;
  background-position: 13px center;
  margin-right: 5px;
}
.search_guests {
  width: 30%
}
.select_box {
  width: 100%;
  height: 40px;
  border: 1px solid #dddddd;
  border-radius: 50px;
  cursor: pointer;
  align-items: center;
  background-color: white;
  outline-color: #dddddd;
  text-align: center;
  color: #5e5e5e;
}
.select_option {
  position: absolute; 
  top: 28px;
  left: 0;
  width: 100%;
  background: white;
  color: #5e5e5e;
  list-style-type: none;
  padding: 0;
  overflow: hidden;
  max-height: 0;
  transition: .3s ease-in;
}
.arrow_icon {
  position: absolute;
  height: 40px;
  align-items: center;
  display: flex;

}
.arrow_icon>img {
  width: 15px;
  opacity: 0.7;
  transition: .3s;
}
.select_box:focus + .arrow_icon>img {
  transform: rotate(180deg);
}
.camps_count {
  width: 95%;
  text-align: right;
  margin-top: 5px;
  margin-bottom: 5px;
  color: #5e5e5e;
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
}
</style>