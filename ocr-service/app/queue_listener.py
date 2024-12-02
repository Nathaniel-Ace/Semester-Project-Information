import json
import time
import os
import boto3
from botocore.exceptions import NoCredentialsError, PartialCredentialsError
import pika
from config import Config
from ocr import process_ocr


def download_pdf_from_minio(file_url, bucket_name):
    """
    Lädt eine Datei von MinIO basierend auf der übergebenen URL herunter.
    """
    try:
        # MinIO Client initialisieren
        s3_client = boto3.client(
            "s3",
            endpoint_url=Config.MINIO_ENDPOINT,  # MinIO-Endpoint
            aws_access_key_id=Config.MINIO_ACCESS_KEY,  # Access Key
            aws_secret_access_key=Config.MINIO_SECRET_KEY,  # Secret Key
        )

        # Extrahiere den Dateinamen aus der URL
        file_name = file_url.split("/")[-1]

        # Lokaler Speicherpfad
        local_path = os.path.join("/tmp", file_name)

        # Datei herunterladen
        s3_client.download_file(bucket_name, file_name, local_path)

        print(f"File downloaded successfully: {local_path}")
        return local_path

    except NoCredentialsError as e:
        print(f"Credentials not available: {e}")
    except PartialCredentialsError as e:
        print(f"Incomplete credentials provided: {e}")
    except Exception as e:
        print(f"Error occurred while downloading the file: {e}")
        return None


class QueueListener:
    def __init__(self):
        self.connection = None
        self.channel = None
        self.connect()

    def connect(self):
        """
        Verbindet sich mit RabbitMQ und erstellt den Channel.
        """
        print("Initializing Queue Listener...")
        try:
            # RabbitMQ-Verbindung herstellen
            credentials = pika.PlainCredentials(Config.RABBITMQ_USERNAME, Config.RABBITMQ_PASSWORD)
            self.connection = pika.BlockingConnection(
                pika.ConnectionParameters(
                    host=Config.RABBITMQ_HOST,
                    port=Config.RABBITMQ_PORT,
                    credentials=credentials,
                )
            )
            self.channel = self.connection.channel()
            print(f"Successfully connected to RabbitMQ at {Config.RABBITMQ_HOST}:{Config.RABBITMQ_PORT}")

            # Queue deklarieren (sicherstellen, dass sie existiert)
            print(f"Declaring queue: {Config.OCR_QUEUE}")
            self.channel.queue_declare(queue=Config.OCR_QUEUE, durable=True)
            print(f"Listening on queue: {Config.OCR_QUEUE}")
        except pika.exceptions.AMQPConnectionError as conn_err:
            print(f"Connection error: {conn_err}")
            time.sleep(5)  # Warte 5 Sekunden und versuche erneut
            self.connect()
        except Exception as e:
            print(f"Failed to initialize RabbitMQ connection: {e}")
            raise

    def callback(self, ch, method, properties, body):
        """
        Callback-Funktion für den Empfang von Nachrichten aus RabbitMQ.
        """
        try:
            print("Received message:")
            print(body.decode())
            message = json.loads(body)

            print("Processed JSON message:")
            print(json.dumps(message, indent=4))

            # Datei-Download aus MinIO
            file_url = message["fileUrl"]
            bucket_name = Config.MINIO_BUCKET  # MinIO-Bucket-Name
            local_file_path = download_pdf_from_minio(file_url, bucket_name)

            if local_file_path:
                print(f"File ready for OCR process: {local_file_path}")
                # OCR-Prozess starten und zusätzliche Felder weitergeben
                document_id = message.get("documentId")
                page_count = message.get("pageCount", None)
                process_ocr(local_file_path, page_count, document_id)

            print("Message acknowledged.")
        except json.JSONDecodeError as json_err:
            print(f"Failed to decode JSON message: {json_err}")
        except KeyError as key_err:
            print(f"Missing required field in JSON: {key_err}")
        except Exception as e:
            print(f"Error processing message: {e}")

    def start(self):
        """
        Startet den Listener für die RabbitMQ-Warteschlange.
        """
        while True:
            try:
                print("Starting to consume messages...")
                self.channel.basic_consume(
                    queue=Config.OCR_QUEUE,
                    on_message_callback=self.callback,
                    auto_ack=True,
                )
                self.channel.start_consuming()
            except pika.exceptions.AMQPConnectionError as conn_err:
                print(f"Connection lost: {conn_err}, reconnecting...")
                self.connect()
            except KeyboardInterrupt:
                print("Stopping Queue Listener...")
                if self.connection:
                    self.connection.close()
                break
            except Exception as e:
                print(f"Unexpected error while consuming messages: {e}")
                time.sleep(5)  # Warte 5 Sekunden vor einem erneuten Versuch


if __name__ == "__main__":
    try:
        print("Starting QueueListener...")
        listener = QueueListener()
        listener.start()
    except Exception as e:
        print(f"Queue Listener failed: {e}")
