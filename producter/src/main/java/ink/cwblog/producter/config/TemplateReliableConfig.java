package ink.cwblog.producter.config;

import com.alibaba.fastjson.JSON;
import ink.cwblog.producter.dao.MessageLogDAO;
import ink.cwblog.producter.model.MessageLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SerializerMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * @description: 自定义rabbitTemplate
 * @author: ChenWei
 * @create: 2020/5/14 - 23:28
 **/

@Slf4j
@Configuration
public class TemplateReliableConfig {

	@Autowired
	MessageLogDAO messageLogDAO;


	@Bean(name = "templateReliable")
	public RabbitTemplate createRabbitTempalte(ConnectionFactory connectionFactory){
		RabbitTemplate rabbitTemplate = new RabbitTemplate();
		rabbitTemplate.setConnectionFactory(connectionFactory);
		//设置开启mandatory,才能触发回调函数
		rabbitTemplate.setMandatory(true);
		rabbitTemplate.setMessageConverter(new SerializerMessageConverter());

		/**
		 * 设置消息确认回调
		 */
		rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				MessageLog messageLog = null;
				if (ack){
					//log.info("消息成功发送到Exchange，相关数据：{}，原因：{}", JSON.toJSONString(correlationData),cause);
					//更新消息状态为已投递
					messageLog = new MessageLog()
							.setStatus(1)
							.setMessageId(correlationData.getId())
							.setUpdateTime(new Date());
				}else {
					log.error("消息发送到Exchange失败，相关数据：{}，原因：{}", JSON.toJSONString(correlationData), cause);
					// 更新消息状态为投递失败
					messageLog = new MessageLog()
							.setStatus(2)
							.setMessageId(correlationData.getId())
							.setUpdateTime(new Date());

				}
				messageLogDAO.updateMessageLogStatus(messageLog);
			}
		});


		/**
		 * 设置 消息 return 回调
		 */
		rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
			@Override
			public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
				log.error("消息路由到队列失败,当前消息：{}，回应码：{}，回应信息：{}，交换机：{}，路由Key：{}",
						JSON.toJSONString(message),replyCode,replyText,exchange,routingKey);
				//更新消息库状态
				MessageLog messageLog = new MessageLog()
						.setStatus(2)
						.setMessageId(message.getMessageProperties().getCorrelationId())
						.setUpdateTime(new Date());
				messageLogDAO.updateMessageLogStatus(messageLog);
			}
		});
		return rabbitTemplate;
	}
}
