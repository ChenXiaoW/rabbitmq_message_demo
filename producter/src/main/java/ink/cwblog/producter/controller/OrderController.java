package ink.cwblog.producter.controller;

import ink.cwblog.producter.model.BaseModel;
import ink.cwblog.producter.model.Order;
import ink.cwblog.producter.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * @description: 订单控制层
 * @author: ChenWei
 * @create: 2020/5/13 - 20:40
 **/
@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	OrderService orderService;

	/**
	 * 推送订单
	 *
	 * @param order 订单实体
	 * @return
	 */
	@PostMapping("/sendOrder")
	BaseModel sendOrder(@RequestBody Order order){
		return orderService.sendOrder(order);
	}
}
