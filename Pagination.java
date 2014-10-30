package com.hans.util;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.HibernateTemplate;

public class Pagination {
	private List<Object> list;
	private int currentPageNumber;
	private int maxPageNumber;
	private int allObjectNumber;

	public Pagination(HibernateTemplate hibernateTemplate, String hql,
			int pageSize, int pageNumber) {
		if (pageSize<=0) throw new IllegalArgumentException("PageSize <= 0");
		// 确定总条数
		allObjectNumber = (int) getCount(hibernateTemplate, hql);
		// 确定最大页数
		if (allObjectNumber % pageSize == 0) {
			maxPageNumber = allObjectNumber / pageSize;
		} else {
			maxPageNumber = allObjectNumber / pageSize + 1;
		}
		// 确定当前页数
		if (pageNumber <= 0) {
			currentPageNumber = 1;
		} else if (pageNumber > maxPageNumber) {
			currentPageNumber = maxPageNumber;
		} else {
			currentPageNumber = pageNumber;
		}
		// 确定list内容
		list = getListByPage(hibernateTemplate,hql,(currentPageNumber-1)*pageSize, pageSize);
	}
	//分页主体
	private List<Object> getListByPage(HibernateTemplate hibernateTemplate,
			final String hql, final int offset, final int length) {
		List<Object> list = (List<Object>) hibernateTemplate
				.execute(new HibernateCallback<List<Object>>() {

					public List<Object> doInHibernate(Session session)
							throws HibernateException {
						Query query = session.createQuery(hql);
						query.setFirstResult(offset);
						query.setMaxResults(length);
						List<Object> list = query.list();
						return list;
					}
				});
		return list;
	}
	//算聚合函数
	private  long getCount(HibernateTemplate hibernateTemplate, String hql) {
		Long result = null;
		result = (Long) hibernateTemplate
				.execute(new HibernateCallback<Long>() {
					public Long doInHibernate(Session arg0)
							throws HibernateException {
						Query query = arg0.createQuery("select count(*) "+hql);
						return (Long) query.uniqueResult();
					}
				});
		return result;
	}

	public List<Object> getList() {
		return list;
	}

	public int getCurrentPageNumber() {
		return currentPageNumber;
	}

	public int getMaxPageNumber() {
		return maxPageNumber;
	}

	public int getAllObjectNumber() {
		return allObjectNumber;
	}

}
