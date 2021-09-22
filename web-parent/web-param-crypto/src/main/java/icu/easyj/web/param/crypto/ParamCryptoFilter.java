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
import java.util.Map;
import java.util.Set;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import icu.easyj.core.util.StringUtils;
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

		Assert.notNull(cryptoHandler, "'cryptoHandler' must not be null");
		Assert.notNull(cryptoHandlerProperties, "'cryptoHandlerProperties' must not be null");

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

		// 解密queryString
		request = this.decryptQueryString(request);

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
		// 获取`待解密的queryString`
		String encryptedQueryString = this.getEncryptedQueryString(request);

		// 如果`待解密的queryString`不为空，才需要解密
		if (StringUtils.isNotEmpty(encryptedQueryString)) {
			// 处理被转义的字符
			encryptedQueryString = cryptoHandler.handleEscapedChars(encryptedQueryString);

			// 判断：是否强制要求调用端加密 或 入参就是加密过的串，则进行解密操作
			if (cryptoHandlerProperties.isNeedEncryptInputParam() || cryptoHandler.isEncryptedQueryString(encryptedQueryString)) {
				try {
					// 解密
					String queryString = cryptoHandler.decrypt(encryptedQueryString);

					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("QueryString入参解密成功！\r\n==>\r\n解密前: {}\r\n解密后: {}\r\n<==", encryptedQueryString, queryString);
					}

					// 设为null，方便GC回收
					encryptedQueryString = null;

					// 包装Request：将解密后的`QueryString`重新注入到request中
					return new QueryStringHttpServletRequestWrapper(request, queryString);
				} catch (RuntimeException e) {
					// 设为null，方便GC回收
					encryptedQueryString = null;

					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("QueryString入参未加密或格式有误，解密失败！\r\n==>\r\nQuery String: {}\r\nErrorMessage: {}\r\n<==", request.getQueryString(), e.getMessage());
					}

					// 如果强制要求调用端加密，则抛出异常，否则直接返回request
					if (cryptoHandlerProperties.isNeedEncryptInputParam()) {
						throw new ParamDecryptException("QueryString入参未加密或格式有误，解密失败", "DECRYPT_FAILED", e);
					} else {
						return request;
					}
				}
			}
		}

		return request;
	}

	/**
	 * 获取加密过的queryString
	 *
	 * @param request 请求实例
	 * @return 加密过的queryString
	 */
	private String getEncryptedQueryString(HttpServletRequest request) {
		if (StringUtils.isNotEmpty(super.filterProperties.getQueryStringName())) {
			// 判断是否有参数未加密（如果强制要求入参加密）
			if (cryptoHandlerProperties.isNeedEncryptInputParam()) {
				Map<String, String[]> parameterMap = request.getParameterMap();
				if (parameterMap != null && parameterMap.size() > 1) {
					Set<String> parameterNames = new HashSet<>(parameterMap.keySet());
					parameterNames.remove(super.filterProperties.getQueryStringName());

					String errorMsg = "存在未加密的参数：" + icu.easyj.core.util.StringUtils.toString(parameterNames);

					// 设为null，方便GC回收
					parameterNames.clear();
					parameterNames = null;

					if (LOGGER.isInfoEnabled()) {
						LOGGER.info("{}, queryString: {}", errorMsg, request.getQueryString());
					}
					throw new ParamNotEncryptedException(errorMsg, "HAS_UN_ENCRYPTED_QUERY_STRING");
				}
			}

			// 取指定参数名的参数值作为加密过的参数
			return request.getParameter(super.filterProperties.getQueryStringName());
		} else {
			// 取整个queryString作为加密过的参数（推荐方案）
			return request.getQueryString();
		}
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
