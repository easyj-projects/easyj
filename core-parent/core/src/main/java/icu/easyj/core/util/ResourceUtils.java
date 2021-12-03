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
package icu.easyj.core.util;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import cn.hutool.core.io.IORuntimeException;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * 资源工具类
 *
 * @author wangliang181230
 */
public abstract class ResourceUtils {

	private static final Resource[] EMPTY_RESOURCE_ARRAY = new Resource[0];

	private static final ResourcePatternResolver RESOURCE_RESOLVER = new PathMatchingResourcePatternResolver();


	/**
	 * 获取位置下的所有文件资源
	 *
	 * @param locationPattern 要解析的位置
	 * @return resources 资源数组
	 */
	public static Resource[] getResources(String locationPattern) {
		try {
			return RESOURCE_RESOLVER.getResources(locationPattern);
		} catch (IOException e) {
			return EMPTY_RESOURCE_ARRAY;
		}
	}

	/**
	 * 获取所有位置下的所有文件资源
	 *
	 * @param locationPatternArr 要解析的位置数组
	 * @return resources 资源数组
	 */
	public static Resource[] getResources(String... locationPatternArr) {
		return Stream
				.of(Optional.ofNullable(locationPatternArr).orElse(ArrayUtils.EMPTY_STRING_ARRAY))
				.flatMap(locationPattern -> Stream.of(getResources(locationPattern)))
				.toArray(Resource[]::new);
	}

	/**
	 * 获取资源路径
	 *
	 * @param resource 目录或文件资源
	 * @return 资源路径
	 */
	public static String getResourceUri(Resource resource) {
		try {
			return resource.getURI().toString();
		} catch (IOException e) {
			throw new IORuntimeException("获取资源路径失败", e);
		}
	}
}
