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
echo "🌐 Access information:"
echo "   Frontend: http://localhost:3000"
echo "   Backend API: http://localhost:8080"
echo
echo "📊 Test account:"
echo "   Email: test@test.com"
echo "   Password: 1234"
echo
echo "✅ Project run successfully!"
echo
echo "📝 Note: This method runs without building the source code."
