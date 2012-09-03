/**
 * 文件名：MainController.java
 *
 * 版本信息：
 * 日期：2012-9-3
 * Copyright 足下 Corporation 2012 
 * 版权所有
 *
 */
package org.richie144.leave.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * 项目名称：activiti-leave
 * 类名称：MainController
 * 类描述：首页控制器
 * 创建人：任欢
 * 创建时间：2012-9-3 下午2:00:28
 * 修改人：任欢
 * 修改时间：2012-9-3 下午2:00:28
 * 修改备注：
 * @version 
 * 
 */
@Controller
@RequestMapping("/main")
public class MainController {
	@RequestMapping(value = "/index")
	public String index() {
		return "/main/index";
	}
	
	@RequestMapping(value = "/welcome")
	public String welcome() {
		return "/main/welcome";
	}
}
