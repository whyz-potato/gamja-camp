<template>
  <div class="sheet">
    <login-form></login-form>
  </div>
</template>


<script>
import LoginForm from '@/components/member/LoginForm'

export default {
  components: {
    LoginForm
  }
}
</script>


<style scoped>
.sheet {
  /* background-color: #fceae1; */
  width: 450px;
  /* margin: auto; */
  padding: 10px;
  border-radius: 12px;
  border: 1px solid #dddddd;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>