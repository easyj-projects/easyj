/*
 * Copyright 2021-2024 the original author or authors.
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
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

/**
 * 百度云DWZ响应数据
 *
 * @author wangliang181230
 */
public class BaiduDwzResponseData {

	/**
	 * 短链接
	 */
	@JsonProperty("ShortUrl") // jackson键别名
	@SerializedName("ShortUrl") // gson键别名
	@Alias("ShortUrl") // hutool键别名
	private String shortUrl;

	/**
	 * 对应的长连接
	 */
	@JsonProperty("LongUrl") // jackson键别名
	@SerializedName("LongUrl") // gson键别名
	@Alias("LongUrl") // hutool键别名
	private String longUrl;

	/**
	 * 错误信息
	 */
	@JsonProperty("ErrMsg") // jackson键别名
	@SerializedName("ErrMsg") // gson键别名
	@Alias("ErrMsg") // hutool键别名
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
