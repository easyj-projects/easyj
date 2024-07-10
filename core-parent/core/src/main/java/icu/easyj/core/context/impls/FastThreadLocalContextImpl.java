/*
 * Copyright 2021-2024 the original author or authors.
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
package icu.easyj.core.context.impls;

import icu.easyj.core.context.Context;
import icu.easyj.core.loader.LoadLevel;
import icu.easyj.core.loader.condition.DependsOnClass;
import io.netty.util.concurrent.FastThreadLocal;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储于 {@link FastThreadLocal} 的上下文
 *
 * @author wangliang181230
 */
@LoadLevel(name = "fast-thread-local", order = 0)
@DependsOnClass(FastThreadLocal.class)
public class FastThreadLocalContextImpl implements Context {

	private static final FastThreadLocal<Map<String, Object>> CONTEXT = new FastThreadLocal<Map<String, Object>>() {
		@Override
		protected Map<String, Object> initialValue() {
			return new HashMap<>();
		}
	};

	protected Map<String, Object> map() {
		return CONTEXT.get();
	}


	@Nullable
	@Override
	public <V> V put(String key, V value) {
		return (V)this.map().put(key, value);
	}

	@Nullable
	@Override
	public <V> V get(String key) {
		return (V)this.map().get(key);
	}

	@Nullable
	@Override
	public <V> V remove(String key) {
		return (V)this.map().remove(key);
	}

	@Override
	public boolean containsKey(String key) {
		return this.map().containsKey(key);
	}

	@NonNull
	@Override
	public Map<String, Object> entries() {
		return this.map();
	}

	@Override
	public void clear() {
		this.map().clear();
	}
}
