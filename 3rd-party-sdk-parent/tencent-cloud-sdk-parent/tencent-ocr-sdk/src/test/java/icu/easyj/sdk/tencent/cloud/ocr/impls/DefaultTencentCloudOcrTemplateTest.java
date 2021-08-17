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
import java.util.Base64;

import cn.hutool.core.io.IoUtil;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.IDCardOCRResponse;
import icu.easyj.sdk.tencent.cloud.ocr.TencentCloudConfig;
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
	@Disabled("请自行将二代身份证图片放在指定目录，并设置好密钥对后，再执行该测试用例")
	void testIDCardOCR() throws Exception {
		// 身份证图片存放路径（因为为敏感信息，不方便在源码中存放身份证图片文件，请自行放入指定目录进行测试）
		String IDCardImageFilePath = "D:\\IDCard.jpg";

		// 前往这个页面（https://console.cloud.tencent.com/cam/capi）获取密钥对
		String secretId = "AKIDRtxlKhSGCxbpqL6ooX8YEooa7cw38C2d"; // 请设置密钥对的ID
		String secretKey = "Rggmrjz3wbCNHWWGkMtAuDVw71U1Zt7r"; // 请设置密钥对的Key

		// 地域
		String region = "ap-shanghai";

		// 实例化配置对象
		TencentCloudConfig globalConfig = new TencentCloudConfig();
		globalConfig.setSecretId(secretId);
		globalConfig.setSecretKey(secretKey);
		globalConfig.setRegion(region);

		// 实例化template对象
		DefaultTencentCloudOcrTemplate template = new DefaultTencentCloudOcrTemplate(globalConfig);

		// 实例化一个请求对象,每个接口都会对应一个request对象
		IDCardOCRRequest req = new IDCardOCRRequest();
		// 读取base64串
		byte[] bytes = IoUtil.readBytes(new FileInputStream(IDCardImageFilePath));
		String base64 = Base64.getEncoder().encodeToString(bytes);
		req.setImageBase64(base64);

		// 返回的resp是一个IDCardOCRResponse的实例，与请求对象对应
		IDCardOCRResponse resp = template.IDCardOCR(req);
		Assertions.assertNotNull(resp);

		// 输出json格式的字符串回包
		System.out.println(IDCardOCRResponse.toJsonString(resp));
	}
}
