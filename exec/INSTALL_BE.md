### 도커 설치

아래의 명령어로 필요한 모듈들을 설치합니다.

1. 저장소 설정
   
   ```shell
   sudo apt-get update
   sudo apt-get install \
    ca-certificates \
    curl \
    gnupg \
    lsb-release
   sudo mkdir -p /etc/apt/keyrings
   curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
   echo \
   "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
   ```

2. 도커 엔진 설치
   
   ```shell
   sudo apt-get update
   sudo apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin
   ```

### 쿠버네티스 설치 및 단일 노드 클러스터 구성

- Docker Engine은 필수로 설치된 상태여야 한다.
1. 쿠버네티스 설치
   
   ```shell
   sudo apt-get update
   sudo apt-get install -y apt-transport-https ca-certificates curl
   curl -fsSL https://dl.k8s.io/apt/doc/apt-key.gpg | sudo gpg --dearmor -o /etc/apt/keyrings/kubernetes-archive-keyring.gpg
   echo "deb [signed-by=/etc/apt/keyrings/kubernetes-archive-keyring.gpg] https://apt.kubernetes.io/ kubernetes-xenial main" | sudo tee /etc/apt/sources.list.d/kubernetes.list
   ```

sudo apt-get update
sudo apt-get install -y kubelet kubeadm kubectl
sudo apt-mark hold kubelet kubeadm kubectl

```
2. 클러스터 초기화
```shell
sudo kubeadm init --pod-network-cidr=10.244.0.0/16
```

3. 구성 파일 복사 및 권한 부여
   
   ```shell
   mkdir -p $HOME/.kube
   sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
   sudo chown $(id -u):$(id -g) $HOME/.kube/config
   ```

4. 네트워크 CNI 플러그인 애드온 설치
   
   ```shell
   kubectl apply -f https://docs.projectcalico.org/manifests/calico.yaml
   ```

5. 클러스터 노드 확인
   
   ```shell
   kubectl get nodes
   ```

6. 단일 노드 클러스터로 사용하기 위해 테인트 설정
   
   ```shell
   taint nodes {노드 이름} node-role.kubernetes.io/control-plane:NoSchedule-
   ```

### MySQL 설치

```shell
sudo docker pull mysql
sudo docker run -d --name mysql-container -e MYSQL_ROOT_PASSWORD={비밀번호} -p 3306:3306 mysql
```

### MongoDB 설치

```shell
sudo docker pull mongo
sudo docker run -d --name mongodb-container -p 27017:27017 mongo:latest4
```

### Redis 설치

```shell
sudo docker pull redis
sudo docker run -d --name redis -p 6379:6379 redis
```

### RabbitMQ 설치

```shell
sudo docker pull rabbitmq
sudo docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 -p 61613:61613 rabbitmq:latest
sudo docker exec rabbitmq rabbitmq-plugins enable rabbitmq_management
sudo docker exec rabbitmq rabbitmq-plugins enable rabbitmq_web_stomp
```

### Openvidu 설치

```shell
sudo su
cd /opt
curl https://s3-eu-west-1.amazonaws.com/aws.openvidu.io/install_openvidu_latest.sh | bash
```

### Nginx 설치

```shell
sudo su
docker pull nginx
```