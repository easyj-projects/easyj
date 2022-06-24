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

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import icu.easyj.core.loader.EnhancedServiceLoader;
import icu.easyj.core.modelfortest.TestGeneric;
import icu.easyj.core.modelfortest.TestUser;
import icu.easyj.core.modelfortest.TestUser3;
import icu.easyj.core.util.DateUtils;
import icu.easyj.core.util.ObjectUtils;
import icu.easyj.core.util.StringUtils;
import icu.easyj.test.util.TestUtils;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static icu.easyj.core.loader.ServiceProviders.FASTJSON;
import static icu.easyj.core.loader.ServiceProviders.FASTJSON2;
import static icu.easyj.core.loader.ServiceProviders.GSON;
import static icu.easyj.core.loader.ServiceProviders.HUTOOL;
import static icu.easyj.core.loader.ServiceProviders.JACKSON;

/**
 * {@link JSONUtils} 测试类
 *
 * @author wangliang181230
 */
public class JSONUtilsTest {

	private static final int THREAD_COUNT = 5;
	private static final int TIMES = 10000;

	private static final String JSON1 = "{\"Name\":\"某某人1\",\"Age\":31,\"Birthday\":\"1990-10-01\"}";
	private static final String JSON2 = "{\"Name\":\"某某人2\",\"Age\":32,\"Birthday\":\"1989-10-02\"}";
	private static final String JSON3 = "{\"a\":" + JSON1 + ",\"b\":" + JSON2 + "}";
	private static final String JSON4 = "{\"user_name\":\"某某人3\",\"user_age\":33,\"user_birthday\":\"1988-10-03\"}";
	private static final String LIST_JSON = "[" + JSON1 + "," + JSON2 + "]";

	private static final IJSONService DEFAULT_SERVICE = EnhancedServiceLoader.load(IJSONService.class);

	private static final IJSONService FASTJSON_SERVICE = EnhancedServiceLoader.load(IJSONService.class, FASTJSON);
	private static final IJSONService FASTJSON2_SERVICE = EnhancedServiceLoader.load(IJSONService.class, FASTJSON2);
	private static final IJSONService JACKSON_SERVICE = EnhancedServiceLoader.load(IJSONService.class, JACKSON);
	private static final IJSONService GSON_SERVICE = EnhancedServiceLoader.load(IJSONService.class, GSON);
	private static final IJSONService HUTOOL_SERVICE = EnhancedServiceLoader.load(IJSONService.class, HUTOOL);

	private static final List<IJSONService> SERVICES = EnhancedServiceLoader.loadAll(IJSONService.class);

	@Test
	public void testServiceLoader() {
		Assertions.assertEquals(DEFAULT_SERVICE, FASTJSON_SERVICE);

		Assertions.assertEquals(FASTJSON_SERVICE, SERVICES.get(0));
		Assertions.assertEquals("AlibabaFastJSONServiceImpl", FASTJSON_SERVICE.getClass().getSimpleName());

		Assertions.assertEquals(FASTJSON2_SERVICE, SERVICES.get(1));
		Assertions.assertEquals("AlibabaFastJSON2ServiceImpl", FASTJSON2_SERVICE.getClass().getSimpleName());

		Assertions.assertEquals(JACKSON_SERVICE, SERVICES.get(2));
		Assertions.assertEquals("JacksonJSONServiceImpl", JACKSON_SERVICE.getClass().getSimpleName());

		Assertions.assertEquals(GSON_SERVICE, SERVICES.get(3));
		Assertions.assertEquals("GoogleGsonJSONServiceImpl", GSON_SERVICE.getClass().getSimpleName());

		Assertions.assertEquals(HUTOOL_SERVICE, SERVICES.get(4));
		Assertions.assertEquals("HutoolJSONServiceImpl", HUTOOL_SERVICE.getClass().getSimpleName());
	}

