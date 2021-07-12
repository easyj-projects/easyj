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
package icu.easyj.web.filter;

import java.util.List;

/**
 * 过滤器配置基类
 *
 * @author wangliang181230
 */
public interface IFilterProperties {

	/**
	 * @return 启用状态
	 */
	boolean isEnable();

	/**
	 * @param enable 启用状态
	 */
	void setEnable(boolean enable);

	/**
	 * @return 需拦截的Url地址匹配列表
	 */
	List<String> getUrlPatterns();

	/**
	 * @param urlPatterns 需拦截的Url地址匹配列表
	 */
	void setUrlPatterns(List<String> urlPatterns);

	/**
	 * @return 无需拦截的Url地址匹配列表
	 */
	List<String> getExclusions();

	/**
	 * @param exclusions 无需拦截的Url地址匹配列表
	 */
	void setExclusions(List<String> exclusions);
}