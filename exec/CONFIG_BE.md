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

### Openvidu 설정 적용
```shell
# OpenVidu configuration
# ----------------------
# 도메인 또는 퍼블릭IP 주소
DOMAIN_OR_PUBLIC_IP=

# 오픈비두 서버와 통신을 위한 시크릿 키 
OPENVIDU_SECRET=MY_SECRET

# Certificate type
# ----------------
# SSL 인증 방식(selfsigned, owncert, letsencrypt)
CERTIFICATE_TYPE=owncert

# HTTP port
HTTP_PORT=8442

# HTTPS port(해당 포트를 통해 오픈비두 서버와 연결)
HTTPS_PORT=8443


```





### 리버스 프록시 설정 적용
```shell
server {
				# HTTPS 설정
        listen 443 ssl default_server;
        listen [::]:443 ssl default_server;

				# 본인이 인증받은 CA 기관의 키 경로
        ssl_certificate /etc/ssl/full_chain.crt;
        ssl_certificate_key /etc/ssl/private.key;

				# 서버 도메인 OR IP
        server_name honjarang.kro.kr;

				# 프론트엔드
        location / {
                 proxy_pass http://honjarang.kro.kr:3000;
                 proxy_set_header Host $host;
                 proxy_set_header X-Real-IP $remote_addr;
                 proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        }

				# 채팅
        location /chatting {
                proxy_pass http://honjarang.kro.kr:3000/chatting;
                proxy_set_header Host $host;
                proxy_set_header X-Real-IP $remote_addr;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        }
				
				# 백엔드 
        location /api {
                proxy_set_header Host $host;
                proxy_set_header X-Real-IP $remote_addr;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_pass http://honjarang.kro.kr:30000/api;
        }
				
				# Spring REST Docs
				location /docs {
                proxy_set_header Host $host;
                proxy_set_header X-Real-IP $remote_addr;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_pass http://honjarang.kro.kr:30000/docs/index.html;
        }
				
				# 웹소켓  
        location /chat {
                proxy_pass http://honjarang.kro.kr:30000/chat;
                proxy_http_version 1.1;
                proxy_set_header Upgrade $http_upgrade;
                proxy_set_header Connection "Upgrade";
                proxy_set_header Host $host;
                proxy_set_header Origin "";
        }

    }

    server {
				# HTTP 설정
        listen 80;
					
				# 서버 도메인 OR IP 
        server_name honjarang.kro.kr;

				# HTTP로 진입 시 HTTPS로 리다이렉트
        location / {
            return 301 https://$server_name$request_uri;
        }


    }

```
