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

import icu.easyj.core.util.ArrayUtils;

/**
 * 依赖类校验器
 *
 * @author wangliang181230
 */
public class DependsOnClassValidator implements IDependsOnValidator {

	@Override
	public void validate(Class<?> serviceClass, ClassLoader classLoader) throws ServiceDependencyException {
		// 获取注解的信息
		DependsOnClass dependsOnClass;
		try {
			// 在java11之前的版本中，如果设置的value中有类不存在，则会在这行代码直接抛出ArrayStoreException异常
			dependsOnClass = serviceClass.getAnnotation(DependsOnClass.class);
		} catch (ArrayStoreException | TypeNotPresentException e) {
			throw new ServiceDependencyException("the depends on classes is not found", e);
		}

		if (dependsOnClass == null) {
			return;
		}

		try {
			// 在java11及以上版本中，必须访问过一次注解的属性值，才会抛出TypeNotPresentException异常
			@SuppressWarnings("all")
			Class<?>[] dependsOnClasses = dependsOnClass.value();
		} catch (ArrayStoreException | TypeNotPresentException e) {
			if (dependsOnClass.strategy() == ValidateStrategy.ALL) {
				throw new ServiceDependencyException("the depends on classes is not found", e);
			}
		}

		// 校验类名对应的类是否存在
		this.validateClassNames(dependsOnClass, classLoader);
	}

	/**
	 * 校验类名对应的类是否存在
	 *
	 * @param dependsOnClass 注解信息
	 * @param classLoader    类加载器
	 * @throws ServiceDependencyException 异常
	 */
	private void validateClassNames(DependsOnClass dependsOnClass, ClassLoader classLoader) throws ServiceDependencyException {
		// 根据类名判断
		String[] dependsOnClassNames = dependsOnClass.name();
		if (ArrayUtils.isEmpty(dependsOnClassNames)) {
			return;
		}

		if (dependsOnClass.strategy() == ValidateStrategy.ALL) {
			try {
				for (String dependsOnClassName : dependsOnClassNames) {
					Class.forName(dependsOnClassName, true, classLoader);
				}
			} catch (ArrayStoreException | TypeNotPresentException | ClassNotFoundException e) {
				throw new ServiceDependencyException("the depends on classes is not found", e);
			}
		} else {
			for (String dependsOnClassName : dependsOnClassNames) {
				try {
					Class.forName(dependsOnClassName, true, classLoader);
					// 只要有一个类加载成功，就返回
					return;
				} catch (ArrayStoreException | TypeNotPresentException | ClassNotFoundException ignore) {
					// 这里不抛异常，继续校验下一个类是否存在
				}
			}
			throw new ServiceDependencyException("all of the depends on classes is not found");
		}
	}
}
