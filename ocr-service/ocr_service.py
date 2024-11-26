from flask import Flask, request, jsonify
import pytesseract
from PIL import Image
from minio import Minio
from elasticsearch import Elasticsearch
import pika
import io
import threading

# Flask-App erstellen
app = Flask(__name__)

# MinIO-Client
minio_client = Minio(
    "minio:9000",
    access_key="minioadmin",
    secret_key="minioadmin",
    secure=False
)

# Elasticsearch-Client
es_client = Elasticsearch(["http://elasticsearch:9200"])

# RabbitMQ-Verbindung
rabbitmq_connection = pika.BlockingConnection(pika.ConnectionParameters(host="rabbitmq"))
rabbitmq_channel = rabbitmq_connection.channel()
rabbitmq_channel.queue_declare(queue="ocr_task_queue")
rabbitmq_channel.queue_declare(queue="ocr_result_queue")


def perform_ocr(image_data):
    """Führt OCR auf einer Bilddatei durch."""
    image = Image.open(io.BytesIO(image_data))
    return pytesseract.image_to_string(image)


def process_rabbitmq_message(ch, method, properties, body):
    """Verarbeitet Nachrichten aus RabbitMQ."""
    data = body.decode()  # MinIO-Dateipfad
    print(f"Nachricht empfangen: {data}")

    try:
        bucket, file_path = data.split("|")
        # Datei von MinIO abrufen
        response = minio_client.get_object(bucket, file_path)
        image_data = response.read()

        # OCR ausführen
        ocr_result = perform_ocr(image_data)

        # Ergebnis in Elasticsearch speichern
        es_client.index(index="documents", body={"content": ocr_result})
        print("OCR-Ergebnis in Elasticsearch gespeichert.")

        # Ergebnis an RabbitMQ zurücksenden
        rabbitmq_channel.basic_publish(exchange="", routing_key="ocr_result_queue", body=ocr_result)
        print("OCR-Ergebnis an RabbitMQ gesendet.")
    except Exception as e:
        print(f"Fehler bei der Verarbeitung: {e}")


def start_rabbitmq_consumer():
    """Startet den RabbitMQ-Consumer in einem separaten Thread."""
    print("RabbitMQ-Consumer gestartet...")
    rabbitmq_channel.basic_consume(queue="ocr_task_queue", on_message_callback=process_rabbitmq_message, auto_ack=True)
    rabbitmq_channel.start_consuming()


@app.route("/ocr", methods=["POST"])
def ocr_endpoint():
    """HTTP-Endpoint für OCR."""
    data = request.json
    bucket = data.get("bucket")
    file_path = data.get("file_path")

    try:
        # Datei von MinIO abrufen
        response = minio_client.get_object(bucket, file_path)
        image_data = response.read()

        # OCR ausführen
        ocr_result = perform_ocr(image_data)

        # Ergebnis in Elasticsearch speichern
        es_client.index(index="documents", body={"content": ocr_result})
        print("OCR-Ergebnis in Elasticsearch gespeichert.")

        return jsonify({"success": True, "text": ocr_result}), 200
    except Exception as e:
        return jsonify({"success": False, "error": str(e)}), 500


if __name__ == "__main__":
    # RabbitMQ-Consumer in separatem Thread starten
    threading.Thread(target=start_rabbitmq_consumer, daemon=True).start()

    # Flask-App starten
    app.run(host="0.0.0.0", port=8082)
