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
package icu.easyj.core.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.EasyjFastjsonBugfixUtils;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import icu.easyj.core.loader.EnhancedServiceLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static icu.easyj.core.loader.ServiceProviders.FASTJSON;

/**
 * FastJSON的BUG 测试类<br>
 * 用于判断FastJSON的BUG是否修复过
 *
 * @author wangliang181230
 */
public class FastJSONBugTest {

	private static final IJSONService FASTJSON_SERVICE = EnhancedServiceLoader.load(IJSONService.class, FASTJSON);


	//region 测试Fastjson的BUG通过临时方案是否得到解决，并确认当前版本的fastjson的BUG是否还存在

	/**
	 * 测试Fastjson的BUG
	 *
	 * @see <a href="https://github.com/alibaba/fastjson/issues/3720">BUG的ISSUE</a>
	 */
	@Test
	public void testFastjsonBug() {
		SerializeConfig.getGlobalInstance().put(Long.class, ToStringSerializer.instance);

		this.testFastjsonBeforeBugfix();

		EasyjFastjsonBugfixUtils.handleSerializeConfig(SerializeConfig.getGlobalInstance());
		System.out.println();
		System.out.println("修复BUG后----------------------------------------------------------------------------------------------------");
		System.out.println();

		this.testFastjsonAfterBugfix();

		EasyjFastjsonBugfixUtils.recoverySerializeConfig(SerializeConfig.getGlobalInstance());
		System.out.println();
		System.out.println("恢复原样后----------------------------------------------------------------------------------------------------");
		System.out.println();

		this.testFastjsonBeforeBugfix();
	}


	////region Private

	/**
	 * BUG修复前
	 */
	private void testFastjsonBeforeBugfix() {
		// Long
		System.out.println("Long：正常");
		Assertions.assertEquals("\"9223372036854775807\"", JSON.toJSONString(Long.MAX_VALUE)); // 含双引号，正确的
		Assertions.assertEquals("\"-9223372036854775808\"", JSON.toJSONString(Long.MIN_VALUE)); // 含双引号，正确的

		// Long[]
		Long[] longArr = new Long[2];
		longArr[0] = Long.MAX_VALUE;
		longArr[1] = Long.MIN_VALUE;
		System.out.println("Long[]：正常");
		Assertions.assertEquals("[\"9223372036854775807\",\"-9223372036854775808\"]", JSON.toJSONString(longArr)); // 含双引号，正确的


		// 以下情况为错误的情况 --------------------------------------------------------

		// long[]
		long[] longArr2 = new long[2];
		longArr2[0] = Long.MAX_VALUE;
		longArr2[1] = Long.MIN_VALUE;
		System.out.println("long[]：异常");
		Assertions.assertEquals("[9223372036854775807,-9223372036854775808]", JSON.toJSONString(longArr2)); // 不含双引号，错误的

		Collection<Long> coll;
		// ArrayList<Long>
		coll = new ArrayList<>();
		coll.add(Long.MAX_VALUE);
		coll.add(Long.MIN_VALUE);
		System.out.println("ArrayList<Long>：异常");
		Assertions.assertEquals("[9223372036854775807,-9223372036854775808]", JSON.toJSONString(coll)); // 不含双引号，错误的

		// LinkedList<Long>
		coll = new LinkedList<>();
		coll.add(Long.MAX_VALUE);
		System.out.println("LinkedList<Long>：异常");
		Assertions.assertEquals("[9223372036854775807]", JSON.toJSONString(coll)); // 不含双引号，错误的

		// HashSet<Long>
		coll = new HashSet<>();
		coll.add(Long.MAX_VALUE);
		coll.add(Long.MIN_VALUE);
		System.out.println("HashSet<Long>：异常");
		Assertions.assertEquals("[9223372036854775807,-9223372036854775808]", JSON.toJSONString(coll)); // 不含双引号，错误的

		// LinkedHashSet<Long>
		coll = new LinkedHashSet<>();
		coll.add(Long.MAX_VALUE);
		System.out.println("LinkedHashSet<Long>：异常");
		Assertions.assertEquals("[9223372036854775807]", JSON.toJSONString(coll)); // 不含双引号，错误的
	}

	/**
	 * BUG修复后
	 */
	private void testFastjsonAfterBugfix() {
		// Long
		System.out.println("Long：正常");
		Assertions.assertEquals("\"9223372036854775807\"", FASTJSON_SERVICE.toJSONString(Long.MAX_VALUE)); // 含双引号，正确的
		Assertions.assertEquals("\"-9223372036854775808\"", FASTJSON_SERVICE.toJSONString(Long.MIN_VALUE)); // 含双引号，正确的

		// Long[]
		Long[] longArr = new Long[2];
		longArr[0] = Long.MAX_VALUE;
		longArr[1] = Long.MIN_VALUE;
		System.out.println("Long[]：正常");
		Assertions.assertEquals("[\"9223372036854775807\",\"-9223372036854775808\"]", FASTJSON_SERVICE.toJSONString(longArr)); // 含双引号，正确的

		// 以下情况原本为错误的情况，现已全部正常 --------------------------------------------------------

		// long[]
		long[] longArr2 = new long[2];
		longArr2[0] = Long.MAX_VALUE;
		longArr2[1] = Long.MIN_VALUE;
		System.out.println("long[]：正常");
		Assertions.assertEquals("[\"9223372036854775807\",\"-9223372036854775808\"]", FASTJSON_SERVICE.toJSONString(longArr2)); // 含双引号，正确的，bug已修复

		Collection<Long> coll;
		// ArrayList<Long>
		coll = new ArrayList<>();
		coll.add(Long.MAX_VALUE);
		coll.add(Long.MIN_VALUE);
		System.out.println("ArrayList<Long>：正常");
		Assertions.assertEquals("[\"9223372036854775807\",\"-9223372036854775808\"]", FASTJSON_SERVICE.toJSONString(coll)); // 含双引号，正确的，bug已修复

		// LinkedList<Long>
		coll = new LinkedList<>();
		coll.add(Long.MAX_VALUE);
		System.out.println("LinkedList<Long>：正常");
		Assertions.assertEquals("[\"9223372036854775807\"]", FASTJSON_SERVICE.toJSONString(coll)); // 含双引号，正确的，bug已修复


		// HashSet<Long>
		coll = new HashSet<>();
		coll.add(Long.MAX_VALUE);
		coll.add(Long.MIN_VALUE);
		System.out.println("HashSet<Long>：正常");
		Assertions.assertEquals("[\"9223372036854775807\",\"-9223372036854775808\"]", FASTJSON_SERVICE.toJSONString(coll)); // 含双引号，正确的，bug已修复

		// LinkedHashSet<Long>
		coll = new LinkedHashSet<>();
		coll.add(Long.MAX_VALUE);
		System.out.println("LinkedHashSet<Long>：正常");
		Assertions.assertEquals("[\"9223372036854775807\"]", FASTJSON_SERVICE.toJSONString(coll)); // 含双引号，正确的，bug已修复
	}

	////endregion

	//endregion
}
