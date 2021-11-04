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
package icu.easyj.spring.boot.test;

import java.io.UnsupportedEncodingException;

import icu.easyj.core.json.JSONUtils;
import icu.easyj.core.util.ReflectionUtils;
import icu.easyj.spring.boot.test.result.CharacterEncodingResult;
import icu.easyj.spring.boot.test.result.ContentResult;
import icu.easyj.spring.boot.test.result.ContentTypeResult;
import icu.easyj.spring.boot.test.result.FileExportResult;
import icu.easyj.spring.boot.test.result.GenericContentResult;
import icu.easyj.spring.boot.test.result.HeaderResult;
import icu.easyj.spring.boot.test.result.StatusResult;
import icu.easyj.test.exception.TestException;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

/**
 * 模拟Response
 *
 * @author wangliang181230
 */
public class MockResponse {

	private static final String CHARSET_PREFIX = "charset=";

	private final ResultActions resultActions;
	private final MvcResult mvcResult;
	private MockHttpServletResponse response;

	public MockResponse(ResultActions resultActions) {
		this.resultActions = resultActions;
		this.mvcResult = resultActions.andReturn();
	}


	//region Response Results 响应结果

	/**
	 * 获取响应状态结果
	 *
	 * @return 响应状态结果
	 */
	public StatusResult status() {
		return new StatusResult(this, this.getStatus());
	}

	/**
	 * 获取响应头结果
	 *
	 * @return 响应头结果
	 */
	public HeaderResult header() {
		return new HeaderResult(this, this.getResponse());
	}

	/**
	 * 获取响应内容类型结果
	 *
	 * @return 响应内容类型结果
	 */
	public ContentTypeResult contentType() {
		return new ContentTypeResult(this, this.getContentType());
	}

	/**
	 * 获取响应内容编码结果
	 *
	 * @return 响应内容编码结果
	 */
	public CharacterEncodingResult characterEncoding() {
		return new CharacterEncodingResult(this, this.getCharacterEncoding());
	}

	/**
	 * 获取泛型响应内容结果
	 *
	 * @param contentClass 响应内容类型
	 * @param <T>          响应内容类型
	 * @return 泛型响应内容结果
	 */
	public <T> GenericContentResult<T> content(Class<T> contentClass) {
		return new GenericContentResult<>(this, this.resultActions, this.getContent(contentClass));
	}

	/**
	 * 获取响应内容结果
	 *
	 * @return 响应内容结果
	 */
	public ContentResult content() {
		return new ContentResult(this, this.resultActions, this.getContentAsString());
	}

	/**
	 * 获取文件导出结果
	 *
	 * @return 文件导出结果
	 */
	public FileExportResult file() {
		return new FileExportResult(this, this.resultActions, mvcResult.getResponse().getContentAsByteArray());
	}

	//endregion


	//region 快捷校验响应结果的方法

	/**
	 * 请求成功，响应200
	 *
	 * @return self
	 */
	public MockResponse isOk() {
		Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
		return this;
	}

	//endregion


	//region Private

	/**
	 * 获取响应
	 *
	 * @return 响应
	 */
	private MockHttpServletResponse getResponse() {
		if (response == null) {
			MockHttpServletResponse response = mvcResult.getResponse();

			// 默认的编码方式修改为UTF-8
			if (response.getContentType() == null || !response.getContentType().contains(CHARSET_PREFIX)) {
				// 不使用set方法，因为它会影响`contentType`的值
				try {
					ReflectionUtils.setFieldValue(response, "characterEncoding", "UTF-8");
				} catch (NoSuchFieldException ignore) {
				}
			}

			this.response = response;
		}

		return response;
	}

	/**
	 * 获取响应状态
	 *
	 * @return 响应状态
	 */
	private int getStatus() {
		return this.getResponse().getStatus();
	}

	/**
	 * 获取响应内容类型
	 *
	 * @return 响应内容类型
	 */
	private String getContentType() {
		return this.getResponse().getContentType();
	}

	/**
	 * 获取响应内容编码
	 *
	 * @return 响应内容编码
	 */
	private String getCharacterEncoding() {
		return this.getResponse().getCharacterEncoding();
	}

	/**
	 * 获取响应内容
	 *
	 * @return 响应内容
	 */
	private String getContentAsString() {
		try {
			return this.getResponse().getContentAsString();
		} catch (UnsupportedEncodingException e) {
			throw new TestException("不支持的编码", e);
		}
	}

	/**
	 * 获取响应内容实例
	 *
	 * @return 响应内容实例
	 */
	private <T> T getContent(Class<T> contentClass) {
		String content = getContentAsString();
		if (contentClass.equals(String.class)) {
			return (T)content;
		} else {
			return JSONUtils.toBean(content, contentClass);
		}
	}

	//endregion
}