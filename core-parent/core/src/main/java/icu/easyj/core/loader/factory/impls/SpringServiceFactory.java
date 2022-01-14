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
package icu.easyj.core.loader.factory.impls;

import cn.hutool.extra.spring.SpringUtil;
import icu.easyj.core.loader.ExtensionDefinition;
import icu.easyj.core.loader.factory.IServiceFactory;
import icu.easyj.core.util.ArrayUtils;
import org.springframework.lang.Nullable;

/**
 * 服务工厂 的 Spring实现
 *
 * @author wangliang181230
 */
public class SpringServiceFactory implements IServiceFactory {

	@Nullable
	@Override
	public <S> S create(ExtensionDefinition definition, Class<S> type, Class<?>[] argTypes, Object[] args) {
		// 当参数类型数组不为空时，使用SpringUtil获取bean
		if (ArrayUtils.isNotEmpty(argTypes)) {
			return null;
		}

		// 通过 SpringUtil 获取SpringBean
		return (S)SpringUtil.getBean(definition.getServiceClass());
	}
}
