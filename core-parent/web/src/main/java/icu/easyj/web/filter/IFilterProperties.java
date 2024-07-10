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
package icu.easyj.web.filter;

import java.util.List;

/**
 * 过滤器配置基类
 *
 * @author wangliang181230
 */
public interface IFilterProperties {

	/**
	 * 过滤器启用状态<br>
	 * 用于判断该过滤器是否启用，该状态一旦服务启动，就无法动态变更。
	 *
	 * @return 过滤器启用状态
	 */
	default boolean isEnabled() {
		return true;
	}

	/**
	 * 设置过滤器启用状态
	 *
	 * @param enabled 启用状态
	 */
	void setEnabled(boolean enabled);

	/**
	 * 过滤器使用状态<br>
	 * 用于判断该过滤器是否禁用，该状态在服务运行期间，可动态变更
	 *
	 * @return 过滤器使用状态，true=禁用|false=启用(默认)
	 */
	default boolean isDisabled() {
		return false;
	}

	/**
	 * 设置过滤器使用状态
	 *
	 * @param disabled 使用状态
	 */
	void setDisabled(boolean disabled);

	/**
	 * 获取过滤器执行顺序
	 *
	 * @return 过滤器执行顺序
	 */
	Integer getOrder();

	/**
	 * 设置执行顺序
	 *
	 * @param order 执行顺序
	 */
	void setOrder(int order);

	/**
	 * 获取需拦截的Url地址匹配列表
	 *
	 * @return 需拦截的Url地址匹配列表
	 */
	List<String> getUrlPatterns();

	/**
	 * 设置需拦截的Url地址匹配列表
	 *
	 * @param urlPatterns 需拦截的Url地址匹配列表
	 */
	void setUrlPatterns(List<String> urlPatterns);

	/**
	 * 获取无需拦截的Url地址匹配列表
	 *
	 * @return 无需拦截的Url地址匹配列表
	 */
	List<String> getExclusions();

	/**
	 * 设置无需拦截的Url地址匹配列表
	 *
	 * @param exclusions 无需拦截的Url地址匹配列表
	 */
	void setExclusions(List<String> exclusions);
}