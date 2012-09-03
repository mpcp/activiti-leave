/**
 * �ļ�����LeaveManager.java
 *
 * �汾��Ϣ��
 * ���ڣ�2012-9-3
 * Copyright ���� Corporation 2012 
 * ��Ȩ����
 *
 */
package org.richie144.leave.service.oa.leave;

import java.util.Date;

import org.richie144.leave.dao.LeaveDao;
import org.richie144.leave.entity.oa.Leave;

/**
 * 
 * ��Ŀ���ƣ�activiti-leave
 * �����ƣ�LeaveManager
 * �����������ʵ�����
 * �����ˣ��λ�
 * ����ʱ�䣺2012-9-3 ����9:17:24
 * �޸��ˣ��λ�
 * �޸�ʱ�䣺2012-9-3 ����9:17:24
 * �޸ı�ע��
 * @version 
 * 
 */
public class LeaveManager {
	
	private LeaveDao leaveDao;
	/**
	 * 
	 * getLeave(�������ʵ��id�õ����ʵ��)
	 * @param  id ���ʵ��id
	 * @return String    DOM����
	 * @Exception �쳣����
	 * @since  CodingExample��Ver(���뷶���鿴) 1.1
	 */
	
	
	public Leave getLeave(Long id) {
		return leaveDao.findOne(id);
	}
	
	public LeaveDao getLeaveDao() {
		return leaveDao;
	}

	public void setLeaveDao(LeaveDao leaveDao) {
		this.leaveDao = leaveDao;
	}

	public void saveLeave(Leave leave) {
		if(leave.getId() == null) {
			leave.setApplyTime(new Date());
		}
		leaveDao.save(leave);
	}
}
