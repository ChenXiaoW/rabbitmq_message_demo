package ink.cwblog.consumer.service;

import org.springframework.amqp.rabbit.annotation.*;

/**
 * @description: 下游消费订单信息
 * @author: ChenWei
 * @create: 2020/5/14 - 23:45
 **/
@RabbitListener(bindings = @QueueBinding(value = @Queue(value = "", durable = "true", autoDelete = "false"),
		exchange = @Exchange(value = "", durable = "true", type = "direct", autoDelete = "false"), key = ""))
public class ConsumerService {
	/**
	 * 消费订单消息
	 */
	@RabbitHandler
	public void consumerOrderMsg() {

	}
}
