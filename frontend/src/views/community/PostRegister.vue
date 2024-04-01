<template>
  <div class="sheet">
    <post-register-from></post-register-from>
  </div>
</template>

<script>
import PostRegisterFrom from '@/components/commuity/PostRegisterFrom'

export default {
  components: {
    PostRegisterFrom
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