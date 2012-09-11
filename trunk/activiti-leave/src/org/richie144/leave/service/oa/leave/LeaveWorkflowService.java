/**
 * 文件名：LeaveWorkFlowService.java
 *
 * 版本信息：
 * 日期：2012-9-3
 * Copyright 足下 Corporation 2012 
 * 版权所有
 *
 */
package org.richie144.leave.service.oa.leave;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.richie144.leave.entity.oa.Leave;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * 项目名称：activiti-leave
 * 类名称：LeaveWorkFlowService
 * 类描述：请假流程业务类
 * 创建人：任欢
 * 创建时间：2012-9-3 上午10:23:26
 * 修改人：任欢
 * 修改时间：2012-9-3 上午10:23:26
 * 修改备注：
 * @version 
 * 
 */
@Component
@Transactional(readOnly = true)
public class LeaveWorkflowService {
	private static Logger log = LoggerFactory.getLogger(LeaveWorkflowService.class);
	
	@Resource
	private LeaveManager leaveManager;
	
	@Resource
	private IdentityService identityService;
	
	@Resource
	private RuntimeService runtimeService;
	
	@Resource
	private TaskService taskService;
	
	@Resource
	private RepositoryService repositoryService;
	
	@Resource
	private HistoryService historyService;
	
	public LeaveManager getLeaveManager() {
		return leaveManager;
	}

	public void setLeaveManager(LeaveManager leaveManager) {
		this.leaveManager = leaveManager;
	}

	public IdentityService getIdentityService() {
		return identityService;
	}

	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}

	public RuntimeService getRuntimeService() {
		return runtimeService;
	}

	public void setRuntimeService(RuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public void setRepositoryService(RepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}

	public void setHistoryService(HistoryService historyService) {
		this.historyService = historyService;
	}

	/**
	 * 
	 * startWorkflow(启动流程)
	 * @param  leave 请假实体
	 * @param  variables 变量
	 * @return processInstance 流程实例对象
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public ProcessInstance startWorkflow(Leave leave,Map<String,Object> variables) {
		ProcessInstance processInstance = null;
		leaveManager.saveLeave(leave);
		log.debug("save entity :{}",leave);
		String businessKey = leave.getId().toString();
		
		/**
		 * 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
		 */
		identityService.setAuthenticatedUserId(leave.getUserId());
		processInstance = runtimeService.startProcessInstanceByKey("leave", businessKey, variables);
		String processInstanceId = processInstance.getId();
		leave.setProcessInstanceId(processInstanceId);
		log.debug("start process of {key={}, bkey={}, pid={}, variables={}}", new Object[] { "leave", businessKey,
				processInstanceId, variables });
		return processInstance;
		
	}
	
	/**
	 * 
	 * findToDoTasks(查寻待办任务)
	 * @param   userId 当前登录用户的id
	 * @return 当前登录人的待办事件列表
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public List<Leave> findToDoTasks(String userId) {
		List<Leave> todoTasks = new ArrayList<Leave>();
		List<Task> tasks = new ArrayList<Task>();
		/**
		 * 根据当前人的id查询
		 */
		List<Task> todoList = taskService.createTaskQuery().processDefinitionKey("leave").taskAssignee(userId).orderByTaskPriority().desc().orderByTaskCreateTime().desc().list();
		/**
		 * 当前人未签收的任务
		 */
		List<Task> unsignedTasks = taskService.createTaskQuery().processDefinitionKey("leave").taskCandidateUser(userId).orderByTaskPriority().desc()
				.orderByTaskCreateTime().desc().list();
		//合并
		tasks.addAll(todoList);
		tasks.addAll(unsignedTasks);
		// 根据流程的业务ID查询实体并关联
		for (Task task : tasks) {
			String processInstanceId = task.getProcessInstanceId();
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
							.singleResult();
			String businessKey = processInstance.getBusinessKey();
			Leave leave = leaveManager.getLeave(new Long(businessKey));
			leave.setTask(task);
			leave.setProcessInstance(processInstance);
			leave.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
			todoTasks.add(leave);
		}		
		return todoTasks;
	}
	
	/**
	 * 
	 * findRunningProcessInstance(读取运行中的流程)
	 * @return results    运行中的流程实例列表
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public List<Leave> findRunningProcessInstance() {
		List<Leave> results = new ArrayList<Leave>();
		List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().processDefinitionKey("leave").list();
		// 关联业务实体
		for (ProcessInstance processInstance : list) {
			String businessKey = processInstance.getBusinessKey();
			Leave leave = leaveManager.getLeave(new Long(businessKey));
			leave.setProcessInstance(processInstance);
			leave.setProcessDefinition(getProcessDefinition(processInstance.getProcessDefinitionId()));
			results.add(leave);
			// 设置当前任务信息
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).orderByTaskCreateTime()
					.desc().listPage(0, 1);
			leave.setTask(tasks.get(0));

		}
		return results;
	}
	
	/**
	 * 
	 * findFinishedProcessInstaces(读取已完成流程)
	 * @return results    已完成的流程的列表
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public List<Leave> findFinishedProcessInstaces() {
		List<Leave> results = new ArrayList<Leave>();
		List<HistoricProcessInstance> list = historyService.createHistoricProcessInstanceQuery().processDefinitionKey("leave")
				.finished().list();

		// 关联业务实体
		for (HistoricProcessInstance historicProcessInstance : list) {
			String businessKey = historicProcessInstance.getBusinessKey();
			Leave leave = leaveManager.getLeave(new Long(businessKey));
			leave.setProcessDefinition(getProcessDefinition(historicProcessInstance.getProcessDefinitionId()));
			leave.setHistoricProcessInstance(historicProcessInstance);
			results.add(leave);
		}
		return results;
	}
	/**
	 *  查询流程定义对象
	 * @param processDefinitionId	流程定义ID
	 * @return
	 */
	protected ProcessDefinition getProcessDefinition(String processDefinitionId) {
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		return processDefinition;
	}
	
	
}
