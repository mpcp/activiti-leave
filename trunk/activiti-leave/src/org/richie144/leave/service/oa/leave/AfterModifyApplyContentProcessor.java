/**
 * 文件名：AfterModifyApplyContentProcessor.java
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
 * 类名称：AfterModifyApplyContentProcessor
 * 类描述：调整请假内容的处理器
 * 创建人：任欢
 * 创建时间：2012-9-3 上午9:31:10
 * 修改人：任欢
 * 修改时间：2012-9-3 上午9:31:10
 * 修改备注：
 * @version 
 * 
 */
public class AfterModifyApplyContentProcessor implements TaskListener {
	
	private LeaveManager leaveManager;
	private RuntimeService runtimeService;
	@Override
	public void notify(DelegateTask delegateTask) {
		//得到流程实例id
		 String processInstanceId = delegateTask.getProcessInstanceId();
		 ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		 Leave leave = leaveManager.getLeave(new Long(processInstance.getBusinessKey()));
		 leave.setLeaveType((String)delegateTask.getVariable("leaveType"));
		 leave.setStartTime((Date) delegateTask.getVariable("startTime"));
		 leave.setEndTime((Date) delegateTask.getVariable("endTime"));
		 leave.setReason((String) delegateTask.getVariable("reason"));
		 leaveManager.saveLeave(leave);

	}

}
