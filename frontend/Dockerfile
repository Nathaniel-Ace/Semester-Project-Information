# Stufe 1: Verwende Node, um das React-Frontend zu bauen
FROM node:16 AS build

WORKDIR /app

# Kopiere package.json und package-lock.json, um alle Abhängigkeiten zu installieren
COPY package.json package-lock.json ./

# Installiere alle Abhängigkeiten, einschließlich react-scripts
RUN npm install

# Kopiere den gesamten Code und baue das Frontend
COPY . .
RUN npm run build

# Stufe 2: Verwende das Nginx-Image, um die statischen Dateien zu servieren
FROM nginx:alpine

# Kopiere die Nginx-Konfiguration in den Container
COPY ./nginx/nginx.conf /etc/nginx/nginx.conf

# Kopiere den gebauten React-App-Output aus der ersten Stufe nach Nginx
COPY --from=build /app/build /usr/share/nginx/html

# Exponiere den Port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
