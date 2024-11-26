FROM python:3.10-slim

# Installiere Tesseract OCR
RUN apt-get update && apt-get install -y tesseract-ocr libtesseract-dev

# Arbeitsverzeichnis
WORKDIR /app

# Abhängigkeiten installieren
COPY requirements.txt .
RUN pip install -r requirements.txt

# Anwendung kopieren
COPY . .

# Flask-Server starten
CMD ["python", "ocr_service.py"]
