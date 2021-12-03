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
package icu.easyj.sdk.tencent.cloud.ocr.idcardocr;

import java.io.InputStream;

import cn.hutool.core.codec.Base64;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRResponse;
import icu.easyj.sdk.ocr.CardSide;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * 腾讯云 身份证识别（IDCardOCR） 服务接口
 *
 * @author wangliang181230
 */
public interface ITencentCloudIdCardOcrService {

	/**
	 * 执行身份证识别
	 *
	 * @param request 请求
	 * @param config  当前请求的个性配置
	 * @return response 响应
	 * @throws TencentCloudSDKException 调用腾讯云出现异常
	 * @see <a href="https://cloud.tencent.com/document/api/866/33524">API文档</a>
	 * @see <a href="https://console.cloud.tencent.com/api/explorer?Product=ocr&Version=2018-11-19&Action=IDCardOCR">调试页面</a>
	 */
	IDCardOCRResponse doIdCardOcr(IDCardOCRRequest request, @Nullable TencentCloudIdCardOcrConfig config) throws TencentCloudSDKException;

	/**
	 * 重载方法：执行身份证识别
	 *
	 * @param request 请求
	 * @return response 响应
	 * @throws TencentCloudSDKException 调用腾讯云出现异常
	 */
	default IDCardOCRResponse doIdCardOcr(IDCardOCRRequest request) throws TencentCloudSDKException {
		return this.doIdCardOcr(request, null);
	}

	/**
	 * 重载方法：执行身份证识别
	 *
	 * @param idCardImageInputStream 身份证图片输入流
	 * @param cardSide               身份证正反面枚举
	 * @param config                 当前请求的个性配置
	 * @return response 响应
	 * @throws TencentCloudSDKException 调用腾讯云出现异常
	 */
	default IDCardOCRResponse doIdCardOcr(InputStream idCardImageInputStream, CardSide cardSide, @Nullable TencentCloudIdCardOcrConfig config) throws TencentCloudSDKException {
		IDCardOCRRequest request = new IDCardOCRRequest();
		request.setImageBase64(Base64.encode(idCardImageInputStream));
		request.setCardSide(cardSide.name());
		return this.doIdCardOcr(request, config);
	}

	/**
	 * 重载方法：执行身份证识别
	 *
	 * @param idCardImageInputStream 身份证图片输入流
	 * @param cardSide               身份证正反面枚举
	 * @return response 响应
	 * @throws TencentCloudSDKException 调用腾讯云出现异常
	 */
	default IDCardOCRResponse doIdCardOcr(InputStream idCardImageInputStream, CardSide cardSide) throws TencentCloudSDKException {
		return this.doIdCardOcr(idCardImageInputStream, cardSide, null);
	}

	/**
	 * 重载方法：执行身份证识别
	 *
	 * @param idCardImageBytes 身份证图片byte数组
	 * @param cardSide         身份证正反面枚举
	 * @param config           当前请求的个性配置
	 * @return response 响应
	 * @throws TencentCloudSDKException 调用腾讯云出现异常
	 */
	default IDCardOCRResponse doIdCardOcr(byte[] idCardImageBytes, CardSide cardSide, @Nullable TencentCloudIdCardOcrConfig config) throws TencentCloudSDKException {
		IDCardOCRRequest request = new IDCardOCRRequest();
		request.setImageBase64(Base64.encode(idCardImageBytes));
		request.setCardSide(cardSide.name());
		return this.doIdCardOcr(request, config);
	}

	/**
	 * 重载方法：执行身份证识别
	 *
	 * @param idCardImageBytes 身份证图片byte数组
	 * @param cardSide         身份证正反面枚举
	 * @return response 响应
	 * @throws TencentCloudSDKException 调用腾讯云出现异常
	 */
	default IDCardOCRResponse doIdCardOcr(byte[] idCardImageBytes, CardSide cardSide) throws TencentCloudSDKException {
		return this.doIdCardOcr(idCardImageBytes, cardSide, null);
	}

	/**
	 * 获取全局配置
	 *
	 * @return 全局配置
	 */
	@NonNull
	TencentCloudIdCardOcrConfig getGlobalConfig();
}
