# Verwende das Nginx-Image als Basis
FROM nginx:alpine

# Kopiere die Nginx-Konfiguration in den Container
COPY ./nginx/nginx.conf /etc/nginx/nginx.conf

# Kopiere den Build-Output der React-App in das Verzeichnis, das Nginx bedient
COPY ./build/ /usr/share/nginx/html

# Exponiere den Port 81
EXPOSE 81
