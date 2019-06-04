import sys
import boto3
from time import sleep

def get_queue_by_name(name):
    queue_initialized = False
    queue = None
    while not queue_initialized:
        try:
            queue = sqs.get_queue_by_name(QueueName=name)
            queue_initialized = True
        except:
            print("Queue are still not initialized. Waiting...")
            sleep(1)
    return queue

my_queue_name = sys.argv[1]
queue_to_send_name = sys.argv[2]
sleep(15) # wait for queues to become initialized
sqs = boto3.resource('sqs', endpoint_url='http://localstack:4576/', aws_access_key_id="access", aws_secret_access_key="secret")
my_queue = get_queue_by_name(my_queue_name)
queue_to_send = get_queue_by_name(queue_to_send_name)
while True:
    for message in my_queue.receive_messages():
        new_num = str(int(message.body) + 1)
        print("Queue " + my_queue_name + " received " + message.body, flush=True)
        message.delete()
        queue_to_send.send_message(MessageBody=(new_num))

