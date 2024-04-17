<template>
  <div class="sheet">
    <general-update-form></general-update-form>
  </div>
</template>


<script>
import GeneralUpdateForm from '@/components/commuity/GeneralUpdateForm'

export default {
  components: {
    GeneralUpdateForm
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