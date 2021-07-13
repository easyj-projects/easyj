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

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import icu.easyj.web.constant.FilterOrderConstants;
import icu.easyj.web.filter.AbstractFilter;
import icu.easyj.web.param.crypto.exception.ParamDecryptException;
import icu.easyj.web.param.crypto.exception.ParamNotEncryptedException;
import icu.easyj.web.util.HttpUtils;
import icu.easyj.web.wrapper.QueryStringHttpServletRequestWrapper;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 入参解密过滤器
 *
 * @author wangliang181230
 */
@Order(FilterOrderConstants.PARAM_ENCRYPT)
public class ParamCryptoFilter extends AbstractFilter<IParamCryptoFilterProperties> {

	/**
	 * 参数加密处理器配置
	 */
	private final IParamCryptoHandlerProperties cryptoHandlerProperties;

	/**
	 * 参数加密处理器
	 */
	private final IParamCryptoHandler cryptoHandler;

	/**
	 * 构造函数
	 *
	 * @param filterProperties        拦截器配置
	 * @param cryptoHandlerProperties 参数加密处理器配置
	 * @param cryptoHandler           参数加密处理器
	 */
	public ParamCryptoFilter(IParamCryptoFilterProperties filterProperties,
							 IParamCryptoHandlerProperties cryptoHandlerProperties,
							 IParamCryptoHandler cryptoHandler) {
		super(filterProperties);

		Assert.notNull(cryptoHandler, "'cryptoHandler' must be not null");
		Assert.notNull(cryptoHandlerProperties, "'cryptoHandlerProperties' must be not null");

		this.cryptoHandlerProperties = cryptoHandlerProperties;
		this.cryptoHandler = cryptoHandler;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		// 转换类型
		@SuppressWarnings("all")
		HttpServletRequest request = (HttpServletRequest)servletRequest;

		// 跳过内部请求 或 判断是否需要执行当前过滤器
		if (HttpUtils.isInternalRequest() || !super.isNeedDoFilter(request)) {
			filterChain.doFilter(servletRequest, servletResponse);
			return;
		}

		// 解密QueryString
		request = this.decryptQueryString(request);
		// 解密Body
		request = this.decryptBody(request);
		// 解密Form
		request = this.decryptForm(request);

		// 继续执行过滤器
		filterChain.doFilter(request, servletResponse);
	}

	/**
	 * 解密QueryString
	 *
	 * @param request 请求实例
	 * @return 返回原request或包装后的request
	 */
	private HttpServletRequest decryptQueryString(HttpServletRequest request) {
		// 待解密的参数
		String encryptedData;
		if (StringUtils.hasLength(super.filterProperties.getParameterName())) {
			// 判断是否有参数未加密
			if (request.getParameterMap().size() > 1) {
				Set<String> parameterNames = new HashSet<>(request.getParameterMap().keySet());
				parameterNames.remove(super.filterProperties.getParameterName());
				throw new ParamNotEncryptedException("存在未加密的参数：" + icu.easyj.core.util.StringUtils.toString(parameterNames));
			}
			encryptedData = request.getParameter(super.filterProperties.getParameterName());
		} else {
			encryptedData = request.getQueryString();
		}

		// 如果不为空，才需要解密
		if (StringUtils.hasText(encryptedData)) {
			// 解密后，正常的queryString
			String queryString;

			// 判断：是否强制要求加密 或 入参就是base64加密串，则进行解密操作
			if (cryptoHandlerProperties.isNeedEncryptInputParam() && cryptoHandler.isNeedDecrypt(encryptedData)) {
				try {
					// 将空格转换为加号
					if (encryptedData.contains(" ")) {
						encryptedData = encryptedData.replace(" ", "+");
					}

					// 解密
					queryString = cryptoHandler.decrypt(encryptedData);

					// 包装Request
					return new QueryStringHttpServletRequestWrapper(request, queryString);
				} catch (Exception e) {
					throw new ParamDecryptException("QueryString入参格式有误，解密失败", e);
				}
			}
		}

		return request;
	}

	/**
	 * 解密Body
	 *
	 * @param request 请求实例
	 * @return 返回原request或包装后的request
	 */
	private HttpServletRequest decryptBody(HttpServletRequest request) {
		// TODO: 待开发
		return request;
	}

	/**
	 * 解密Form
	 *
	 * @param request 请求实例
	 * @return 返回原request或包装后的request
	 */
	private HttpServletRequest decryptForm(HttpServletRequest request) {
		// TODO: 待开发
		return request;
	}
}
