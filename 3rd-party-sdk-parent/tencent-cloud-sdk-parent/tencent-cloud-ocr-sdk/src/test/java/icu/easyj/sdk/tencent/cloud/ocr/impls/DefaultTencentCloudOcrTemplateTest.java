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
package icu.easyj.sdk.tencent.cloud.ocr.impls;

import java.io.FileInputStream;

import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRResponse;
import icu.easyj.core.util.Base64Utils;
import icu.easyj.sdk.tencent.cloud.ocr.ITencentCloudOcrTemplate;
import icu.easyj.sdk.tencent.cloud.ocr.TencentCloudConfig;
import icu.easyj.sdk.tencent.cloud.ocr.build.OcrRequestBuilder;
import icu.easyj.sdk.tencent.cloud.ocr.idcard.IdCardOcrAdvancedInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * {@link DefaultTencentCloudOcrTemplate} 测试类
 *
 * @author wangliang181230
 */
class DefaultTencentCloudOcrTemplateTest {

	@Test
	@Disabled("请自行将二代身份证图片放在指定目录，并设置好密钥对后，再手动执行该测试用例")
	void testIDCardOCR() throws Exception {
		// 身份证图片存放路径（因为为敏感信息，不方便在源码中存放身份证图片文件，请自行放入指定目录进行测试）
		String idCardFrontImageFilePath = "D:\\IDCard_FRONT.jpg"; // 正面照图片
		String idCardBackImageFilePath = "D:\\IDCard_BACK.jpg"; // 反面照图片

		// 请前往页面 https://console.cloud.tencent.com/cam/capi 获取密钥对
		String secretId = "AKIDRtxlKhSGCxbpqL6ooX8YEooa7cw38C2d"; // 密钥对中的ID
		String secretKey = "Rggmrjz3wbCNHWWGkMtAuDVw71U1Zt7r"; // 密钥对中的Key

		// 地域
		String region = "ap-shanghai";

		// 实例化配置对象
		TencentCloudConfig globalConfig = new TencentCloudConfig();
		globalConfig.setSecretId(secretId);
		globalConfig.setSecretKey(secretKey);
		globalConfig.setRegion(region);

		// 实例化template对象
		ITencentCloudOcrTemplate template = new DefaultTencentCloudOcrTemplate(globalConfig);


		//region case1: 身份证正面照（未开启任何高级功能）

		// 实例化一个请求对象,每个接口都会对应一个request对象
		IDCardOCRRequest req = OcrRequestBuilder.idCardOcrRequestBuilder()
				.image(new FileInputStream(idCardFrontImageFilePath))
				.frontCardSide() // 正面
				.build();

		// 返回的resp是一个IDCardOCRResponse的实例，与请求对象对应
		IDCardOCRResponse resp = template.doIdCardOcr(req);
		Assertions.assertNotNull(resp);
		// 正面照没有签发机关和有效期限
		Assertions.assertEquals("", resp.getAuthority());
		Assertions.assertEquals("", resp.getValidDate());
		// 没有开启高级功能，扩展信息为空
		Assertions.assertEquals("{}", resp.getAdvancedInfo());

		// 输出json格式的字符串回包
		//System.out.println("正面照响应（未开启任何高级功能）：\r\n" + IDCardOCRResponse.toJsonString(resp));

		//endregion


		//region case2: 身份证反面照（未开启任何高级功能）

		// 实例化一个请求对象,每个接口都会对应一个request对象
		req = OcrRequestBuilder.idCardOcrRequestBuilder()
				.image(new FileInputStream(idCardBackImageFilePath))
				.backCardSide() // 反面
				.build();

		// 返回的resp是一个IDCardOCRResponse的实例，与请求对象对应
		resp = template.doIdCardOcr(req);
		Assertions.assertNotNull(resp);
		// 反面照只有签发机关和有效期限
		Assertions.assertEquals("", resp.getName());
		Assertions.assertEquals("", resp.getSex());
		Assertions.assertEquals("", resp.getNation());
		Assertions.assertEquals("", resp.getBirth());
		Assertions.assertEquals("", resp.getAddress());
		Assertions.assertEquals("", resp.getIdNum());
		// 没有开启高级功能，扩展信息为空
		Assertions.assertEquals("{}", resp.getAdvancedInfo());

		// 输出json格式的字符串回包
		//System.out.println("反面照响应（未开启任何高级功能）：\r\n" + IDCardOCRResponse.toJsonString(resp));

		//endregion


		//region case3: 身份证正面照（开启了所有高级功能）

		// 实例化一个请求对象,每个接口都会对应一个request对象
		req = OcrRequestBuilder.idCardOcrRequestBuilder()
				.image(new FileInputStream(idCardFrontImageFilePath))
				.frontCardSide() // 正面
				.enableAllAdvanced() // 开启所有高级功能
				.build();

		// 返回的resp是一个IDCardOCRResponse的实例，与请求对象对应
		resp = template.doIdCardOcr(req);
		Assertions.assertNotNull(resp);
		// 正面照没有签发机关和有效期限
		Assertions.assertEquals("", resp.getAuthority());
		Assertions.assertEquals("", resp.getValidDate());
		// 开启了高级功能，扩展信息不为空
		// 扩展信息转换为对象并校验
		IdCardOcrAdvancedInfo advancedInfo = IdCardOcrAdvancedInfo.fromJsonString(resp.getAdvancedInfo());
		Assertions.assertNotNull(advancedInfo);
		Assertions.assertTrue(Base64Utils.isBase64(advancedInfo.getIdCardBase64()));
		Assertions.assertTrue(Base64Utils.isBase64(advancedInfo.getPortraitBase64()));
		Assertions.assertTrue(advancedInfo.getQuality() >= 0 && advancedInfo.getQuality() <= 100);
		Assertions.assertTrue(advancedInfo.getBorderCodeValue() >= 0 && advancedInfo.getBorderCodeValue() <= 100);

		// 输出json格式的字符串回包
		//System.out.println("正面照响应（开启了所有高级功能）：\r\n" + IDCardOCRResponse.toJsonString(resp));

		//endregion


		//region case4: 身份证反面照（开启了所有高级功能）

		// 实例化一个请求对象,每个接口都会对应一个request对象
		req = OcrRequestBuilder.idCardOcrRequestBuilder()
				.image(new FileInputStream(idCardBackImageFilePath))
				.backCardSide() // 反面
				.enableAllAdvanced() // 开启所有高级功能
				.build();

		// 返回的resp是一个IDCardOCRResponse的实例，与请求对象对应
		resp = template.doIdCardOcr(req);
		Assertions.assertNotNull(resp);
		// 反面照只有签发机关和有效期限
		Assertions.assertEquals("", resp.getName());
		Assertions.assertEquals("", resp.getSex());
		Assertions.assertEquals("", resp.getNation());
		Assertions.assertEquals("", resp.getBirth());
		Assertions.assertEquals("", resp.getAddress());
		Assertions.assertEquals("", resp.getIdNum());
		// 开启了高级功能，扩展信息不为空
		// 扩展信息转换为对象并校验
		advancedInfo = IdCardOcrAdvancedInfo.fromJsonString(resp.getAdvancedInfo());
		Assertions.assertNotNull(advancedInfo);
		if (advancedInfo.getIdCardBase64() != null) {
			Assertions.assertTrue(Base64Utils.isBase64(advancedInfo.getIdCardBase64()));
		}
		if (advancedInfo.getPortraitBase64() != null) {
			Assertions.assertEquals("", advancedInfo.getPortraitBase64()); // 反面没有人像照片
		}
		Assertions.assertTrue(advancedInfo.getQuality() >= 0 && advancedInfo.getQuality() <= 100);
		Assertions.assertTrue(advancedInfo.getBorderCodeValue() >= 0 && advancedInfo.getBorderCodeValue() <= 100);


		// 输出json格式的字符串回包
		//System.out.println("反面照响应（开启了所有高级功能）：\r\n" + IDCardOCRResponse.toJsonString(resp));

		//endregion
	}
}
