package ink.cwblog.producter.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 *
 *
 * @description: RabbitTemplate 自定义配置，主配置
 * @author: ChenWei
 * @create: 2020/5/13 - 20:53
 **/

@Slf4j
@Configuration
public class TemplateConfig {

	@Primary //标记为主配置
	@Bean
	public RabbitTemplate createRabbitTempalte(ConnectionFactory connectionFactory){
		RabbitTemplate rabbitTemplate = new RabbitTemplate();
		rabbitTemplate.setConnectionFactory(connectionFactory);
		//设置开启mandatory,才能触发回调函数
		rabbitTemplate.setMandatory(true);
		/**
		 * 设置消息确认回调
		 */
		rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				if (ack){
					log.info("消息成功发送到Exchange，相关数据：{}，原因：{}", JSON.toJSONString(correlationData),cause);
				}else {
					log.debug("消息发送到Exchange失败，相关数据：{}，原因：{}", JSON.toJSONString(correlationData),cause);
				}
			}
		});


		/**
		 * 设置 消息 return 回调
		 */
		rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
			@Override
			public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
				log.debug("消息路由到队列失败,当前消息：{}，回应码：{}，回应信息：{}，交换机：{}，路由Key：{}",
						JSON.toJSONString(message),replyCode,replyText,exchange,routingKey);
			}
		});
		return rabbitTemplate;
	}

}
