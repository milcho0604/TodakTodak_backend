# 멀티 스테이지 빌드 방법 사용

# 첫번째 스테이지
FROM openjdk:11 as stage1
WORKDIR /app

# eureka 디렉토리의 gradlew 파일 복사
COPY gradlew ./eureka/
# eureka 디렉토리의 gradle 폴더 복사
COPY gradle ./eureka/gradle
# eureka 디렉토리의 src 복사
COPY src ./eureka/src
# eureka 디렉토리의 build.gradle 및 settings.gradle 복사
COPY build.gradle ./eureka/
COPY settings.gradle ./eureka/

# gradlew에 실행 권한 부여
RUN chmod +x ./eureka/gradlew

# BootJar 실행
WORKDIR /app/eureka

# BootJar 실행
RUN ./gradlew bootJar

# 두번째 스테이지
FROM openjdk:11
WORKDIR /app

# 첫번째 스테이지에서 생성된 JAR 파일을 복사
COPY --from=stage1 /app/eureka/build/libs/*.jar app.jar

# 컨테이너 실행 시 JAR 파일 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