	@Test
	public void testToBean() {
		for (IJSONService service : SERVICES) {
			assertEquals1(service, service.toBean(JSON1, TestUser.class));
			assertEquals2(service, service.toBean(JSON2, (Type)TestUser.class)); // 测试重载方法
			assertEquals3(service, service.toBean(JSON3, TestGeneric.class, TestUser.class, TestUser.class)); // 测试重载方法

			// jackson、json、fastjson2不会自动匹配Key的下划线格式，必须手动添加注解@JsonProperty("xxx_yyy")
			if (ObjectUtils.notIn(service.getName(), JACKSON, GSON, FASTJSON2)) {
				assertEquals4(service, service.toBean(JSON4, TestUser3.class));
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
	public void testToMap() {
		String text;

		// case 1
		text = "{\"a\": \"1\", \"b\": 2}";
		for (IJSONService service : SERVICES) {
			Map<String, String> map = service.toMap2(text, String.class);
			Assertions.assertEquals(2, map.size());
			Assertions.assertEquals("1", map.get("a"));
			Assertions.assertEquals("2", map.get("b"));
		}

		// case 2
		text = "{\"a\": \"1\", \"b\": \"2\"}";
		for (IJSONService service : SERVICES) {
			Map<String, Integer> map = service.toMap(text, String.class, Integer.class);
			Assertions.assertEquals(2, map.size());
			Assertions.assertEquals(1, (long)map.get("a"));
			Assertions.assertEquals(2, (long)map.get("b"));
		}

		// case 3: 警告：解析Map时，不推荐使用toBean方法，因为数据类型会不符合泛型变量的泛型类型。在调用Map.get方法时，会抛出ClassCastException异常
		text = "{\"a\": \"1\", \"b\": 2}";
		for (IJSONService service : SERVICES) {
			Map<String, String> map = service.toBean(text, Map.class); // 不推荐toBean方法，推荐toMap方法
			final Map<String, String> finalMap = map;
			Assertions.assertEquals(2, map.size());
			Assertions.assertEquals("1", map.get("a"));
			Assertions.assertThrows(ClassCastException.class, () -> System.out.println("b的类型：" + finalMap.get("b").getClass()), service.getName() + ", "); // 会抛异常

			// 如果用toBean方法，则请使用重载方法（toMap方法其实就是以下代码的封装）
			map = service.toBean(text, Map.class, String.class, String.class);
			Assertions.assertEquals(2, map.size());
			Assertions.assertEquals("1", map.get("a"));
			Assertions.assertEquals("2", map.get("b"));
		}
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
		Assertions.assertEquals(user1.toString(), service.toBean(jsonStr1, TestUser.class).toString(), service.getName());
		Assertions.assertEquals(user2.toString(), service.toBean(jsonStr2, TestUser.class).toString(), service.getName());
		Assertions.assertTrue(jsonStr1.contains("\"Name\""), service.getName());
		Assertions.assertTrue(jsonStr1.contains("\"Age\""), service.getName());
		Assertions.assertTrue(jsonStr1.contains("\"Birthday\""), service.getName());

		List<TestUser> list1 = new ArrayList<>();
		list1.add(user1);
		list1.add(user2);

		List<TestUser> list2 = service.toList(service.toJSONString(list1), TestUser.class);
		Assertions.assertNotNull(list2, service.getName());
		Assertions.assertEquals(2, list2.size(), service.getName());

		Assertions.assertEquals(list1.get(0).toString(), list2.get(0).toString(), service.getName());
		Assertions.assertEquals(list1.get(1).toString(), list2.get(1).toString(), service.getName());
	}

	private void assertEquals1(IJSONService service, TestUser user) {
		Assertions.assertNotNull(user, service.getName());
		Assertions.assertEquals("某某人1", user.getName(), service.getName());
		Assertions.assertEquals(31, user.getAge().intValue(), service.getName());
		Assertions.assertEquals("1990-10-01 00:00:00.000", DateUtils.toMilliseconds(user.getBirthday()), service.getName());
	}

	private void assertEquals2(IJSONService service, TestUser user) {
		Assertions.assertNotNull(user, service.getName());
		Assertions.assertEquals("某某人2", user.getName(), service.getName());
		Assertions.assertEquals(32, user.getAge().intValue(), service.getName());
		Assertions.assertEquals("1989-10-02 00:00:00.000", DateUtils.toMilliseconds(user.getBirthday()), service.getName());
	}

	private void assertEquals3(IJSONService service, TestGeneric<TestUser, TestUser> data) {
		Assertions.assertNotNull(data, service.getName());
		assertEquals1(service, data.getA());
		assertEquals2(service, data.getB());
	}

	private void assertEquals4(IJSONService service, TestUser3 user) {
		Assertions.assertNotNull(user, service.getName());
		Assertions.assertEquals("某某人3", user.getUserName(), service.getName());
		Assertions.assertEquals(33, user.getUserAge().intValue(), service.getName());
		Assertions.assertEquals("1988-10-03 00:00:00.000", DateUtils.toMilliseconds(user.getUserBirthday()), service.getName());
	}

	//endregion
}
