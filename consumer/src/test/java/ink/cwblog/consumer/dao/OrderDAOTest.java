package ink.cwblog.consumer.dao;

import ink.cwblog.consumer.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderDAOTest {

	@Autowired
	OrderDAO orderDAO;

	@Test
	void insertOrder() {
		Order order = new Order();
		order.setOid("12");
		order.setOrderInfo("fsdfsd");
		int i = orderDAO.insertOrder(order);
		System.out.println("[打印]>>>" + i);

	}
}