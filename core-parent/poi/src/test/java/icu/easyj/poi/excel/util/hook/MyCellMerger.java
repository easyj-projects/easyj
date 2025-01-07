package icu.easyj.poi.excel.util.hook;

import java.util.Objects;

import icu.easyj.poi.excel.hook.ICellMerger;
import icu.easyj.poi.excel.model.ExcelCellMapping;
import icu.easyj.poi.excel.util.model.TestClass;

public class MyCellMerger implements ICellMerger<TestClass> {

	@Override
	public boolean needMerge(ExcelCellMapping cellMapping, TestClass previous, TestClass current) {
		switch (cellMapping.getColumnFromFieldOrColumn()) {
			case "name":
				return Objects.equals(previous.getName(), current.getName());
			case "age":
				return Objects.equals(previous.getName(), current.getName()) &&
						Objects.equals(previous.getAge(), current.getAge());
			case "bClass.age":
				return Objects.equals(previous.getName(), current.getName()) &&
						Objects.equals(
								previous.getbClass() == null ? null : previous.getbClass().getAge(),
								current.getbClass() == null ? null : current.getbClass().getAge()
						);
			default:
				return false;
		}
	}
}
