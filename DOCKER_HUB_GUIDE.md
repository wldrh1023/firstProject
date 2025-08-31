# 🐳 Docker Hub 사용 가이드

## 📋 Docker Hub 이미지 정보

### 🏷️ 이미지 태그

- **백엔드**: `wldrh1023/shop-backend:latest`
- **프론트엔드**: `wldrh1023/shop-frontend:latest`

### 🔗 Docker Hub 링크

- [wldrh1023/shop-backend](https://hub.docker.com/r/wldrh1023/shop-backend)
- [wldrh1023/shop-frontend](https://hub.docker.com/r/wldrh1023/shop-frontend)

## 🚀 빠른 실행 방법

### 방법 1: 자동 스크립트 사용 (추천)

#### Windows

```bash
run-hub.bat
```

#### Linux/Mac

```bash
chmod +x run-hub.sh
./run-hub.sh
```

### 방법 2: 수동 실행

```bash
# 1. 이미지 다운로드
docker pull wldrh1023/shop-backend:latest
docker pull wldrh1023/shop-frontend:latest

# 2. 프로젝트 실행
docker-compose -f docker-compose.hub.yml up -d
```

### 방법 3: 최소 파일로 실행

필요한 파일만 복사:

- `docker-compose.hub.yml`
- `database/init.sql` (데이터베이스 초기화용)

```bash
# 실행
docker-compose -f docker-compose.hub.yml up -d
```

## 🌐 접속 정보

- **프론트엔드**: http://localhost:3000
- **백엔드 API**: http://localhost:8080
- **데이터베이스**: localhost:3306 (root/1234)

## 📊 테스트 계정

- **이메일**: test@test.com
- **비밀번호**: 1234

## 🔄 이미지 업데이트

### 새 버전 푸시

```bash
# 1. 이미지 빌드
docker-compose build

# 2. 태그 설정
docker tag firstproject-backend:latest wldrh1023/shop-backend:latest
docker tag firstproject-frontend:latest wldrh1023/shop-frontend:latest

# 3. Docker Hub에 푸시
docker push wldrh1023/shop-backend:latest
docker push wldrh1023/shop-frontend:latest
```

### 새 버전 다운로드

```bash
# 기존 컨테이너 중지
docker-compose -f docker-compose.hub.yml down

# 새 이미지 다운로드
docker pull wldrh1023/shop-backend:latest
docker pull wldrh1023/shop-frontend:latest

# 재시작
docker-compose -f docker-compose.hub.yml up -d
```

## 💡 장점

1. **빠른 실행**: 소스 코드 빌드 없이 바로 실행
2. **간편한 배포**: 최소한의 파일만으로 실행 가능
3. **버전 관리**: 태그를 통한 버전 관리
4. **공유 용이**: 다른 환경에서 쉽게 실행 가능

## 🔍 문제 해결

### 이미지 다운로드 실패

```bash
# Docker Hub 로그인
docker login

# 이미지 다시 다운로드
docker pull wldrh1023/shop-backend:latest
docker pull wldrh1023/shop-frontend:latest
```

### 포트 충돌

```bash
# docker-compose.hub.yml에서 포트 변경
# 예: 3000:3000 → 3001:3000
```

### 데이터베이스 초기화

```bash
# 데이터베이스 초기화
docker-compose -f docker-compose.hub.yml down -v
docker-compose -f docker-compose.hub.yml up -d
```

## 📝 주의사항

1. **인터넷 연결**: Docker Hub에서 이미지를 다운로드하므로 인터넷 연결 필요
2. **첫 실행 시간**: 이미지 다운로드로 인해 첫 실행 시 시간이 걸릴 수 있음
3. **보안**: 프로덕션 환경에서는 기본 비밀번호 변경 권장
