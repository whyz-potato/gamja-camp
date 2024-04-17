<template>
  <div class="sheet">
    <gather-register-form></gather-register-form>
  </div>
</template>


<script>
import GatherRegisterForm from '@/components/commuity/GatherRegisterForm'

export default {
  components: {
    GatherRegisterForm
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