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
package icu.easyj.sdk.baidu.cloud.dwz;

import cn.hutool.core.annotation.Alias;

/**
 * 百度云DWZ响应数据
 *
 * @author wangliang181230
 */
public class BaiduDwzResponseData {

	/**
	 * 短链接
	 */
	@Alias("ShortUrl")
	private String shortUrl;

	/**
	 * 对应的长连接
	 */
	@Alias("LongUrl")
	private String longUrl;

	/**
	 * 错误信息
	 */
	@Alias("ErrMsg")
	private String errMsg;


	//region Getter、Setter

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public String getLongUrl() {
		return longUrl;
	}

	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	//endregion
}
