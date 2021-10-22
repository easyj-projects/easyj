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
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import icu.easyj.sdk.dwz.DwzRequest;

/**
 * 百度云DWZ请求参数
 *
 * @author wangliang181230
 * @see BaiduDwzTemplateImpl#createShortUrl(DwzRequest)
 * @deprecated 由于参数比较简单，现已直接通过字符串拼接，不再使用该请求类
 */
@Deprecated
public class BaiduDwzRequest {

	/**
	 * 长链接
	 */
	@JSONField(name = "LongUrl") // fastjson键别名
	@JsonProperty("LongUrl") // jackson键别名
	@SerializedName("LongUrl") // gson键别名
	@Alias("LongUrl") // hutool键别名
	private String longUrl;

	/**
	 * 短网址有效期<br>
	 * 值域：
	 * <ul>
	 *     <li>long-term：长期</li>
	 *     <li>1-year：1年</li>
	 * </ul>
	 */
	@JSONField(name = "TermOfValidity") // fastjson键别名
	@JsonProperty("TermOfValidity") // jackson键别名
	@SerializedName("TermOfValidity") // gson键别名
	@Alias("TermOfValidity") // hutool键别名
	private String termOfValidity;


	//region Constructor

	public BaiduDwzRequest() {
	}

	public BaiduDwzRequest(String longUrl) {
		this.longUrl = longUrl;
	}

	public BaiduDwzRequest(String longUrl, String termOfValidity) {
		this.longUrl = longUrl;
		this.termOfValidity = termOfValidity;
	}

	//endregion


	//region Getter、Setter

	public String getLongUrl() {
		return longUrl;
	}

	public void setLongUrl(String longUrl) {
		this.longUrl = longUrl;
	}

	public String getTermOfValidity() {
		return termOfValidity;
	}

	public void setTermOfValidity(String termOfValidity) {
		this.termOfValidity = termOfValidity;
	}

	//endregion
}
