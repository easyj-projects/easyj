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

import icu.easyj.core.util.StringUtils;
import icu.easyj.sdk.dwz.DwzRequest;
import icu.easyj.sdk.dwz.DwzResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * {@link BaiduDwzTemplateImpl} 测试类
 *
 * @author wangliang181230
 */
@Disabled("由于免费额度有限，不执行该测试用例")
class BaiduDwzTemplateImplTest {

	/**
	 * 测试百度云短链接服务
	 */
	@Test
	void testCreateShortUrl() {
		BaiduDwzConfig config = new BaiduDwzConfig("14b303c38c494cb0bfe36fd80c8b8a69");
		BaiduDwzTemplateImpl template = new BaiduDwzTemplateImpl(config);

		String longUrl = "https://www.baidu.com/?a=1&b=2";
		DwzResponse response = template.createShortUrl(new DwzRequest(longUrl));
		System.out.println(StringUtils.toString(response));
		Assertions.assertTrue(response.getShortUrl().startsWith("https://dwz.cn/"));
	}
}
