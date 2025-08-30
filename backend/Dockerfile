# 멀티 스테이지 빌드를 사용하여 이미지 크기 최적화
FROM gradle:8.5-jdk17 AS builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 관련 파일들을 먼저 복사 (의존성 캐싱을 위해)
COPY build.gradle settings.gradle ./
COPY gradle/ gradle/

# 의존성 다운로드 (캐시 활용)
RUN gradle dependencies --no-daemon

# 소스 코드 복사
COPY src/ src/

# 애플리케이션 빌드
RUN gradle build --no-daemon -x test

# 실행 스테이지
FROM openjdk:17-jre-slim

# 헬스체크를 위한 curl 설치
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일을 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 애플리케이션 실행을 위한 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
