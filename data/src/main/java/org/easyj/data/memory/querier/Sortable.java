package org.easyj.data.memory.querier;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.CollectionUtils;

/**
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
