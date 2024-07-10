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
package icu.easyj.core.loader.condition;

import cn.hutool.system.JavaInfo;
import cn.hutool.system.SystemUtil;

/**
 * 依赖Java版本校验器
 *
 * @author wangliang181230
 */
public class DependsOnJavaVersionValidator implements IDependsOnValidator {

	@Override
	public void validate(Class<?> serviceClass, ClassLoader classLoader) throws ServiceDependencyException {
		// 获取注解`@DependsOnJavaVersion`的信息，并判断Java版本是否符合
		DependsOnJavaVersion dependsOnJavaVersion = serviceClass.getAnnotation(DependsOnJavaVersion.class);
		if (dependsOnJavaVersion == null) {
			return;
		}

		// 获取依赖的Java版本范围
		int dependsOnMinJavaVersion = (int)(dependsOnJavaVersion.min() * 100);
		int dependsOnMaxJavaVersion = (int)(dependsOnJavaVersion.max() * 100);
		// 默认包含所有小版本处理
		dependsOnMaxJavaVersion = this.handleDependsOnMaxJavaVersion(dependsOnMaxJavaVersion);
		// 判断依赖的Java版本
		if (dependsOnMinJavaVersion > 0 || dependsOnMaxJavaVersion > 0) {
			JavaInfo javaInfo = SystemUtil.getJavaInfo();
			if (dependsOnMinJavaVersion > 0 && javaInfo.getVersionInt() < dependsOnMinJavaVersion) {
				throw new ServiceDependencyException("java version is less than v" + dependsOnJavaVersion.min());
			}
			if (dependsOnMaxJavaVersion > 0 && javaInfo.getVersionInt() > dependsOnMaxJavaVersion) {
				throw new ServiceDependencyException("java version is greater than v" + dependsOnJavaVersion.max());
			}
		}
	}

	private int handleDependsOnMaxJavaVersion(int dependsOnMaxJavaVersion) {
		if (dependsOnMaxJavaVersion > 0) {
			if (dependsOnMaxJavaVersion < 190) {
				if (dependsOnMaxJavaVersion % 10 == 0) {
					dependsOnMaxJavaVersion += 9;
				}
			} else {
				if (dependsOnMaxJavaVersion % 100 == 0) {
					dependsOnMaxJavaVersion += 99;
				}
			}
		}
		return dependsOnMaxJavaVersion;
	}
}
