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
package icu.easyj.core.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.system.JavaInfo;
import cn.hutool.system.SystemUtil;
import icu.easyj.test.util.TestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * {@link StringUtils} 测试类
 *
 * @author wangliang181230
 */
public class StringUtilsTest {

	@Test
	public void testIsEmptyAndIsBlank() {
		// isEmpty
		Assertions.assertTrue(StringUtils.isEmpty(null));
		Assertions.assertTrue(StringUtils.isEmpty(""));
		Assertions.assertFalse(StringUtils.isEmpty(" "));
		Assertions.assertFalse(StringUtils.isEmpty(" 1 "));
		// isNotEmpty
		Assertions.assertFalse(StringUtils.isNotEmpty(null));
		Assertions.assertFalse(StringUtils.isNotEmpty(""));
		Assertions.assertTrue(StringUtils.isNotEmpty(" "));
		Assertions.assertTrue(StringUtils.isNotEmpty(" 1 "));

		// isBlank
		Assertions.assertTrue(StringUtils.isBlank(null));
		Assertions.assertTrue(StringUtils.isBlank(""));
		Assertions.assertTrue(StringUtils.isBlank(" "));
		Assertions.assertFalse(StringUtils.isBlank(" 1 "));
		// isNotBlank
		Assertions.assertFalse(StringUtils.isNotBlank(null));
		Assertions.assertFalse(StringUtils.isNotBlank(""));
		Assertions.assertFalse(StringUtils.isNotBlank(" "));
		Assertions.assertTrue(StringUtils.isNotBlank(" 1 "));
	}


	//region test getValue 和 getCoder

	@Test
	public void testGetValue() {
		String s1 = "aaabbbccc";
		String s2 = "aaabbbccc啊";

		JavaInfo javaInfo = SystemUtil.getJavaInfo();
		System.out.println("Java version: " + javaInfo.getVersionFloat());

		if (javaInfo.getVersionInt() >= 900 && javaInfo.getVersionInt() < 1600) {
			byte[] bytes = (byte[])StringUtils.getValue(s1);
			Assertions.assertEquals(bytes.length, s1.length());

			bytes = (byte[])StringUtils.getValue(s2);
			Assertions.assertEquals(bytes.length, s2.length() * 2);
		} else {
			char[] chars = (char[])StringUtils.getValue(s1);
			Assertions.assertEquals(chars.length, s1.length());

			chars = (char[])StringUtils.getValue(s2);
			Assertions.assertEquals(chars.length, s2.length());
		}
	}


	@Test
	public void testGetCoder() {
		Assertions.assertEquals(0, StringUtils.getCoder("aaabbbccc=="));
		Assertions.assertEquals(1, StringUtils.getCoder("1啊啊啊哦哦哦呃呃呃"));
		Assertions.assertEquals(1, StringUtils.getCoder("2啊啊啊哦哦哦呃呃呃="));
		Assertions.assertEquals(1, StringUtils.getCoder("3啊啊啊哦哦哦呃呃呃=="));
		String s = "asdlfkjlasdfjasdlfkjlasdfjasdlfkjlasdfjasdlfkjlasdfjasdlfkjlasdfj啊";
		long[] costs = TestUtils.performanceTest(5, 100 * 10000, () -> {
			StringUtils.getCoder(s);
			return "default";
		}, () -> {
			this.getCoder(s);
			return "to_bytes";
		});

		if (costs[0] > costs[1]) {
			System.out.println("\r\n[WARNING] default的getCoder()方法比to_bytes的性能要低了，请注意替换实现。");
		}
	}

	private byte getCoder(String s) {
		byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
		if (bytes.length != s.length()) {
			return 1;
		} else {
			return 0;
		}
	}

	//endregion


