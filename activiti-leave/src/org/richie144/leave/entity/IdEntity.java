package org.richie144.leave.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 
 * 
 * 项目名称：activiti-leave
 * 类名称：IdEntity
 * 类描述：基类统一定义id的属性名称、数据类型、列名映射及生成策略
 * 子类可重载getId()函数重定义id的列名映射和生成策略.
 * 创建人：任欢
 * 创建时间：2012-8-27 上午9:12:17
 * 修改人：任欢
 * 修改时间：2012-8-27 上午9:12:17
 * 修改备注：
 * @version 
 *
 */

@MappedSuperclass
public abstract class IdEntity {
	
	protected Long id;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
