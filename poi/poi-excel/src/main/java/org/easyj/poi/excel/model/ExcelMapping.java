package org.easyj.poi.excel.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easyj.poi.excel.annotation.Excel;
import org.springframework.util.StringUtils;

/**
 * model中的属性和excel表格中的列的映射关系
 *
 * @author wangliang181230
 */
public class ExcelMapping {

	//******************************** fields *********************************/

	// 映射类
	private Class<?> clazz;

	// 注解信息
	private Excel anno;
	// 解析注解信息
	private String sheetName; // 表名
	private boolean needBorder = true; // 是否需要边框

	private int defaultWidth = -1; // 默认列宽
	private boolean widthAutoSize = false; // 列宽自适应（警告：此功能可能存在性能问题，数据较多时谨慎使用。）

	private boolean needHeadRow = true; // 是否需要头名称行
	private boolean freezeHeadRow = true; // 是否冻结头行，默认：true=冻结

	private boolean needNumberCell = true; // 是否需要序号列
	private String numberCellHeadName = "序号"; // 序号列的列头
	private boolean freezeNumberCell = false; // 是否冻结序号列

	private int freezeDataCells; // 冻结的数据列的数量，不包含序号列

	private boolean needFilter = true; // 是否需要列筛选功能


	// 列信息
	private List<ExcelCellMapping> cellMappingList; // 列表


	//******************************** getter and setter *********************************/

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Excel getAnno() {
		return anno;
	}

	public void setAnno(Excel anno) {
		this.anno = anno;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public boolean isNeedBorder() {
		return needBorder;
	}

	public void setNeedBorder(boolean needBorder) {
		this.needBorder = needBorder;
	}

	public int getDefaultWidth() {
		return defaultWidth;
	}

	public void setDefaultWidth(int defaultWidth) {
		this.defaultWidth = defaultWidth;
	}

	public boolean isWidthAutoSize() {
		return widthAutoSize;
	}

	public void setWidthAutoSize(boolean widthAutoSize) {
		this.widthAutoSize = widthAutoSize;
	}

	public boolean isNeedHeadRow() {
		return needHeadRow;
	}

	public void setNeedHeadRow(boolean needHeadRow) {
		this.needHeadRow = needHeadRow;
	}

	public boolean isFreezeHeadRow() {
		return freezeHeadRow;
	}

	public void setFreezeHeadRow(boolean freezeHeadRow) {
		this.freezeHeadRow = freezeHeadRow;
	}

	public boolean isNeedNumberCell() {
		return needNumberCell;
	}

	public void setNeedNumberCell(boolean needNumberCell) {
		this.needNumberCell = needNumberCell;
	}

	public String getNumberCellHeadName() {
		if (StringUtils.isEmpty(numberCellHeadName)) {
			numberCellHeadName = "序号";
		}
		return numberCellHeadName;
	}

	public void setNumberCellHeadName(String numberCellHeadName) {
		this.numberCellHeadName = numberCellHeadName;
	}

	public boolean isFreezeNumberCell() {
		return freezeNumberCell;
	}

	public void setFreezeNumberCell(boolean freezeNumberCell) {
		this.freezeNumberCell = freezeNumberCell;
	}

	public int getFreezeDataCells() {
		return freezeDataCells;
	}

	public void setFreezeDataCells(int freezeDataCells) {
		this.freezeDataCells = freezeDataCells;
	}

	public boolean isNeedFilter() {
		return needFilter;
	}

	public void setNeedFilter(boolean needFilter) {
		this.needFilter = needFilter;
	}

	public List<ExcelCellMapping> getCellMappingList() {
		return cellMappingList;
	}

	public void setCellMappingList(List<ExcelCellMapping> cellMappingList) {
		this.cellMappingList = cellMappingList;
	}


	//******************************** static *********************************/

	private static final Map<Class<?>, ExcelMapping> caches = new HashMap<>();

	/**
	 * 获取属性与表格的映射关系
	 *
	 * @param clazz 类
	 * @return excelMapping excel表映射映射
	 */
	public static ExcelMapping getMapping(Class<?> clazz) {
		ExcelMapping mapping = caches.get(clazz);
		if (mapping != null) {
			return mapping;
		}

		mapping = new ExcelMapping();
		// 类信息
		mapping.setClazz(clazz);

		Excel anno = clazz.getAnnotation(Excel.class);
		if (anno != null) {
			// 注解信息
			mapping.setAnno(anno);
			// 解析注解信息
			mapping.setSheetName(anno.sheetName()); // 表名
			mapping.setNeedBorder(anno.needBorder()); // 是否需要边框
			mapping.setDefaultWidth(anno.defaultWidth()); // 默认列宽
			mapping.setWidthAutoSize(anno.widthAutoSize()); // 列宽自适应
			mapping.setNeedHeadRow(anno.needHeadRow()); // 是否需要头名称行
			mapping.setFreezeHeadRow(anno.needHeadRow() && anno.freezeHeadRow()); // 是否冻结头行，如果没有头行，则肯定不冻结
			mapping.setNeedNumberCell(anno.needNumberCell()); // 是否需要序号列
			mapping.setNumberCellHeadName(anno.numberCellHeadName()); // 序号列的列头
			mapping.setFreezeNumberCell(anno.needNumberCell() && anno.freezeNumberCell()); // 是否冻结序号列，如果没有序号列，则肯定不冻结
			mapping.setFreezeDataCells(anno.freezeDataCells()); // 冻结的数据列的数量，不包含序号列
			mapping.setNeedFilter(anno.needFilter()); // 是否需要列筛选功能
		}
		// 读取列信息列表
		List<ExcelCellMapping> cellMappingList = ExcelCellMapping.getCellMappingList(clazz, mapping);
		mapping.setCellMappingList(cellMappingList);

		caches.put(clazz, mapping);

		return mapping;
	}
}