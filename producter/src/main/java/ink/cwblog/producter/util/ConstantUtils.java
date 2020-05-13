package ink.cwblog.producter.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 *
 *
 * @description: 常用工具类
 * @author: ChenWei
 * @create: 2020/5/13 - 21:20
 **/
public class ConstantUtils {
	/**
	 * UUID获取
	 * @return
	 */
	public static String getUUID(){
		String uuid = UUID.randomUUID().toString();
		return uuid.replace("-","");
	}
}
