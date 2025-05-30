# Étape 1 : builder l'app Angular
FROM node:20-alpine as build
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build --prod

# Étape 2 : servir avec Nginx
FROM nginx:alpine
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
