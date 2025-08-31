#!/bin/bash

echo "🐳 Docker Hub image run..."
echo

echo "📋 Docker status check..."
if ! command -v docker &> /dev/null; then
    echo "❌ Docker is not installed. Please install Docker."
    exit 1
fi

echo
echo "🔧 Docker Hub image download..."
docker pull wldrh1023/shop-backend:latest
docker pull wldrh1023/shop-frontend:latest

echo
echo "🚀 Project run..."
docker-compose -f docker-compose.hub.yml up -d

echo
echo "⏳ Service start waiting..."
sleep 15

echo
echo "🔧 Setting up image files..."
docker exec -it shop-backend mkdir -p /app/static/img
docker exec -it shop-backend cp /app/static/images/1.jpg /app/static/img/
docker exec -it shop-backend cp /app/static/images/2.jpg /app/static/img/
docker exec -it shop-backend cp /app/static/images/3.jpg /app/static/img/
docker exec -it shop-backend cp /app/static/images/4.png /app/static/img/
docker exec -it shop-backend cp /app/static/images/5.png /app/static/img/
docker exec -it shop-backend cp /app/static/images/6.png /app/static/img/

echo
echo "📊 Adding sample data..."
docker exec -it shop-mariadb mariadb -u root -p1234 -e "USE gallery; INSERT INTO items (name, price, discount_per, img_path) VALUES ('스마트폰', 1000000, 5, '/img/1.jpg'), ('노트북', 2000000, 10, '/img/2.jpg'), ('헤드폰', 300000, 30, '/img/3.jpg'), ('스마트워치', 500000, 40, '/img/4.png'), ('태블릿', 250000, 55, '/img/5.png'), ('블루투스 스피커', 550000, 25, '/img/6.png');" 2>/dev/null

echo
echo "🌐 Access information:"
echo "   Frontend: http://localhost:3000"
echo "   Backend API: http://localhost:8080"
echo "   Database: localhost:3307 (root/1234)"
echo
echo "📊 Test account:"
echo "   Email: test@test.com"
echo "   Password: 1234"
echo
echo "✅ Project run successfully!"
echo
echo "📝 Note: This method runs without building the source code."
