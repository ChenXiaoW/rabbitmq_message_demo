package ink.cwblog.consumer.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description: 响应模型
 * @author: ChenWei
 * @create: 2020/5/13 - 20:41
 **/
@Data
@Accessors(chain = true)
public class BaseModel {
	/**
	 * 响应结果
	 */
	private String result;
	/**
	 * 响应信息
	 */
	private String message;
	/**
	 * 响应数据
	 */
	private Object data;
	/**
	 * 响应码
	 */
	private Integer code;

}
