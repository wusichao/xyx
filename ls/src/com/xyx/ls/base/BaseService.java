package com.xyx.ls.base;

import java.util.List;
public interface BaseService<E> {
	void add( E e, Long id);
	void delete(String ids, Long did);
	void update(E e);
	E findByIds(String ids);
	List<E> findAll(String name);
}
