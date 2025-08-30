# 🐳 Docker 컨테이너 실행 가이드

이 프로젝트는 Spring Boot 백엔드, Vue.js 프론트엔드, MariaDB 데이터베이스를 Docker 컨테이너로 실행할 수 있습니다.

## 📋 사전 요구사항

- Docker Desktop 설치 (Windows/Mac) 또는 Docker Engine (Linux)
- Docker Compose 설치 (Docker Desktop에 포함됨)

## 🚀 빠른 시작

### 1. 프로젝트 클론 및 디렉토리 이동

```bash
git clone <repository-url>
cd firstProject
```

### 2. Docker Compose로 전체 애플리케이션 실행

```bash
# 모든 서비스 빌드 및 실행
docker-compose up --build

# 백그라운드에서 실행하려면
docker-compose up --build -d
```

### 3. 애플리케이션 접속

- **프론트엔드**: http://localhost (포트 80)
- **백엔드 API**: http://localhost:8080
- **데이터베이스**: localhost:3307 (외부 접속용)

## 🛠️ 개별 서비스 관리

### 특정 서비스만 실행

```bash
# 데이터베이스만 실행
docker-compose up mariadb

# 백엔드만 실행 (데이터베이스 의존성 포함)
docker-compose up backend

# 프론트엔드만 실행
docker-compose up frontend
```

### 서비스 중지

```bash
# 모든 서비스 중지
docker-compose down

# 볼륨까지 삭제 (데이터베이스 데이터 삭제됨)
docker-compose down -v
```

### 서비스 재빌드

```bash
# 특정 서비스 재빌드
docker-compose build backend
docker-compose build frontend

# 모든 서비스 재빌드
docker-compose build
```

## 📊 서비스 상태 확인

### 실행 중인 컨테이너 확인

```bash
docker-compose ps
```

### 로그 확인

```bash
# 모든 서비스 로그
docker-compose logs

# 특정 서비스 로그
docker-compose logs backend
docker-compose logs frontend
docker-compose logs mariadb

# 실시간 로그 확인
docker-compose logs -f backend
```

### 헬스체크 확인

```bash
# 백엔드 헬스체크
curl http://localhost:8080/actuator/health

# 프론트엔드 헬스체크
curl http://localhost
```

## 🗄️ 데이터베이스 접속

### 컨테이너 내부에서 MariaDB 접속

```bash
docker-compose exec mariadb mariadb -u root -p
# 비밀번호: 1234
```

### 외부 클라이언트에서 접속

- **호스트**: localhost
- **포트**: 3307
- **사용자명**: root
- **비밀번호**: 1234
- **데이터베이스**: gallery

## 🔧 개발 환경 설정

### 개발 중 코드 변경 시

1. **백엔드 코드 변경**: 컨테이너를 재빌드해야 합니다

   ```bash
   docker-compose build backend
   docker-compose up backend
   ```

2. **프론트엔드 코드 변경**: 컨테이너를 재빌드해야 합니다
   ```bash
   docker-compose build frontend
   docker-compose up frontend
   ```

### 볼륨 마운트를 통한 개발 환경 (선택사항)

개발 중 빠른 반영을 위해 docker-compose.override.yml 파일을 생성할 수 있습니다:

```yaml
# docker-compose.override.yml
version: "3.8"
services:
  backend:
    volumes:
      - ./backend/src:/app/src
    environment:
      SPRING_DEVTOOLS_RESTART_ENABLED: true
```

## 🐛 문제 해결

### 포트 충돌 문제

```bash
# 사용 중인 포트 확인 (Windows)
netstat -ano | findstr :80
netstat -ano | findstr :8080
netstat -ano | findstr :3307

# 사용 중인 포트 확인 (Linux/Mac)
lsof -i :80
lsof -i :8080
lsof -i :3307
```

### 컨테이너 완전 정리

```bash
# 모든 컨테이너, 네트워크, 볼륨 삭제
docker-compose down -v --remove-orphans

# 사용하지 않는 Docker 이미지 정리
docker system prune -a
```

### 데이터베이스 초기화

```bash
# 데이터베이스 볼륨 삭제 후 재생성
docker-compose down -v
docker-compose up mariadb
```

## 📝 환경 변수 설정

필요시 `.env` 파일을 생성하여 환경 변수를 설정할 수 있습니다:

```env
# .env
DB_PASSWORD=your_password
JWT_SECRET=your_jwt_secret
```

## 🔐 보안 고려사항

프로덕션 환경에서는 다음 사항을 고려해주세요:

1. **데이터베이스 비밀번호 변경**
2. **JWT 시크릿 키 설정**
3. **포트 노출 최소화**
4. **HTTPS 설정**
5. **방화벽 설정**

## 📞 지원

문제가 발생하면 다음을 확인해주세요:

1. Docker Desktop이 실행 중인지 확인
2. 포트 충돌이 없는지 확인
3. 디스크 공간이 충분한지 확인
4. 로그에서 오류 메시지 확인

---

**Happy Coding! 🚀**
