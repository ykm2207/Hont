# 1. 빌드 스테이지 (현재 프로젝트 버전에 맞춰 JDK 25 사용)
FROM openjdk:25-jdk-slim AS build
WORKDIR /app
COPY . .
# 실행 권한 부여 후 빌드
RUN chmod +x ./gradlew
RUN ./gradlew clean bootJar

# 2. 실행 스테이지
FROM openjdk:25-jdk-slim
WORKDIR /app
# 빌드 스테이지에서 생성된 jar 파일 복사
COPY --from=build /app/build/libs/*-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]