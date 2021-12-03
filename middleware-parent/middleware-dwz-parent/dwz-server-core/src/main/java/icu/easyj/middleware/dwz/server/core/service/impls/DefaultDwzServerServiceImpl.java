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

import icu.easyj.core.util.DateUtils;
import icu.easyj.middleware.dwz.server.core.domain.entity.DwzLogEntity;
import icu.easyj.middleware.dwz.server.core.domain.enums.DwzLogStatus;
import icu.easyj.middleware.dwz.server.core.service.IDwzServerService;
import icu.easyj.middleware.dwz.server.core.store.IDwzLogStore;
import icu.easyj.web.util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link IDwzServerService} 默认实现
 *
 * @author wangliang181230
 */
public class DefaultDwzServerServiceImpl implements IDwzServerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDwzServerServiceImpl.class);

	private final IDwzLogStore logStore;


	public DefaultDwzServerServiceImpl(IDwzLogStore logStore) {
		this.logStore = logStore;
	}


	@Override
	@Transactional
	public DwzLogEntity createShortUrlCode(String longUrl, Date termOfValidity) {
		// 先校验参数
		if (!HttpUtils.isHttpOrHttps(longUrl)) {
			LOGGER.warn("长链接不是有效的http(s)地址，longUrl = {}", longUrl);
			throw new IllegalArgumentException("长链接不是有效的http(s)地址：" + longUrl);
		}
		if (termOfValidity != null && termOfValidity.getTime() <= System.currentTimeMillis()) {
			String termOfValidityStr = DateUtils.toString(termOfValidity);
			LOGGER.warn("termOfValidity不能小于等于当前时间, termOfValidity = {}，longUrl = {}", termOfValidityStr, longUrl);
			throw new IllegalArgumentException("termOfValidity不能小于等于当前时间：" + termOfValidityStr);
		}

		// 先获取一次数据库中的数据是否存在，如果存在，则复用数据
		DwzLogEntity dwzLog = logStore.getByLongUrlForUpdate(longUrl);
		if (dwzLog != null) {
			// 标记是否需要更新数据
			boolean needUpdate = false;

			// 状态不为有效时，更新为有效
			if (dwzLog.isNotStatus(DwzLogStatus.EFFECTIVE)) {
				dwzLog.setStatus(DwzLogStatus.EFFECTIVE);
				needUpdate = true;
			}

			// 查看超时时间哪个更久，就用哪个，以兼容之前请求该数据的业务
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

		return logStore.save(longUrl, termOfValidity);
	}

	@Override
	public String getLongUrlByShortUrlCode(String shortUrlCode) {
		return logStore.getLongUrlByShortUrlCode(shortUrlCode);
	}
}
