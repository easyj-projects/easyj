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
public class BaseFilterProperties implements IFilterProperties {

	/**
	 * 过滤器启用状态，默认：生效<br>
	 */
	protected boolean enable = true;

	/**
	 * 过滤器使用状态，默认：启用<br>
	 * volatile关键字是必须的
	 */
	protected volatile boolean disabled = false;

	/**
	 * 过滤器需要过滤的请求，默认：全部都过滤
	 */
	protected List<String> urlPatterns = null;

	/**
	 * 过滤器不想过滤的请求，默认：无排除项
	 */
	protected List<String> exclusions = null;


	//region Getter、Setter

	@Override
	public boolean isEnable() {
		return enable;
	}

	@Override
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	@Override
	public boolean isDisabled() {
		return disabled;
	}

	@Override
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	@Override
	public List<String> getUrlPatterns() {
		return urlPatterns;
	}

	@Override
	public void setUrlPatterns(List<String> urlPatterns) {
		this.urlPatterns = urlPatterns;
	}

	@Override
	public List<String> getExclusions() {
		return exclusions;
	}

	@Override
	public void setExclusions(List<String> exclusions) {
		this.exclusions = exclusions;
	}

	//endregion
}
