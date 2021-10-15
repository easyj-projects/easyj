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
package icu.easyj.core.loader.condition;

import icu.easyj.core.util.JarInfo;
import icu.easyj.core.util.JarUtils;
import icu.easyj.core.util.VersionUtils;

/**
 * 依赖Jar及其版本校验器
 *
 * @author wangliang181230
 */
public class DependsOnJarValidator implements IDependsOnValidator {

	@Override
	public void validate(Class<?> serviceClass, ClassLoader classLoader) throws ServiceDependencyException {
		// 获取注解`@DependsOnJar`的信息，并判断Jar版本是否符合
		DependsOnJar dependsOnJar = serviceClass.getAnnotation(DependsOnJar.class);
		if (dependsOnJar != null) {
			// Jar名称
			String name = dependsOnJar.name();
			JarInfo jarInfo = JarUtils.getJar(name, classLoader);
			if (jarInfo == null) {
				throw new ServiceDependencyException("jar [" + name + "] not found");
			}

			long minVersion = VersionUtils.toLong(dependsOnJar.minVersion());
			long maxVersion = VersionUtils.toLong(dependsOnJar.maxVersion());
			if (minVersion == 0 && maxVersion == 0) {
				return;
			}

			if (minVersion > 0 && jarInfo.getVersionLong() < minVersion) {
				throw new ServiceDependencyException("jar[" + name + "] version is less than v" + dependsOnJar.minVersion());
			}
			if (maxVersion > 0 && jarInfo.getVersionLong() > maxVersion) {
				throw new ServiceDependencyException("jar[" + name + "] version is greater than v" + dependsOnJar.maxVersion());
			}
		}
	}
}
