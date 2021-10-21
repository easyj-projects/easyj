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
import java.util.List;
import java.util.function.Supplier;

import icu.easyj.core.enums.DateFormatType;
import icu.easyj.core.loader.EnhancedServiceLoader;
import icu.easyj.core.loader.ServiceProviders;
import icu.easyj.core.modelfortest.TestUser;
import icu.easyj.core.modelfortest.TestUser3;
import icu.easyj.core.util.DateUtils;
import icu.easyj.core.util.StringUtils;
import icu.easyj.test.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link JSONUtils} 测试类
 *
 * @author wangliang181230
 */
class JSONUtilsTest {

	private static final int THREAD_COUNT = 10;
	private static final int TIMES = 2 * 10000;

	private static final String JSON1 = "{\"Name\":\"某某人1\",\"Age\":31,\"Birthday\":\"1990-10-01\"}";
	private static final String JSON2 = "{\"Name\":\"某某人2\",\"Age\":32,\"Birthday\":\"1989-10-02\"}";
	private static final String JSON3 = "{\"user_name\":\"某某人3\",\"user_age\":33,\"user_birthday\":\"1988-10-03\"}";
	private static final String LIST_JSON = "[" + JSON1 + "," + JSON2 + "]";

	private static final IJSONService DEFAULT = EnhancedServiceLoader.load(IJSONService.class);

	private static final IJSONService FASTJSON = EnhancedServiceLoader.load(IJSONService.class, ServiceProviders.FASTJSON);
	private static final IJSONService JACKSON = EnhancedServiceLoader.load(IJSONService.class, ServiceProviders.JACKSON);
	private static final IJSONService HUTOOL = EnhancedServiceLoader.load(IJSONService.class, ServiceProviders.HUTOOL);

	private static final List<IJSONService> SERVICES = EnhancedServiceLoader.loadAll(IJSONService.class);

	@Test
	void testServiceLoader() {
		Assertions.assertEquals(DEFAULT, FASTJSON);

		Assertions.assertEquals(FASTJSON, SERVICES.get(0));
		Assertions.assertEquals("icu.easyj.core.json.impls.AlibabaFastJSONServiceImpl", FASTJSON.getClass().getName());

		Assertions.assertEquals(JACKSON, SERVICES.get(1));
		Assertions.assertEquals("icu.easyj.core.json.impls.JacksonJSONServiceImpl", JACKSON.getClass().getName());

		Assertions.assertEquals(HUTOOL, SERVICES.get(2));
		Assertions.assertEquals("icu.easyj.core.json.impls.HutoolJSONServiceImpl", HUTOOL.getClass().getName());
	}

	@Test
	void testToBean() {
		for (IJSONService service : SERVICES) {
			assertEquals1(service, service.toBean(JSON1, TestUser.class));
			assertEquals2(service, service.toBean(JSON2, TestUser.class));

			// jackson不会自动匹配Key的下划线格式，必须手动添加注解@JsonProperty("xxx_yyy")
			if (!ServiceProviders.JACKSON.equals(service.getName())) {
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
	void testToList() {
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
	void testToJSONString() throws ParseException {
		for (IJSONService service : SERVICES) {
			testToJSONString(service);
		}

		TestUser user1 = new TestUser("某某人1", 31, DateUtils.parseDate("1990-10-01"));
		TestUser user2 = new TestUser("某某人2", 32, DateUtils.parseDate("1989-10-02"));

		System.out.println(JACKSON.toJSONString(user1));

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
			if (!ServiceProviders.JACKSON.equals(service.getName())) {
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
			if (!ServiceProviders.JACKSON.equals(service.getName())) {
				throw t;
			}
		}
	}

	private void assertEquals1(IJSONService service, TestUser user) {
		Assertions.assertNotNull(user);
		Assertions.assertEquals("某某人1", user.getName());
		Assertions.assertEquals(31, user.getAge());
		try {
			Assertions.assertEquals("1990-10-01", DateUtils.format(DateFormatType.DD, user.getBirthday()));
		} catch (Throwable t) {
			// FIXME: jackson 在 github/actions 上会存在时区问题
			if (!ServiceProviders.JACKSON.equals(service.getName())) {
				throw t;
			}
		}
	}

	private void assertEquals2(IJSONService service, TestUser user) {
		Assertions.assertNotNull(user);
		Assertions.assertEquals("某某人2", user.getName());
		Assertions.assertEquals(32, user.getAge());
		try {
			Assertions.assertEquals("1989-10-02", DateUtils.format(DateFormatType.DD, user.getBirthday()));
		} catch (Throwable t) {
			// FIXME: jackson 在 github/actions 上会存在时区问题
			if (!ServiceProviders.JACKSON.equals(service.getName())) {
				throw t;
			}
		}
	}

	private void assertEquals3(IJSONService service, TestUser3 user) {
		Assertions.assertNotNull(user);
		Assertions.assertEquals("某某人3", user.getUserName());
		Assertions.assertEquals(33, user.getUserAge());
		try {
			Assertions.assertEquals("1988-10-03", DateUtils.format(DateFormatType.DD, user.getUserBirthday()));
		} catch (Throwable t) {
			// FIXME: jackson 在 github/actions 上会存在时区问题
			if (!ServiceProviders.JACKSON.equals(service.getName())) {
				throw t;
			}
		}
	}
}
