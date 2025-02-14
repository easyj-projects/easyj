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
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import icu.easyj.core.util.CollectionUtils;
import icu.easyj.core.util.StringUtils;

/**
 * code相同的服务组
 *
 * @param <S> 服务类型
 * @author wangliang181230
 * @since 0.8.0
 */
@SuppressWarnings("unused")
public class ServiceGroup<S> {

	@Nonnull
	private final String code;

	/**
	 * 已排好序的 服务列表
	 */
	@Nonnull
	private final List<ServiceInfo<S>> sortedServiceList;

	/**
	 * 构造方法
	 */
	public ServiceGroup(String code) {
		if (StringUtils.isBlank(code)) {
			throw new IllegalArgumentException("code 不能为空");
		}

		this.code = code.toLowerCase();
		this.sortedServiceList = new ArrayList<>();
	}


	// get --------------------------------------------------------------------------

	@Nullable
	public ServiceInfo<S> getFirstInfo() {
		if (CollectionUtils.isNotEmpty(this.sortedServiceList)) {
			return this.sortedServiceList.get(0);
		} else {
			return null;
		}
	}

	@Nullable
	public S getFirst() {
		if (CollectionUtils.isNotEmpty(this.sortedServiceList)) {
			return this.sortedServiceList.get(0).getService();
		} else {
			return null;
		}
	}

	public List<ServiceInfo<S>> getAllInfo() {
		return new ArrayList<>(this.sortedServiceList);
	}

	public List<S> getAllService() {
		List<S> serviceList = new ArrayList<>();
		for (ServiceInfo<S> serviceInfo : this.sortedServiceList) {
			serviceList.add(serviceInfo.getService());
		}
		return serviceList;
	}


	// add --------------------------------------------------------------------------

	public void add(ServiceInfo<S> serviceInfo) {
		this.checkServiceInfo(serviceInfo);

		this.sortedServiceList.add(serviceInfo);
		this.sort();
	}

	public void addAll(List<ServiceInfo<S>> serviceInfoList) {
		if (CollectionUtils.isEmpty(serviceInfoList)) {
			return;
		}

		serviceInfoList.forEach(this::checkServiceInfo);

		this.sortedServiceList.addAll(serviceInfoList);
		this.sort();
	}


	// Getter ---------------------------------------------------------------

	public String getCode() {
		return code;
	}

	public boolean isDefaultGroup() {
		return ServiceFactory.DEFAULT_SERVICE_CODE.equals(this.code);
	}

	public List<ServiceInfo<S>> getOriginalList() {
		return this.sortedServiceList;
	}


	// Private ---------------------------------------------------------------

	private void checkServiceInfo(ServiceInfo<S> serviceInfo) {
		if (ServiceFactory.DEFAULT_SERVICE_CODE.equals(this.code) && serviceInfo.isDefault()) {
			return;
		}

		if (!Objects.equals(this.code, serviceInfo.getCode())) {
			throw new IllegalArgumentException("serviceInfo 的 code 与 ServiceGroup 的 code 不一致");
		}
	}

	/**
	 * 按 order、code、className 排序
	 */
	private void sort() {
		this.sortedServiceList.sort((a, b) -> {
			int ret = a.getOrder() - b.getOrder();
			if (ret == 0) {
				ret = a.getCode().compareTo(b.getCode());
				if (ret == 0) {
					ret = a.getService().getClass().getName().compareTo(b.getService().getClass().getName());
				}
			} else {
				if (ret < 0) {
					ret = -1;
				} else {
					ret = 1;
				}
			}
			return ret;
		});
	}


	@Override
	public String toString() {
		if (this.sortedServiceList.isEmpty()) {
			return "{ \"code\": \"" + this.code + "\"}";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("{ \"code\": \"").append(this.code).append("\", \"list\": [ ");
		int length = sb.length();

		for (ServiceInfo<S> serviceInfo : this.sortedServiceList) {
			if (sb.length() > length) {
				sb.append(", ");
			}
			sb.append('"').append(isDefaultGroup() ? serviceInfo.toString() : serviceInfo.toStringNoCode()).append('"');
		}

		sb.append(" ]}");

		return sb.toString();
	}

}
