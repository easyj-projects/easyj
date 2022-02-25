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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import icu.easyj.core.executor.Initialize;
import icu.easyj.core.loader.ExtensionDefinition;
import icu.easyj.core.loader.factory.IServiceFactory;
import org.apache.commons.lang3.ArrayUtils;

/**
 * 服务工厂类 的 默认实现
 *
 * @author wangliang181230
 */
public class DefaultServiceFactory implements IServiceFactory {

	@Override
	public <S> S create(ExtensionDefinition definition, Class<S> type, Class<?>[] argTypes, Object[] args) {
		Class clazz = definition.getServiceClass();
		try {
			return (S)initInstance(type, clazz, argTypes, args);
		} catch (Exception e) {
			throw new IllegalStateException("Extension instance(definition: " + definition + ", class: " +
					definition.getServiceClass() + ")  could not be instantiated: " + e.getMessage(), e);
		}
	}

	/**
	 * init instance
	 *
	 * @param type      the service interface class
	 * @param implClazz the impl clazz
	 * @param argTypes  the arg types
	 * @param args      the args
	 * @return s
	 * @throws IllegalAccessException    the illegal access exception
	 * @throws InstantiationException    the instantiation exception
	 * @throws NoSuchMethodException     the no such method exception
	 * @throws InvocationTargetException the invocation target exception
	 */
	private <S> S initInstance(Class<S> type, Class<S> implClazz, Class<?>[] argTypes, Object[] args)
			throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
		Constructor<S> constructor = implClazz.getDeclaredConstructor(argTypes != null ? argTypes : ArrayUtils.EMPTY_CLASS_ARRAY);
		if (!constructor.isAccessible()) {
			constructor.setAccessible(true);
		}

		S s = type.cast(constructor.newInstance(args != null ? args : ArrayUtils.EMPTY_OBJECT_ARRAY));
		if (s instanceof Initialize) {
			((Initialize)s).init();
		}
		return s;
	}
}
