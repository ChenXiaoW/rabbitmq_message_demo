# rabbitmq_message_demo
rabbitmq 消息100%可靠性投递

- producter 作为消息生产者
<br> 开启return机制
<br> 开启confirm消息确认
- consumer 作为消息消费者
<br> 开启手动ack
<br> 加入死信队列
<br> 加入redis防止重复消费
