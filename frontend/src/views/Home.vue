<template>
  <div class="max-w-2xl mx-auto">
    <!-- 게시물 작성 -->
    <PostComposer @posted="handleNewPost" />

    <!-- 피드 -->
    <div class="mt-6 space-y-6">
      <div v-if="isLoading && posts.length === 0" class="text-center py-8">
        <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-primary-600 mx-auto"></div>
        <p class="mt-2 text-gray-600">피드를 불러오는 중...</p>
      </div>

      <div v-else-if="posts.length === 0" class="text-center py-12">
        <p class="text-gray-600 text-lg">아직 게시물이 없습니다.</p>
        <p class="text-gray-500 mt-2">첫 번째 게시물을 작성해보세요!</p>
      </div>

      <PostCard v-for="post in posts" :key="post.id" :post="post" @like="handleLike" @comment="handleComment" />

      <!-- 더 보기 버튼 -->
      <div v-if="hasMore" class="text-center py-4">
        <button @click="loadMore" :disabled="isLoading" class="btn-outline disabled:opacity-50">
          <span v-if="isLoading">로딩 중...</span>
          <span v-else>더 보기</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { usePostsStore } from '@/stores/posts'
import PostComposer from '@/components/PostComposer.vue'
import PostCard from '@/components/PostCard.vue'

const router = useRouter()

const postsStore = usePostsStore()

// Computed
const posts = computed(() => postsStore.feedPosts)
const isLoading = computed(() => postsStore.isLoading)
const hasMore = computed(() => postsStore.hasMore)

// Methods
const handleNewPost = () => {
  // 새 게시물이 작성되면 피드 새로고침
  postsStore.refreshFeed()
}

const handleLike = async (postId) => {
  await postsStore.toggleLike(postId)
}

const handleComment = (postId) => {
  // 게시물 상세 페이지로 이동
  router.push(`/post/${postId}`)
}

const loadMore = () => {
  postsStore.loadMorePosts()
}

// Lifecycle
onMounted(() => {
  postsStore.fetchFeed()
})
</script>
