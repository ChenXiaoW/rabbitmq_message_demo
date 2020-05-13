package ink.cwblog.producter.model;

import lombok.Data;

/**
 *
 *
 * @description: 订单实体类
 * @author: ChenWei
 * @create: 2020/5/13 - 20:44
 **/
@Data
public class Order {
	/**
	 * 订单ID
	 */
	private String oid;
	/**
	 * 订单信息
	 */
	private String orderInfo;

}
