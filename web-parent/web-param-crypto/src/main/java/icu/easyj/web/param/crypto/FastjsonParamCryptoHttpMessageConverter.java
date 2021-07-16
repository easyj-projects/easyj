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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONPObject;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonContainer;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.support.spring.MappingFastJsonValue;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import icu.easyj.web.param.crypto.exception.ParamDecryptException;
import icu.easyj.web.param.crypto.exception.ParamEncryptException;
import icu.easyj.web.util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

/**
 * JSON入参解密/出参加密 消息转换器
 *
 * @author wangliang181230
 */
public class FastjsonParamCryptoHttpMessageConverter extends FastJsonHttpMessageConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(FastjsonParamCryptoHttpMessageConverter.class);

	/**
	 * 参数加密解密过滤器（需要使用到它的校验功能）
	 */
	private final ParamCryptoFilter paramCryptoFilter;

	/**
	 * 构造函数
	 *
	 * @param paramCryptoFilter 参数加密解密过滤器
	 */
	public FastjsonParamCryptoHttpMessageConverter(@NonNull ParamCryptoFilter paramCryptoFilter) {
		Assert.notNull(paramCryptoFilter, "'paramCryptoFilter' must be not null");
		this.paramCryptoFilter = paramCryptoFilter;
	}


	//region 入参解密

	//region Override，以下两个方法必须重写

	@Override
	public Object read(Type type, Class<?> contextClass,
					   HttpInputMessage inputMessage) throws HttpMessageNotReadableException {
		return this.readType(getType(type, contextClass), inputMessage);
	}

	@Override
	protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws HttpMessageNotReadableException {
		return readType(getType(clazz, null), inputMessage);
	}

	//endregion

	/**
	 * 入参解密
	 * 注：由于父类中该方法为private，所以并没有添加`@Override`，但实际上该方法是属于Override。
	 * 所以在父类中调用了`readType`方法，都需要`@Override`。
	 *
	 * @param type         入参类型
	 * @param inputMessage HTTP输入消息
	 * @return 返回入参值
	 */
	protected Object readType(Type type, HttpInputMessage inputMessage) {
		FastJsonConfig fastJsonConfig = super.getFastJsonConfig();

		try {
			// 获取输入流并转换为body字符串
			InputStream in = inputMessage.getBody();
			String body = StreamUtils.copyToString(in, fastJsonConfig.getCharset());

			// 判断是否需要解密
			String bodyJsonStr;
			if (isNeedDecryptBody(body)) {
				try {
					// 解密，还原入参JSON串
					bodyJsonStr = this.paramCryptoFilter.getCryptoHandler().decrypt(body);
				} catch (RuntimeException e) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Body入参未加密或格式有误，解密失败！\r\n==>\r\nBody: {}\r\nErrorMessage: {}\r\n<==", body, e.getMessage());
					}
					if (e instanceof ParamDecryptException) {
						throw e;
					} else {
						throw new ParamDecryptException("Body入参未加密或格式有误，解密失败", e);
					}
				}
			} else {
				// 无需解密
				bodyJsonStr = body;
			}

			// JSON数据转换为指定入参类型的数据
			return JSON.parseObject(bodyJsonStr,
					type,
					fastJsonConfig.getParserConfig(),
					fastJsonConfig.getParseProcess(),
					JSON.DEFAULT_PARSER_FEATURE,
					fastJsonConfig.getFeatures());
		} catch (JSONException e) {
			throw new HttpMessageNotReadableException("JSON parse error: " + e.getMessage(), e, inputMessage);
		} catch (IOException e) {
			throw new HttpMessageNotReadableException("I/O error while reading input message", e, inputMessage);
		}
	}

	/**
	 * 判断入参是否需要解密
	 *
	 * @param body body入参
	 * @return isNeedDecrypt 是否需要解密
	 */
	private boolean isNeedDecryptBody(String body) {
		// 判断：内部请求无需解密
		if (HttpUtils.isInternalRequest()) {
			return false;
		}

		// 判断：过滤器判断是否需要解密
		if (!this.paramCryptoFilter.isNeedDoFilter(HttpUtils.getRequest())) {
			return false;
		}

		// 判断：是否强制要求调用端加密 或 入参就是加密过的串，则进行解密操作
		return (this.paramCryptoFilter.getCryptoHandlerProperties().isNeedEncryptInputParam()
				|| this.paramCryptoFilter.getCryptoHandler().checkFormat(body));
	}

	//endregion


	//region 出参加密、Override

	/**
	 * 出参加密、Override
	 * <p>
	 * 注：该方法中标注了`@Override`的代码为重写过的代码。
	 *
	 * @param object        出参
	 * @param outputMessage HTTP输出消息
	 * @throws IOException                     IO异常
	 * @throws HttpMessageNotWritableException HTTP消息写入异常
	 */
	@Override
	protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		FastJsonConfig fastJsonConfig = super.getFastJsonConfig();

		try (ByteArrayOutputStream outnew = new ByteArrayOutputStream()) {
			HttpHeaders headers = outputMessage.getHeaders();

			//获取全局配置的filter
			SerializeFilter[] globalFilters = fastJsonConfig.getSerializeFilters();
			List<SerializeFilter> allFilters = new ArrayList<>(Arrays.asList(globalFilters));

			boolean isJsonp = false;

			//不知道为什么会有这行代码， 但是为了保持和原来的行为一致，还是保留下来
			Object value = strangeCodeForJackson(object);

			if (value instanceof FastJsonContainer) {
				FastJsonContainer fastJsonContainer = (FastJsonContainer)value;
				PropertyPreFilters filters = fastJsonContainer.getFilters();
				allFilters.addAll(filters.getFilters());
				value = fastJsonContainer.getValue();
			}

			//revise 2017-10-23 ,
			// 保持原有的MappingFastJsonValue对象的contentType不做修改 保持旧版兼容。
			// 但是新的JSONPObject将返回标准的contentType：application/javascript ，不对是否有function进行判断
			if (value instanceof MappingFastJsonValue) {
				if (!StringUtils.isEmpty(((MappingFastJsonValue)value).getJsonpFunction())) {
					isJsonp = true;
				}
			} else if (value instanceof JSONPObject) {
				isJsonp = true;
			}


			//region @Override 加密处理

			// 是否加密过
			boolean isEncrypt = false;
			// 判断是否需要加密，
			if (!HttpUtils.isInternalRequest() && this.paramCryptoFilter.isNeedDoFilter(HttpUtils.getRequest())
					&& this.paramCryptoFilter.getCryptoHandlerProperties().isNeedEncryptOutputParam()) {
				String jsonStr = JSON.toJSONString(value,
						fastJsonConfig.getSerializeConfig(),
						allFilters.toArray(new SerializeFilter[0]),
						fastJsonConfig.getDateFormat(),
						JSON.DEFAULT_GENERATE_FEATURE,
						fastJsonConfig.getSerializerFeatures());
				// 加密
				try {
					value = this.paramCryptoFilter.getCryptoHandler().encrypt(jsonStr);
				} catch (ParamEncryptException e) {
					throw e;
				} catch (RuntimeException e) {
					throw new ParamEncryptException("出参加密失败", e);
				}

				// 标记为已加密，后面设置响应内容类型时使用
				isEncrypt = true;
			}

			//endregion


			//region @Override 响应内容及响应长度，按实际情况设置

			int len = !isJsonp && isEncrypt ? value.toString().length() : JSON.writeJSONStringWithFastJsonConfig(outnew, //
					fastJsonConfig.getCharset(), //
					value, //
					fastJsonConfig.getSerializeConfig(), //
					//fastJsonConfig.getSerializeFilters(), //
					allFilters.toArray(new SerializeFilter[0]),
					fastJsonConfig.getDateFormat(), //
					JSON.DEFAULT_GENERATE_FEATURE, //
					fastJsonConfig.getSerializerFeatures());

			if (isJsonp) {
				headers.setContentType(APPLICATION_JAVASCRIPT);
			} else {
				if (isEncrypt) {
					headers.setContentType(MediaType.TEXT_PLAIN);
					outnew.write(value.toString().getBytes(fastJsonConfig.getCharset()));
				} else {
					headers.setContentType(MediaType.APPLICATION_JSON);
				}
			}
			//endregion

			if (fastJsonConfig.isWriteContentLength()) {
				headers.setContentLength(len);
			}

			outnew.writeTo(outputMessage.getBody());

		} catch (JSONException jsonException) {
			throw new HttpMessageNotWritableException("Could not write JSON: " + jsonException.getMessage(), jsonException);
		}
	}

	//endregion


	//region Override

	//endregion


	//region 将父类中的私有方法复制过来，并转换成受保护方法

	protected Object strangeCodeForJackson(Object obj) {
		if (obj != null) {
			String className = obj.getClass().getName();
			if ("com.fasterxml.jackson.databind.node.ObjectNode".equals(className)) {
				return obj.toString();
			}
		}
		return obj;
	}

	//endregion
}
