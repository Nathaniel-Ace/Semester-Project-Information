import os

class Config:
    # RabbitMQ Configuration
    RABBITMQ_HOST = os.getenv("RABBITMQ_HOST")
    RABBITMQ_PORT = int(os.getenv("RABBITMQ_PORT", 5672))
    RABBITMQ_USERNAME = os.getenv("RABBITMQ_USERNAME")
    RABBITMQ_PASSWORD = os.getenv("RABBITMQ_PASSWORD")
    OCR_QUEUE = "OCR_QUEUE"
    RESULT_QUEUE = "RESULT_QUEUE"

    # MinIO Configuration
    MINIO_ENDPOINT = os.getenv("MINIO_ENDPOINT")
    MINIO_ACCESS_KEY = os.getenv("MINIO_ACCESS_KEY")
    MINIO_SECRET_KEY = os.getenv("MINIO_SECRET_KEY")
    MINIO_BUCKET = os.getenv("MINIO_BUCKET")

    # OCR Temporary Directory
    TEMP_DIR = os.getenv("TEMP_DIR", "/tmp/ocr_temp")

# Ensure TEMP_DIR exists
os.makedirs(Config.TEMP_DIR, exist_ok=True)
