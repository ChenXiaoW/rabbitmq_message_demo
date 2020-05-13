package ink.cwblog.producter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("ink.cwblog.producter.dao")
@SpringBootApplication
public class ProducterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProducterApplication.class, args);
	}

}
