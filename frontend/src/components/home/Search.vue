<template>
  <div>
    <v-menu v-model="menu" :close-on-content-click="false" :nudge-width="500"
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
          <!--
          <v-item-group>
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
          <button v-on:click="currentTab = 0">
            <div>
              <div class="btn-title">지역</div>
              <div class="btn-subtitle">구/시</div>
            </div>    
          </button>
          <button v-on:click="currentTab = 1">
            <div class="d-flex">
              <div class="mr-8">
                <div class="btn-title">체크인</div>
                <div class="btn-subtitle">22</div>
              </div>
              <div>
                <div class="btn-title">체크아웃</div>
                <div class="btn-subtitle">22</div>
              </div>
            </div>
            
          </button>
          <button v-on:click="currentTab = 2">
            <div>
               <div class="btn-title">인원수</div>
              <div class="btn-subtitle">11</div>
            </div>
          </button>
          <button style="background: #e4dabc;">
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
                      @click="toggle" rounded outlined color="grey"
                      class="ma-1">{{ list.SIG_KOR_NM }}</v-btn>
                  </v-item>
                </v-item-group>
                
              </v-card>
            </div>
          </v-window-item>

          <v-window-item :value="1">
            <div class="d-flex">
              <div>
                <div>체크인</div>
                <v-date-picker class="ma-3" no-title
                  :first-day-of-week="1" :min="today" ></v-date-picker>
              </div>
              <v-divider vertical></v-divider>
              <div>
                <div>체크아웃</div>
                <v-date-picker class="ma-3" no-title
                  :first-day-of-week="1" :min="today" ></v-date-picker>
              </div>
            </div>
            
          </v-window-item>

          <v-window-item :value="2">
            인원
          </v-window-item>
        </v-window>

      </v-card>
    </v-menu>
  </div>
</template>


<script>
import si_list from '@/assets/data/si_list'
import gu_list from '@/assets/data/gu_list'

export default {
  data () {
    return {
      dates: null,
      menu: false,
      siList: null,
      allGuList: null,
      si: null,
      gu: null,
      inputSearch: null,
      search: false,
      tabs: [ "지역", "기간", "인원" ],
      currentTab: 0,
      guList: [],
      daterange: { startDaate: null, endDate: null }
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
    this.siList = si_list
    this.allGuList = gu_list
  },
  methods: {
    selectSi (siCode) {
      this.guList = []

      for (let i = 0; i < this.allGuList.length; i++) {
        if (this.allGuList[i].SIG_CD.toString().slice(0,2) == siCode) {
          this.guList.push(this.allGuList[i])         
        }
      }
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
</style>