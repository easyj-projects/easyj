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

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * 资源工具类
 *
 * @author wangliang181230
 */
public abstract class ResourceUtils {

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
			return new Resource[0];
		}
	}

	/**
	 * 获取所有位置下的所有文件资源
	 *
	 * @param locationPatternArr 要解析的位置数组
	 * @return resources 资源数组
	 */
	public static Resource[] getResources(String[] locationPatternArr) {
		return Stream
				.of(Optional.ofNullable(locationPatternArr).orElse(new String[0]))
				.flatMap(locationPattern -> Stream.of(getResources(locationPattern)))
				.toArray(Resource[]::new);
	}
}
