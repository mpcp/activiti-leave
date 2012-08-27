/**
 * 文件名：WorkflowUtils.java
 *
 * 版本信息：
 * 日期：2012-8-27
 * Copyright 足下 Corporation 2012 
 * 版权所有
 *
 */
package org.richie144.leave.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 项目名称：activiti-leave
 * 类名称：WorkflowUtils
 * 类描述：流程辅助类
 * 创建人：任欢
 * 创建时间：2012-8-27 下午5:07:52
 * 修改人：任欢
 * 修改时间：2012-8-27 下午5:07:52
 * 修改备注：
 * @version 
 * 
 */
public class WorkflowUtils {
	
	/**
	 * 
	 * parseToZhType(转换流程节点类型为中文说明)
	 * @param   type 英文名称 
	 * @return  转换后中文名称
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public static String parseToZhType(String type) {
		Map<String, String> types = new HashMap<String, String>();
		types.put("userTask", "用户任务");
		types.put("serviceTask", "系统任务");
		types.put("startEvent", "开始节点");
		types.put("endEvent", "结束节点");
		types.put("exclusiveGateway", "条件判断节点(系统自动根据条件处理)");
		types.put("inclusiveGateway", "并行处理任务");
		types.put("callActivity", "子流程");
		String handled = types.get(type);
		return handled == null ? type :handled;
	}
}
