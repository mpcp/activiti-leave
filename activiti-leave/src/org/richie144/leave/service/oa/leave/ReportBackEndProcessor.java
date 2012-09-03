/**
 * 文件名：ReportBackEndProcessor.java
 *
 * 版本信息：
 * 日期：2012-9-3
 * Copyright 足下 Corporation 2012 
 * 版权所有
 *
 */
package org.richie144.leave.service.oa.leave;

import java.util.Date;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.richie144.leave.entity.oa.Leave;

/**
 * 
 * 项目名称：activiti-leave
 * 类名称：ReportBackEndProcessor
 * 类描述：销假后的处理器,更改销假时间
 * 创建人：任欢
 * 创建时间：2012-9-3 上午9:47:27
 * 修改人：任欢
 * 修改时间：2012-9-3 上午9:47:27
 * 修改备注：
 * @version 
 * 
 */
public class ReportBackEndProcessor implements TaskListener {

	private LeaveManager leaveManager;
	private RuntimeService runtimeService;
	
	@Override
	public void notify(DelegateTask delegateTask) {
		String processInstanceId = delegateTask.getProcessInstanceId();
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		Leave leave = leaveManager.getLeave(new Long(processInstance.getBusinessKey()));
		Object realityStartTime = delegateTask.getVariable("realityStartTime");
		leave.setRealityStartTime((Date) realityStartTime);
		Object realityEndTime = delegateTask.getVariable("realityEndTime");
		leave.setRealityEndTime((Date) realityEndTime);
		
		leaveManager.saveLeave(leave);

	}

}
