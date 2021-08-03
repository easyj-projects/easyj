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
package icu.easyj.spring.boot.env.enhanced.impls;

import java.util.List;

import icu.easyj.core.loader.LoadLevel;
import org.springframework.util.StringUtils;

/**
 * 如果类存在，则添加配置源；反之，则不添加。
 *
 * @author wangliang181230
 */
@LoadLevel(name = "on-class", order = 1)
public class OnClassPropertySourceFilter extends AbstractConditionPropertySourceFilter {

	public static final String PROPERTY_NAME = "easyj.config.activate.on-class";

	public OnClassPropertySourceFilter() {
		super(PROPERTY_NAME);
	}


	//region Override

	@Override
	public boolean doConditionFilter(List<String> conditionPropertyList) {
		try {
			for (String property : conditionPropertyList) {
				if (StringUtils.hasLength(property)) {
					// 尝试加载类
					this.loadClass(property);
				}
			}
			// 类存在，不过滤
			return false;
		} catch (ClassNotFoundException ignore) {
			// 类不存在，过滤掉
			return true;
		}
	}

	//endregion


	/**
	 * 尝试加载类
	 *
	 * @param className 类名
	 * @throws ClassNotFoundException 类未找到的异常
	 */
	private void loadClass(String className) throws ClassNotFoundException {
		Class.forName(className, false, Thread.currentThread().getContextClassLoader());
	}
}
