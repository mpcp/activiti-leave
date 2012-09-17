/**
 * �ļ�����ProcessEngineTest.java
 *
 * �汾��Ϣ��
 * ���ڣ�2012-9-14
 * Copyright ���� Corporation 2012 
 * ��Ȩ����
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
 * ��Ŀ���ƣ�activiti-leave
 * �����ƣ�ProcessEngineTest
 * ���������������������ܷ�����
 * �����ˣ��λ�
 * ����ʱ�䣺2012-9-14 ����1:49:28
 * �޸��ˣ��λ�
 * �޸�ʱ�䣺2012-9-14 ����1:49:28
 * �޸ı�ע��
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
