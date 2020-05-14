package ink.cwblog.consumer.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @description: 日志消息实体类
 * @author: ChenWei
 * @create: 2020/5/13 - 21:13
 **/
@Data
@Accessors(chain = true)
public class MessageLog {
	/**
	 * 消息唯一ID
	 */
	private String messageId;
	/**
	 * 消息 - JSON格式
	 */
	private String message;
	/**
	 * 交换机
	 */
	private String exchange;
	/**
	 * 路由键
	 */
	private String routingKey;
	/**
	 * 消息状态: 0投递中 1投递成功 2投递失败 3已消费
	 */
	private Integer status;
	/**
	 * 重试次数
	 */
	private Integer retryCount;
	/**
	 * 下一次重试时间
	 */
	private Date nextTryTime;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;
}
