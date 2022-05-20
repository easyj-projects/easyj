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
package icu.easyj.core.util;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link CycleDependencyHandler} 测试类
 *
 * @author wangliang181230
 */
public class CycleDependencyHandlerTest {

	@Test
	public void testGetUniqueSubstituteObject() {
		Set<Object> objSet = new HashSet<>();

		long startNanoTime = System.nanoTime();
		int count = 4 * 10000;
		int i = count;
		Object obj;
		while (i-- > 0) {
			obj = CycleDependencyHandler.getUniqueSubstituteObject(new Object());
			if (objSet.contains(obj)) {
				throw new RuntimeException("\r\n注意：getUniqueSubstituteObject方法执行了" + (count - i) + "次后，出现了重复对象，请重写此方法的实现，避免出现重复的对象，重复对象为：" + obj);
			}
			objSet.add(obj);
		}
		long cost = System.nanoTime() - startNanoTime;
		System.out.println("getUniqueSubstituteObject方法测试通过，执行了" + count + "次，没有出现重复对象。耗时：" + (cost / 1000000) + " ms。");
	}
}
