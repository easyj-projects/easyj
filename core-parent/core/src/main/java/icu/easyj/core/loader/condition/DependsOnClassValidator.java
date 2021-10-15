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

/**
 * 依赖类校验器
 *
 * @author wangliang181230
 */
public class DependsOnClassValidator implements IDependsOnValidator {

	@Override
	public void validate(Class<?> serviceClass, ClassLoader classLoader) throws ServiceDependencyException {
		// 获取注解`@DependsOnClass`的信息，并判断类是否存在
		try {
			DependsOnClass dependsOnClass = serviceClass.getAnnotation(DependsOnClass.class);
			if (dependsOnClass != null) {
				// 在java11及以上版本中，必须访问过一次注解的属性值，才会抛出TypeNotPresentException异常
				@SuppressWarnings("all")
				Class<?>[] dependsOnClasses = dependsOnClass.value();

				// 根据类名判断
				String[] dependsOnClassNames = dependsOnClass.name();
				for (String dependsOnClassName : dependsOnClassNames) {
					Class.forName(dependsOnClassName, true, classLoader);
				}
			}
		} catch (ArrayStoreException | TypeNotPresentException | ClassNotFoundException e) {
			throw new ServiceDependencyException("the depends on classes is not found", e);
		}
	}
}
