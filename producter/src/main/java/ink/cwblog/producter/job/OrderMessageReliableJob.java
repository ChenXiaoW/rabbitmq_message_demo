package ink.cwblog.producter.job;

import ink.cwblog.producter.dao.MessageLogDAO;
import ink.cwblog.producter.model.MessageLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 消息重试定时任务
 * @author: ChenWei
 * @create: 2020/5/13 - 22:43
 **/
@Slf4j
@Component
@EnableScheduling
public class OrderMessageReliableJob {

	@Autowired
	MessageLogDAO messageLogDAO;

	@Resource(name = "templateReliable")
	RabbitTemplate rabbitTemplate;

	/**
	 * 消息重试定时任务
	 */
	@Scheduled(cron = "10/10 * * * * ?")
	public void reliableJob() {
		Map<String, Object> params = new HashMap<>(4);
		//参数说明：获取状态为2：投递失败，并且retryCount次数小于3，每次获取10条记录
		params.put("status", 2);
		params.put("retryCount", 3);
		params.put("limit", 10);
		List<MessageLog> messageLogs = messageLogDAO.queryRetryMessage(params);
		if (CollectionUtils.isEmpty(messageLogs)) {
			return;
		}
		try {//循环遍历消息，并且重新发送
			for (MessageLog messageLog : messageLogs) {
				messageLog.setRetryCount(messageLog.getRetryCount() + 1);
				//todo mq重试时间策略，每次重试时间不同
				int updateStatus = messageLogDAO.updateMessageLogRetryCount(messageLog);
				if (updateStatus == 0) {
					continue;
				}
				rabbitTemplate.convertAndSend(messageLog.getExchange(), messageLog.getRoutingKey(), messageLog.getMessage(), new CorrelationData(messageLog.getMessageId()));
			}
		} catch (Exception e) {
			log.error("扫描作业异常：{}", e);
		}
	}
}
