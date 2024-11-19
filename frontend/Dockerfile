# Build-Phase
FROM node:18 AS build
WORKDIR /app

# Kopiere nur die notwendigen Dateien, um das Docker-Image effizienter zu machen
COPY package.json package-lock.json ./
RUN npm install

# Kopiere den restlichen Code
COPY . .

# Baue das Projekt
RUN npm run build

# Nginx-Phase
FROM nginx:alpine

# Kopiere die gebauten Dateien aus der Build-Phase
COPY --from=build /app/build /usr/share/nginx/html

# Exponiere Port 80
EXPOSE 80

# Starte Nginx im Vordergrund
CMD ["nginx", "-g", "daemon off;"]