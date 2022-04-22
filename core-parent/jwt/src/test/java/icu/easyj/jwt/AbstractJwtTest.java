/*
 * Copyright 2021-2022 the original author or authors.
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
package icu.easyj.jwt;

import icu.easyj.core.util.MapUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * {@link IJwtParser} 抽象测试类
 *
 * @author wangliang181230
 */
public abstract class AbstractJwtTest {

	private IJwt jwt;

	public void setJwt(IJwt jwt) {
		this.jwt = jwt;
	}

	@Test
	public void testCreateAndParse() throws InterruptedException {
		// 准备参数
		String jwtId = "ididididididid";
		Map<String, Object> claims = MapUtils.quickMap("aa", "1", "bb", "2");

		// 创建JWT
		Date issuedAt = new Date();
		issuedAt.setTime(issuedAt.getTime() - issuedAt.getTime() % 1000);

		int expiredTime = 2;
		String jwtStr = this.jwt.create(jwtId, claims, issuedAt, expiredTime);

		// 解析JWT
		JwtInfo jwtInfo = this.jwt.parse(jwtStr, 2000);
		Assertions.assertEquals(jwtId, jwtInfo.getJwtId());
		Assertions.assertEquals(2, jwtInfo.getClaims().size());
		Assertions.assertEquals("1", jwtInfo.getClaims().get("aa"));
		Assertions.assertEquals("2", jwtInfo.getClaims().get("bb"));
		Assertions.assertEquals(Date.class, jwtInfo.getIssuedAt().getClass());
		Assertions.assertEquals(Date.class, jwtInfo.getExpiresAt().getClass());
		Assertions.assertEquals(issuedAt, jwtInfo.getIssuedAt());
		Assertions.assertEquals(new Date(issuedAt.getTime() + expiredTime * 1000), jwtInfo.getExpiresAt());

		// case: 过期后，执行解析会抛异常
		Thread.sleep(2000);
		Assertions.assertThrows(RuntimeException.class, () -> {
			jwt.parse(jwtStr, 0L);
		});
	}

	protected SecretKey generalKey() {
		byte[] encodedKey = Base64.getDecoder().decode("1234567890123456");
		return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
	}
}
