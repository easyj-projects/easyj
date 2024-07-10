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

import icu.easyj.core.util.ArrayUtils;
import icu.easyj.core.util.jar.JarInfo;
import icu.easyj.core.util.jar.JarUtils;
import icu.easyj.core.util.version.VersionUtils;

/**
 * 依赖JAR及其版本校验器
 *
 * @author wangliang181230
 */
public class DependsOnJarVersionValidator implements IDependsOnValidator {

	@Override
	public void validate(Class<?> serviceClass, ClassLoader classLoader) throws ServiceDependencyException {
		// 获取注解`@DependsOnJarVersion`的信息，并判断JAR版本是否符合
		DependsOnJarVersion dependsOnJarVersion = serviceClass.getAnnotation(DependsOnJarVersion.class);
		if (dependsOnJarVersion == null) {
			return;
		}

		// 获取JAR信息，如果不存在，则抛出依赖异常
		String[] names = dependsOnJarVersion.name();
		JarInfo jarInfo = null;
		for (String name : names) {
			jarInfo = JarUtils.getJar(name, classLoader);
			if (jarInfo != null) {
				break;
			}
		}
		if (jarInfo == null) {
			throw new ServiceDependencyException("jar " + ArrayUtils.toString(names) + " not found");
		}

		// 获取JAR版本号限制，如果都为0，则说明不限制
		long minVersion = VersionUtils.toLong(dependsOnJarVersion.minVersion());
		long maxVersion = VersionUtils.toLong(dependsOnJarVersion.maxVersion());
		if (minVersion == 0 && maxVersion == 0) {
			return;
		}

		// 判断JAR版本号是否符合设置值
		if (minVersion > 0 && jarInfo.getVersionLong() < minVersion) {
			throw new ServiceDependencyException("JAR[" + jarInfo.getName() + "] version is less than v" + dependsOnJarVersion.minVersion());
		}
		if (maxVersion > 0 && jarInfo.getVersionLong() > maxVersion) {
			throw new ServiceDependencyException("JAR[" + jarInfo.getName() + "] version is greater than v" + dependsOnJarVersion.maxVersion());
		}
	}
}
