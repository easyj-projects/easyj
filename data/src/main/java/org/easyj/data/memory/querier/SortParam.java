package org.easyj.data.memory.querier;

/**
 * @author wangliang181230
 */
public class SortParam {

	private String sortFieldName;

	private SortOrder sortOrder;

	public SortParam() {
	}

	public SortParam(String sortFieldName) {
		this.sortFieldName = sortFieldName;
	}

	public SortParam(String sortFieldName, SortOrder sortOrder) {
		this.sortFieldName = sortFieldName;
		this.sortOrder = sortOrder;
	}

	public String getSortFieldName() {
		return sortFieldName;
	}

	public void setSortFieldName(String sortFieldName) {
		this.sortFieldName = sortFieldName;
	}

	public SortOrder getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(SortOrder sortOrder) {
		this.sortOrder = sortOrder;
	}
}
