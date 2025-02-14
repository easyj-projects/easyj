/*
 * Copyright 2021-2025 the original author or authors.
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
package icu.easyj.core.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 简易服务工厂
 *
 * @param <S> 服务类
 * @author wangliang181230
 * @since 0.8.0
 */
@SuppressWarnings("unused")
public class ServiceFactory<S> {

	public static final String DEFAULT_SERVICE_CODE = "default";


	private final Class<S> serviceClass;
	private final HashMap<String, ServiceGroup<S>> serviceMap = new HashMap<>();
	private final List<ServiceGroup<S>> serviceList = new ArrayList<>();


	public ServiceFactory(Class<S> serviceClass) {
		this.serviceClass = serviceClass;
	}

	public ServiceFactory(Class<S> serviceClass, List<S> serviceList) {
		this(serviceClass);

		this.addAll(serviceList);
	}

	/**
	 * 获取服务类
	 *
	 * @return 服务类
	 */
	public Class<S> getServiceClass() {
		return serviceClass;
	}


	// get ---------------------------------------------------------------

	/**
	 * 获取默认实现类
	 *
	 * @return 默认实现类
	 */
	@Nullable
	public S getDefault() {
		return get(DEFAULT_SERVICE_CODE);
	}

	/**
	 * 获取默认组
	 *
	 * @return 默认实现类
	 */
	@Nullable
	public ServiceGroup<S> getDefaultGroup() {
		return serviceMap.get(DEFAULT_SERVICE_CODE);
	}

	/**
	 * 获取默认实现类
	 *
	 * @return 默认实现类
	 */
	@Nonnull
	public List<S> getDefaultList() {
		return getList(DEFAULT_SERVICE_CODE);
	}

	@Nullable
	public S get(String code) {
		ServiceGroup<S> serviceGroup = serviceMap.get(code);
		if (serviceGroup == null) {
			return null;
		}
		return serviceGroup.getFirst();
	}

	@Nullable
	public S get(String code, boolean getDefaultIfNull) {
		S service = get(code);
		if (service == null && getDefaultIfNull) {
			service = getDefault();
		}
		return service;
	}

	@Nonnull
	public List<S> getList(String code) {
		ServiceGroup<S> serviceGroup = serviceMap.get(code);
		if (serviceGroup == null) {
			return new ArrayList<>();
		}
		return serviceGroup.getAllService();
	}

	@Nullable
	public ServiceGroup<S> getGroup(String code) {
		return serviceMap.get(code);
	}

	@Nullable
	public ServiceGroup<S> getGroup(String code, boolean getDefaultIfNull) {
		ServiceGroup<S> group = getGroup(code);
		if (group == null && getDefaultIfNull) {
			group = getDefaultGroup();
		}
		return group;
	}


	// add ---------------------------------------------------------------

	public void add(S service) {
		ServiceInfo<S> serviceInfo = ServiceInfo.of(service);
		this.serviceMap.computeIfAbsent(serviceInfo.getCode(), ServiceGroup::new).add(serviceInfo);

		if (serviceInfo.isDefault() && !DEFAULT_SERVICE_CODE.equals(serviceInfo.getCode())) {
			this.serviceMap.computeIfAbsent(DEFAULT_SERVICE_CODE, ServiceGroup::new).add(serviceInfo);
		}
	}

	public void addAll(List<S> serviceList) {
		if (serviceList == null || serviceList.isEmpty()) {
			return;
		}

		HashMap<String, List<ServiceInfo<S>>> map = new HashMap<>();
		for (S service : serviceList) {
			ServiceInfo<S> serviceInfo = ServiceInfo.of(service);
			map.computeIfAbsent(serviceInfo.getCode(), k -> new ArrayList<>()).add(serviceInfo);

			if (serviceInfo.isDefault() && !DEFAULT_SERVICE_CODE.equals(serviceInfo.getCode())) {
				map.computeIfAbsent(DEFAULT_SERVICE_CODE, k -> new ArrayList<>()).add(serviceInfo);
			}
		}

		map.forEach((code, serviceInfoList) -> {
			ServiceGroup<S> group = this.serviceMap.computeIfAbsent(code, ServiceGroup::new);
			group.addAll(serviceInfoList);
		});
	}

}
