import os

def check_config():
    print("=== Checking Configuration ===")
    # RabbitMQ Configuration
    print("RabbitMQ Host:", os.getenv("RABBITMQ_HOST", "Not Set"))
    print("RabbitMQ Port:", os.getenv("RABBITMQ_PORT", "Not Set"))
    print("RabbitMQ Username:", os.getenv("RABBITMQ_USERNAME", "Not Set"))
    print("RabbitMQ Password:", os.getenv("RABBITMQ_PASSWORD", "Not Set"))

    # MinIO Configuration
    print("MinIO Endpoint:", os.getenv("MINIO_ENDPOINT", "Not Set"))
    print("MinIO Access Key:", os.getenv("MINIO_ACCESS_KEY", "Not Set"))
    print("MinIO Secret Key:", os.getenv("MINIO_SECRET_KEY", "Not Set"))
    print("MinIO Bucket:", os.getenv("MINIO_BUCKET", "Not Set"))
    print("=== Configuration Check Complete ===")

if __name__ == "__main__":
    check_config()
