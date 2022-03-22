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
package icu.easyj.core.trace.impls;

import brave.Span;
import brave.Tracer;
import cn.hutool.extra.spring.SpringUtil;
import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.loader.condition.DependsOnClass;
import icu.easyj.core.trace.TraceService;
import org.springframework.lang.Nullable;

/**
 * 基于 {@link Tracer} 的追踪服务
 *
 * @author wangliang181230
 */
@LoadLevel(name = "Zipkin-Tracer", order = 2, validators = ZipkinTraceServiceValidate.class)
@DependsOnClass({Tracer.class})
public class ZipkinTraceServiceImpl implements TraceService {

	//region 追踪服务单例持有者（设计模式-创建型模式-单例模式：枚举实现单例）

	private enum TraceServiceSingletonHolder {
		// 单例
		INSTANCE;

		private final Tracer instance = SpringUtil.getBean(Tracer.class);

		public Tracer getInstances() {
			return INSTANCE.instance;
		}
	}

	static Tracer getTracer() {
		return TraceServiceSingletonHolder.INSTANCE.instance;
	}

	@Nullable
	static Span currentSpan() {
		return getTracer().currentSpan();
	}

	//endregion


	@Override
	public boolean canTrace() {
		Span span = currentSpan();
		return span != null && !span.isNoop();
	}

	@Override
	public void put(String key, String value) {
		Span span = currentSpan();
		if (span == null) {
			return;
		}

		if (key == null) {
			key = "null";
		}
		if (value == null) {
			value = "null";
		}

		span.tag(key, value);
	}

	@Override
	public void remove(String key) {
		// do nothing: zipkin不支持remove
	}

	@Override
	public void clear() {
		// do nothing: zipkin会自动清理
	}
}
