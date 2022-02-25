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
package icu.easyj.core.loader.factory;

import icu.easyj.core.loader.ExtensionDefinition;
import org.springframework.lang.Nullable;

/**
 * 服务工厂接口
 *
 * @author wangliang181230
 */
public interface IServiceFactory {

	/**
	 * 创建服务实例
	 *
	 * @param definition 服务扩展定义
	 * @return 服务实例，允许为空；为空时，由默认服务工厂生成实例。
	 */
	@Nullable
	<S> S create(ExtensionDefinition definition, Class<S> type, Class<?>[] argTypes, Object[] args);
}
