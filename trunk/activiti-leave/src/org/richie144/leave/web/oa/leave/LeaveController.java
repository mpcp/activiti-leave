/**
 * �ļ�����LeaveController.java
 *
 * �汾��Ϣ��
 * ���ڣ�2012-9-3
 * Copyright ���� Corporation 2012 
 * ��Ȩ����
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
 * ��Ŀ���ƣ�activiti-leave
 * �����ƣ�LeaveController
 * ����������ٿ�����
 * �����ˣ��λ�
 * ����ʱ�䣺2012-9-3 ����2:02:51
 * �޸��ˣ��λ�
 * �޸�ʱ�䣺2012-9-3 ����2:02:51
 * �޸ı�ע��
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
	 * startWorkflow(�����������)
	 * @param   name
	 * @param  @return    �趨�ļ�
	 * @return String    DOM����
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
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
			redirectAttributes.addFlashAttribute("message", "����������������ID��" + processInstance.getId());
		} catch (ActivitiException e) {
			if (e.getMessage().indexOf("no processes deployed with key") != -1) {
				log.warn("û�в�������!", e);
				redirectAttributes.addFlashAttribute("error", "û�в������̣�����[������]->[���̹���]ҳ����<���²�������>");
			} else {
				log.error("����Ͷ������ʧ�ܣ�", e);
				redirectAttributes.addFlashAttribute("error", "ϵͳ�ڲ�����");
			}
			e.printStackTrace();
		} catch (Exception e) {
			log.error("����Ͷ������ʧ�ܣ�", e);
			redirectAttributes.addFlashAttribute("error", "ϵͳ�ڲ�����");
		}
		return "redirect:/oa/leave/apply";
	}
	
	/**
	 * �����б�
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
	 * ��ȡ�������е�����
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
	 * ��ȡ�Ѿ���ɵ�����
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
	 * ǩ������
	 */
	@RequestMapping(value = "task/claim/{id}")
	public String claim(@PathVariable("id") String taskId, HttpSession session, RedirectAttributes redirectAttributes) {
		String userId = RequestUtil.getUserFromSession(session).getId();
		taskService.claim(taskId, userId);
		redirectAttributes.addFlashAttribute("message", "������ǩ��");
		return "redirect:/oa/leave/list/task";
	}
	
	/**
	 * 
	 * getLeave(��ȡ��ϸ����)
	 * @param   name
	 * @param  @return    �趨�ļ�
	 * @return String    DOM����
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	@RequestMapping("detail/{id")
	@ResponseBody
	public Leave getLeave( @PathVariable("id")Long id) {
		Leave leave = leaveManager.getLeave(id);
		return leave;
	}
	

	/**
	 * ��ȡ��ϸ����
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
	 * complete(�������)
	 * @param   name
	 * @param  @return    �趨�ļ�
	 * @return String    DOM����
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
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
