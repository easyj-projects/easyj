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
package icu.easyj.sdk.tencent.cloud.ocr.idcardocr.impls;

import java.io.FileInputStream;

import cn.hutool.core.codec.Base64;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRResponse;
import icu.easyj.core.codec.Base64Utils;
import icu.easyj.core.util.StringUtils;
import icu.easyj.sdk.ocr.CardSide;
import icu.easyj.sdk.ocr.IOcrTemplate;
import icu.easyj.sdk.ocr.WrapperOcrTemplate;
import icu.easyj.sdk.ocr.idcardocr.IdCardOcrAdvanced;
import icu.easyj.sdk.ocr.idcardocr.IdCardOcrResponse;
import icu.easyj.sdk.tencent.cloud.ocr.OcrRequestBuilder;
import icu.easyj.sdk.tencent.cloud.ocr.idcardocr.IdCardOcrAdvancedInfo;
import icu.easyj.sdk.tencent.cloud.ocr.idcardocr.TencentCloudIdCardOcrConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * {@link DefaultTencentCloudIdCardOcrServiceImpl} 测试类
 *
 * @author wangliang181230
 */
public class DefaultTencentCloudIdCardOcrServiceImplTest {

	@Test
	@Disabled("由于身份证件为敏感信息，所以请自行将二代身份证图片放到指定目录中，并设置好密钥对后，再手动执行该测试用例")
	public void testIDCardOCR() throws Exception {
		// 设置密钥对
		// 获取方式：请前往页面 https://console.cloud.tencent.com/cam/capi 获取密钥对
		// 特别说明：以下是测试用的腾讯云账号的密钥对，由于文字识别接口免费额度有限，请大家尽量少执行测试。
		//         如果免费额度用完导致测试用例执行失败，请大家自行注册腾讯云账号，开通文字识别服务，并设置自己的密钥对，再进行测试。
		// 注意：请不要将生产环境使用的密钥对提交到代码库中，以免泄露。如已泄露，请重新生成一个密钥对，将生产环境的配置替换掉。
		String secretId = "AKIDb1B6mCMFrDJ8hz0kDQN3SgeKjriJdgSx"; // 密钥对中的ID
		String secretKey = "FEyRe05uSjjSv60dz3qMbaIkwrUqwmKb"; // 密钥对中的Key

		// 地域代码
		// IdCardOCR接口仅支持：ap-beijing、ap-guangzhou、ap-hongkong、ap-shanghai、na-toronto
		String region = "ap-shanghai";

		// 身份证图片存放路径（因为为敏感信息，不方便在源码中存放身份证图片文件，请自行放入指定目录进行测试）
		String idCardFrontImageFilePath = "D:\\IDCard_FRONT.jpg"; // 正面照图片
		String idCardBackImageFilePath = "D:\\IDCard_BACK.jpg"; // 反面照图片

		String frontIdCardBase64, backIdCardBase64;
		try (FileInputStream frontImageIS = new FileInputStream(idCardFrontImageFilePath);
			 FileInputStream backImageIS = new FileInputStream(idCardBackImageFilePath)) {
			frontIdCardBase64 = Base64.encode(frontImageIS);
			backIdCardBase64 = Base64.encode(backImageIS);
		}

		// 实例化配置对象
		TencentCloudIdCardOcrConfig globalConfig = new TencentCloudIdCardOcrConfig();
		globalConfig.setSecretId(secretId);
		globalConfig.setSecretKey(secretKey);
		globalConfig.setRegion(region);

		// 实例化template对象
		DefaultTencentCloudIdCardOcrServiceImpl service = new DefaultTencentCloudIdCardOcrServiceImpl(globalConfig);


		//region case1: 身份证正面照（未开启任何高级功能）
		{
			// 实例化一个请求对象,每个接口都会对应一个request对象
			IDCardOCRRequest req = OcrRequestBuilder.idCardOcrRequestBuilder()
					.image(frontIdCardBase64)
					.frontCardSide() // 正面
					.build();

			// 返回的resp是一个IDCardOCRResponse的实例，与请求对象对应
			IDCardOCRResponse resp = service.doIdCardOcr(req);
			Assertions.assertNotNull(resp);
			// 正面照没有签发机关和有效期限
			Assertions.assertEquals("", resp.getAuthority());
			Assertions.assertEquals("", resp.getValidDate());
			// 没有开启高级功能，扩展信息为空
			Assertions.assertEquals("{}", resp.getAdvancedInfo());

			// 输出json格式的字符串回包
			//System.out.println("正面照响应（未开启任何高级功能）：\r\n" + IDCardOCRResponse.toJsonString(resp));
		}
		//endregion


		//region case2: 身份证反面照（未开启任何高级功能）
		{
			// 实例化一个请求对象,每个接口都会对应一个request对象
			IDCardOCRRequest req = OcrRequestBuilder.idCardOcrRequestBuilder()
					.image(backIdCardBase64)
					.backCardSide() // 反面
					.build();

			// 返回的resp是一个IDCardOCRResponse的实例，与请求对象对应
			IDCardOCRResponse resp = service.doIdCardOcr(req);
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
		}
		//endregion


		//region case3: 身份证正面照（开启了所有高级功能）
		{
			// 实例化一个请求对象,每个接口都会对应一个request对象
			IDCardOCRRequest req = OcrRequestBuilder.idCardOcrRequestBuilder()
					.image(frontIdCardBase64)
					.frontCardSide() // 正面
					.enableAllAdvanced() // 开启所有高级功能
					.build();

			// 返回的resp是一个IDCardOCRResponse的实例，与请求对象对应
			IDCardOCRResponse resp = service.doIdCardOcr(req);
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
		}
		//endregion


		//region case4: 身份证反面照（开启了所有高级功能）
		{
			// 实例化一个请求对象,每个接口都会对应一个request对象
			IDCardOCRRequest req = OcrRequestBuilder.idCardOcrRequestBuilder()
					.image(backIdCardBase64)
					.backCardSide() // 反面
					.enableAllAdvanced() // 开启所有高级功能
					.build();

			// 返回的resp是一个IDCardOCRResponse的实例，与请求对象对应
			IDCardOCRResponse resp = service.doIdCardOcr(req);
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
			IdCardOcrAdvancedInfo advancedInfo = IdCardOcrAdvancedInfo.fromJsonString(resp.getAdvancedInfo());
			Assertions.assertNotNull(advancedInfo);
			if (advancedInfo.getIdCardBase64() != null) {
				Assertions.assertTrue(Base64Utils.isBase64(advancedInfo.getIdCardBase64()));
			}
			Assertions.assertNull(advancedInfo.getPortraitBase64()); // 反面没有人像照片
			Assertions.assertTrue(advancedInfo.getQuality() >= 0 && advancedInfo.getQuality() <= 100);
			Assertions.assertTrue(advancedInfo.getBorderCodeValue() >= 0 && advancedInfo.getBorderCodeValue() <= 100);

			// 输出json格式的字符串回包
			//System.out.println("反面照响应（开启了所有高级功能）：\r\n" + IDCardOCRResponse.toJsonString(resp));
		}
		//endregion


		//region case5: 测试 EasyJ 的 IOcrTemplate 接口

		IOcrTemplate ocrTemplate = new WrapperOcrTemplate(new TencentEasyjIdCardOcrTemplateImpl(service));

		//region case5.1: 正面
		{
			IdCardOcrResponse response = ocrTemplate.idCardOcr(frontIdCardBase64, IdCardOcrAdvanced.ALL_ARRAY);
			response.setIdCardBase64(null);
			response.setBackIdCardBase64(null);
			response.setPortraitBase64(null);
			System.out.println("正面照响应（EasyJ）：\r\n" + StringUtils.toString(response));
		}
		//endregion

		//region case5.2: 反面
		{
			IdCardOcrResponse response = ocrTemplate.idCardOcr(backIdCardBase64, IdCardOcrAdvanced.ALL_ARRAY);
			// base64不打印在日志中
			response.setIdCardBase64(null);
			response.setBackIdCardBase64(null);
			response.setPortraitBase64(null);
			System.out.println("反面照响应（EasyJ）：\r\n" + StringUtils.toString(response));
		}
		//endregion

		//region case5.3: 正反面一起
		{
			IdCardOcrResponse response = ocrTemplate.idCardOcr(frontIdCardBase64, backIdCardBase64, IdCardOcrAdvanced.ALL_ARRAY);// base64不打印在日志中
			Assertions.assertEquals(CardSide.BOTH, response.getCardSide());

			// 两张照片反过来再请求一次，响应内容应该是一样的
			IdCardOcrResponse response2 = ocrTemplate.idCardOcr(backIdCardBase64, frontIdCardBase64, IdCardOcrAdvanced.ALL_ARRAY);// base64不打印在日志中

			// 判断两个响应结果一致性
			// 正反面
			Assertions.assertEquals(response.getCardSide(), response2.getCardSide());
			// 正面信息
			Assertions.assertEquals(response.getName(), response2.getName());
			Assertions.assertEquals(response.getSex(), response2.getSex());
			Assertions.assertEquals(response.getNation(), response2.getNation());
			Assertions.assertEquals(response.getBirthday(), response2.getBirthday());
			Assertions.assertEquals(response.getAddress(), response2.getAddress());
			Assertions.assertEquals(response.getIdNum(), response2.getIdNum());
			// 反面信息
			Assertions.assertEquals(response.getAuthority(), response2.getAuthority());
			Assertions.assertEquals(response.getValidDateStart(), response2.getValidDateStart());
			Assertions.assertEquals(response.getValidDateEnd(), response2.getValidDateEnd());
			// 高级功能信息
			Assertions.assertEquals(response.getIdCardBase64(), response2.getIdCardBase64());
			if (response2.getBackIdCardBase64() != null) {
				// 有时候，无法获取到身份证裁剪图片，可能是因为存在告警的原因，导致识别不稳定
				Assertions.assertEquals(response.getBackIdCardBase64(), response2.getBackIdCardBase64());
			}
			Assertions.assertEquals(response.getPortraitBase64(), response2.getPortraitBase64());
			Assertions.assertEquals(response.getWarns().size(), response2.getWarns().size());

			// 打印日志
			response.setIdCardBase64(null);
			response.setBackIdCardBase64(null);
			response.setPortraitBase64(null);
			System.out.println("正反面照一起的响应（EasyJ）：\r\n" + StringUtils.toString(response));
			response2.setIdCardBase64(null);
			response2.setBackIdCardBase64(null);
			response2.setPortraitBase64(null);
			System.out.println("正反面照一起的响应（EasyJ）：\r\n" + StringUtils.toString(response2));
		}
		//endregion

		//endregion
	}
}
