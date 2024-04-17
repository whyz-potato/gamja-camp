<template>
  <div class="sheet">
    <general-register-form></general-register-form>
  </div>
</template>

<script>
import GeneralRegisterForm from '@/components/commuity/GeneralRegisterForm'

export default {
  components: {
    GeneralRegisterForm
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