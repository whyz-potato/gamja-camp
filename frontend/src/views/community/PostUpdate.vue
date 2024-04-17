<template>
  <div class="sheet">
    <post-update-form></post-update-form>
  </div>
</template>


<script>
import PostUpdateForm from '@/components/commuity/PostUpdateForm'

export default {
  components: {
    PostUpdateForm
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