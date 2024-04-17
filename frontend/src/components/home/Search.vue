<template>
  <div>
    <v-menu v-model="menu" :close-on-content-click="false" 
      transition="scale-transition" bottom rounded="lg" max-width="700">
      <template v-slot:activator="{ on, attrs }">
        <div v-bind="attrs" v-on="on" class="search-bar">
          <div>지역</div>
          <v-divider vertical></v-divider>
          <div>날짜</div>
          <v-divider vertical></v-divider>
          <div>인원</div>
          <!-- <v-divider vertical></v-divider>
          <div>검색어</div> -->
          <button class="search-btn">
            <v-icon class="material-icons">search</v-icon>
          </button>
        </div>
      </template>

      <v-card class="card pa-2" width="400">
        <v-card class="card">
          <div class="pa-2">
            <div class="pa-2">지역</div>
            <v-divider></v-divider>
            <div class="d-flex mb-n8">
              <div class="pa-2">
                <div class="mb-1">시/도</div>
                <v-autocomplete :items="siList" item-text="CTP_KOR_NM" v-model="si" return-object
                  @change="selectSi" solo>
                </v-autocomplete>
              </div>
              <div class="pa-2">
                <div class="mb-1">구/군</div>
                <v-autocomplete :items="guList" item-text="SIG_KOR_NM" v-model="gu" return-object
                  @change="selectGu" solo hide-no-data>
                </v-autocomplete>
              </div>
            </div>
          </div>
        </v-card>

        <v-card class="card mt-2">
          <div class="pa-2">
            <div class="pa-2">기간</div>
            <v-divider></v-divider>
            <div class="d-flex mb-n8">
              <div class="pa-2">
                <div class="mb-1">체크인</div>
                <v-menu ref="menu" v-model="checkInMenu">
                  <template v-slot:activator="{ on }">
                    <v-text-field v-model="checkIn" readonly v-on="on" solo></v-text-field>
                  </template>
                  <v-date-picker v-model="checkIn" no-title color="primary"
                    :first-day-of-week="1" :min="today"
                    :weekday-format="getDay"
                    :month-format="getMonth"
                    :title-date-format="getMonth"
                    :header-date-format="getHeaderTitleMonth"
                    @input="checkInMenu = false"
                    reactive>
                  </v-date-picker>
                </v-menu> 
              </div>
              <div class="pa-2">
                <div class="mb-1">체크아웃</div>
                <v-menu ref="menu" v-model="checkOutMenu">
                  <template v-slot:activator="{ on }">
                    <v-text-field v-model="checkOut" readonly v-on="on" solo></v-text-field>
                  </template>
                  <v-date-picker v-model="checkOut" no-title color="primary"
                  :first-day-of-week="1" :min="afterCheckInDate" 
                  :weekday-format="getDay"
                  :month-format="getMonth"
                  :title-date-format="getMonth"
                  :header-date-format="getHeaderTitleMonth"
                  :disabled="!checkIn"
                  @input="checkInMenu = false">
                  </v-date-picker>
                </v-menu> 
              </div>
            </div>
          </div>
        </v-card>

        <v-card class="card mt-2">
          <div class="pa-2">
            <div class="pa-2">인원</div>
            <v-divider></v-divider>
            <div class="d-flex mb-n8">
              <div class="pa-2">
                <div class="mb-1">성인</div>
                <v-autocomplete :items="personnel" v-model="adult" solo>
                </v-autocomplete>
              </div>
              <div class="pa-2">
                <div class="mb-1">어린이</div>
                <v-autocomplete :items="personnel" v-model="kids" solo>
                </v-autocomplete>
              </div>
            </div>
          </div>
        </v-card>

        <v-card class="card mt-2">
          <div class="pa-2">
            <div class="pa-2">검색</div>
            <v-divider></v-divider>
            <div class="d-flex mb-n8 pa-2 mt-1">
              <v-text-field v-model="searchWord" solo></v-text-field>
              <v-btn class="ml-3 mt-2" @click="search">검색</v-btn>
            </div>
          </div>
        </v-card>
      </v-card>
    </v-menu>
  </div>
