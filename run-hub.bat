@echo off
echo 🐳 Docker Hub 이미지로 쇼핑몰 프로젝트 실행 중...
echo.

echo 📋 Docker 상태 확인...
docker --version
if %errorlevel% neq 0 (
    echo ❌ Docker가 설치되지 않았습니다. Docker Desktop을 설치해주세요.
    pause
    exit /b 1
)

echo.
echo 🔧 Docker Hub 이미지 다운로드 중...
docker pull wldrh1023/shop-backend:latest
docker pull wldrh1023/shop-frontend:latest

echo.
echo 🚀 프로젝트 실행 중...
docker-compose -f docker-compose.hub.yml up -d

echo.
echo ⏳ 서비스 시작 대기 중...
timeout /t 15 /nobreak > nul

echo.
echo 🌐 접속 정보:
echo    프론트엔드: http://localhost:3000
echo    백엔드 API: http://localhost:8080
echo.
echo 📊 테스트 계정:
echo    이메일: test@test.com
echo    비밀번호: 1234
echo.
echo ✅ 프로젝트가 성공적으로 실행되었습니다!
echo.
echo 📝 참고: 이 방법은 소스 코드 빌드 없이 바로 실행됩니다.
pause
