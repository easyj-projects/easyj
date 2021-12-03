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
package icu.easyj.core.util.shortcode.impls;

import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.util.shortcode.IShortCodeService;
import org.springframework.util.Assert;

/**
 * long型ID 与 短字符串 互相转换服务
 * <p>
 * 该实现的目的是为了缩短短字符的长度。<br>
 * 按照default的实现，id值越小，生成的短字符串的长度就越短。<br>
 * 可以适当的缩短长度，在短链接码的应用中，缩短几个长度也能减少很多的短信成本。
 *
 * @author wangliang181230
 */
@LoadLevel(name = "min-id", order = 50)
public class MinIdShortCodeServiceImpl implements IShortCodeService {

	protected final IShortCodeService shortCodeService;
	protected final long minId;

	/**
	 * 构造函数
	 *
	 * @param shortCodeService 短字符串服务
	 * @param minId            最小ID
	 */
	public MinIdShortCodeServiceImpl(IShortCodeService shortCodeService, long minId) {
		Assert.notNull(shortCodeService, "'shortCodeService' must not be null");
		Assert.isTrue(minId > 0, "minId必须大于0");

		this.shortCodeService = shortCodeService;
		this.minId = minId;
	}

	public MinIdShortCodeServiceImpl(long minId) {
		this(new DefaultShortCodeServiceImpl(), minId);
	}


	@Override
	public String toCode(Long id) {
		Assert.isTrue(id != null && id >= minId, "ID必须大于等于最小ID：" + minId);
		id -= minId; // 减掉最小ID
		return shortCodeService.toCode(id);
	}

	@Override
	public long toId(String shortCode) {
		long id = shortCodeService.toId(shortCode);
		id += minId; // 加上最小ID
		if (id < minId) { // 如果加上最小ID后反而变小了，说明ID超过Long.MAX_VALUE了，shortCode并非当前实例生成的。
			throw new IllegalArgumentException("短字符串 '" + shortCode + "' 有误，它可能不是当前服务生成的！");
		}
		return id;
	}


	public IShortCodeService getShortCodeService() {
		return shortCodeService;
	}

	public long getMinId() {
		return minId;
	}
}
