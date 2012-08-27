/**
 * 文件名：WorkflowTraceService.java
 *
 * 版本信息：
 * 日期：2012-8-27
 * Copyright 足下 Corporation 2012 
 * 版权所有
 *
 */
package org.richie144.leave.service.activiti;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;


import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import org.richie144.leave.util.WorkflowUtils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 项目名称：activiti-leave
 * 类名称：WorkflowTraceService
 * 类描述：工作流跟踪相关Service
 * 创建人：任欢
 * 创建时间：2012-8-27 下午2:28:35
 * 修改人：任欢
 * 修改时间：2012-8-27 下午2:28:35
 * 修改备注：
 * @version 
 * 
 */
public class WorkflowTraceService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private RepositoryService repositoryService;
	
	private RuntimeService runtimeService;
	
	private TaskService taskService;
	
	private IdentityService identityService;
	
	public List<Map<String,Object>> processTrace(String processInstanceId) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		List<Map<String, Object>> activityInfos = null;
		//开始执行流程
		Execution execution = runtimeService.createExecutionQuery().executionId(processInstanceId).singleResult();
		
		Object property = PropertyUtils.getProperty(execution, "activityId");
		
		String activityId = "";
		if(property != null){
			activityId = property.toString();
		}
		
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		RepositoryServiceImpl repositoryServiceImpl = (RepositoryServiceImpl)repositoryService;
		
		ProcessDefinitionEntity processDefinition =(ProcessDefinitionEntity)repositoryServiceImpl.getDeployedProcessDefinition(processInstance.getProcessDefinitionId());
		
		/**
		 * 获得当前任务的所有节点
		 */
		List<ActivityImpl> activitiList = processDefinition.getActivities();
		
		activityInfos = new ArrayList<Map<String,Object>>();
		for(ActivityImpl activity:activitiList){
			boolean currentActiviti = false;
			String id = activity.getId();
			/**
			 * 当前节点
			 */
			if(id.equals(activityId)) {
				currentActiviti = true;
			}
			Map<String, Object> activityImageInfo = packageSingleActivitiInfo(activity, processInstance, currentActiviti);
			activityInfos.add(activityImageInfo);
		}
		return activityInfos;
	}
	
	/**
	 * 
	 * packageSingleActivitiInfo(封装输出信息，包括：当前节点的X、Y坐标、变量信息、任务类型、任务描述)
	 * @param  activity
	 * @param  processInstance
	 * @param  activiti
	 * @return activityInfo
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	private Map<String,Object> packageSingleActivitiInfo(ActivityImpl activity,ProcessInstance processInstance,boolean currentActiviti) {
		Map<String, Object> vars = new HashMap<String, Object>();
		Map<String, Object> activityInfo = new HashMap<String, Object>();
		activityInfo.put("currentActiviti", currentActiviti);
		//设置坐标
		setPosition(activity, activityInfo);
		//设置宽和高
		setWidthAndHeight(activity, activityInfo);
		Map<String,Object> props = activity.getProperties();
		vars.put("任务类型", WorkflowUtils.parseToZhType(props.get("type").toString()));
		ActivityBehavior activityBehavior = activity.getActivityBehavior();
		logger.debug("activityBehavior={}", activityBehavior);
		if(activityBehavior instanceof UserTaskActivityBehavior) {
			Task currentTask = null;
			/*
			 * 当前节点的task
			 */
			if (currentActiviti) {
				currentTask = getCurrentNodeInfo(processInstance);
			}
			
			/**
			 * 当前任务的分配角色
			 */
			UserTaskActivityBehavior userTaskActivityBehavior = (UserTaskActivityBehavior) activityBehavior;
			TaskDefinition taskDefinition = userTaskActivityBehavior.getTaskDefinition();
			Set<Expression> candidateGroupIdExpressions = taskDefinition.getCandidateGroupIdExpressions();
			if(!candidateGroupIdExpressions.isEmpty()) {
				//任务的处理角色
				setTaskGroup(vars, candidateGroupIdExpressions);
				// 当前处理人
				if (currentTask != null) {
					setCurrentTaskAssignee(vars, currentTask);
				}
			}
		}
		
		vars.put("节点说明", props.get("documentation"));
		String description = activity.getProcessDefinition().getDescription();
		vars.put("描述", description);
		logger.debug("trace variables: {}", vars);
		activityInfo.put("vars", vars);
		return activityInfo;
	}
	
	/**
	 * 
	 * setTaskGroup(设置任务所属的角色)
	 * @param  vars  任务信息变量
	 * @param  currentTask    当前任务节点
	 * @return void
	 * @Exception 无
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	private void setTaskGroup(Map<String, Object> vars, Set<Expression> candidateGroupIdExpressions) {
		String roles = null;
		
		for(Expression expression:candidateGroupIdExpressions) {
			String expressionText = expression.getExpressionText();
			if(expressionText.startsWith("$")) {
				expressionText = expressionText.replace("${insuranceType}", "life");
			}
			String roleName = identityService.createGroupQuery().groupId(expressionText).singleResult().getName();
			roles += roleName;
		}
		vars.put("任务所属角色", roles);
	}
	
	/**
	 * 
	 * setCurrentTaskAssignee(设置当前处理人的信息)
	 * @param   vars  任务信息变量
	 * @param  currentTask    当前任务节点
	 * @return String    DOM对象
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	private void setCurrentTaskAssignee(Map<String, Object> vars, Task currentTask) {
		/**
		 * 获取当前任务的执行者
		 */
		String assignee = currentTask.getAssignee();
		
		if(assignee != null) {
			User assigneeUser = identityService.createUserQuery().userId(assignee).singleResult();
			
			String userInfo = assigneeUser.getFirstName() + " " + assigneeUser.getLastName();
			
			vars.put("当前处理人", userInfo);
		}
	}
	
	/**
	 * 
	 * getCurrentNodeInfo(根据流程实例获取当前节点信息)
	 * @param  processInstance 正在执行的流程实例
	 * @return currentTask    当前任务节点
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	private Task getCurrentNodeInfo(ProcessInstance processInstance) {
		Task currentTask = null;
		try {
			String activitiId = (String)PropertyUtils.getProperty(processInstance, "activityId");
			logger.debug("current activity id: {}", activitiId);
			currentTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDefinitionKey(activitiId).singleResult();
			logger.debug("current task for processInstance: {}",ToStringBuilder.reflectionToString(currentTask));
		} catch (Exception e) {
			logger.error("can not get property activityId from processInstance: {}", processInstance);
			e.printStackTrace();
		} 
		return currentTask;
	}
	
	/**
	 * 
	 * setPosition(设置坐标的位置)
	 * @param   activity
	 * @param  activityInfo
	 * @return void
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	private void setPosition(ActivityImpl activity,Map<String, Object> activityInfo) {
		activityInfo.put("x", activity.getX());
		activityInfo.put("y", activity.getY());
	}
	
	/**
	 * 
	 * setPosition(设置宽度，高度属性)
	 * @param   activity
	 * @param  activityInfo
	 * @return void
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	private void setWidthAndHeight(ActivityImpl activity, Map<String, Object> activityInfo) {
		activityInfo.put("width", activity.getWidth());
		activityInfo.put("height", activity.getHeight());
	}
}



















