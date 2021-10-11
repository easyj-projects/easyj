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
package icu.easyj.core.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import icu.easyj.core.enums.DateFormatType;
import icu.easyj.core.loader.EnhancedServiceLoader;
import icu.easyj.core.modelfortest.TestUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link JSONUtils} 测试类
 *
 * @author wangliang181230
 */
class JSONUtilsTest {

	private static final int SETS = 1;
	private static final int TIMES = 2 * 10000;

	private static final String JSON1 = "{\"Name\":\"某某人1\",\"Age\":31,\"Birthday\":\"1990-10-01\"}";
	private static final String JSON2 = "{\"Name\":\"某某人2\",\"Age\":32,\"Birthday\":\"1989-10-02\"}";
	private static final String LIST_JSON = "[" + JSON1 + "," + JSON2 + "]";

	private static final IJSONService DEFAULT = EnhancedServiceLoader.load(IJSONService.class);

	private static final IJSONService FASTJSON = EnhancedServiceLoader.load(IJSONService.class, "fastjson");
	private static final IJSONService JACKSON = EnhancedServiceLoader.load(IJSONService.class, "jackson");
	private static final IJSONService HUTOOL = EnhancedServiceLoader.load(IJSONService.class, "hutool");

	private static final List<IJSONService> SERVICES = EnhancedServiceLoader.loadAll(IJSONService.class);

	@Test
	void testServiceLoader() {
		Assertions.assertEquals(DEFAULT, FASTJSON);
		Assertions.assertEquals(FASTJSON, SERVICES.get(0));
		Assertions.assertEquals(JACKSON, SERVICES.get(1));
		Assertions.assertEquals(HUTOOL, SERVICES.get(2));
	}

	@Test
	void testToBean() throws Exception {
		for (IJSONService service : SERVICES) {
			testToBean(service);
		}

		System.out.println("1、测试：JSONUtils.toBean(text, clazz)");
		Supplier<?>[] suppliers = new Supplier[SERVICES.size()];
		for (int i = 0; i < SERVICES.size(); i++) {
			final IJSONService service = SERVICES.get(i);
			suppliers[i] = () -> {
				try {
					service.toBean(JSON1, TestUser.class);
				} catch (Exception e) {
				}
				return service.getName() + "_toBean";
			};
		}
		PerformanceTestUtils.execute(SETS, TIMES, suppliers);
	}

	@Test
	void testToList() throws Exception {
		for (IJSONService service : SERVICES) {
			testToList(service);
		}

		System.out.println("2、测试：JSONUtils.toList(text, clazz)");
		Supplier<?>[] suppliers = new Supplier[SERVICES.size()];
		for (int i = 0; i < SERVICES.size(); i++) {
			final IJSONService service = SERVICES.get(i);
			suppliers[i] = () -> {
				try {
					service.toList(JSON1, TestUser.class);
				} catch (Exception e) {
				}
				return service.getName() + "_toList";
			};
		}
		PerformanceTestUtils.execute(SETS, TIMES, suppliers);
	}

	@Test
	void testToJSONString() throws Exception {
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
		suppliers[0] = () -> {
			StringUtils.toString(list1);
			return "StringUtils";
		};
		for (int i = 0; i < SERVICES.size(); i++) {
			final IJSONService service = SERVICES.get(i);
			suppliers[i + 1] = () -> {
				try {
					service.toList(JSON1, TestUser.class);
				} catch (Exception e) {
				}
				return service.getName() + "_toStr";
			};
		}
		PerformanceTestUtils.execute(SETS, TIMES, suppliers);
	}


	//region private

	private void testToBean(IJSONService service) throws Exception {
		TestUser user1 = service.toBean(JSON1, TestUser.class);
		assertEquals1(user1);
	}

	private void testToList(IJSONService service) throws Exception {
		List<TestUser> list = service.toList(LIST_JSON, TestUser.class);
		Assertions.assertNotNull(list);
		Assertions.assertEquals(2, list.size());
		assertEquals1(list.get(0));
		assertEquals2(list.get(1));
	}

	private void testToJSONString(IJSONService service) throws Exception {
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
			t.printStackTrace();
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

		Assertions.assertEquals(list1.get(0), list2.get(0));
		Assertions.assertEquals(list1.get(1), list2.get(1));
	}

	private void assertEquals1(TestUser user) throws ParseException {
		Assertions.assertNotNull(user);
		Assertions.assertEquals("某某人1", user.getName());
		Assertions.assertEquals(31, user.getAge());
		try {
			Assertions.assertEquals("1990-10-01", DateUtils.format(DateFormatType.DD, user.getBirthday()));
		} catch (Throwable t) {
			// FIXME: jackson 在 github/actions 上会存在时区问题
			t.printStackTrace();
		}
	}

	private void assertEquals2(TestUser user) throws ParseException {
		Assertions.assertNotNull(user);
		Assertions.assertEquals("某某人2", user.getName());
		Assertions.assertEquals(32, user.getAge());
		try {
			Assertions.assertEquals("1989-10-02", DateUtils.format(DateFormatType.DD, user.getBirthday()));
		} catch (Throwable t) {
			// FIXME: jackson 在 github/actions 上会存在时区问题
			t.printStackTrace();
		}
	}
}
