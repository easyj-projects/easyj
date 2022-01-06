/*
 * Copyright 2021-2022 the original author or authors.
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

import java.util.List;

import icu.easyj.core.util.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * 可排序的
 *
 * @author wangliang181230
 */
public interface Sortable {

	/**
	 * Gets sort params.
	 *
	 * @return the sort params
	 */
	SortParam[] getSortParams();

	/**
	 * Sets sort params.
	 *
	 * @param sortParams the sort params
	 */
	void setSortParams(SortParam... sortParams);

	/**
	 * Has sort params.
	 *
	 * @return the boolean
	 */
	default boolean hasSortParams() {
		return ArrayUtils.isNotEmpty(getSortParams());
	}

	/**
	 * Is need sort.
	 *
	 * @param dataList the data list
	 * @return the boolean
	 */
	default boolean isNeedSort(List<?> dataList) {
		return !CollectionUtils.isEmpty(dataList) && this.hasSortParams();
	}
}
