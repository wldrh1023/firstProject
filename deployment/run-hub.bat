@echo off
echo Docker Hub image run...
echo.

echo Docker status check...
docker --version
if %errorlevel% neq 0 (
    echo ❌ Docker is not installed. Please install Docker.
    pause
    exit /b 1
)

echo.
echo Docker Hub image download...
docker pull wldrh1023/shop-backend:v1.2
docker pull wldrh1023/shop-frontend:v1.2

echo.
echo Project run...
docker-compose -f docker-compose.hub.yml up -d

echo.
echo Service start waiting...
timeout /t 15 /nobreak > nul

echo.
echo Setting up image files...
docker exec -it shop-backend mkdir -p /app/static/img
docker exec -it shop-backend cp /app/static/images/1.jpg /app/static/img/
docker exec -it shop-backend cp /app/static/images/2.jpg /app/static/img/
docker exec -it shop-backend cp /app/static/images/3.jpg /app/static/img/
docker exec -it shop-backend cp /app/static/images/4.png /app/static/img/
docker exec -it shop-backend cp /app/static/images/5.png /app/static/img/
docker exec -it shop-backend cp /app/static/images/6.png /app/static/img/

echo.
echo Adding sample data...
docker exec -it shop-mariadb mariadb -u root -p1234 -e "USE gallery; INSERT INTO items (name, price, discount_per, img_path) VALUES ('pic1', 1000000, 5, '/img/1.jpg'), ('pic2', 2000000, 10, '/img/2.jpg'), ('pic3', 300000, 30, '/img/3.jpg'), ('pic4', 500000, 40, '/img/4.png'), ('pic5', 250000, 55, '/img/5.png'), ('pic6', 550000, 25, '/img/6.png');" 2>nul

echo.
echo Access information:
echo    Frontend: http://localhost:3000
echo    Backend API: http://localhost:8080
echo    Database: localhost:3307 (root/1234)
echo.
echo Test account:
echo    Email: test@test.com
echo    Password: 1234
echo.
echo Project run successfully!
echo.
echo Note: This method runs without building the source code.
pause
