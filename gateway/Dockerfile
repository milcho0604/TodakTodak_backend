# 멀티 스테이지 빌드 방법 사용

# 첫번째 스테이지
FROM openjdk:11 as stage1
WORKDIR /app

# gateway 디렉토리의 gradlew 파일 복사
COPY gradlew ./gateway/
# gateway 디렉토리의 gradle 폴더 복사
COPY gradle ./gateway/gradle
# eureka 디렉토리의 src 복사
COPY src ./gateway/src
# eureka 디렉토리의 build.gradle 및 settings.gradle 복사
COPY build.gradle ./gateway/
COPY settings.gradle ./gateway/

# gradlew에 실행 권한 부여
RUN chmod +x ./gateway/gradlew

# BootJar 실행
WORKDIR /app/gateway

# BootJar 실행
RUN ./gradlew bootJar

# 두번째 스테이지
FROM openjdk:11
WORKDIR /app

# 첫번째 스테이지에서 생성된 JAR 파일을 복사
COPY --from=stage1 /app/gateway/build/libs/*.jar app.jar

# 컨테이너 실행 시 JAR 파일 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
