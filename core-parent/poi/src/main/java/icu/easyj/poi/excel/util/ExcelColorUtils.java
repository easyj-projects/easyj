/*
 * Copyright 2021-2024 the original author or authors.
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
package icu.easyj.poi.excel.util;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import icu.easyj.core.util.ReflectionUtils;
import icu.easyj.core.util.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * Excel 颜色相关工具类
 *
 * @author wangliang181230
 */
public abstract class ExcelColorUtils {

	public static final short TEAL_INDEX = 21;


	/**
	 * 获取颜色的数字值，即颜色值从16进制转换为10进制后的值
	 *
	 * @param color 颜色
	 * @return colorIndex 颜色index值
	 */
	public static short getColorIndex(final String color) {
		if (StringUtils.isEmpty(color)) {
			return 0;
		}

		// 从 HSSFColor.HSSFColorPredefined 枚举中匹配颜色
		try {
			// 使用反射，为了兼容低版本POI
			Class<?> HSSFColorPredefinedClass = Class.forName(HSSFColor.class.getName() + "$HSSFColorPredefined");
			Object[] colors = HSSFColorPredefinedClass.getEnumConstants();
			for (Object colorEnum : colors) {
				if (colorEnum.toString().equalsIgnoreCase(color)) {
					try {
						return (Short)ReflectionUtils.invokeMethod(colorEnum, "getIndex");
					} catch (NoSuchMethodException | InvocationTargetException e) {
						throw new RuntimeException(e);
					}
				}
			}
		} catch (ClassNotFoundException e) {
			Collection<HSSFColor> colorList = HSSFColor.getTripletHash().values();
			for (HSSFColor hssfColor : colorList) {
				if (hssfColor.getClass().getSimpleName().equalsIgnoreCase(color)) {
					return hssfColor.getIndex();
				}
			}
		}

		short colorIndexRet = 0;

		String colorHex = color;
		if (colorHex.contains("#")) {
			colorHex = StringUtils.remove(colorHex, '#');
		}

		if (colorHex.length() == 3) {
			colorHex = "" + colorHex.charAt(0) + colorHex.charAt(0) + colorHex.charAt(1) + colorHex.charAt(1) + colorHex.charAt(2) + colorHex.charAt(2);
		}

		if (colorHex.length() == 6) {
			// 将颜色字符串转换为
			Color awtColor = Color.getColor(null, Integer.valueOf(colorHex, 16));
			for (Integer colorIndex : HSSFColor.getIndexHash().keySet()) {
				HSSFColor hssfColor = HSSFColor.getIndexHash().get(colorIndex);
				short[] triplet = hssfColor.getTriplet();
				if (triplet == null || triplet.length < 3) {
					continue;
				}
				if (awtColor.getRed() == triplet[0] && awtColor.getGreen() == triplet[1] && awtColor.getBlue() == triplet[2]) {
					colorIndexRet = Short.parseShort(colorIndex.toString());
					break;
				}
			}
		}

		return colorIndexRet;
	}

}
