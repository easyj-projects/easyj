/*
 * Copyright 2021 the original author or authors.
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
import java.util.Collections;
import java.util.List;

import icu.easyj.core.constant.PageConstants;
import icu.easyj.core.util.CollectionUtils;
import org.springframework.lang.NonNull;

/**
 * @author wangliang181230
 */
public abstract class AbstractQuerier<T> implements Querier<T>, Sortable, Pageable {

	/**
	 * The sort params
	 */
	protected SortParam[] sortParams;

	/**
	 * The page number
	 */
	protected int pageNumber = PageConstants.FIRST_PAGE_NUMBER;

	/**
	 * The page size
	 */
	protected int pageSize = PageConstants.DEFAULT_PAGE_SIZE;

	/**
	 * Compare by field name.
	 *
	 * @param a             the object a
	 * @param b             the object b
	 * @param sortFieldName the sort field name
	 * @param <D>           the type of the object a and b
	 * @return the compare result
	 */
	public abstract <D extends T> int compareByFieldName(D a, D b, String sortFieldName);


	//region Override methods: doSort, doPaging

	/**
	 * Do sort.
	 *
	 * @param list the list
	 * @return the list after sort
	 */
	@NonNull
	@Override
	public <D extends T> List<D> doSort(List<D> list) {
		if (CollectionUtils.isEmpty(list)) {
			return new ArrayList<>();
		}

		if (!this.isNeedSort(list)) {
			return list;
		}

		list.sort((a, b) -> {
			int ret;
			for (SortParam sortParam : this.getSortParams()) {
				ret = this.compareByFieldName(a, b, sortParam.getSortFieldName());
				if (ret == 0) {
					continue;
				}

				if (sortParam.getSortOrder() == SortOrder.DESC) {
					ret = ret > 0 ? -1 : 1; // -1 * ret
				}
				return ret;
			}
			return 0;
		});
		return list;
	}

	/**
	 * Do paging.
	 *
	 * @param list the list
	 * @return the list after paging
	 */
	@Override
	public <D extends T> List<D> doPaging(List<D> list) {
		if (list == null) {
			return Collections.emptyList();
		}

		if (list.isEmpty() || getPageSize() <= 0) {
			return list;
		}

		int fromIndex = this.getFromIndex();
		if (fromIndex < 0) {
			fromIndex = 0;
		}
		if (fromIndex >= list.size()) {
			return Collections.emptyList();
		}

		int toIndex = this.getToIndex(fromIndex);
		if (toIndex > list.size()) {
			toIndex = list.size();
		}

		return list.subList(fromIndex, toIndex);
	}

	//endregion

	/**
	 * The starting index of the current page
	 *
	 * @return fromIndex
	 */
	public int getFromIndex() {
		return (pageNumber - PageConstants.FIRST_PAGE_NUMBER) * pageSize;
	}

	/**
	 * The end index of the current page
	 *
	 * @param fromIndex 起始索引
	 * @return toIndex 截止索引
	 */
	public int getToIndex(int fromIndex) {
		return fromIndex + pageSize;
	}

	//region Gets and Sets

	@Override
	public SortParam[] getSortParams() {
		return sortParams;
	}

	@Override
	public void setSortParams(SortParam... sortParams) {
		this.sortParams = sortParams;
	}

	@Override
	public int getPageNumber() {
		return pageNumber;
	}

	@Override
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	@Override
	public int getPageSize() {
		return pageSize;
	}

	@Override
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	//endregion
}
