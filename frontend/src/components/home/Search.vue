<template>
  <div>
    <v-menu v-model="menu" :close-on-content-click="false" :nudge-width="500"
      transition="slide-x-transition" offset-x rounded="lg">
      <template v-slot:activator="{ on, attrs }">
        <v-btn v-bind="attrs" v-on="on" fab>검색</v-btn>
      </template>

      <v-card>
        <div>
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
          <v-chip-group>
            <v-chip v-for="tab, index in tabs" :key="index"
              v-on:click="currentTab = index"
              outlined>{{ tab }}</v-chip>
          </v-chip-group>
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
            기간
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
      guList: []
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
.search_bar {
  background-color: white;

}
</style>