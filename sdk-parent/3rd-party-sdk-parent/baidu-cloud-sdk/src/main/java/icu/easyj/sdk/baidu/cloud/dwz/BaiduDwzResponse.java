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

import java.util.List;

import cn.hutool.core.annotation.Alias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import icu.easyj.core.util.StringUtils;
import org.springframework.lang.Nullable;

/**
 * 百度云DWZ响应参数
 *
 * @author wangliang181230
 */
public class BaiduDwzResponse {

	@JsonProperty("Code") // jackson键别名
	@SerializedName("Code") // gson键别名
	@Alias("Code") // hutool键别名
	private Integer code;

	@JsonProperty("ErrMsg") // jackson键别名
	@SerializedName("ErrMsg") // gson键别名
	@Alias("ErrMsg") // hutool键别名
	private String errMsg;

	@JsonProperty("ShortUrls") // jackson键别名
	@SerializedName("ShortUrls") // gson键别名
	@Alias("ShortUrls") // hutool键别名
	private List<BaiduDwzResponseData> shortUrls;


	/**
	 * 判断是否请求成功
	 *
	 * @return 是否请求成功
	 */
	public boolean isSuccess() {
		return code != null && code.equals(0);
	}

	/**
	 * 获取错误类型枚举
	 *
	 * @return 错误类型枚举
	 */
	@Nullable
	public BaiduDwzErrorType getErrorType() {
		return BaiduDwzErrorType.getByCode(this.code);
	}

	/**
	 * 获取错误代码和信息
	 *
	 * @return 错误代码和信息字符串
	 */
	String getErrorCodeAndMessage() {
		if (code != null) {
			if (BaiduDwzErrorType.PART_LONG_URL_ERROR.getCode() == code && shortUrls != null) {
				for (BaiduDwzResponseData data : shortUrls) {
					if (StringUtils.isNotBlank(data.getErrMsg())) {
						return "[" + this.code + "]" + data.getErrMsg();
					}
				}
			}
		}

		return "[" + this.code + "]" + errMsg;
	}


	//region Getter、Setter

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public List<BaiduDwzResponseData> getShortUrls() {
		return shortUrls;
	}

	public void setShortUrls(List<BaiduDwzResponseData> shortUrls) {
		this.shortUrls = shortUrls;
	}


	//endregion
}
