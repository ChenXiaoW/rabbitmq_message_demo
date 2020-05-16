package ink.cwblog.consumer.service;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import ink.cwblog.consumer.dao.MessageLogDAO;
import ink.cwblog.consumer.dao.OrderDAO;
import ink.cwblog.consumer.model.MessageLog;
import ink.cwblog.consumer.model.Order;
import ink.cwblog.consumer.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;


import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


import java.io.IOException;

/**
 * @description: 下游消费订单信息
 * @author: ChenWei
 * @create: 2020/5/14 - 23:45
 **/
@Slf4j
@Service
public class ConsumerService {

	@Autowired
	OrderDAO orderDAO;

	@Autowired
	RedisUtil redisUtil;

	@Autowired
	MessageLogDAO messageLogDAO;

	/**
	 * 消费订单消息
	 */
	@RabbitListener(queues = "order.mail")
	@Transactional(rollbackFor = Exception.class)
	public void consumerOrderMsg(Message message, Channel channel) {
		log.info("message：{}", message);

		//通过message唯一ID来防止重复消费：为什么需要防止重复消费，消费端没有在有效时间内确认消息已消费
		//1 - 获取message唯一ID
		String messageId = message.getMessageProperties().getMessageId();
		//2 - 通过redis查询是否存在该消息ID，存在则不再消费
		if (messageId != null && !redisUtil.exists(messageId)) {
			long deliveryTag = message.getMessageProperties().getDeliveryTag();
			String msg = new String(message.getBody());
			Order order = JSON.parseObject(msg, Order.class);
			int result = orderDAO.insertOrder(order);
			try {
				if (result == 0) {
					channel.basicNack(deliveryTag, false, false);
					log.error("消费结果：消息签收失败");
				} else {
					channel.basicAck(deliveryTag, false);
					log.info("消费结果：消息签收成功，消息唯一ID：{}", messageId);
					//更改msg状态 - 消费成功
					result = messageLogDAO.updateMessageLogStatus(new MessageLog().setMessageId(messageId).setStatus(3));
					if (result != 0) {
						//3 - 消费完成，在redis 中设置该key，value为true
						redisUtil.set(messageId, true);
					}
				}
			} catch (Exception e) {
				log.error("消费异常：{}", e);
				//回滚事务
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				//更改msg状态 - 消费失败
				result = messageLogDAO.updateMessageLogStatus(new MessageLog().setMessageId(messageId).setStatus(4));
			}
		} else {
			log.error("该消息已经被消费：{}", messageId);
		}
	}
}
