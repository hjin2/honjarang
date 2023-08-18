## Getting Started / 어떻게 시작하나요?

아래의 명령어로 서버를 시작할 수 있습니다.

- 프론트엔드 서버 실행
  
  ```shell
  pnpm dev
  ```

```
- 백엔드 서버 실행
```shell
kubectl apply -f honjarang.yml
```

- 리버스 프록시 실행
  
  ```shell
  docker run --name some-nginx -d -p 80:80 443:443 some-content-nginx
  ```

- opevidu 실행
  
  ```shell
  ./openvidu start
  ```

### Prerequisites / 선행 조건

- 아래의 내용을 참고하여 설치 및 구성이 완료되어 있어야 합니다.

[백엔드 서버 설치](INSTALL_BE.md)

[프론트엔드 서버 설치](INSTALL_FE.md)

[서버 구성](CONFIG.md)