</template>


<script>
import si_list from '@/assets/data/si_list'
import gu_list from '@/assets/data/gu_list'
import api from '@/api'

export default {
  props: {
    coords: {}
  },
  data () {
    return {
      menu: false,
      siList: [],
      allGuList: null,
      si: null,
      gu: null,
      guList: [],
      checkIn: null,
      checkOut: null,
      adult: 2,
      kids: 0,
      personnel: [0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20],
      checkInMenu: false,
      checkOutMenu: false,
      searchWord: null
    }
  },
  computed: {
    today () {
      const today = new Date()
      const year = today.getFullYear()
      const month = ('0' + (today.getMonth() + 1)).slice(-2)
      const day = ('0' + today.getDate()).slice(-2)

      return year + '-' + month + '-' + day
    },
    afterCheckInDate () {
      const checkIn = new Date(this.checkIn)
      const year = checkIn.getFullYear()
      const month = ('0' + (checkIn.getMonth() + 1)).slice(-2)
      const day = Number(('0' + checkIn.getDate()).slice(-2)) + 1

      return year + '-' + month + '-' + day
    }
  },
  watch: {
    coords () {
      //console.log(this.coords)
    }
  },
  mounted () {
    this.siList = si_list
    this.allGuList = gu_list
    
  },
  methods: {
    getDay(date) {
      const daysOfWeek = ['일', '월', '화', '수', '목', '금', '토']
      let i = new Date(date).getDay(date)
      return daysOfWeek[i]
    },
    getMonth(date) {
      const monthName = [
        '1월',
        '2월',
        '3월',
        '4월',
        '5월',
        '6월',
        '7월',
        '8월',
        '9월',
        '10월',
        '11월',
        '12월',
      ]

      let i = new Date(date).getMonth(date)
      return monthName[i]
    },
    getHeaderTitleMonth(date) {
      const monthName = [
      "1월",
      "2월",
      "3월",
      "4월",
      "5월",
      "6월",
      "7월",
      "8월",
      "9월",
      "10월",
      "11월",
      "12월",
      ]
      let i = new Date(date).getMonth(date)
      const year = new Date(date).getFullYear(date)
      const month = monthName[i]
      return `${year}년 ${month}`
    },
    selectSi () {
      this.guList = []
      const siCode = this.si.CTPRVN_CD

      for (let i = 0; i < this.allGuList.length; i++) {
        if (this.allGuList[i].SIG_CD.toString().slice(0,2) == siCode) {
          this.guList.push(this.allGuList[i])         
        }
      }
    },
    selectGu () {
      this.$emit('selectGu', this.gu.lat, this.gu.lng)
    },
    search () {
      const guests = this.adult + this.kids
      
      if(!this.checkIn) this.checkIn = this.today, this.checkOut = this.afterCheckInDate

      api.get(`/camps/search?ne-lat=${this.coords.neLat}&ne-lng=${this.coords.neLng}
        &sw-lat=${this.coords.swLat}&sw-lng=${this.coords.swLng}&query=${this.searchWord}
        &check-in=${this.checkIn}&check-out=${this.checkOut}&guests=${guests}&start=0&page=0`).then(res => {

        console.log(res.data)
      })

      this.menu = false
    },

  }
}
</script>


<style scoped>
.search-bar {
  display: flex;
  border-radius: 50px;
  background-color: white;
  height: 50px;
  width: 300px;
  font-size: 18px;
  box-shadow: 0 6px 20px rgba(0,0,0,0.2);
  padding: 8px;
  cursor: pointer;
  margin: 5px;
}
.search-bar > div {
  margin: auto;
}
.search-btn {
  border-radius: 50px;
  background: #e4dabc;
  width: 35px;
}
.theme--light.v-icon {
  color: white;
}
.card {
  border-radius: 12px;
}
</style>