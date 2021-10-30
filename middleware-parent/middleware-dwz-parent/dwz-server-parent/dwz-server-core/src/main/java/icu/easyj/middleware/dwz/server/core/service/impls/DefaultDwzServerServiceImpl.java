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
package icu.easyj.middleware.dwz.server.core.service.impls;

import java.util.Date;

import icu.easyj.db.util.DbClockUtils;
import icu.easyj.middleware.dwz.server.core.domain.entity.DwzLogEntity;
import icu.easyj.middleware.dwz.server.core.service.IDwzServerService;
import icu.easyj.middleware.dwz.server.core.store.IDwzLogStore;
import icu.easyj.middleware.dwz.server.core.store.IDwzShortCodeStore;
import icu.easyj.web.util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * {@link IDwzServerService} 默认实现
 *
 * @author wangliang181230
 */
public class DefaultDwzServerServiceImpl implements IDwzServerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDwzServerServiceImpl.class);

	private final IDwzLogStore logStore;
	private final IDwzShortCodeStore shortCodeStore;


	public DefaultDwzServerServiceImpl(IDwzLogStore logStore, IDwzShortCodeStore shortCodeStore) {
		this.logStore = logStore;
		this.shortCodeStore = shortCodeStore;
	}


	@NonNull
	@Override
	@Transactional
	public DwzLogEntity createShortUrlCode(@NonNull String longUrl, @Nullable Date termOfValidity) {
		//region 校验参数

		if (!HttpUtils.isHttpOrHttps(longUrl)) {
			LOGGER.warn("长链接不是有效的http(s)地址，longUrl = {}", longUrl);
			throw new IllegalArgumentException("长链接不是有效的http(s)地址：" + longUrl);
		}

		Assert.isTrue(termOfValidity == null || termOfValidity.compareTo(DbClockUtils.now()) > 0, "termOfValidity可为空或必须大于当前时间");

		//endregion

		// 先获取一次数据库中的数据是否存在，如果存在，则复用数据
		DwzLogEntity dwzLog = logStore.getByLongUrlForUpdate(longUrl);
		if (dwzLog != null) {
			// 标记是否需要更新数据
			boolean needUpdate = false;

			// 状态不为有效时，更新为有效
			if (dwzLog.getStatus() != 1) {
				dwzLog.setStatus(1);
				needUpdate = true;
			}

			// 查看超时时间哪个更久，就用哪个
			if (dwzLog.getTermOfValidity() != null) {
				if (termOfValidity == null
						|| dwzLog.getTermOfValidity().compareTo(termOfValidity) < 0) {
					dwzLog.setTermOfValidity(null);
					needUpdate = true;
				}
			}

			if (needUpdate) {
				logStore.update(dwzLog);
			}

			return dwzLog;
		}

		String shortUrlCode = shortCodeStore.createShortUrlCode();
		return logStore.save(shortUrlCode, longUrl, termOfValidity);
	}

	@Nullable
	@Override
	public String getLongUrlByShortUrlCode(@NonNull String shortUrlCode) {
		return logStore.getLongUrlByShortUrlCode(shortUrlCode);
	}
}