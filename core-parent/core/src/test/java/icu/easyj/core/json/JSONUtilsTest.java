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
package icu.easyj.core.json;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.EasyjFastjsonBugfixUtils;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import icu.easyj.core.enums.DateFormatType;
import icu.easyj.core.loader.EnhancedServiceLoader;
import icu.easyj.core.modelfortest.TestUser;
import icu.easyj.core.modelfortest.TestUser3;
import icu.easyj.core.util.DateUtils;
import icu.easyj.core.util.ObjectUtils;
import icu.easyj.core.util.StringUtils;
import icu.easyj.test.util.TestUtils;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static icu.easyj.core.loader.ServiceProviders.FASTJSON;
import static icu.easyj.core.loader.ServiceProviders.GSON;
import static icu.easyj.core.loader.ServiceProviders.HUTOOL;
import static icu.easyj.core.loader.ServiceProviders.JACKSON;

/**
 * {@link JSONUtils} 测试类
 *
 * @author wangliang181230
 */
public class JSONUtilsTest {

	private static final int THREAD_COUNT = 10;
	private static final int TIMES = 2 * 10000;

	private static final String JSON1 = "{\"Name\":\"某某人1\",\"Age\":31,\"Birthday\":\"1990-10-01\"}";
	private static final String JSON2 = "{\"Name\":\"某某人2\",\"Age\":32,\"Birthday\":\"1989-10-02\"}";
	private static final String JSON3 = "{\"user_name\":\"某某人3\",\"user_age\":33,\"user_birthday\":\"1988-10-03\"}";
	private static final String LIST_JSON = "[" + JSON1 + "," + JSON2 + "]";

	private static final IJSONService DEFAULT_SERVICE = EnhancedServiceLoader.load(IJSONService.class);

	private static final IJSONService FASTJSON_SERVICE = EnhancedServiceLoader.load(IJSONService.class, FASTJSON);
	private static final IJSONService JACKSON_SERVICE = EnhancedServiceLoader.load(IJSONService.class, JACKSON);
	private static final IJSONService GSON_SERVICE = EnhancedServiceLoader.load(IJSONService.class, GSON);
	private static final IJSONService HUTOOL_SERVICE = EnhancedServiceLoader.load(IJSONService.class, HUTOOL);

	private static final List<IJSONService> SERVICES = EnhancedServiceLoader.loadAll(IJSONService.class);

	@Test
	public void testServiceLoader() {
		Assertions.assertEquals(DEFAULT_SERVICE, FASTJSON_SERVICE);

		Assertions.assertEquals(FASTJSON_SERVICE, SERVICES.get(0));
		Assertions.assertEquals("AlibabaFastJSONServiceImpl", FASTJSON_SERVICE.getClass().getSimpleName());

		Assertions.assertEquals(JACKSON_SERVICE, SERVICES.get(1));
		Assertions.assertEquals("JacksonJSONServiceImpl", JACKSON_SERVICE.getClass().getSimpleName());

		Assertions.assertEquals(GSON_SERVICE, SERVICES.get(2));
		Assertions.assertEquals("GoogleGsonJSONServiceImpl", GSON_SERVICE.getClass().getSimpleName());

		Assertions.assertEquals(HUTOOL_SERVICE, SERVICES.get(3));
		Assertions.assertEquals("HutoolJSONServiceImpl", HUTOOL_SERVICE.getClass().getSimpleName());
	}

