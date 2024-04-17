<template>
  <div class="sheet">
    <gather-update-form></gather-update-form>
  </div>
</template>


<script>
import GatherUpdateForm from '@/components/commuity/GatherUpdateForm'

export default {
  components: {
    GatherUpdateForm
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