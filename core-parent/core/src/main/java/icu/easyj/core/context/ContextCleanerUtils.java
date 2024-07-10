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
package icu.easyj.core.context;

import icu.easyj.core.loader.EnhancedServiceLoader;

import java.util.List;

/**
 * 上下文清理者工具类
 *
 * @author wangliang181230
 */
public abstract class ContextCleanerUtils {

	//region 上下文清理者单例持有者（设计模式-创建型模式-单例模式：枚举实现单例）

	private enum ContextCleanerSingletonHolder {
		// 单例
		INSTANCE;

		private final List<IContextCleaner> instances = EnhancedServiceLoader.loadAll(IContextCleaner.class);

		public List<IContextCleaner> getInstances() {
			return INSTANCE.instances;
		}
	}

	static List<IContextCleaner> getInstances() {
		return ContextCleanerSingletonHolder.INSTANCE.getInstances();
	}

	//endregion


	/**
	 * 清空所有上下文
	 */
	public static void clear() {
		getInstances().forEach(IContextCleaner::clear);
	}
}
