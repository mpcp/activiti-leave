/**
 * 文件名：LeaveManager.java
 *
 * 版本信息：
 * 日期：2012-9-3
 * Copyright 足下 Corporation 2012 
 * 版权所有
 *
 */
package org.richie144.leave.service.oa.leave;

import java.util.Date;

import org.richie144.leave.dao.LeaveDao;
import org.richie144.leave.entity.oa.Leave;

/**
 * 
 * 项目名称：activiti-leave
 * 类名称：LeaveManager
 * 类描述：请假实体管理
 * 创建人：任欢
 * 创建时间：2012-9-3 上午9:17:24
 * 修改人：任欢
 * 修改时间：2012-9-3 上午9:17:24
 * 修改备注：
 * @version 
 * 
 */
public class LeaveManager {
	
	private LeaveDao leaveDao;
	/**
	 * 
	 * getLeave(根据请假实体id得到请假实体)
	 * @param  id 请假实体id
	 * @return String    DOM对象
	 * @Exception 异常对象
	 * @since  CodingExample　Ver(编码范例查看) 1.1
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
