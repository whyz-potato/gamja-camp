<template>
  <div class="sheet">
    <room-detail-form></room-detail-form>
  </div>
</template>


<script>
import RoomDetailForm from '@/components/home/RoomDetailForm'

export default {
  components: {
    RoomDetailForm
  }
}
</script>


<style scoped>
.sheet {
  width: 800px;
  padding: 10px;
  border-radius: 12px;
  border: 1px solid #dddddd;
  position: absolute;
  /* top: 50%;
  left: 50%;
  transform: translate(-50%, -50%); */
  left: 50%;
  transform: translate(-50%,0);
  margin-top: 60px;
}
</style>