# 멀티 스테이지 빌드 방법 사용

# 첫번째 스테이지
FROM openjdk:11 as stage1
WORKDIR /app

# member 디렉토리의 gradlew 파일 복사
COPY member/gradlew ./member/
# member 디렉토리의 gradle 폴더 복사
COPY member/gradle ./member/gradle
# member 디렉토리의 src 복사
COPY member/src ./member/src
# member 디렉토리의 build.gradle 및 settings.gradle 복사
COPY member/build.gradle ./member/
COPY member/settings.gradle ./member/

# gradlew에 실행 권한 부여
RUN ls ./member
RUN chmod +x ./member/gradlew

# BootJar 실행
#RUN ./member/gradlew bootJar
# 작업 디렉토리 변경
WORKDIR /app/member

# BootJar 실행
RUN ./gradlew bootJar

# 두번째 스테이지
FROM openjdk:11
WORKDIR /app

# 첫번째 스테이지에서 생성된 JAR 파일을 복사
COPY --from=stage1 /app/member/build/libs/*.jar app.jar

# 컨테이너 실행 시 JAR 파일 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
