/**
 * 文件名：ProcessEngineTest.java
 *
 * 版本信息：
 * 日期：2012-9-14
 * Copyright 足下 Corporation 2012 
 * 版权所有
 *
 */
package org.richie144.leave.engine;

import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.junit.Test;


/**
 * 
 * 项目名称：activiti-leave
 * 类名称：ProcessEngineTest
 * 类描述：测试流程引擎能否启动
 * 创建人：任欢
 * 创建时间：2012-9-14 下午1:49:28
 * 修改人：任欢
 * 修改时间：2012-9-14 下午1:49:28
 * 修改备注：
 * @version 
 * 
 */
//@ContextConfiguration(locations={"/applicationContext.xml"})
public class ProcessEngineTest {
	@Resource
	private RepositoryService repositoryService;
	
	@Resource
	private RuntimeService runtimeService;
	
	@Resource
	private FormService formService;
	
	@Resource
	private IdentityService identityService;
	
	@Resource
	private TaskService taskService;
	
	@Resource
	private HistoryService historyService;
	
	@Resource
	private ManagementService managementService;
	
	@Test
	public void testProcessEngine() {
		assertNotNull(repositoryService);
		assertNotNull(runtimeService);
		assertNotNull(formService);
		assertNotNull(identityService);
		assertNotNull(taskService);
		assertNotNull(historyService);
		assertNotNull(managementService);
	}
}
