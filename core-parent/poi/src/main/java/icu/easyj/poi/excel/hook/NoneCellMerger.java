package icu.easyj.poi.excel.hook;

import icu.easyj.poi.excel.model.ExcelCellMapping;

public class NoneCellMerger implements ICellMerger<Object> {

	@Override
	public boolean needMerge(ExcelCellMapping cellMapping, Object previous, Object current) {
		return false;
	}

}
