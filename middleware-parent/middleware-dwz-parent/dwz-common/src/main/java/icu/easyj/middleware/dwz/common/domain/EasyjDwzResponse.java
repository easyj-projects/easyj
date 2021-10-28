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
package icu.easyj.middleware.dwz.common.domain;

import java.util.Date;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * @author wangliang181230
 */
public class EasyjDwzResponse {

	/**
	 * 短链接地址
	 */
	private String shortUrl;

	/**
	 * 短链接创建时间
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 到期时效，单位：毫秒。<br>
	 * 空或0表示长期有效。
	 */
	private Long expireIn;


	public EasyjDwzResponse() {
	}

	public EasyjDwzResponse(@NonNull String shortUrl, @NonNull Date createTime, @Nullable Date termOfValidity) {
		Assert.notNull(shortUrl, "'shortUrl' must not be null");
		Assert.notNull(createTime, "'createTime' must not be null");
		Assert.isTrue(termOfValidity == null || termOfValidity.compareTo(createTime) > 0, "termOfValidity可为空或必须大于createTime");

		this.shortUrl = shortUrl;
		this.createTime = createTime;
		this.expireIn = termOfValidity == null ? null : termOfValidity.getTime() - createTime.getTime();
	}


	//region Getter、Setter

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
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
