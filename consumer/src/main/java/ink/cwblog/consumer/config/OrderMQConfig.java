package ink.cwblog.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 订单队列配置
 * @author: ChenWei
 * @create: 2020/5/15 - 22:11
 **/

@Configuration
public class OrderMQConfig {
	/**
	 * 订单交换机
	 */
	private String orderExchange = "OrderExchange";

	/**
	 * 订单队列
	 */
	private String orderQueue = "order.mail";

	/**
	 * 订单routinekey
	 */
	private String orderRoutingKey = "order.mail.key";

	/**
	 * 死信交换机
	 */
	private String orderDlxExchange = "Dlx_Exchange";
	/**
	 * 死信队列
	 */
	private String orderDlxQueue = "Dlx_orderQueue";

	/**
	 * 死信 路由
	 */
	private String orderDlxRoutingKey = "dlx.order.key";

	/**
	 * 订单交换机声明 = 生产端投递的交换机
	 *
	 * @return
	 */
	@Bean
	DirectExchange orderExchange() {
		return new DirectExchange(orderExchange, true, false);
	}

	/**
	 * 订单队列声明 并绑定死信交换机
	 *
	 * @return
	 */
	@Bean
	Queue orderQueue() {
		//return new Queue(orderQueue,true,false,false);
		Map<String, Object> arguments = new HashMap<>(2);
		arguments.put("x-dead-letter-exchange", orderDlxExchange);
		arguments.put("x-dead-letter-routing-key", orderDlxRoutingKey);
		return QueueBuilder.durable(orderQueue).withArguments(arguments).build();
	}

	/**
	 * 交换机 - 队列 通过路由键绑定
	 *
	 * @return
	 */
	@Bean
	Binding bindingOrder() {
		return BindingBuilder.bind(orderQueue()).to(orderExchange()).with(orderRoutingKey);
	}

	/**
	 * 声明死信交换机
	 *
	 * @return
	 */
	@Bean
	DirectExchange orderDlxExchange() {
		return new DirectExchange(orderDlxExchange, true, false);
	}

	/**
	 * 声明死信队列
	 *
	 * @return
	 */
	@Bean
	Queue orderDlxQueue() {
		return new Queue(orderDlxQueue, true, false, false);
	}

	/**
	 * 通过路由键建立死信队列与死信交换机的绑定关系
	 *
	 * @return
	 */
	@Bean
	Binding bindingDlxOrder() {
		return BindingBuilder.bind(orderDlxQueue()).to(orderDlxExchange()).with(orderDlxRoutingKey);
	}


}
