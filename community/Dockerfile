# 멀티 스테이지 빌드 방법 사용

# 첫번째 스테이지
FROM openjdk:11 as stage1
WORKDIR /app

# community 디렉토리의 gradlew 파일 복사
COPY gradlew ./community/
# community 디렉토리의 gradle 폴더 복사
COPY gradle ./community/gradle
# community 디렉토리의 src 복사
COPY src ./community/src
# community 디렉토리의 build.gradle 및 settings.gradle 복사
COPY build.gradle ./community/
COPY settings.gradle ./community/

# gradlew에 실행 권한 부여
RUN chmod +x ./community/gradlew

# BootJar 실행
WORKDIR /app/community

# BootJar 실행
RUN ./gradlew bootJar

# 두번째 스테이지
FROM openjdk:11
WORKDIR /app

# 첫번째 스테이지에서 생성된 JAR 파일을 복사
COPY --from=stage1 /app/community/build/libs/*.jar app.jar

# 컨테이너 실행 시 JAR 파일 실행
ENTRYPOINT ["java -jar app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
