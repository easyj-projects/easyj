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

import icu.easyj.core.util.StringUtils;
import icu.easyj.sdk.dwz.DwzRequest;
import icu.easyj.sdk.dwz.DwzResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * {@link S3DwzTemplateImpl} 测试类
 *
 * @author wangliang181230
 */
@Disabled("经常报错，暂时禁用")
public class S3DwzTemplateImplTest {

	/**
	 * 测试S-3短链接服务
	 */
	@Test
	public void testCreateShortUrl() {
		S3DwzConfig config = new S3DwzConfig("108104", "d49ca510520b0f02004e03ddc2de7c49");
		S3DwzTemplateImpl template = new S3DwzTemplateImpl(config);

		String longUrl = "https://www.nbgzjk.cn/index?a=1&b=2";
		DwzResponse response = template.createShortUrl(new DwzRequest(longUrl));
		System.out.println(StringUtils.toString(response));
		Assertions.assertTrue(response.getShortUrl().startsWith("https://s-3.cn/"));
	}

}
