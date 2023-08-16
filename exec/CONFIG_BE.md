### 쿠버네티스 시크릿 파일 구성
- 아래의 파일을 작성하여 secret.yml 파일을 생성해야 합니다.
- 모든 값은 Base64로 인코딩 되어야 합니다.
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: honjarang-secret
type: kubernetes.io/dockerconfigjson
data:
  .dockerconfigjson: Docker Hub 인증 정보
  datasource_url: MySQL 데이터베이스 URL
  datasource_username: MySQL 데이터베이스 사용자 이름
  datasource_password: MySQL 데이터베이스 비밀번호
  aws_ses_access_key: AWS SES 액세스 키
  aws_ses_secret_key: AWS SES 시크릿 키
  aws_ses_sender_email: AWS SES 발신자 이메일
  mongodb_uri: MongoDB URI
  mongodb_database: MongoDB 데이터베이스 이름
  kakao_rest_api_key: 카카오 REST API 키
  naver_client_id: 네이버 클라이언트 ID
  naver_client_secret: 네이버 클라이언트 시크릿
  aws_s3_access_key: AWS S3 액세스 키
  aws_s3_secret_key: AWS S3 시크릿 키
  rabbitmq_host: RabbitMQ 호스트
  rabbitmq_port: RabbitMQ 포트
  rabbitmq_username: RabbitMQ 사용자 이름
  rabbitmq_password: RabbitMQ 비밀번호
  rabbitmq_stomp_port: RabbitMQ STOMP 포트
  redis_host: Redis 호스트
  redis_port: Redis 포트
  redis_username: Redis 사용자 이름
  redis_password: Redis 비밀번호
  openvidu_url: OpenVidu URL
  openvidu_secret: OpenVidu 시크릿
  fire_base_admin_json: Firebase Admin SDK JSON
  ```

### 쿠버네티스 시크릿 파일 적용
```shell
kubectl apply -f secret.yml
```