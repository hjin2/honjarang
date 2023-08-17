# Node.js 이미지를 사용합니다. (여기서는 LTS 버전을 사용합니다)
FROM node:lts-alpine

# 작업 디렉토리를 /app으로 설정합니다.
WORKDIR /app

# 호스트의 package.json과 package-lock.json (또는 pnpm-lock.yaml) 파일을 이미지의 /app 디렉토리로 복사합니다.
# (package.json과 package-lock.json 파일이 있는 경로에 맞게 수정해주세요)
COPY package*.json ./

# 의존성을 설치합니다. (pnpm을 사용하여 설치합니다)
RUN npm install -g pnpm
RUN pnpm install

# 호스트의 모든 파일들을 이미지의 /app 디렉토리로 복사합니다.
# (이미 프로젝트 빌드를 한 경우에는 필요하지 않습니다)
COPY . .

# Vite를 사용하여 React 앱을 빌드합니다.
RUN pnpm run build

RUN babel src --out-dir dist

# 빌드된 정적 파일을 호스팅하기 위해 간단한 웹 서버를 사용합니다.
# (여기서는 serve 패키지를 사용하여 정적 파일을 제공합니다)

# 포트 80에서 웹 서버를 실행합니다.
EXPOSE 3000

# 웹 서버를 실행합니다.
CMD ["pnpm", "preview"]
