import boto3
from time import sleep

def create_queue(name):
    queue_created = False
    while not queue_created:
        try:
            queue = sqs.create_queue(QueueName=name, Attributes={'DelaySeconds': '3'})
            queue_created = True
            print("Queue " + name + " created successfully: " + queue.url)
        except:
            print("LocalStack haven't started yet. Waiting...")
            sleep(1)

def send_message(name_to, message):
    queue = sqs.get_queue_by_name(QueueName=name_to)
    print("Start sending initial message")
    response = queue.send_message(MessageBody=(message))
    print("Initial message was sent. Message ID: " + str(response['MessageId']))


sleep(12) # wait for localstack to be ready
sqs = boto3.resource('sqs', endpoint_url='http://localstack:4576/', aws_access_key_id="access", aws_secret_access_key="secret")
create_queue("A")
create_queue("B")
send_message("B", "1")
