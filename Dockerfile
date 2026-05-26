# 1. 빌드 스테이지 (Gradle 사전 설치 이미지 사용 - wrapper 다운로드 불필요)
FROM gradle:9.4-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean bootJar -x test --no-daemon

# 2. 실행 스테이지
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/build/libs/*-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