	@Test
	public void testJoin() {
		// join
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			StringUtils.join('?', ',', -1);
		});
		Assertions.assertEquals("", StringUtils.join('?', ',', 0));
		Assertions.assertEquals("?", StringUtils.join('?', ',', 1));
		Assertions.assertEquals("?,?", StringUtils.join('?', ',', 2));
		Assertions.assertEquals("?,?,?", StringUtils.join('?', ',', 3));

		// joinWithSpace
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			StringUtils.joinWithSpace('?', ',', -1);
		});
		Assertions.assertEquals("", StringUtils.joinWithSpace('?', ',', 0));
		Assertions.assertEquals("?", StringUtils.joinWithSpace('?', ',', 1));
		Assertions.assertEquals("?, ?", StringUtils.joinWithSpace('?', ',', 2));
		Assertions.assertEquals("?, ?, ?", StringUtils.joinWithSpace('?', ',', 3));
	}


	@Test
	public void testRemove() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			StringUtils.remove(null, '/');
		});
		Assertions.assertEquals(" ", StringUtils.remove("/ /", '/'));
		Assertions.assertEquals("aabbcc ", StringUtils.remove("//aabbcc/ /", '/'));
		Assertions.assertEquals(" aabbcc", StringUtils.remove("/ /aabbcc", '/'));
		Assertions.assertEquals(" aabbcc  ", StringUtils.remove(" aabbcc / /", '/'));

		Assertions.assertEquals(" aabbcc  ", " aabbcc / /".replace("/", ""));
		Assertions.assertEquals(" aabbcc  ", " aabbcc / /".replaceAll("/", ""));

		// 比较 StringUtils.remove 方法 和 String.replace方法的性能
		{
			TestUtils.performanceTest(1, 10 * 10000, () -> {
				StringUtils.remove(RandomUtil.randomString(20), 'a');
				return "remove";
			}, () -> {
				RandomUtil.randomString(20).replace("a", "");
				return "replace";
			}, () -> {
				RandomUtil.randomString(20).replaceAll("a", "");
				return "replaceAll";
			});
		}
		{
			TestUtils.performanceTest(5, 10 * 10000, () -> {
				StringUtils.remove(RandomUtil.randomString(20), 'a');
				return "remove";
			}, () -> {
				RandomUtil.randomString(20).replace("a", "");
				return "replace";
			}, () -> {
				RandomUtil.randomString(20).replaceAll("a", "");
				return "replaceAll";
			});
		}
	}

	@Test
	public void testTrim() {
		// trim
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			StringUtils.trim(null, '/');
		});
		Assertions.assertEquals("", StringUtils.trim("/ /", '/'));
		Assertions.assertEquals("aabbcc", StringUtils.trim("//aabbcc/ /", '/'));
		Assertions.assertEquals("aabbcc", StringUtils.trim("/ /aabbcc", '/'));
		Assertions.assertEquals("aabbcc", StringUtils.trim(" aabbcc / /", '/'));

		// trimStart
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			StringUtils.trimStart(null, '/');
		});
		Assertions.assertEquals("", StringUtils.trimStart("/ /", '/'));
		Assertions.assertEquals("aabbcc/ /", StringUtils.trimStart("//aabbcc/ /", '/'));
		Assertions.assertEquals("aabbcc", StringUtils.trimStart("//aabbcc", '/'));
		Assertions.assertEquals("aabbcc / /", StringUtils.trimStart(" aabbcc / /", '/'));


		// trimEnd
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			StringUtils.trimEnd(null, '/');
		});
		Assertions.assertEquals("", StringUtils.trimEnd("/ /", '/'));
		Assertions.assertEquals("//aabbcc", StringUtils.trimEnd("//aabbcc/ /", '/'));
		Assertions.assertEquals("/ /aabbcc", StringUtils.trimEnd("/ /aabbcc", '/'));
		Assertions.assertEquals(" aabbcc", StringUtils.trimEnd(" aabbcc / /", '/'));
	}

	@Test
	public void testIndexOf() {
		Assertions.assertEquals(4, StringUtils.indexOf("aaccbdb", 'b', 1));
		Assertions.assertEquals(6, StringUtils.indexOf("aaccbdb", 'b', 2));
		Assertions.assertEquals(-2, StringUtils.indexOf("aaccbdb", 'b', 3));
		Assertions.assertEquals(-1, StringUtils.indexOf("11111", 'b', 3));
	}


	//region 测试`StringUtils.toString()`方法和CycleDependency类

	@Test
	@SuppressWarnings("all")
	public void testToStringAndCycleDependency() throws Exception {
		//case: String
		assertEquals("\"aaa\"", StringUtils.toString("aaa"));

		//case: CharSequence
		assertEquals("\"bbb\"", StringUtils.toString(new StringBuilder("bbb")));
		//case: Number
		assertEquals("1", StringUtils.toString(1));
		//case: Number-Long
		assertEquals("1L", StringUtils.toString(1L));
		//case: Boolean
		assertEquals("true", StringUtils.toString(true));
		//case: Character
		assertEquals("'2'", StringUtils.toString('2'));
		//case: Charset
		assertEquals("UTF-8", StringUtils.toString(StandardCharsets.UTF_8));
		//case: Thread
		try {
			assertEquals("Thread[main,5,main]", StringUtils.toString(Thread.currentThread()));
		} catch (AssertionFailedError ignore) {
			// for java21 and above
			assertEquals("Thread[#" + Thread.currentThread().getId() + ",main,5,main]", StringUtils.toString(Thread.currentThread()));
		}

		//case: Date
		Date date = new Date(2021 - 1900, 6 - 1, 15);
		assertEquals("2021-06-15", StringUtils.toString(date));
		date.setTime(date.getTime() + 3600000);
		assertEquals("2021-06-15 01:00", StringUtils.toString(date));
		date.setTime(date.getTime() + 60000);
		assertEquals("2021-06-15 01:01", StringUtils.toString(date));
		date.setTime(date.getTime() + 50000);
		assertEquals("2021-06-15 01:01:50", StringUtils.toString(date));
		date.setTime(date.getTime() + 12);
		assertEquals("2021-06-15 01:01:50.012", StringUtils.toString(date));

		//case: Enum
		assertEquals("TestEnum.AAA", StringUtils.toString(TestEnum.AAA));

		//case: Annotation
		TestAnnotation annotation = TestClass.class.getAnnotation(TestAnnotation.class);
		assertEquals("@" + TestAnnotation.class.getSimpleName() + "(test=true)", StringUtils.toString(annotation));

		//case: Class
		Class<?> clazz = TestClass.class;
		assertEquals("Class<" + clazz.getSimpleName() + ">", StringUtils.toString(clazz));

		//case: Method
		Method method = clazz.getMethod("setObj", TestClass.class);
		assertEquals("Method<" + clazz.getSimpleName() + ".setObj(" + clazz.getSimpleName() + ")>", StringUtils.toString(method));

		//case: Field
		Field field = clazz.getDeclaredField("s");
		assertEquals("Field<" + clazz.getSimpleName() + ".(String s)>", StringUtils.toString(field));

		//case: List, and cycle dependency
		List<Object> list = new ArrayList<>();
		list.add("xxx");
		list.add(111);
		list.add(list);
		assertEquals("[\"xxx\", 111, (this ArrayList)]", StringUtils.toString(list));

		//case: Map, and cycle dependency
		Map<Object, Object> map = new HashMap<>();
		map.put("aaa", 111);
		map.put("bbb", true);
		map.put("self", map);
		assertEquals("{\"aaa\" -> 111, \"bbb\" -> true, \"self\" -> (this HashMap)}", StringUtils.toString(map));
		assertFalse((boolean)ReflectionUtils.invokeStaticMethod(CycleDependencyHandler.class, "isStarting"));
		//case: Map, and cycle dependency（deep case）
		List<Object> list2 = new ArrayList<>();
		list2.add(map);
		list2.add('c');
		map.put("list", list2);
		assertEquals("{\"aaa\" -> 111, \"bbb\" -> true, \"self\" -> (this HashMap), \"list\" -> [(ref HashMap), 'c']}", StringUtils.toString(map));
		assertFalse((boolean)ReflectionUtils.invokeStaticMethod(CycleDependencyHandler.class, "isStarting"));

		//case: String Array
		String[] strArr = new String[2];
		strArr[0] = "11";
		strArr[1] = "22";
		assertEquals("[\"11\", \"22\"]", StringUtils.toString(strArr));
		//case: int Array
		int[] intArr = new int[2];
		intArr[0] = 11;
		intArr[1] = 22;
		assertEquals("[11, 22]", StringUtils.toString(intArr));
		//case: Array, and cycle dependency
		Object[] array = new Object[3];
		array[0] = 1;
		array[1] = '2';
		array[2] = array;
		assertEquals("[1, '2', (this Object[])]", StringUtils.toString(array));


		//case: Object
		assertEquals("CycleDependency(s=\"a\", obj=null)", StringUtils.toString(new CycleDependency("a")));
		//case: Object, and cycle dependency
		CycleDependency obj = new CycleDependency("c");
		obj.setObj(obj);
		assertEquals("CycleDependency(s=\"c\", obj=(this CycleDependency))", StringUtils.toString(obj));
		//case: Object
		CycleDependency obj2 = new CycleDependency("d");
		obj.setObj(obj2);
		assertEquals("CycleDependency(s=\"c\", obj=CycleDependency(s=\"d\", obj=null))", StringUtils.toString(obj));
		//case: Object, and cycle dependency
		TestClass a = new TestClass();
		a.setObj(a);
		assertEquals("TestClass(obj=(this TestClass), s=null)", StringUtils.toString(a));
		//case: Object, and cycle dependency（deep case）
		TestClass b = new TestClass();
		TestClass c = new TestClass();
		b.setObj(c);
		c.setObj(a);
		a.setObj(b);
		assertEquals("TestClass(obj=TestClass(obj=TestClass(obj=(ref TestClass), s=null), s=null), s=null)", StringUtils.toString(a));

		//case: anonymous class from an interface
		Object anonymousObj = new TestInterface() {
			private String a = "aaa";

			@Override
			public void test() {
			}
		};
		assertEquals("TestInterface$(a=\"aaa\")", StringUtils.toString(anonymousObj));

		//case: anonymous class from an abstract class
		anonymousObj = new TestAbstractClass() {
			private String a = "aaa";

			@Override
			public void test() {
			}
		};
		assertEquals("TestAbstractClass$(a=\"aaa\")", StringUtils.toString(anonymousObj));

		//final confirm: do not triggered the `toString` and `hashCode` methods
		assertFalse(TestClass.hashCodeTriggered);
		assertFalse(TestClass.toStringTriggered);
		assertFalse(CycleDependency.hashCodeTriggered);
		assertFalse(CycleDependency.toStringTriggered);
	}

	////region 辅助测试的类、注解、枚举等

	enum TestEnum {
		AAA
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@interface TestAnnotation {
		boolean test() default false;
	}

	interface TestInterface {
		void test();
	}

	abstract class TestAbstractClass {
		abstract void test();
	}

	@TestAnnotation(test = true)
	static class TestClass {
		public static boolean hashCodeTriggered = false;
		public static boolean toStringTriggered = false;

		private TestClass obj;
		private String s;

		@Override
		public int hashCode() {
			hashCodeTriggered = true;
			return super.hashCode();
		}

		@Override
		public String toString() {
			toStringTriggered = true;
			return StringUtils.toString(this);
		}

		public TestClass getObj() {
			return obj;
		}

		public void setObj(TestClass obj) {
			this.obj = obj;
		}
	}

	static class CycleDependency {
		public static boolean hashCodeTriggered = false;
		public static boolean toStringTriggered = false;

		private final String s;
		private CycleDependency obj;

		private CycleDependency(String s) {
			this.s = s;
		}

		public CycleDependency getObj() {
			return obj;
		}

		public void setObj(CycleDependency obj) {
			this.obj = obj;
		}

		@Override
		public int hashCode() {
			hashCodeTriggered = true;
			return super.hashCode();
		}

		@Override
		public String toString() {
			toStringTriggered = true;
			return "(" +
					"s=" + s + "," +
					"obj=" + (obj != this ? String.valueOf(obj) : "(this CycleDependency)") +
					')';
		}
	}

	////endregion

	//endregion
}
