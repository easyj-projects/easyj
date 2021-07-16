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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 入参解密过滤器
 *
 * @author wangliang181230
 */
@Order(FilterOrderConstants.PARAM_ENCRYPT)
public class ParamCryptoFilter extends AbstractFilter<IParamCryptoFilterProperties> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ParamCryptoFilter.class);

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
	public ParamCryptoFilter(@NonNull IParamCryptoFilterProperties filterProperties,
							 @NonNull IParamCryptoHandlerProperties cryptoHandlerProperties,
							 @NonNull IParamCryptoHandler cryptoHandler) {
		super(filterProperties);

		Assert.notNull(cryptoHandler, "'cryptoHandler' must be not null");
		Assert.notNull(cryptoHandlerProperties, "'cryptoHandlerProperties' must be not null");

		this.cryptoHandlerProperties = cryptoHandlerProperties;
		this.cryptoHandler = cryptoHandler;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		// 转换类型
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
	 * 解密queryString
	 *
	 * @param request 请求实例
	 * @return 返回原request或包装后的request
	 */
	private HttpServletRequest decryptQueryString(HttpServletRequest request) {
		// 待解密的queryString
		String encryptedQueryString;
		if (StringUtils.hasLength(super.filterProperties.getQueryStringName())) {
			// 判断是否有参数未加密（如果强制要求入参加密）
			if (cryptoHandlerProperties.isNeedEncryptInputParam() && request.getParameterMap().size() > 1) {
				Set<String> parameterNames = new HashSet<>(request.getParameterMap().keySet());
				parameterNames.remove(super.filterProperties.getQueryStringName());

				String errorMsg = "存在未加密的参数：" + icu.easyj.core.util.StringUtils.toString(parameterNames);
				if (LOGGER.isInfoEnabled()) {
					LOGGER.info("{}, queryString: {}", errorMsg, request.getQueryString());
				}
				throw new ParamNotEncryptedException(errorMsg);
			}

			// 取指定参数名的参数值作为加密过的参数
			encryptedQueryString = request.getParameter(super.filterProperties.getQueryStringName());
		} else {
			// 取整个queryString作为加密过的参数（推荐方案）
			encryptedQueryString = request.getQueryString();
		}

		// 如果不为空，才需要解密
		if (StringUtils.hasLength(encryptedQueryString)) {
			// 处理被转义的字符
			encryptedQueryString = cryptoHandler.handleEscapedChars(encryptedQueryString);

			// 判断：是否强制要求调用端加密 或 入参就是加密过的串，则进行解密操作
			if (cryptoHandlerProperties.isNeedEncryptInputParam() || cryptoHandler.checkFormat(encryptedQueryString)) {
				// 解密后，正常的queryString
				String queryString;
				try {
					// 解密
					queryString = cryptoHandler.decrypt(encryptedQueryString);
				} catch (RuntimeException e) {
					if (LOGGER.isInfoEnabled()) {
						LOGGER.info("入参未加密或格式有误，解密失败！\r\n==>\r\nQuery String: {}\r\nErrorMessage: {}\r\n<==", request.getQueryString(), e.getMessage());
					}

					// 如果强制要求调用端加密，则抛出异常，否则直接返回request
					if (cryptoHandlerProperties.isNeedEncryptInputParam()) {
						throw new ParamDecryptException("入参未加密或格式有误，解密失败", e);
					} else {
						return request;
					}
				}

				// 包装Request
				return new QueryStringHttpServletRequestWrapper(request, queryString);
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


	//region Getter

	public IParamCryptoHandlerProperties getCryptoHandlerProperties() {
		return cryptoHandlerProperties;
	}

	public IParamCryptoHandler getCryptoHandler() {
		return cryptoHandler;
	}

	//endregion
}