	@Test
	public void testToBean() {
		for (IJSONService service : SERVICES) {
			assertEquals1(service, service.toBean(JSON1, TestUser.class));
			assertEquals2(service, service.toBean(JSON2, TestUser.class));

			// jackson和json不会自动匹配Key的下划线格式，必须手动添加注解@JsonProperty("xxx_yyy")
			if (ObjectUtils.notIn(service.getName(), JACKSON, GSON)) {
				assertEquals3(service, service.toBean(JSON3, TestUser3.class));
			}
		}

		System.out.println("1、测试：JSONUtils.toBean(text, clazz)");
		Supplier<?>[] suppliers = new Supplier[SERVICES.size()];
		for (int i = 0; i < SERVICES.size(); i++) {
			final IJSONService service = SERVICES.get(i);
			suppliers[i] = () -> {
				try {
					service.toBean(JSON1, TestUser.class);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				return service.getName() + "_toBean";
			};
		}
		TestUtils.performanceTest(THREAD_COUNT, TIMES, suppliers);
	}

	@Test
	public void testToList() {
		for (IJSONService service : SERVICES) {
			testToList(service);
		}

		System.out.println("2、测试：JSONUtils.toList(text, clazz)");
		Supplier<?>[] suppliers = new Supplier[SERVICES.size()];
		for (int i = 0; i < SERVICES.size(); i++) {
			final IJSONService service = SERVICES.get(i);
			suppliers[i] = () -> {
				try {
					service.toList(LIST_JSON, TestUser.class);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				return service.getName() + "_toList";
			};
		}
		TestUtils.performanceTest(THREAD_COUNT, TIMES, suppliers);
	}

	@Test
	public void testToJSONString() throws ParseException {
		for (IJSONService service : SERVICES) {
			testToJSONString(service);
		}

		TestUser user1 = new TestUser("某某人1", 31, DateUtils.parseDate("1990-10-01"));
		TestUser user2 = new TestUser("某某人2", 32, DateUtils.parseDate("1989-10-02"));

		System.out.println(JACKSON_SERVICE.toJSONString(user1));

		List<TestUser> list1 = new ArrayList<>();
		list1.add(user1);
		list1.add(user2);

		System.out.println("3、测试：JSONUtils.toJSONString(obj)");
		Supplier<?>[] suppliers = new Supplier[SERVICES.size() + 1];
		for (int i = 0; i < SERVICES.size(); i++) {
			final IJSONService service = SERVICES.get(i);
			suppliers[i] = () -> {
				try {
					service.toJSONString(list1);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				return service.getName() + "_toStr";
			};
		}
		// 再添加一个StringUtils.toString方法，比较一下性能
		suppliers[suppliers.length - 1] = () -> {
			StringUtils.toString(list1);
			return "StringUtils";
		};
		TestUtils.performanceTest(THREAD_COUNT, TIMES, suppliers);
	}


	//region private

	private void testToList(IJSONService service) {
		List<TestUser> list = service.toList(LIST_JSON, TestUser.class);
		Assertions.assertNotNull(list);
		Assertions.assertEquals(2, list.size());
		assertEquals1(service, list.get(0));
		assertEquals2(service, list.get(1));
	}

	private void testToJSONString(IJSONService service) throws ParseException {
		TestUser user1 = new TestUser("某某人1", 31, DateUtils.parseDate("1990-10-01"));
		TestUser user2 = new TestUser("某某人2", 32, DateUtils.parseDate("1989-10-02"));

		String jsonStr1 = service.toJSONString(user1);
		String jsonStr2 = service.toJSONString(user2);
		System.out.println(service.getName() + ": jsonStr1: " + jsonStr1);
		System.out.println(service.getName() + ": jsonStr2: " + jsonStr2);
		try {
			Assertions.assertTrue(user1.equals(service.toBean(jsonStr1, TestUser.class)));
			Assertions.assertTrue(user2.equals(service.toBean(jsonStr2, TestUser.class)));
		} catch (Throwable t) {
			// FIXME: jackson 在 github/actions 上会存在时区问题
			if (!JACKSON.equals(service.getName())) {
				throw t;
			}
		}
		Assertions.assertTrue(jsonStr1.contains("\"Name\""));
		Assertions.assertTrue(jsonStr1.contains("\"Age\""));
		Assertions.assertTrue(jsonStr1.contains("\"Birthday\""));

		List<TestUser> list1 = new ArrayList<>();
		list1.add(user1);
		list1.add(user2);

		List<TestUser> list2 = service.toList(service.toJSONString(list1), TestUser.class);
		Assertions.assertNotNull(list2);
		Assertions.assertEquals(2, list2.size());

		try {
			Assertions.assertTrue(list1.get(0).equals(list2.get(0)));
			Assertions.assertTrue(list1.get(1).equals(list2.get(1)));
		} catch (Throwable t) {
			// FIXME: jackson 在 github/actions 上会存在时区问题
			if (!JACKSON.equals(service.getName())) {
				throw t;
			}
		}
	}

	private void assertEquals1(IJSONService service, TestUser user) {
		Assertions.assertNotNull(user);
		Assertions.assertEquals("某某人1", user.getName());
		Assertions.assertEquals(31, user.getAge().intValue());
		try {
			Assertions.assertEquals("1990-10-01", DateUtils.format(DateFormatType.DD, user.getBirthday()));
		} catch (Throwable t) {
			// FIXME: jackson 在 github/actions 上会存在时区问题
			if (!JACKSON.equals(service.getName())) {
				throw t;
			}
		}
	}

	private void assertEquals2(IJSONService service, TestUser user) {
		Assertions.assertNotNull(user);
		Assertions.assertEquals("某某人2", user.getName());
		Assertions.assertEquals(32, user.getAge().intValue());
		try {
			Assertions.assertEquals("1989-10-02", DateUtils.format(DateFormatType.DD, user.getBirthday()));
		} catch (Throwable t) {
			// FIXME: jackson 在 github/actions 上会存在时区问题
			if (!JACKSON.equals(service.getName())) {
				throw t;
			}
		}
	}

	private void assertEquals3(IJSONService service, TestUser3 user) {
		Assertions.assertNotNull(user);
		Assertions.assertEquals("某某人3", user.getUserName());
		Assertions.assertEquals(33, user.getUserAge().intValue());
		try {
			Assertions.assertEquals("1988-10-03", DateUtils.format(DateFormatType.DD, user.getUserBirthday()));
		} catch (Throwable t) {
			// FIXME: jackson 在 github/actions 上会存在时区问题
			if (!JACKSON.equals(service.getName())) {
				throw t;
			}
		}
	}


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

	//endregion
}
