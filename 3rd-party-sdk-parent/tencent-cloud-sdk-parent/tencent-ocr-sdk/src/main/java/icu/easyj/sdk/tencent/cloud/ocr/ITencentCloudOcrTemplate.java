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
package icu.easyj.sdk.tencent.cloud.ocr;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRResponse;

/**
 * 腾讯云 文字识别（OCR） 服务接口
 *
 * @author wangliang181230
 */
public interface ITencentCloudOcrTemplate {

	//region 身份证识别

	/**
	 * 身份证识别
	 *
	 * @param request 请求
	 * @return 响应
	 * @see <a href="https://cloud.tencent.com/document/api/866/33524">API文档</a>
	 * @see <a href="https://console.cloud.tencent.com/api/explorer?Product=ocr&Version=2018-11-19&Action=IDCardOCR">调试页面</a>
	 */
	IDCardOCRResponse IDCardOCR(IDCardOCRRequest request) throws TencentCloudSDKException;

	/**
	 * 身份证识别
	 *
	 * @param request 请求
	 * @param config  请求配置
	 * @return 响应
	 */
	IDCardOCRResponse IDCardOCR(IDCardOCRRequest request, TencentCloudConfig config) throws TencentCloudSDKException;

	//endregion
}
