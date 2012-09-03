/**
 * �ļ�����AfterModifyApplyContentProcessor.java
 *
 * �汾��Ϣ��
 * ���ڣ�2012-9-3
 * Copyright ���� Corporation 2012 
 * ��Ȩ����
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
 * ��Ŀ���ƣ�activiti-leave
 * �����ƣ�AfterModifyApplyContentProcessor
 * ������������������ݵĴ�����
 * �����ˣ��λ�
 * ����ʱ�䣺2012-9-3 ����9:31:10
 * �޸��ˣ��λ�
 * �޸�ʱ�䣺2012-9-3 ����9:31:10
 * �޸ı�ע��
 * @version 
 * 
 */
public class AfterModifyApplyContentProcessor implements TaskListener {
	
	private LeaveManager leaveManager;
	private RuntimeService runtimeService;
	@Override
	public void notify(DelegateTask delegateTask) {
		//�õ�����ʵ��id
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
