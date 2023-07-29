FROM openjdk:17-jdk-alpine

# 기본값으로 'prod' 프로파일을 설정합니다.
ARG SPRING_PROFILE=prod

# 환경 변수를 사용하여 프로파일을 지정합니다.
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILE}

# 애플리케이션 JAR 파일을 복사합니다.
COPY build/libs/*.jar app.jar

# 포트를 열어줍니다.
EXPOSE 8000

# 애플리케이션 실행
ENTRYPOINT ["java","-jar","/app.jar"]