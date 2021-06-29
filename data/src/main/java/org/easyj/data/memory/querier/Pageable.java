package org.easyj.data.memory.querier;

/**
 * @author wangliang181230
 */
public interface Pageable {

	/**
	 * Gets page number
	 *
	 * @return the page number
	 */
	int getPageNumber();

	/**
	 * Sets page number
	 *
	 * @param pageNumber the page number
	 */
	void setPageNumber(int pageNumber);

	/**
	 * Gets page size
	 *
	 * @return the page size
	 */
	int getPageSize();

	/**
	 * Sets page size
	 *
	 * @param pageSize the page size
	 */
	void setPageSize(int pageSize);
}
