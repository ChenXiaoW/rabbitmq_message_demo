package ink.cwblog.producter.service;

import ink.cwblog.producter.model.BaseModel;
import ink.cwblog.producter.model.Order;

public interface OrderService {
	/**
	 * 推送订单
	 *
	 * @param order
	 * @return
	 */
	BaseModel sendOrder(Order order);
}
