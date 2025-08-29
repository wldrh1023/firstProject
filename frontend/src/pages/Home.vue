<template>
  <div class="home">
    <div class="album py-5 bg-body-tertiary">
      <div class="container">
        <!-- 로딩 상태 -->
        <div v-if="state.loading" class="text-center">
          <div class="spinner-border" role="status">
            <span class="visually-hidden">로딩 중...</span>
          </div>
          <p class="mt-2">상품을 불러오는 중입니다...</p>
        </div>

        <!-- 오류 상태 -->
        <div v-else-if="state.error" class="alert alert-danger" role="alert">
          <h4 class="alert-heading">오류 발생!</h4>
          <p>{{ state.error }}</p>
          <hr>
          <p class="mb-0">서버가 실행 중인지 확인해주세요.</p>
        </div>

        <!-- 데이터 표시 -->
        <div v-else class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
          <div class="col" v-for="(item, i) in state.items" :key="i">
            <CardComponent :item="item" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import CardComponent from '@/components/Card.vue';
import { reactive } from 'vue';

export default {
  name: "HomeComponent",
  components: {
    CardComponent
  },
  setup() {
    const state = reactive({
      items: [],
    })

    // 데이터 로딩
    axios.get("/api/items")
      .then(({ data }) => {
        state.items = data;
      })


    return { state }
  }
}
</script>

<style scoped></style>