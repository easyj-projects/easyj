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
package icu.easyj.poi.excel.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import icu.easyj.core.util.ReflectionUtils;
import icu.easyj.core.util.StringUtils;
import icu.easyj.poi.excel.annotation.Excel;
import icu.easyj.poi.excel.annotation.ExcelCell;
import icu.easyj.poi.excel.annotation.ExcelCells;
import icu.easyj.poi.excel.style.ExcelFormats;
import icu.easyj.poi.excel.util.ExcelColorUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * model中的属性和excel表格中的列的映射关系
 *
 * @author wangliang181230
 */
public class ExcelCellMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	//******************************** fields *********************************/

	// 对应的属性
	private Field field; // 注解在的那个字段对象
	private String column; // 所要展示的数据字段名，可以是多级的，如：obj.field1.field2

	// 注解信息
	private ExcelCell anno;
	// 解析注解信息
	private String headName; // 列头
	private String headComment; // 列头注释
	private int cellNum; // 列号（值从0开始，除了序号列外）
	private int width = -1; // 列宽（-1表示让excel自动分配宽度）
	private boolean wrapText = false; // 是否允许换行
	private boolean hidden = false; // 是否隐藏列
	private String color; // 字体颜色
	private short colorIndex; // 字体颜色的索引
	private String backgroundColor; // 背景颜色
	private short backgroundColorIndex; // 背景颜色的索引
	private String format; // 格式化（应用于Date数据、数字等等的格式化输出）
	private String align; // 水平位置：left、center、right
	private String verAlign; // 竖直位置：top、middle|center、bottom
	private String trueText; // boolean型数据为true时，显示的文字
	private String falseText; // boolean型数据为false时，显示的文字
	private Map<String, String> convertMap; // 值转换
	private Map<String, String> convertMap2; // 值反向转换

	//******************************** getter and setter *********************************/

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public ExcelCell getAnno() {
		return anno;
	}

	public void setAnno(ExcelCell anno) {
		this.anno = anno;
	}

	public String getHeadName() {
		return headName;
	}

	public void setHeadName(String headName) {
		this.headName = headName;
	}

	public String getHeadComment() {
		return headComment;
	}

	public void setHeadComment(String headComment) {
		this.headComment = headComment;
	}

	public int getCellNum() {
		return cellNum;
	}

	public void setCellNum(int cellNum) {
		this.cellNum = cellNum;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public boolean isWrapText() {
		return wrapText;
	}

	public void setWrapText(boolean wrapText) {
		this.wrapText = wrapText;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public short getColorIndex() {
		return colorIndex;
	}

	public void setColorIndex(short colorIndex) {
		this.colorIndex = colorIndex;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public short getBackgroundColorIndex() {
		return backgroundColorIndex;
	}

	public void setBackgroundColorIndex(short backgroundColorIndex) {
		this.backgroundColorIndex = backgroundColorIndex;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getVerAlign() {
		return verAlign;
	}

	public void setVerAlign(String verAlign) {
		this.verAlign = verAlign;
	}

	public String getTrueText() {
		return trueText;
	}

	public void setTrueText(String trueText) {
		this.trueText = trueText;
	}

	public String getFalseText() {
		return falseText;
	}

	public void setFalseText(String falseText) {
		this.falseText = falseText;
	}

	public Map<String, String> getConvertMap() {
		return convertMap;
	}

	public void setConvertMap(Map<String, String> convertMap) {
		this.convertMap = convertMap;
	}

	public Map<String, String> getConvertMap2() {
		return convertMap2;
	}

	public void setConvertMap2(Map<String, String> convertMap2) {
		this.convertMap2 = convertMap2;
	}


	//******************************** static *********************************/


	private static void addMapping(ExcelCell[] excelCells, Class<?> clazz, ExcelMapping mapping, Field field, List<ExcelCellMapping> mappingList) {
		if (excelCells.length > 0) {
			ExcelCellMapping cellMapping;
			for (ExcelCell excelCell : excelCells) {
				// 注解转换为映射类对象
				cellMapping = toMapping(excelCell, clazz, field, mapping);
				// 添加到列表中
				mappingList.add(cellMapping);
			}
		}
	}

	/**
	 * 获取属性与表格的映射关系
	 *
	 * @param clazz   the clazz
	 * @param mapping excel表映射
	 * @return cellMappingList excel列映射列表
	 */
	public static List<ExcelCellMapping> getCellMappingList(Class<?> clazz, ExcelMapping mapping) {
		List<ExcelCellMapping> mappingList = new ArrayList<>();

		// 获取在字段上配置的列映射
		Field[] fields = clazz.getDeclaredFields();
		ExcelCellMapping cellMapping;
		for (Field f : fields) {
			ExcelCell[] excelCells = f.getAnnotationsByType(ExcelCell.class);
			addMapping(excelCells, clazz, mapping, f, mappingList);

			ExcelCells annos = f.getAnnotation(ExcelCells.class);
			if (annos != null) {
				addMapping(annos.value(), clazz, mapping, f, mappingList);
			}
		}

		// 获取`@Excel`里配置的列映射
		if (mapping.getAnno() != null && ArrayUtils.isNotEmpty(mapping.getAnno().cells())) {
			Field f;
			for (ExcelCell cellAnno : mapping.getAnno().cells()) {
				if (StringUtils.isEmpty(cellAnno.column())) {
					throw new RuntimeException("在@" + Excel.class.getSimpleName() + "注解的cells属性中配置的列信息，column属性不能为空");
				}
				try {
					f = ReflectionUtils.getField(clazz, cellAnno.column().split("\\.")[0]);
				} catch (NoSuchFieldException e) {
					throw new RuntimeException("获取字段失败：" + cellAnno.column(), e);
				}
				// 注解转换为映射类对象
				cellMapping = toMapping(cellAnno, clazz, f, mapping);
				// 添加到列表中
				mappingList.add(cellMapping);
			}
		}

		// 根据列号排序
		mappingList.sort(Comparator.comparingInt(ExcelCellMapping::getCellNum));

		return mappingList;
	}

	/**
	 * 注解转换为映射类对象
	 *
	 * @param anno    列注解
	 * @param clazz   数据类型
	 * @param f       字段
	 * @param mapping 表映射
	 * @return 列映射
	 */
	public static ExcelCellMapping toMapping(ExcelCell anno, Class<?> clazz, Field f, ExcelMapping mapping) {
		ExcelCellMapping cellMapping = new ExcelCellMapping();

		// 属性信息
		cellMapping.setField(f);
		if (StringUtils.isNotBlank(anno.column()) && !anno.column().equals(f.getName())) {
			String column = anno.column();
			// 拼接当前的字段名到column中
			if (column.startsWith("#") || column.startsWith("$")) {
				column = f.getName() + column.substring(1);
			} else if (column.startsWith(".")) {
				column = f.getName() + column;
			} else {
				if (!column.contains(".")) {
					column = f.getName() + "." + column;
				} else {
					if (!column.startsWith(f.getName())) {
						throw new RuntimeException("在类“" + clazz.getName() + "”的属性“" + f.getName() + "”上的注解@"
								+ ExcelCell.class.getSimpleName() + "(column=\"" + column + "\")有误");
					}
				}
			}
			cellMapping.setColumn(column);

			Field cellField;
			try {
				cellField = ReflectionUtils.getField(clazz, column); // 重新获取里面的字段
			} catch (NoSuchFieldException e) {
				throw new RuntimeException("获取字段失败：" + column, e);
			}
			cellMapping.setField(cellField);

			if (cellMapping.getField() == null) {
				throw new RuntimeException("在类“" + clazz.getName() + "”中未找到属性：" + column);
			}
		}
		// 注解信息
		cellMapping.setAnno(anno);
		// excel表格信息
		cellMapping.setHeadName(anno.headName().trim()); // 列头
		cellMapping.setHeadComment(anno.headComment()); // 列头注释
		cellMapping.setCellNum(anno.cellNum()); // 列号
		cellMapping.setWidth(anno.width()); // 列宽
		cellMapping.setWrapText(anno.wrapText()); // 是否允许自动换行
		cellMapping.setHidden(anno.hidden()); // 是否隐藏列
		cellMapping.setColor(anno.color().toUpperCase()); // 字体颜色
		cellMapping.setColorIndex(ExcelColorUtils.getColorIndex(cellMapping.getColor())); // 字体颜色的索引号
		cellMapping.setBackgroundColor(anno.backgroundColor().toUpperCase()); // 背景颜色
		cellMapping.setBackgroundColorIndex(ExcelColorUtils.getColorIndex(cellMapping.getBackgroundColor())); // 背景颜色的索引号
		cellMapping.setFormat(anno.format()); // 格式化
		cellMapping.setAlign(anno.align().toLowerCase().trim()); // 水平位置
		cellMapping.setVerAlign(anno.verAlign().toLowerCase().trim()); // 竖直位置
		cellMapping.setTrueText(anno.trueText().trim()); // boolean型数据为true时，显示的文字
		cellMapping.setFalseText(anno.falseText().trim()); // boolean型数据为false时，显示的文字
		if (StringUtils.isNotBlank(anno.convert())) {
			Map<String, String> convertMap = new HashMap<>(4);
			Map<String, String> convertMap2 = new HashMap<>(4);

			String splitRegex = (anno.convert().contains("|") ? "\\|" : ",");

			String[] convertArr = anno.convert().replace("，", splitRegex).split(splitRegex);
			if (ArrayUtils.isNotEmpty(convertArr)) {
				for (String convertPair : convertArr) {
					String[] convertPairArr = convertPair.split("=");
					if (convertPairArr.length == 2) {
						convertMap.put(convertPairArr[0].trim(), convertPairArr[1].trim());
						convertMap2.put(convertPairArr[1].trim(), convertPairArr[0].trim());
					}
				}
			}

			if (convertMap.keySet().isEmpty()) {
				throw new RuntimeException("在类“" + clazz.getName() + "”中的“" + f.getName() + "”属性上的@ExcelCellMapping注解所配置的convert属性有误，请检查格式。");
			}

			cellMapping.setConvertMap(convertMap);
			cellMapping.setConvertMap2(convertMap2);
		}

		// 处理映射信息，初始化部分情况的映射内容
		if (StringUtils.isEmpty(cellMapping.getFormat())) {
			if (cellMapping.getField().getType().equals(Double.class)
					|| cellMapping.getField().getType().equals(Float.class)
					|| cellMapping.getField().getType().equals(BigDecimal.class)) {
				cellMapping.setFormat(ExcelFormats.FLOAT_2); // 浮点型数据默认格式化为“0.00_ ”
			} else if (Number.class.isAssignableFrom(cellMapping.getField().getType())) {
				cellMapping.setFormat(ExcelFormats.INT);
			} else if (cellMapping.getField().getType().equals(Date.class)) {
				cellMapping.setFormat(ExcelFormats.YYYYMMDDHHMMSS);
			}
		} else {
			// 解决浮点数字格式化不成功的问题
			if (cellMapping.getFormat().endsWith("_")) { // 如果格式化以“_”结尾时
				cellMapping.setFormat(cellMapping.getFormat() + " "); // 后面需加一个空格
			}
		}
		if (cellMapping.getWidth() <= 0) { // 已自定义列宽
			if (cellMapping.getFormat().contains(ExcelFormats.YYYY)) {
				cellMapping.setWidth((int)(cellMapping.getFormat().length() * 6.8 + 8.0 * (1 + 8.0 / cellMapping.getFormat().length())));
			}
		}

		// 计算headName的大概字符宽度，如果headName的宽度比默认的宽度或已设的宽度要大，则
		if (mapping != null && mapping.isNeedHeadRow()) {
			int headCellWidth = getCellWidthByText(cellMapping.getHeadName(), 10, true) + (mapping.isNeedFilter() ? 16 : 0);
			if (headCellWidth > 63 && headCellWidth > cellMapping.getWidth()) { // 63为excel默认的宽度
				cellMapping.setWidth(headCellWidth);
			}
		}

		if (StringUtils.isEmpty(cellMapping.getAlign())) {
			if (cellMapping.getConvertMap() != null) {
				cellMapping.setAlign("center"); // 枚举默认居中
			} else if (cellMapping.getField().getType().equals(Date.class)
					|| cellMapping.getField().getType().equals(Boolean.class)) {
				cellMapping.setAlign("center"); // 日期和boolean默认居中
			} else if (Number.class.isAssignableFrom(cellMapping.getField().getType())) {
				cellMapping.setAlign("right"); // 数字默认居右
			}
		}

		return cellMapping;
	}

	/**
	 * 根据文本获取列宽
	 * 中文算两个长度
	 *
	 * @param text   文本内容
	 * @param size   字体大小
	 * @param isBold 是否为粗体
	 * @return 列宽
	 */
	public static int getCellWidthByText(String text, int size, boolean isBold) {
		int textLength = icu.easyj.core.util.StringUtils.chineseLength(text);
		return (int)(textLength * (isBold ? 7.2 : 6.8) * (size / 10)) + 10;
	}
}