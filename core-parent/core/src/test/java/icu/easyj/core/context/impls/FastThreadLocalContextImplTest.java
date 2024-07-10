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

import icu.easyj.core.context.AbstractContextImplTest;

/**
 * {@link FastThreadLocalContextImpl} 测试类
 *
 * @author wangliang181230
 */
public class FastThreadLocalContextImplTest extends AbstractContextImplTest {

	public FastThreadLocalContextImplTest() {
		super(new FastThreadLocalContextImpl());
	}
}
