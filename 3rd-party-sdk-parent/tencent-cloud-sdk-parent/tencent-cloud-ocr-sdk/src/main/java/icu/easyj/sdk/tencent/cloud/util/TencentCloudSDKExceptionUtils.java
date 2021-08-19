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
package icu.easyj.sdk.tencent.cloud.util;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import icu.easyj.core.util.ReflectionUtils;
import org.springframework.lang.Nullable;

/**
 * 腾讯云SDK异常工具类
 *
 * @author wangliang181230
 */
public abstract class TencentCloudSDKExceptionUtils {

	/**
	 * 通过反射获取异常的错误代码
	 *
	 * @param e 腾讯云SDK异常
	 * @return errorCode 错误码
	 */
	@Nullable
	public static String getErrorCode(TencentCloudSDKException e) {
		String errorCode = null;
		try {
			// 不知道为什么，腾讯把`e.getErrorCode()`方法给注释掉了，通过反射获取errorCode
			errorCode = ReflectionUtils.getFieldValue(e, "errorCode");
		} catch (NoSuchFieldException ignore) {
		}
		return errorCode;
	}
}
