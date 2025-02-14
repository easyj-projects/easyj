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

import javax.annotation.Nonnull;

import icu.easyj.core.util.StringUtils;
import org.springframework.core.Ordered;

/**
 * 服务信息
 *
 * @param <S>
 * @author wangliang181230
 * @since 0.8.0
 */
public class ServiceInfo<S> {

	private final S service;

	private final String code;
	private final boolean isDefault;
	private final int order;

	private ServiceInfo(String code, boolean isDefault, int order, S service) {
		this.code = code.toLowerCase(); // 转为小写
		this.isDefault = isDefault;
		this.order = order;
		this.service = service;
	}

	@Nonnull
	public static <S> ServiceInfo<S> of(S service) {
		if (service == null) {
			throw new IllegalArgumentException("service不能为空");
		}

		ServiceMark mark = service.getClass().getAnnotation(ServiceMark.class);
		String code = mark.code();
		if (StringUtils.isBlank(code)) {
			throw new IllegalArgumentException("service的@ServiceMark.code或@ServiceMark.value不能为空");
		}

		return new ServiceInfo<>(code, mark.isDefault(), mark.order(), service);
	}

	public static <S> ServiceInfo<S> of(String code, S service) {
		if (StringUtils.isBlank(code)) {
			throw new IllegalArgumentException("code不能为空");
		}
		if (service == null) {
			throw new IllegalArgumentException("service不能为空");
		}

		return new ServiceInfo<>(code, false, 0, service);
	}

	public static <S> ServiceInfo<S> of(String code, boolean isDefault, S service) {
		if (StringUtils.isBlank(code)) {
			throw new IllegalArgumentException("code不能为空");
		}
		if (service == null) {
			throw new IllegalArgumentException("service不能为空");
		}

		return new ServiceInfo<>(code, isDefault, Ordered.LOWEST_PRECEDENCE, service);
	}

	public static <S> ServiceInfo<S> of(String code, boolean isDefault, int order, S service) {
		if (StringUtils.isBlank(code)) {
			throw new IllegalArgumentException("code不能为空");
		}
		if (service == null) {
			throw new IllegalArgumentException("service不能为空");
		}

		return new ServiceInfo<>(code, isDefault, order, service);
	}


	@Nonnull
	public S getService() {
		return service;
	}

	@Nonnull
	public String getCode() {
		return code;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public int getOrder() {
		return order;
	}


	@Override
	public String toString() {
		return (isDefault ? "(default) " : "") + (code + " -> " + service.getClass().getName());
	}

	public String toStringNoCode() {
		return (isDefault ? "(default) " : "") + service.getClass().getName();
	}
}
