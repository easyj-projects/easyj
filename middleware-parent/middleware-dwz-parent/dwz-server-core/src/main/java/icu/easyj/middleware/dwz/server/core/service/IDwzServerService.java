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
package icu.easyj.middleware.dwz.server.core.service;

import java.util.Date;

import icu.easyj.data.store.StoreException;
import icu.easyj.middleware.dwz.server.core.domain.entity.DwzLogEntity;

/**
 * 短链接服务接口
 *
 * @author wangliang181230
 */
public interface IDwzServerService {

	/**
	 * 根据长链接生成短链接
	 *
	 * @param longUrl        长链接
	 * @param termOfValidity 有效期截止时间（为空表示永久有效）
	 * @return shortUrlCode 短链接码
	 * @throws StoreException 存储接口异常
	 */
	DwzLogEntity createShortUrlCode(String longUrl, Date termOfValidity);

	/**
	 * 查找短链接码对应的长链接
	 *
	 * @param shortUrlCode 短链接码
	 * @return 短链接码对应的长链接（如果不存在或已过期，则返回null）
	 * @throws StoreException 存储接口异常
	 */
	String getLongUrlByShortUrlCode(String shortUrlCode);
}
