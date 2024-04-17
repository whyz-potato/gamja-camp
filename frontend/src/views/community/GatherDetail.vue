<template>
  <div class="sheet">
    <gather-detail-form></gather-detail-form>
  </div>
</template>


<script>
import GatherDetailForm from '@/components/commuity/GatherDetailForm'

export default {
  components: { 
    GatherDetailForm 
  },
  data () {
    return {
      
    }
  },
  mounted () {
    
  }
  
}
</script>


<style scoped>
.sheet {
  /* background-color: #fceae1; */
  width: 80%;
  margin: auto;
  margin-top: 30px;
  margin-bottom: 30px;
  padding: 20px;
  border-radius: 12px;
  border: 1px solid #dddddd;

}
</style>