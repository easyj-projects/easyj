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
package icu.easyj.core.context;

import icu.easyj.core.context.impls.ThreadLocalContextImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link Context} 测试类
 *
 * @author wangliang181230
 */
public abstract class AbstractContextImplTest {

	static final String KEY = "aaa";


	protected final Context context;

	public AbstractContextImplTest(Context context) {
		this.context = context;
	}


	@Test
	public void test() {
		ThreadLocalContextImpl context = new ThreadLocalContextImpl();

		Assertions.assertNull(context.get(KEY));

		// case: put, return null
		Object value1 = 1;
		Assertions.assertNull(context.put(KEY, value1));
		Assertions.assertEquals(value1, context.get(KEY));

		// case: put, return 1
		Object value2 = 2;
		Assertions.assertEquals(value1, context.put(KEY, value2));

		Assertions.assertFalse(context.remove(KEY, value1)); // 不等于1，不移除，返回false
		Assertions.assertTrue(context.remove(KEY, value2)); // 等于2，移除，返回true
		Assertions.assertFalse(context.remove(KEY, value2)); // 已经移除，不再重复移除，返回false

		context.put(KEY, value2);
		Assertions.assertEquals(value2, context.remove(KEY));
	}
}
