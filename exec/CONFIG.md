### 카카오 REST API 키 발급 및 설정
1. [Kakao Developers](https://developers.kakao.com/) 사이트에 접속하여 로그인 후 내 애플리케이션 메뉴에 가서 애플리케이션 추가하기를 클릭
2. 앱 이름과 회사 이름을 아무거나 입력하고 저장하여 앱을 생성
3. REST API 키 복사

### 네이버 클라이언트 ID 및 시크릿 발급 및 설정
1. [네이버 개발자 센터](https://developers.naver.com/) 사이트에 접속하여 로그인 후 애플리케이션 등록 메뉴에 가서 애플리케이션 등록하기를 클릭
2. 애플리케이션 이름을 아무거나 입력하고 사용 API에 검색 API를 추가하고 저장하여 애플리케이션을 생성
3. 클라이언트 ID와 클라이언트 시크릿 복사

### AWS S3 버킷 생성
1. [AWS IAM](https://console.aws.amazon.com/iam/home) 사이트에 접속하여 로그인 후 액세스 관리 메뉴에 가서 사용자를 클릭
2. 사용자 생성 버튼을 클릭하고 사용자 이름을 입력하고 AWS Management Console 액세스 권한 제공을 체크하고 다음 버튼을 클릭
3. 직접 정책 연결을 클릭하고 정책 필터에 S3를 입력하고 AmazonS3FullAccess를 체크하고 다음 버튼을 클릭
4. 사용자 생성 버튼을 클릭하고 생성된 사용자 정보의 보안 자격 증명 탭에서 액세스 키 만들기 버튼을 클릭
5. 액세스 키 및 모범 사례 및 대안에서 AWS 외부에서 실행되는 애플리케이션 선택후 다음 클릭 후 액세스 키 만들기 버튼 클릭
6. 액세스 키와 비밀 액세스 키 복사
7. [AWS S3](https://s3.console.aws.amazon.com/s3/home) 사이트에 접속하여 로그인 후 버킷 생성을 클릭
8. 버킷 이름에 'honjarang-bucket' 입력하고 리전을 서울로 설정하고 버킷 생성을 클릭
9. 객체 소유권을 ACL 비활성화됨(권장) 선택
10. 이 버킷의 퍼블릭 액세스 차단 설정에서 모든 퍼블릭 액세스 차단 체크
11. 버킷 버전 관리 비활성화 설정
12. 기본암호화는 Amazon S3 관리형 키를 사용한 서버 측 암호화 설정, 버킷키는 활성화
13. 버킷생성 후 권한에 가서 버킷정책에 아래 내용 추가
```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "Stmt1639632673739",
            "Effect": "Allow",
            "Principal": "*",
            "Action": [
                "s3:GetObject",
                "s3:PutObject"
            ],
            "Resource": [
                "arn:aws:s3:::honjarang-bucket",
                "arn:aws:s3:::honjarang-bucket/*"
            ]
        }
    ]
}
```
14. 버킷생성 후 권한에 가서 CORS(Cross-origin 리소스 공유)에 아래 내용 추가
```
[
    {
        "AllowedHeaders": [
            "*"
        ],
        "AllowedMethods": [
            "PUT",
            "POST",
            "DELETE",
            "GET"
        ],
        "AllowedOrigins": [
            "*"
        ],
        "ExposeHeaders": []
    }
]
```

### AWS IAM 계정 생성 및 SES 설정
1. [AWS IAM](https://console.aws.amazon.com/iam/home) 사이트에 접속하여 로그인 후 액세스 관리 메뉴에 가서 사용자를 클릭
2. 사용자 생성 버튼을 클릭하고 사용자 이름을 입력하고 AWS Management Console 액세스 권한 제공을 체크하고 다음 버튼을 클릭
3. 직접 정책 연결을 클릭하고 정책 필터에 SES를 입력하고 AmazonSESFullAccess를 체크하고 다음 버튼을 클릭
4. 사용자 생성 버튼을 클릭하고 생성된 사용자 정보의 보안 자격 증명 탭에서 액세스 키 만들기 버튼을 클릭
5. 액세스 키 및 모범 사례 및 대안에서 AWS 외부에서 실행되는 애플리케이션 선택후 다음 클릭 후 액세스 키 만들기 버튼 클릭
6. 액세스 키와 비밀 액세스 키 복사
7. [AWS SES](https://console.aws.amazon.com/ses/home) 사이트에 접속하여 로그인 후 자격 증명 생성 버튼 클릭
8. 보안 인증 유형으로 이메일 주소를 선택하고 이메일 입력 후 자격 증명 생성 버튼 클릭

### Firebase Cloud Messaging 설정
1. [Firebase](https://console.firebase.google.com/) 사이트에 접속하여 로그인 후 프로젝트 추가 버튼 클릭
2. 프로젝트 이름을 입력하고 프로젝트 만들기 버튼 클릭 후 계속 버튼 클릭
3. Google 애널리틱스 구성에서 Google 애널리틱스 계정을 선택하고 프로젝트 만들기 버튼 클릭
4. 앱에 Firebase를 추가하여 시작하기에서 웹을 선택하고 앱 닉네임에 아무거나 입력 후 앱 등록 버튼 클릭
5. Firebase 프로젝트 설정의 일반 탭에서 firebaseConfig의 내용을 복사
6. Firebase 프로젝트 설정의 서비스 계정 탭에서 새 비공개 키 생성 버튼 클릭
6. 비공개 키 생성 버튼 클릭
7. 다운받은 키 파일 열어서 복사
8. Firebase 프로젝트 설정의 클라우드 메시징 탭에서 웹 푸시 인증서 정보에서 키 페어 생성 후 복사

### .env 파일 구성
- .env 파일은 프론트엔드 프로젝트 최상단에 위치해야 합니다.
```dotenv
VITE_APP_FIREBASE_APIKEY="firebaseConfig의 apiKey"
VITE_APP_FIREBASE_AUTHDOMAIN="firebaseConfig의 authDomain"
VITE_APP_FIREBASE_PROJECTID="firebaseConfig의 projectId"
VITE_APP_FIREBASE_STORAGEBUCKET="firebaseConfig의 storageBucket"
VITE_APP_FIREBASE_MESSAGINGSENDERID="firebaseConfig의 messagingSenderId"
VITE_APP_FIREBASE_APPID="firebaseConfig의 appId"
VITE_APP_FIREBASE_MEASUREMENTID="firebaseConfig의 measurementId"
VITE_APP_FIREBASE_VAPIDKEY="키 페어 생성 후 복사한 값"
VITE_APP_TOSS_CLIENTKEY="토스 결제 클라이언트 키"
VITE_APP_TOSS_CUSTOMERKEY="토스 결제 고객 키"
VITE_APP_STOMP_CLIENTID="RabbitMQ STOMP 클라이언트 ID"
VITE_APP_STOMP_PASSWORD="RabbitMQ STOMP 비밀번호"
```

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
DOMAIN_OR_PUBLIC_IP=도메인주소

# 오픈비두 서버와 통신을 위한 시크릿 키 
OPENVIDU_SECRET=시크릿  키

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
