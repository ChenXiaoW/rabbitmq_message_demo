package ink.cwblog.consumer.dao;

import ink.cwblog.consumer.model.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description: 持久化数据
 * @author: ChenWei
 * @create: 2020/5/16 - 15:29
 **/

@Mapper
public interface OrderDAO {
	/**
	 * 添加订单
	 *
	 * @param order
	 * @return
	 */
	int insertOrder(Order order);
}
