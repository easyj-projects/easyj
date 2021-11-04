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
package icu.easyj.web.param.crypto.impls;

import icu.easyj.core.util.StringUtils;
import icu.easyj.web.filter.BaseFilterProperties;
import icu.easyj.web.param.crypto.IParamCryptoFilterProperties;

/**
 * 请求参数加解密过滤器的配置
 *
 * @author wangliang181230
 */
public class DefaultParamCryptoFilterPropertiesImpl extends BaseFilterProperties
		implements IParamCryptoFilterProperties {

	public DefaultParamCryptoFilterPropertiesImpl() {
		// 参数加解密过滤器，默认禁用，需要配置为true才启用
		super.setEnabled(false);
	}


	/**
	 * 加密参数名
	 * <p>
	 * 为空时，表示取整个queryString作为加密参数值，即：{@code request.getQueryString()}<br>
	 * 非时时，表示取该参数名的参数值作为加密参数值，即：{@code request.getParameter(String name)}
	 */
	private String queryStringName;


	//region Getter、Setter

	@Override
	public String getQueryStringName() {
		return queryStringName;
	}

	@Override
	public void setQueryStringName(String queryStringName) {
		if (StringUtils.isNotBlank(queryStringName)) {
			queryStringName = queryStringName.trim();
		} else {
			queryStringName = null;
		}
		this.queryStringName = queryStringName;
	}

	//endregion
}
