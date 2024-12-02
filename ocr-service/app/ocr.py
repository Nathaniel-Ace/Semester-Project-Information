import pytesseract
from pdf2image import convert_from_path
import pika
import json
from config import Config

def send_result_to_queue(document_id, text):
    """
    Sendet das OCR-Ergebnis an die RESULT_QUEUE.
    """
    try:
        print(f"Sending OCR result for document ID {document_id} to RESULT_QUEUE...")

        # Verbindung zu RabbitMQ herstellen
        credentials = pika.PlainCredentials(Config.RABBITMQ_USERNAME, Config.RABBITMQ_PASSWORD)
        connection = pika.BlockingConnection(
            pika.ConnectionParameters(
                host=Config.RABBITMQ_HOST,
                port=Config.RABBITMQ_PORT,
                credentials=credentials
            )
        )
        channel = connection.channel()

        # Queue deklarieren
        channel.queue_declare(queue=Config.RESULT_QUEUE, durable=True)

        # Nachricht vorbereiten
        message = {
            "documentId": document_id,
            "ocrText": text
        }

        # Debug: Nachricht ausgeben
        print("Sending OCR result to RESULT_QUEUE:", json.dumps(message, indent=4))

        # Nachricht senden
        channel.basic_publish(
            exchange='',
            routing_key=Config.RESULT_QUEUE,
            body=json.dumps(message),
            properties=pika.BasicProperties(delivery_mode=2)  # Persistent message
        )

        print(f"OCR result for document ID {document_id} sent successfully.")
        connection.close()
    except Exception as e:
        print(f"Failed to send OCR result: {e}")

def process_ocr(pdf_path, page_count=None, document_id=None):
    """
    Führt OCR auf der angegebenen PDF-Datei durch.

    :param pdf_path: Pfad zur PDF-Datei
    :param page_count: Anzahl der Seiten (optional, falls aus dem JSON übergeben)
    :param document_id: ID des Dokuments (optional, für die RESULT_QUEUE)
    """
    try:
        print(f"Starting OCR process for file: {pdf_path}")

        # Konvertiere PDF zu Bildern
        images = convert_from_path(pdf_path)

        if page_count:
            print(f"Expected page count: {page_count}, Actual pages: {len(images)}")

        # OCR für jede Seite durchführen
        full_text = ""
        for i, image in enumerate(images):
            text = pytesseract.image_to_string(image)
            print(f"Scanned text for page {i + 1}:\n{text}\n")
            full_text += text + "\n"

        print(f"OCR process completed successfully for file: {pdf_path}")

        # Sende das Ergebnis an die RESULT_QUEUE
        if document_id:
            send_result_to_queue(document_id, full_text)

    except Exception as e:
        print(f"Error during OCR processing: {e}")
