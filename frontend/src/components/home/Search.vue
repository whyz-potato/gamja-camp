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
          <button class="search-btn">
            <v-icon class="material-icons">search</v-icon>
          </button>
        </div>
      </template>

      <v-card class="card pa-3">
        <div class="tab">
          
          <!-- <v-item-group>
            <v-item v-for="tab in tabs" :key="tab.index"
              v-slot="{ active, toggle }">
              <v-btn active-class="primary" :input-value="active"
                v-on:click="currentTab = tab.index"
                @click="toggle" rounded outlined color="grey">{{ tab }}</v-btn>
            </v-item>
          </v-item-group>
          -->
          <!--
          <v-chip-group>
            <v-chip v-for="tab, index in tabs" :key="index"
               v-on:click="currentTab = index"
              outlined>{{ tab }}</v-chip>
          </v-chip-group>
          -->

          
          <button v-on:click="currentTab = 0" value="0">
            <div>
              <div class="btn-title">지역</div>
              <div class="btn-subtitle">{{ selectArea }}</div>
            </div>    
          </button>

          <button v-on:click="currentTab = 1">
            <div class="d-flex">
              <div class="mr-8">
                <div class="btn-title">체크인</div>
                <div class="btn-subtitle">{{ checkIn }}</div>
              </div>
              <div>
                <div class="btn-title">체크아웃</div>
                <div class="btn-subtitle">{{ checkOut }}</div>
              </div>
            </div>
            
          </button>
          <button v-on:click="currentTab = 2">
            <div>
              <div class="btn-title">인원수</div>
              <div class="btn-subtitle">{{ guest }}</div>
            </div>
          </button>

          <button @click="search" style="background: #e4dabc;">
            <v-icon class="material-icons">search</v-icon>
          </button>
        </div>
        
        <v-window v-model="currentTab">
          <v-window-item :value="0">
            <div class="d-flex">
              <v-card class="ma-5" width="300" flat>
                <v-item-group>
                  <v-item v-for="list in siList" :key="list.index"
                    v-slot="{ active, toggle }">
                    <v-btn active-class="primary" :input-value="active"
                      @click="[toggle,selectSi(list.CTPRVN_CD)]" rounded outlined color="grey"
                      class="ma-1">{{ list.CTP_KOR_NM }}</v-btn>
                  </v-item>
                </v-item-group>
              </v-card>
              <v-divider vertical></v-divider>
              <v-card class="ma-5" width="300" flat>
                <v-item-group>
                  <v-item v-for="list in guList" :key="list.index"
                    v-slot="{ active, toggle }">
                    <v-btn active-class="primary" :input-value="active"
                      @click="[toggle, selectGu(list.SIG_KOR_NM, list.lat, list.lng)]" rounded outlined color="grey"
                      class="ma-1">{{ list.SIG_KOR_NM }}</v-btn>
                  </v-item>
                </v-item-group>
                
              </v-card>
            </div>
          </v-window-item>

          <v-window-item :value="1">
            <div class="datePicker">
              <div>
                <v-date-picker class="ma-3" v-model="checkIn" no-title color="primary"
                  :first-day-of-week="1" :min="today"
                  :weekday-format="getDay"
                  :month-format="getMonth"
                  :title-date-format="getMonth"
                  :header-date-format="getHeaderTitleMonth"
                  reactive></v-date-picker>
              </div>
              <v-divider vertical></v-divider>
              <div>
                <v-date-picker class="ma-3" v-model="checkOut" no-title color="primary"
                  :first-day-of-week="1" :min="afterCheckInDate" 
                  :weekday-format="getDay"
                  :month-format="getMonth"
                  :title-date-format="getMonth"
                  :header-date-format="getHeaderTitleMonth"
                  :disabled="!checkIn"
                  ></v-date-picker>
              </div>
            </div>
            
          </v-window-item>

          <v-window-item :value="2">
            <div class="guest">
              <div class="guest-type">
                <span>어른</span>
                <v-btn icon outlined color="grey">
                  <v-icon>mdi-chevron-up</v-icon>
                </v-btn>
                <v-btn icon outlined color="grey">
                  <v-icon>mdi-plus</v-icon>
                </v-btn>
              </div>
              <v-divider></v-divider>
              <div>
                <span>어린이</span>
                <v-btn></v-btn>
                <v-btn></v-btn>
              </div>
              <v-divider></v-divider>
              <div>
                <span>반려동물</span>
                <v-btn></v-btn>
                <v-btn></v-btn>
              </div>
            </div>
          </v-window-item>
        </v-window>

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
      siList: null,
      allGuList: null,
      si: null,
      gu: null,
      inputSearch: null,
      //search: false,
      tabs: [ "지역", "기간", "인원" ],
      currentTab: 0,
      guList: [],
      daterange: { startDaate: null, endDate: null },
      //value: 1,
      selectArea: '',
      guest: null,
      checkIn: null,
      checkOut: null,
    }
  },
  computed: {
    // dateRangeText () {
    //   if(this.dates) {
    //     return this.dates.join(' ~ ')
    //   } return ''
    // },
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
    checkOut () {
      if (this.checkOut) {
        this.currentTab = 2
      }
    },
    coords () {
      console.log(this.coords)
    }
  },
  mounted () {
    this.siList = si_list
    this.allGuList = gu_list
    
  },
  methods: {
    getDay(date) {
      const daysOfWeek = ['일', '월', '화', '수', '목', '금', '토'];
      let i = new Date(date).getDay(date);
      return daysOfWeek[i];
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
      ];

      let i = new Date(date).getMonth(date);
      return monthName[i];
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
    selectSi (siCode) {
      this.guList = []

      for (let i = 0; i < this.allGuList.length; i++) {
        if (this.allGuList[i].SIG_CD.toString().slice(0,2) == siCode) {
          this.guList.push(this.allGuList[i])         
        }
      }
    },
    selectGu (guName, lat, lng) {
      this.selectArea = guName
      this.currentTab = 1

      this.$emit('selectGu', lat, lng)
    },
    search () {
      api.get(`/camps/search?ne-lat=${this.coords.neLat}&ne-lng=${this.coords.neLng}&sw-lat=${this.coords.swLat}&sw-lng=${this.coords.swLng}
        &query=캠핑&check-in=${this.checkIn}&check-out=${this.checkOut}&guests=1&start=0&page=0`).then(res => {
        console.log(res.data)
      })
      this.menu = false
    }
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
.tab {
  height: 55px;
  background-color: #f2f2f2;
  border-radius: 50px !important;
  display: flex;
}
.tab > button {
  border-radius: 50px;
  margin-right: 30px;
  padding: 0 20px 0 20px;
  transition-duration: 0.1s;
}
.tab > button > div {
  margin: 7px;
}
.btn-title {
  font-size: 14px;
  font-weight: bold;
}
.btn-subtitle {
  color: gray;
}
.tab > button:hover {
  background-color: #dddddd;
}
.tab > button:active {
  font-size: 14px;
}
.tab > button:focus {
  background-color: white;
  box-shadow: 0 6px 20px rgba(0,0,0,0.2);
}
.datePicker {
  margin: auto;
  margin-top: 20px;
  display: flex;
  
  
}
.guest {
  width: 300px;
  margin: auto;
  margin-top: 50px;
  margin-bottom: 50px;
}
.guest-type {
  display: flex;
  height: 60px;
  line-height: 60px;
}
</style>