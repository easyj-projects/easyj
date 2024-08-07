/*
 * Copyright 2021-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package icu.easyj.data.memory.querier;

import java.util.ArrayList;
import java.util.List;

import icu.easyj.core.util.CollectionUtils;
import org.springframework.lang.NonNull;

/**
 * 查询者接口
 *
 * @author wangliang181230
 */
public interface Querier<T> {

	/**
	 * Match data
	 *
	 * @param data the data
	 * @param <D>  the data type
	 * @return the boolean
	 */
	<D extends T> boolean isMatch(D data);

	/**
	 * Do count.
	 *
	 * @param list the list
	 * @param <D>  the data type
	 * @return the count
	 */
	default <D extends T> int doCount(List<D> list) {
		int count = 0;
		if (!CollectionUtils.isEmpty(list)) {
			for (D t : list) {
				if (this.isMatch(t)) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * Do filter.
	 *
	 * @param list the list
	 * @param <D>  the data type
	 * @return the list after filter
	 */
	default <D extends T> List<D> doFilter(List<D> list) {
		List<D> found = new ArrayList<>();
		if (!CollectionUtils.isEmpty(list)) {
			for (D t : list) {
				if (this.isMatch(t)) {
					found.add(t);
				}
			}
		}
		return found;
	}

	/**
	 * Do sort.
	 *
	 * @param list the list
	 * @param <D>  the data type
	 * @return the list after sort
	 */
	@NonNull
	<D extends T> List<D> doSort(List<D> list);

	/**
	 * Do paging.
	 *
	 * @param list the list
	 * @param <D>  the data type
	 * @return the list after paging
	 */
	<D extends T> List<D> doPaging(List<D> list);

	/**
	 * Do query.
	 * doFilter + doSort + doPaging
	 *
	 * @param list the list
	 * @param <D>  the data type
	 * @return the list after query
	 */
	default <D extends T> List<D> doQuery(List<D> list) {
		if (list == null || list.isEmpty()) {
			return new ArrayList<>();
		}

		// do filter
		list = this.doFilter(list);
		if (CollectionUtils.isEmpty(list)) {
			return list == null ? new ArrayList<>() : list;
		}

		// do sort
		list = this.doSort(list);

		// do paging
		list = this.doPaging(list);

		return list;
	}
}
