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
package icu.easyj.sdk.s3.dwz;

import cn.hutool.core.annotation.Alias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

/**
 * S-3短链接服务响应数据
 *
 * @author wangliang181230
 */
public class S3DwzResponseData {

	/**
	 * 短链接
	 */
	@JsonProperty("url_short") // jackson键别名
	@SerializedName("url_short") // gson键别名
	@Alias("url_short") // hutool键别名
	private String urlShort;

	/**
	 * 短链接创建时间
	 */
	@JsonProperty("create_time") // jackson键别名
	@SerializedName("create_time") // gson键别名
	@Alias("create_time") // hutool键别名
	private Long createTime;

	/**
	 * 短链接有效时间，为null或0时，表示永久有效
	 */
	@JsonProperty("expire_in") // jackson键别名
	@SerializedName("expire_in") // gson键别名
	@Alias("expire_in") // hutool键别名
	private Long expireIn;


	//region Getter、Setter

	public String getUrlShort() {
		return urlShort;
	}

	public void setUrlShort(String urlShort) {
		this.urlShort = urlShort;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getExpireIn() {
		return expireIn;
	}

	public void setExpireIn(Long expireIn) {
		this.expireIn = expireIn;
	}

	//endregion
}
