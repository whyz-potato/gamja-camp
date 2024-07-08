<template>
  <div class="sheet">
    <camp-admin-form></camp-admin-form>
  </div>
</template>


<script>
import CampAdminForm from '@/components/owner/CampAdminForm'

export default {
  components: {
    CampAdminForm
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