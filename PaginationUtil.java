package com.hans.util;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

public class PaginationUtil {
	private List<Object> list;
	private int currentPageNumber;
	private int maxPageNumber;
	private int allObjectNumber;

	public PaginationUtil(Session session, String hql, int pageSize,
			int pageNumber) {
		// 确定总条数
		allObjectNumber = (int) getCount(session, hql);
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
		list = getListByPage(session, hql, (currentPageNumber - 1) * pageSize,
				pageSize);
	}

	// 算聚合函数
	private long getCount(Session session, String hql) {
		Query query = session.createQuery("select count(*) " + hql);
		return (Long) query.uniqueResult();

	}

	// 分页主体
	private List<Object> getListByPage(Session session, final String hql,
			int offset, int length) {
		Query query = session.createQuery(hql);
		query.setFirstResult(offset);
		query.setMaxResults(length);

		List<Object> list = query.list();
		return list;
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
