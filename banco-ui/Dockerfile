FROM node:22-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM nginx:alpine

# Copiar SOLO el output real del proyecto (dist/banco-ui) al root de nginx
COPY --from=build /app/dist/banco-ui/ /usr/share/nginx/html/

# SPA fallback
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
