package icu.easyj.poi.excel.hook;

import icu.easyj.poi.excel.model.ExcelCellMapping;

public interface ICellMerger<T> {

	/**
	 * 是否合并单元格
	 *
	 * @param cellMapping 当前单元格
	 * @param previous    上一条数据
	 * @param current     当前数据
	 * @return 是否合并：true=合并 | false=不合并
	 */
	boolean needMerge(ExcelCellMapping cellMapping, T previous, T current);

}
