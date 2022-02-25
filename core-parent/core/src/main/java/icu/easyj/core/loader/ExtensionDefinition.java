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
package icu.easyj.core.loader;

import icu.easyj.core.loader.factory.IServiceFactory;
import icu.easyj.core.util.StringUtils;

/**
 * The type ExtensionDefinition
 * 注：从阿里的seata项目中复制过来的。
 *
 * @author haozhibei
 */
public final class ExtensionDefinition {
	private final String name;
	private final Class<?> serviceClass;
	private final Integer order;
	private final Scope scope;
	private final IServiceFactory factory;

	public ExtensionDefinition(String name, Integer order, Scope scope, Class<?> serviceClass, IServiceFactory factory) {
		this.name = name;
		this.order = order;
		this.scope = scope;
		this.serviceClass = serviceClass;
		this.factory = factory;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((serviceClass == null) ? 0 : serviceClass.hashCode());
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ExtensionDefinition other = (ExtensionDefinition)obj;
		if (!StringUtils.equals(name, other.name)) {
			return false;
		}
		if (!serviceClass.equals(other.serviceClass)) {
			return false;
		}
		if (!order.equals(other.order)) {
			return false;
		}
		return !scope.equals(other.scope);
	}

	public String getName() {
		return name;
	}

	public Class<?> getServiceClass() {
		return this.serviceClass;
	}

	public Integer getOrder() {
		return this.order;
	}

	public Scope getScope() {
		return this.scope;
	}

	public IServiceFactory getFactory() {
		return factory;
	}
}
