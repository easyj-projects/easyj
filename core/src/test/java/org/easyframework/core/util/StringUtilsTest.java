package org.easyframework.core.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link StringUtils} 测试类
 *
 * @author wangliang181230
 */
class StringUtilsTest {

	@Test
	@SuppressWarnings("all")
	void testToStringAndCycleDependency() throws Exception {
		//case: String
		Assertions.assertEquals("aaa", StringUtils.toString("aaa"));

		//case: CharSequence
		Assertions.assertEquals("bbb", StringUtils.toString(new StringBuilder("bbb")));
		//case: Number
		Assertions.assertEquals("1", StringUtils.toString(1));
		//case: Boolean
		Assertions.assertEquals("true", StringUtils.toString(true));
		//case: Character
		Assertions.assertEquals("2", StringUtils.toString('2'));

		//case: Date
		Date date = new Date(2021 - 1900, 6 - 1, 15);
		Assertions.assertEquals("2021-06-15", StringUtils.toString(date));
		date.setTime(date.getTime() + 3600000);
		Assertions.assertEquals("2021-06-15 01:00", StringUtils.toString(date));
		date.setTime(date.getTime() + 60000);
		Assertions.assertEquals("2021-06-15 01:01", StringUtils.toString(date));
		date.setTime(date.getTime() + 50000);
		Assertions.assertEquals("2021-06-15 01:01:50", StringUtils.toString(date));
		date.setTime(date.getTime() + 12);
		Assertions.assertEquals("2021-06-15 01:01:50.012", StringUtils.toString(date));

		//case: Enum
		Assertions.assertEquals("TestEnum.AAA", StringUtils.toString(TestEnum.AAA));

		//case: Annotation
		TestAnnotation annotation = TestClass.class.getAnnotation(TestAnnotation.class);
		Assertions.assertEquals("@" + TestAnnotation.class.getSimpleName() + "(test=true)", StringUtils.toString(annotation));

		//case: Class
		Class<?> clazz = TestClass.class;
		Assertions.assertEquals("Class<" + clazz.getSimpleName() + ">", StringUtils.toString(clazz));

		//case: Method
		Method method = clazz.getMethod("setObj", TestClass.class);
		Assertions.assertEquals("Method<" + clazz.getSimpleName() + ".setObj(" + clazz.getSimpleName() + ")>", StringUtils.toString(method));

		//case: Field
		Field field = clazz.getDeclaredField("s");
		Assertions.assertEquals("Field<" + clazz.getSimpleName() + ".(String)s>", StringUtils.toString(field));

		//case: List, and cycle dependency
		List<Object> list = new ArrayList<>();
		list.add("xxx");
		list.add(111);
		list.add(list);
		Assertions.assertEquals("[xxx, 111, (this ArrayList)]", StringUtils.toString(list));

		//case: Map, and cycle dependency
		Map<Object, Object> map = new HashMap<>();
		map.put("aaa", 111);
		map.put("bbb", true);
		map.put("self", map);
		Assertions.assertEquals("{aaa -> 111, bbb -> true, self -> (this HashMap)}", StringUtils.toString(map));
		Assertions.assertFalse((boolean)ReflectionUtils.invokeStaticMethod(CycleDependencyHandler.class, "isStarting"));
		//case: Map, and cycle dependency（deep case）
		List<Object> list2 = new ArrayList<>();
		list2.add(map);
		list2.add('c');
		map.put("list", list2);
		Assertions.assertEquals("{aaa -> 111, bbb -> true, self -> (this HashMap), list -> [(ref HashMap), c]}", StringUtils.toString(map));
		Assertions.assertFalse((boolean)ReflectionUtils.invokeStaticMethod(CycleDependencyHandler.class, "isStarting"));

		//case: Array
		String[] strArr = new String[2];
		strArr[0] = "11";
		strArr[1] = "22";
		Assertions.assertEquals("[11, 22]", StringUtils.toString(strArr));
		//case: Array, and cycle dependency
		Object[] array = new Object[3];
		array[0] = 1;
		array[1] = "22";
		array[2] = array;
		Assertions.assertEquals("[1, 22, (this Object[])]", StringUtils.toString(array));


		//case: Object
		Assertions.assertEquals("CycleDependency(s=a, obj=null)", StringUtils.toString(new CycleDependency("a")));
		//case: Object, and cycle dependency
		CycleDependency obj = new CycleDependency("c");
		obj.setObj(obj);
		Assertions.assertEquals("CycleDependency(s=c, obj=(this CycleDependency))", StringUtils.toString(obj));
		//case: Object
		CycleDependency obj2 = new CycleDependency("d");
		obj.setObj(obj2);
		Assertions.assertEquals("CycleDependency(s=c, obj=CycleDependency(s=d, obj=null))", StringUtils.toString(obj));
		//case: Object, and cycle dependency
		TestClass a = new TestClass();
		a.setObj(a);
		Assertions.assertEquals("TestClass(obj=(this TestClass), s=null)", StringUtils.toString(a));
		//case: Object, and cycle dependency（deep case）
		TestClass b = new TestClass();
		TestClass c = new TestClass();
		b.setObj(c);
		c.setObj(a);
		a.setObj(b);
		Assertions.assertEquals("TestClass(obj=TestClass(obj=TestClass(obj=(ref TestClass), s=null), s=null), s=null)", StringUtils.toString(a));

		//过程中没有触发过对象的toString方法
		Assertions.assertFalse(TestClass.toStringTriggered);
		Assertions.assertFalse(CycleDependency.toStringTriggered);
	}


	//region 辅助测试的类、注解、枚举等

	enum TestEnum {
		AAA
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@interface TestAnnotation {
		boolean test() default false;
	}

	@TestAnnotation(test = true)
	static class TestClass {
		public static boolean toStringTriggered = false;

		private TestClass obj;
		private String s;


		public TestClass getObj() {
			return obj;
		}

		public void setObj(TestClass obj) {
			this.obj = obj;
		}

		@Override
		public String toString() {
			toStringTriggered = true;
			return StringUtils.toString(this);
		}
	}

	static class CycleDependency {
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
		public String toString() {
			toStringTriggered = true;
			return "(" +
					"s=" + s + "," +
					"obj=" + (obj != this ? String.valueOf(obj) : "(this CycleDependency)") +
					')';
		}
	}

	//endregion
}
