package ink.cwblog.producter.dao;

import ink.cwblog.producter.model.MessageLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @description: 持久化
 * @author: ChenWei
 * @create: 2020/5/13 - 21:18
 **/
@Mapper
public interface MessageLogDAO {
	/**
	 * 消息入库
	 *
	 * @param message 消息实体
	 * @return int
	 */
	int insertMessageLog(MessageLog message);

	/**
	 * 更新消息状态
	 *
	 * @param messageLog (status - 消息状态)
	 * @return
	 */
	int updateMessageLogStatus (MessageLog messageLog);

	/**
	 * 查询需要重试的消息
	 *
	 * @param params (status - 状态, retryCount - 重试次数，limit - 条数)
	 * @return
	 */
	List<MessageLog> queryRetryMessage(Map<String,Object> params);

	/**
	 * 更新消息重试次数
	 *
	 * @param messageLog (retryCount - 重试次数)
	 * @return
	 */
	int updateMessageLogRetryCount (MessageLog messageLog);
}
