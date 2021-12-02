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
package icu.easyj.spring.boot.env.enhanced;

import java.util.List;

import icu.easyj.spring.boot.util.EnvironmentUtils;
import org.springframework.core.env.MapPropertySource;

/**
 * 配置源条件过滤器
 *
 * @author wangliang181230
 */
public abstract class AbstractConditionPropertySourceFilter implements IPropertySourceFilter {

	private final String propertyName;

	protected AbstractConditionPropertySourceFilter(String propertyName) {
		this.propertyName = propertyName;
	}

	@Override
	public boolean doFilter(MapPropertySource propertySource) {
		List<String> propertyList = EnvironmentUtils.getPropertyList(propertySource, propertyName);
		if (propertyList.isEmpty()) {
			// 配置不存在，不过滤
			return false;
		}

		return doConditionFilter(propertyList);
	}

	/**
	 * 执行条件过滤
	 *
	 * @param conditionPropertyList 条件配置列表
	 * @return 是否需要过滤，true=过滤掉|false=不过滤
	 */
	protected abstract boolean doConditionFilter(List<String> conditionPropertyList);
}
