# 멀티 스테이지 빌드를 사용하여 이미지 크기 최적화
FROM node:18-alpine AS builder

# 작업 디렉토리 설정
WORKDIR /app

# package.json과 package-lock.json을 먼저 복사 (의존성 캐싱을 위해)
COPY package*.json ./

# 의존성 설치 (빌드를 위해 devDependencies도 필요)
RUN npm ci

# 소스 코드 복사
COPY . .

# Vue.js 애플리케이션 빌드
RUN npm run build

# Nginx를 사용한 실행 스테이지
FROM nginx:alpine

# 빌드된 정적 파일들을 Nginx의 기본 디렉토리로 복사
COPY --from=builder /app/dist /usr/share/nginx/html

# Nginx 설정 파일 복사 (SPA 라우팅을 위한 설정)
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 포트 노출
EXPOSE 80

# Nginx 실행
CMD ["nginx", "-g", "daemon off;"]
