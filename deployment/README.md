# Gallery Shop 배포 가이드

## 📋 필요 조건

- Docker
- Docker Compose

## 🚀 빠른 시작

### 1. 실행

```bash
# 이미지 다운로드 및 실행
docker-compose -f docker-compose.hub.yml up -d
```

### 2. 접속

- **프론트엔드**: http://localhost:3000
- **백엔드 API**: http://localhost:8080
- **데이터베이스**: localhost:3307

## 📁 파일 구조

```
deployment/
├── docker-compose.hub.yml  # 도커 컴포즈 설정
├── database/
│   └── init.sql           # 데이터베이스 초기화
└── README.md              # 이 파일
```

## 🔧 관리 명령어

### 컨테이너 관리

```bash
# 실행
docker-compose -f docker-compose.hub.yml up -d

# 중지
docker-compose -f docker-compose.hub.yml down

# 로그 확인
docker-compose -f docker-compose.hub.yml logs -f

# 재시작
docker-compose -f docker-compose.hub.yml restart
```

### 이미지 업데이트

```bash
# 최신 이미지 다운로드
docker-compose -f docker-compose.hub.yml pull

# 새로 빌드 후 실행
docker-compose -f docker-compose.hub.yml up -d --force-recreate
```

## 🗄️ 데이터베이스

- **타입**: MariaDB 10.6
- **포트**: 3307
- **데이터베이스**: gallery
- **사용자**: root
- **비밀번호**: 1234

## 🔒 보안 주의사항

- 프로덕션 환경에서는 반드시 비밀번호를 변경하세요
- 방화벽 설정을 확인하세요
- HTTPS 사용을 권장합니다

## 🆘 문제 해결

```bash
# 컨테이너 상태 확인
docker-compose -f docker-compose.hub.yml ps

# 특정 서비스 로그 확인
docker-compose -f docker-compose.hub.yml logs backend
docker-compose -f docker-compose.hub.yml logs frontend
docker-compose -f docker-compose.hub.yml logs mariadb
```

