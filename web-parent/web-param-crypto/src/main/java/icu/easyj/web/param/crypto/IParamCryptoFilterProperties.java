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
package icu.easyj.web.param.crypto;

import icu.easyj.web.filter.IFilterProperties;

/**
 * 请求参数加解密过滤器的配置
 *
 * @author wangliang181230
 */
public interface IParamCryptoFilterProperties extends IFilterProperties {

	/**
	 * 加密参数名
	 * 为空时，表示取整个QueryString
	 *
	 * @return 加密参数名
	 */
	default String getParameterName() {
		return "";
	}

	/**
	 * 设置加密参数名
	 *
	 * @param parameterName 加密参数名
	 */
	void setParameterName(String parameterName);
}
