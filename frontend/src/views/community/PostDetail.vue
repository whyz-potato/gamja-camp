<template>
  <div class="sheet">
    <post-detail-form></post-detail-form>
  </div>
</template>


<script>
import PostDetailForm from '@/components/commuity/PostDetailForm'

export default {
  components: { 
    PostDetailForm 
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