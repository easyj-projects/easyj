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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import icu.easyj.core.modelfortest.EmptyTestClass;
import icu.easyj.core.modelfortest.EmptyTestInterface;
import icu.easyj.core.modelfortest.NotExistsNoArgsConstructorTestClass;
import icu.easyj.core.modelfortest.TestClass;
import icu.easyj.core.modelfortest.TestSuperClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.apache.commons.lang3.ArrayUtils.EMPTY_FIELD_ARRAY;
import static org.apache.commons.lang3.ArrayUtils.EMPTY_OBJECT_ARRAY;

/**
 * {@link ReflectionUtils} 测试类
 *
 * @author wangliang181230
 */
class ReflectionUtilsTest {

	@SuppressWarnings("all")
	private static final String STRING = "d";

	//Prevent jvm from optimizing final
	@SuppressWarnings("all")
	public static final String testValue = (null != null ? "hello" : "hello");
	@SuppressWarnings("all")
	public final String testValue2 = (null != null ? "hello world" : "hello world");

	@Test
	void testGetClassByName() throws ClassNotFoundException {
		Assertions.assertEquals(String.class,
				ReflectionUtils.getClassByName("java.lang.String"));
	}

	@Test
	void testGetFieldValue() throws NoSuchFieldException {
		Assertions.assertEquals("d", ReflectionUtils.getFieldValue(new ReflectionUtilsTest(), "STRING"));
		Assertions.assertThrows(ClassCastException.class, () -> {
			Integer var = ReflectionUtils.getFieldValue(new ReflectionUtilsTest(), "STRING");
		});

		Assertions.assertThrows(IllegalArgumentException.class,
				() -> ReflectionUtils.getFieldValue(null, "a1b2c3"));
		Assertions.assertThrows(NoSuchFieldException.class,
				() -> ReflectionUtils.getFieldValue(new Object(), "A1B2C3"));
	}

	@Test
	void testInvokeMethod() throws NoSuchMethodException, InvocationTargetException {
		Assertions.assertEquals(0, ReflectionUtils.invokeMethod("", "length"));
		Assertions.assertEquals(3,
				ReflectionUtils.invokeMethod("foo", "length"));

		Assertions.assertThrows(NoSuchMethodException.class,
				() -> ReflectionUtils.invokeMethod("", "size"));
	}

	@Test
	void testInvokeMethod2() throws NoSuchMethodException, InvocationTargetException {
		Assertions.assertEquals(0, ReflectionUtils
				.invokeMethod("", "length", null, EMPTY_OBJECT_ARRAY));
		Assertions.assertEquals(3, ReflectionUtils
				.invokeMethod("foo", "length", null, EMPTY_OBJECT_ARRAY));

		Assertions.assertThrows(NoSuchMethodException.class, () -> ReflectionUtils
				.invokeMethod("", "size", null, EMPTY_OBJECT_ARRAY));
	}

	@Test
	void testInvokeMethod3() throws NoSuchMethodException, InvocationTargetException {
		Assertions.assertEquals("0", ReflectionUtils.invokeStaticMethod(
				String.class, "valueOf",
				new Class<?>[]{int.class}, 0));
		Assertions.assertEquals("123", ReflectionUtils.invokeStaticMethod(
				String.class, "valueOf",
				new Class<?>[]{int.class}, 123));

		Assertions.assertThrows(NoSuchMethodException.class, () -> ReflectionUtils
				.invokeStaticMethod(String.class, "size", null, EMPTY_OBJECT_ARRAY));
	}

	@Test
	void testGetInterfaces() {
		Assertions.assertArrayEquals(new Object[]{Serializable.class},
				ReflectionUtils.getInterfaces(Serializable.class).toArray());

		Assertions.assertArrayEquals(new Object[]{
						Serializable.class, Comparable.class, CharSequence.class},
				ReflectionUtils.getInterfaces(String.class).toArray());
	}

	@Test
	@SuppressWarnings("all")
	void testSetStaticFinalFieldValue() throws NoSuchFieldException, IllegalAccessException {
		Assertions.assertEquals("hello", testValue);
		ReflectionUtils.setStaticFinalFieldValue(ReflectionUtilsTest.class, "testValue", "hello world");
		Assertions.assertEquals("hello world", testValue);

		// case: not a static field
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ReflectionUtils.setStaticFinalFieldValue(ReflectionUtilsTest.class, "testValue2", "hello");
		});
	}


	//region test the method 'getAllFields'

	@Test
	void testGetAllFields() {
		// TestClass
		this.testGetAllFieldsInternal(TestClass.class, "f1", "f2");
		// TestSuperClass
		this.testGetAllFieldsInternal(TestSuperClass.class, "f2");
		// TestEmptyClass
		this.testGetAllFieldsInternal(EmptyTestClass.class);
		// TestInterface
		this.testGetAllFieldsInternal(EmptyTestInterface.class);
		// Object
		this.testGetAllFieldsInternal(Object.class);

		// case: The fields of TestEmptyClass is `EMPTY_FIELD_ARRAY`
		Assertions.assertSame(EMPTY_FIELD_ARRAY, ReflectionUtils.getAllFields(EmptyTestClass.class));
		// case: The fields of TestInterface is `EMPTY_FIELD_ARRAY`
		Assertions.assertSame(EMPTY_FIELD_ARRAY, ReflectionUtils.getAllFields(EmptyTestInterface.class));
		// case: The fields of Object is `EMPTY_FIELD_ARRAY`
		Assertions.assertSame(EMPTY_FIELD_ARRAY, ReflectionUtils.getAllFields(Object.class));
	}

	private void testGetAllFieldsInternal(Class<?> clazz, String... fieldNames) {
		Field[] fields = ReflectionUtils.getAllFields(clazz);
		Assertions.assertEquals(fieldNames.length, fields.length);
		Field[] fields2 = ReflectionUtils.getAllFields(clazz);
		Assertions.assertSame(fields, fields2);

		if (fieldNames.length == 0) {
			return;
		}

		List<String> fieldNameList = Arrays.asList(fieldNames);
		for (Field field : fields) {
			Assertions.assertTrue(fieldNameList.contains(field.getName()));
		}
	}

	//endregion


	@Test
	@SuppressWarnings("all")
	void testGetSingleton() {
		// case: normal Class
		TestClass testClass = ReflectionUtils.getSingleton(TestClass.class);
		Assertions.assertNotNull(testClass);

		// case: null
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ReflectionUtils.getSingleton(null);
		});

		// case: Interface
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			ReflectionUtils.getSingleton(EmptyTestInterface.class);
		});

		// case: not Exists No-Args Constructor Class
		try {
			ReflectionUtils.getSingleton(NotExistsNoArgsConstructorTestClass.class);
		} catch (RuntimeException e) {
			Assertions.assertEquals(IllegalArgumentException.class, e.getClass());
			Assertions.assertEquals(InstantiationException.class, e.getCause().getClass());
			Assertions.assertEquals(NoSuchMethodException.class, e.getCause().getCause().getClass());
		}

		// case: Inner class
		try {
			ReflectionUtils.getSingleton(TestClass.TestInnerClass.class);
		} catch (RuntimeException e) {
			Assertions.assertEquals(IllegalArgumentException.class, e.getClass());
			Assertions.assertEquals(InstantiationException.class, e.getCause().getClass());
			Assertions.assertEquals(NoSuchMethodException.class, e.getCause().getCause().getClass());
		}
	}
}
