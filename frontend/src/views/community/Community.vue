<template>
  <div class="sheet">
    <div class="tabs">
      <ul>
        <li class="tab">
          <input type="radio" id="tab1" name="tab"
            v-model="tabValue" value="GeneralListForm" checked>
          <label for="tab1">자유게시판</label>
        </li>
        <li class="tab">
          <input type="radio" id="tab2" name="tab"
            v-model="tabValue" value="GatherListForm">
          <label for="tab2">모집게시판</label>
        </li>
      </ul>
    </div>
    <component :is="tabValue"></component>
    
  </div>
</template>


<script>
import GeneralListForm from '@/components/commuity/GeneralListForm'
import GatherListForm from '@/components/commuity/GatherListForm'

export default {
  components: {
    GeneralListForm,
    GatherListForm
  },
  data () {
    return {
      tabValue: 'GeneralListForm'
    }
  },
  watch: {
    tabValue () {
      console.log(this.tabValue)
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
  padding: 30px;
  border-radius: 12px;
  border: 1px solid #dddddd;

}
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
</style>