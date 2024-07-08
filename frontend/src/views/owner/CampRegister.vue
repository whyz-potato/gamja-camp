<template>
  <div class="sheet">
    <camp-register-form></camp-register-form>
  </div>
</template>


<script>
import CampRegisterForm from '@/components/owner/CampRegisterForm'

export default {
  components: {
    CampRegisterForm
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