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

import cn.hutool.json.JSONUtil;
import icu.easyj.spring.boot.test.result.ContentResult;
import icu.easyj.spring.boot.test.result.ContentTypeResult;
import icu.easyj.spring.boot.test.result.GenericContentResult;
import icu.easyj.spring.boot.test.result.HeaderResult;
import icu.easyj.spring.boot.test.result.StatusResult;
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

	private final MvcResult mvcResult;

	public MockResponse(ResultActions resultActions) {
		this.mvcResult = resultActions.andReturn();
	}


	//region Response Results 响应结果

	/**
	 * @return 响应状态结果
	 */
	public StatusResult status() {
		return new StatusResult(this, this.getStatus());
	}

	/**
	 * @return 响应头结果
	 */
	public HeaderResult header() {
		return new HeaderResult(this, this.getResponse());
	}

	/**
	 * @return 响应内容类型结果
	 */
	public ContentTypeResult contentType() {
		return new ContentTypeResult(this, this.getContentType());
	}

	/**
	 * @return 泛型响应内容结果
	 */
	public <T, R extends GenericContentResult<T, R>> GenericContentResult<T, R> content(Class<T> contentClass) {
		return new GenericContentResult<>(this, this.getContent(contentClass));
	}

	/**
	 * @return 响应内容结果
	 */
	public ContentResult content() {
		return new ContentResult(this, this.getContentAsString());
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
	 * @return 响应
	 */
	private MockHttpServletResponse getResponse() {
		return mvcResult.getResponse();
	}

	/**
	 * @return 响应状态
	 */
	private int getStatus() {
		return this.getResponse().getStatus();
	}

	/**
	 * @return 响应内容类型
	 */
	private String getContentType() {
		return this.getResponse().getContentType();
	}

	/**
	 * @return 响应内容
	 */
	private String getContentAsString() {
		try {
			return this.getResponse().getContentAsString();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("不支持的编码", e);
		}
	}

	/**
	 * @return 响应内容实例
	 */
	private <T> T getContent(Class<T> contentClass) {
		String content = getContentAsString();
		if (contentClass.equals(String.class)) {
			return (T)content;
		} else {
			return JSONUtil.toBean(content, contentClass);
		}
	}

	//endregion
}
