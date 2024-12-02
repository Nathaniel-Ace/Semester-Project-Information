# Basisimage verwenden
FROM python:3.9-slim

# Arbeitsverzeichnis im Container erstellen und setzen
WORKDIR /app

# Systemabhängigkeiten installieren (einschließlich Poppler)
RUN apt-get update && apt-get install -y tesseract-ocr poppler-utils && apt-get clean

# Anforderungen und Anwendung in das Arbeitsverzeichnis kopieren
COPY requirements.txt .
COPY app/ ./app

# Abhängigkeiten installieren
RUN pip install --no-cache-dir -r requirements.txt

# Arbeitsverzeichnis wechseln
WORKDIR /app

# Command zum Starten von queue_listener.py
CMD ["python", "app/queue_listener.py"]
