<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8">
      <div class="text-center">
        <h1 class="text-4xl font-bold text-primary-600">ConnectHub</h1>
        <p class="mt-2 text-gray-600">소셜 미디어 플랫폼</p>
      </div>

      <div class="card">
        <RouterView />
      </div>

      <div class="text-center text-sm text-gray-600">
        <p>&copy; 2025 ConnectHub. All rights reserved.</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { RouterView } from 'vue-router'
</script>
