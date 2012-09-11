/**
 * 文件名：LeaveController.java
 *
 * 版本信息：
 * 日期：2012-9-3
 * Copyright 足下 Corporation 2012 
 * 版权所有
 *
 */
package org.richie144.leave.web.oa.leave;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.richie144.leave.dto.Variable;

import org.richie144.leave.entity.oa.Leave;
import org.richie144.leave.service.oa.leave.LeaveManager;
import org.richie144.leave.service.oa.leave.LeaveWorkflowService;
import org.richie144.leave.util.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 
 * 项目名称：activiti-leave
 * 类名称：LeaveController
 * 类描述：请假控制器
 * 创建人：任欢
 * 创建时间：2012-9-3 下午2:02:51
 * 修改人：任欢
 * 修改时间：2012-9-3 下午2:02:51
 * 修改备注：
 * @version 
 * 
 */
@RequestMapping("/oa/leave")
@Controller
public class LeaveController {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource
	private LeaveWorkflowService workflowService;
	
	@Resource
	private TaskService taskService;
	
	@Resource
	private LeaveManager leaveManager;
	
	@RequestMapping(value={"apply",""})
	public String createForm(Model model){
		return "/oa/leave/leaveApply";
	}
	/**
	 * 
	 * startWorkflow(启动请假流程)
	 * @param   name
	 * @param  @return    设定文件
	 * @return String    DOM对象
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	public String startWorkflow(Leave leave,RedirectAttributes redirectAttributes,HttpSession session){
		try {
			User user = RequestUtil.getUserFromSession(session);
			if(user == null || StringUtils.isBlank(user.getId())) {
				return "redirect:/login?timeout=true";
			}
			leave.setUserId(user.getId());
			Map<String, Object> variables = new HashMap<String, Object>();
			ProcessInstance processInstance = workflowService.startWorkflow(leave, variables);
			redirectAttributes.addFlashAttribute("message", "流程已启动，流程ID：" + processInstance.getId());
		} catch (ActivitiException e) {
			if (e.getMessage().indexOf("no processes deployed with key") != -1) {
				log.warn("没有部署流程!", e);
				redirectAttributes.addFlashAttribute("error", "没有部署流程，请在[工作流]->[流程管理]页面点击<重新部署流程>");
			} else {
				log.error("启动投保流程失败：", e);
				redirectAttributes.addFlashAttribute("error", "系统内部错误！");
			}
			e.printStackTrace();
		} catch (Exception e) {
			log.error("启动投保流程失败：", e);
			redirectAttributes.addFlashAttribute("error", "系统内部错误！");
		}
		return "redirect:/oa/leave/apply";
	}
	
	/**
	 * 任务列表
	 * @param leave	
	 */
	@RequestMapping(value = "list/task")
	public ModelAndView taskList(HttpSession session) {
		ModelAndView mav = new ModelAndView("/oa/leave/taskList");
		String userId = RequestUtil.getUserFromSession(session).getId();
		List<Leave> results = workflowService.findToDoTasks(userId);
		mav.addObject("leaves", results);
		return mav;
	}
	
	/**
	 * 读取正在运行的流程
	 * @return
	 */
	@RequestMapping("list/running")
	public ModelAndView runningList() {
		ModelAndView mav = new ModelAndView("/oa/leave/running");
		List<Leave> results = workflowService.findRunningProcessInstance();
		mav.addObject("leave", results);
		return mav;
	}
	
	/**
	 * 读取已经完成的流程
	 * @return
	 */
	@RequestMapping(value = "list/finished")
	public ModelAndView finishedList() {
		ModelAndView mav = new ModelAndView("/oa/leave/finished");
		List<Leave> results = workflowService.findFinishedProcessInstaces();
		mav.addObject("leaves", results);
		return mav;
	}
	
	/**
	 * 签收任务
	 */
	@RequestMapping(value = "task/claim/{id}")
	public String claim(@PathVariable("id") String taskId, HttpSession session, RedirectAttributes redirectAttributes) {
		String userId = RequestUtil.getUserFromSession(session).getId();
		taskService.claim(taskId, userId);
		redirectAttributes.addFlashAttribute("message", "任务已签收");
		return "redirect:/oa/leave/list/task";
	}
	
	/**
	 * 
	 * getLeave(读取详细数据)
	 * @param   name
	 * @param  @return    设定文件
	 * @return String    DOM对象
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	@RequestMapping("detail/{id")
	@ResponseBody
	public Leave getLeave( @PathVariable("id")Long id) {
		Leave leave = leaveManager.getLeave(id);
		return leave;
	}
	

	/**
	 * 读取详细数据
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "detail-with-vars/{id}/{taskId}")
	@ResponseBody
	public Leave getLeaveWithVars(@PathVariable("id") Long id, @PathVariable("taskId") String taskId) {
		Leave leave = leaveManager.getLeave(id);
		Map<String, Object> variables = taskService.getVariables(taskId);
		leave.setVariables(variables);
		return leave;
	}
	/**
	 * 
	 * complete(完成任务)
	 * @param   name
	 * @param  @return    设定文件
	 * @return String    DOM对象
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
	 */
	@RequestMapping(value = "complete/{id}", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String complete(@PathVariable("id")String taskId,Variable var) {
		try {
			Map<String, Object> variables = var.getVariableMap();
			taskService.complete(taskId, variables);
			return "success";
		} catch (Exception e) {
			log.error("error on complete task {}, variables={}", new Object[] { taskId, var.getVariableMap(), e });
			e.printStackTrace();
			return "error";
		}
	}
}
