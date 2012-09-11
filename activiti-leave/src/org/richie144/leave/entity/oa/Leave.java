/**
 * 文件名：Leave.java
 *
 * 版本信息：
 * 日期：2012-8-27
 * Copyright 足下 Corporation 2012 
 * 版权所有
 *
 */
package org.richie144.leave.entity.oa;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;


import org.richie144.leave.entity.IdEntity;


import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 * 项目名称：activiti-leave
 * 类名称：Leave
 * 类描述：请假实体封装
 * 创建人：任欢
 * 创建时间：2012-8-27 上午9:19:11
 * 修改人：任欢
 * 修改时间：2012-8-27 上午9:19:11
 * 修改备注：
 * @version 
 * 
 */

@Entity
@Table(name="oa_leave")
public class Leave extends IdEntity implements Serializable {
	
	private static final long serialVersionUID = 12015398955L;
	
	private String processInstanceId;
	
	private String userId;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	private Date startTime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	private Date endTime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	private Date realityStartTime;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm")
	private Date realityEndTime;
	
	private Date applyTime;
	
	private String leaveType;
	
	private String reason;
	
	/**
	 * 流程任务
	 */
	private Task task;
	
	private Map<String,Object> variables;
	
	/**
	 * 运行中的流程实例
	 */
	private ProcessInstance processInstance;
	
	/**
	 * 历史流程实例
	 */
	private HistoricProcessInstance historicProcessInstance;
	
	/**
	 * 流程定义
	 */
	private ProcessDefinition processDefinition;
	
	@Column(name = "process_instance_id")
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	@Column(name = "user_id")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "start_time")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@Column(name = "end_time")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Column(name = "reality_start_time")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getRealityStartTime() {
		return realityStartTime;
	}

	public void setRealityStartTime(Date realityStartTime) {
		this.realityStartTime = realityStartTime;
	}
	
	@Column(name = "reality_end_time")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getRealityEndTime() {
		return realityEndTime;
	}

	public void setRealityEndTime(Date realityEndTime) {
		this.realityEndTime = realityEndTime;
	}
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}
	
	@Column(name = "leave_type")
	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	@Column
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	@Transient
	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}
	
	@Transient
	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}
	
	@Transient
	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}
	
	@Transient
	public HistoricProcessInstance getHistoricProcessInstance() {
		return historicProcessInstance;
	}

	public void setHistoricProcessInstance(
			HistoricProcessInstance historicProcessInstance) {
		this.historicProcessInstance = historicProcessInstance;
	}
	@Transient
	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}
	
}









