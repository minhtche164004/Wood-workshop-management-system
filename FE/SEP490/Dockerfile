# Dockerfile
FROM node:19.7.0-alpine3.16 as builder
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build --prod

FROM nginx:1.23.3-alpine
COPY --from=builder /app/dist/sep490 /usr/share/nginx/html
COPY nginx/static.conf /etc/nginx/conf.d/default.conf
