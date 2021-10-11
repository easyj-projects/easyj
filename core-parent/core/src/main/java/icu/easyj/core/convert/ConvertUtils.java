/*
 * Copyright 2021 the original author or authors.
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
package icu.easyj.core.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.hutool.core.lang.Assert;
import icu.easyj.core.convert.converter.CharSequenceToDateConverter;
import icu.easyj.core.convert.converter.DateToStringConverter;
import icu.easyj.core.enums.DataType;
import icu.easyj.core.exception.ConvertException;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 转换工具类
 *
 * @author wangliang181230
 */
public abstract class ConvertUtils {

	//region 转换服务

	private static final DefaultConversionService CONVERSION_SERVICE;

	static {
		DefaultConversionService cs = (DefaultConversionService)DefaultConversionService.getSharedInstance();

		cs.addConverter(new DateToStringConverter());
		cs.addConverter(new CharSequenceToDateConverter());

		CONVERSION_SERVICE = cs;
	}

	/**
	 * 获取转换服务
	 *
	 * @return 转换服务
	 */
	public static DefaultConversionService getConversionService() {
		return CONVERSION_SERVICE;
	}

	/**
	 * 添加转换器
	 *
	 * @param converter 转换器
	 */
	public static void addConvert(@NonNull Converter<?, ?> converter) {
		Assert.notNull(converter, "'converter' must not be null");
		CONVERSION_SERVICE.addConverter(converter);
	}

	//endregion


	//region 转换工具方法

	/**
	 * 判断是否可以转换
	 *
	 * @param sourceType 源类型
	 * @param targetType 目标类型
	 * @return 是否可以转换
	 */
	public static boolean canConvert(@Nullable Class<?> sourceType, Class<?> targetType) {
		return getConversionService().canConvert(sourceType, targetType);
	}

	/**
	 * 判断是否可以转换
	 *
	 * @param sourceType 源类型
	 * @param targetType 目标类型
	 * @return 是否可以转换
	 */
	public static boolean canConvert(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType) {
		return getConversionService().canConvert(sourceType, targetType);
	}

	/**
	 * 判断是否可以转换
	 *
	 * @param sourceType 源类型
	 * @param targetType 目标类型
	 * @return 是否可以转换
	 */
	public static boolean canConvert(@Nullable DataType sourceType, DataType targetType) {
		if (sourceType == null) {
			return true;
		} else {
			Assert.notNull(targetType, "'targetType' must not be null");
			return canConvert(sourceType.getTypeDesc(), targetType.getTypeDesc());
		}
	}

	/**
	 * 转换
	 *
	 * @param source      源对象
	 * @param targetClass 目标类
	 * @param <T>         目标类型
	 * @return 转换后的目标类对象
	 * @throws ConvertException 转换失败时，抛出该异常
	 */
	public static <T> T convert(@Nullable Object source, Class<T> targetClass) {
		if (source == null) {
			return null;
		}

		try {
			return getConversionService().convert(source, targetClass);
		} catch (ConversionException | IllegalArgumentException e) {
			throw new ConvertException("数据转换失败", e);
		}
	}

	/**
	 * 转换
	 *
	 * @param source     源对象
	 * @param targetType 目标类描述
	 * @param <T>        目标类型
	 * @return 转换后的目标类对象
	 */
	public static <T> T convert(@Nullable Object source, TypeDescriptor targetType) {
		if (source == null) {
			return null;
		}

		try {
			return (T)getConversionService().convert(source, targetType);
		} catch (ConversionException | IllegalArgumentException e) {
			throw new ConvertException("数据转换失败", e);
		}
	}

	/**
	 * 转换
	 *
	 * @param source     源对象
	 * @param targetType 目标类描述
	 * @param <T>        目标类型
	 * @return 转换后的目标类对象
	 */
	public static <T> T convert(@Nullable Object source, DataType targetType) {
		if (source == null) {
			return null;
		} else {
			Assert.notNull(targetType, "'targetType' must not be null");
			return convert(source, targetType.getTypeDesc());
		}
	}

	/**
	 * 转换
	 *
	 * @param source       源对象
	 * @param targetClass  目标类
	 * @param defaultValue 转换失败时，返回的默认值
	 * @param <T>          目标类型
	 * @return 转换后的目标类对象
	 */
	public static <T> T convert(@Nullable Object source, Class<T> targetClass, @Nullable T defaultValue) {
		if (source == null) {
			return defaultValue;
		}

		try {
			return convert(source, targetClass);
		} catch (ConvertException e) {
			return defaultValue;
		}
	}

	/**
	 * 转换
	 *
	 * @param source       源对象
	 * @param targetType   目标类描述
	 * @param defaultValue 转换失败时，返回的默认值
	 * @param <T>          目标类型
	 * @return 转换后的目标类对象
	 */
	public static <T> T convert(@Nullable Object source, TypeDescriptor targetType, @Nullable T defaultValue) {
		if (source == null) {
			return defaultValue;
		}

		try {
			return convert(source, targetType);
		} catch (ConvertException e) {
			return defaultValue;
		}
	}

	/**
	 * 转换
	 *
	 * @param source       源对象
	 * @param targetType   目标类描述
	 * @param defaultValue 转换失败时，返回的默认值
	 * @param <T>          目标类型
	 * @return 转换后的目标类对象
	 */
	public static <T> T convert(@Nullable Object source, DataType targetType, @Nullable T defaultValue) {
		if (source == null) {
			return defaultValue;
		} else {
			Assert.notNull(targetType, "'targetType' must not be null");
			return convert(source, targetType.getTypeDesc(), defaultValue);
		}
	}

	/**
	 * 转换列表数据
	 *
	 * @param sourceList  源对象列表
	 * @param targetClass 目标类
	 * @param <T>         目标类型
	 * @return 转换后的目标类对象列表
	 */
	@NonNull
	public static <T> List<T> convertList(@Nullable Collection<?> sourceList, Class<T> targetClass) {
		List<T> list = new ArrayList<>();

		if (sourceList != null) {
			for (Object source : sourceList) {
				list.add(convert(source, targetClass));
			}
		}

		return list;
	}

	/**
	 * 转换列表数据
	 *
	 * @param sourceList 源对象列表
	 * @param targetType 目标类描述
	 * @param <T>        目标类型
	 * @return 转换后的目标类对象列表
	 */
	@NonNull
	public static <T> List<T> convertList(@Nullable Collection<?> sourceList, TypeDescriptor targetType) {
		List<T> list = new ArrayList<>();

		if (sourceList != null) {
			for (Object source : sourceList) {
				list.add(convert(source, targetType));
			}
		}

		return list;
	}

	/**
	 * 转换列表数据
	 *
	 * @param sourceList 源对象列表
	 * @param targetType 目标类描述
	 * @param <T>        目标类型
	 * @return 转换后的目标类对象列表
	 */
	@NonNull
	public static <T> List<T> convertList(@Nullable Collection<?> sourceList, DataType targetType) {
		if (sourceList == null) {
			return new ArrayList<>();
		} else {
			Assert.notNull(targetType, "'targetType' must not be null");
			return convertList(sourceList, targetType.getTypeDesc());
		}
	}

	//endregion
}
