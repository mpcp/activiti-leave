package org.richie144.leave.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 
 * 
 * ��Ŀ���ƣ�activiti-leave
 * �����ƣ�IdEntity
 * ������������ͳһ����id���������ơ��������͡�����ӳ�估���ɲ���
 * ���������getId()�����ض���id������ӳ������ɲ���.
 * �����ˣ��λ�
 * ����ʱ�䣺2012-8-27 ����9:12:17
 * �޸��ˣ��λ�
 * �޸�ʱ�䣺2012-8-27 ����9:12:17
 * �޸ı�ע��
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
