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

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cn.hutool.core.text.StrPool;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import static org.apache.commons.lang3.ArrayUtils.EMPTY_CLASS_ARRAY;
import static org.apache.commons.lang3.ArrayUtils.EMPTY_FIELD_ARRAY;
import static org.apache.commons.lang3.ArrayUtils.EMPTY_OBJECT_ARRAY;

/**
 * 反射工具类
 *
 * @author wangliang181230
 */
public abstract class ReflectionUtils {

	//region Constant

	/**
	 * The cache CLASS_FIELDS_CACHE: Class -> Field[]
	 * Warning: This cache has no static or synthetic fields.
	 */
	private static final Map<Class<?>, Field[]> CLASS_FIELDS_CACHE = new ConcurrentHashMap<>();

	/**
	 * The cache FIELD_CACHE: Class -> fieldName -> Field
	 */
	private static final Map<Class<?>, Map<String, Field>> FIELD_CACHE = new ConcurrentHashMap<>();

	/**
	 * The cache METHOD_CACHE: Class -> methodName|paramClassName1,paramClassName2,...,paramClassNameN -> Method
	 */
	private static final Map<Class<?>, Map<String, Method>> METHOD_CACHE = new ConcurrentHashMap<>();

	/**
	 * The cache SINGLETON_CACHE
	 */
	private static final Map<Class<?>, Object> SINGLETON_CACHE = new ConcurrentHashMap<>();


	//region NULL_XXXX

	/**
	 * The NULL_FIELD
	 */
	public static final Field NULL_FIELD;

	/**
	 * The NULL_METHOD
	 */
	public static final Method NULL_METHOD;

	private static class NullXxxxClass {
		private String nullField;

		public void nullMethod() {
		}
	}

	static {
		try {
			NULL_FIELD = NullXxxxClass.class.getDeclaredField("nullField");
			NULL_METHOD = NullXxxxClass.class.getDeclaredMethod("nullMethod");
		} catch (NoSuchFieldException | NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	//endregion

	//endregion


	//region Class

	/**
	 * Gets class by name.
	 *
	 * @param className the class name
	 * @param loader    the class loader
	 * @return the class by name
	 * @throws ClassNotFoundException the class not found exception
	 */
	@NonNull
	public static Class<?> getClassByName(String className, ClassLoader loader) throws ClassNotFoundException {
		return Class.forName(className, true, loader);
	}

	/**
	 * Gets class by name.
	 *
	 * @param className the class name
	 * @return the class by name
	 * @throws ClassNotFoundException the class not found exception
	 */
	@NonNull
	public static Class<?> getClassByName(String className) throws ClassNotFoundException {
		return getClassByName(className, Thread.currentThread().getContextClassLoader());
	}

	//endregion


	//region Interface

	/**
	 * get all interface of the clazz
	 *
	 * @param clazz the clazz
	 * @return set
	 */
	@NonNull
	public static Set<Class<?>> getInterfaces(Class<?> clazz) {
		if (clazz.isInterface()) {
			return Collections.singleton(clazz);
		}
		Set<Class<?>> interfaces = new LinkedHashSet<>();
		while (clazz != null) {
			Class<?>[] ifcs = clazz.getInterfaces();
			for (Class<?> ifc : ifcs) {
				interfaces.addAll(getInterfaces(ifc));
			}
			clazz = clazz.getSuperclass();
		}
		return interfaces;
	}

	//endregion


	//region Accessible

	/**
	 * set accessible to true if false
	 *
	 * @param accessible the accessible
	 * @param <T>        the type of the accessible
	 * @throws SecurityException if the request is denied.
	 */
	public static <T extends AccessibleObject> void setAccessible(T accessible) throws SecurityException {
		if (!accessible.isAccessible()) {
			accessible.setAccessible(true);
		}
	}

	//endregion


	//region Field

	/**
	 * Gets all fields, excluding static or synthetic fields
	 *
	 * @param targetClazz the target class
	 * @return allFields the all fields
	 */
	@NonNull
	public static Field[] getAllFields(final Class<?> targetClazz) {
		if (targetClazz == Object.class || targetClazz.isInterface()) {
			return EMPTY_FIELD_ARRAY;
		}

		// get from the cache
		Field[] fields = CLASS_FIELDS_CACHE.get(targetClazz);
		if (fields != null) {
			return fields;
		}

		// load current class declared fields
		fields = targetClazz.getDeclaredFields();
		final LinkedList<Field> fieldList = new LinkedList<>(Arrays.asList(fields));

		// remove the static or synthetic fields
		fieldList.removeIf(f -> Modifier.isStatic(f.getModifiers()) || f.isSynthetic());

		// load super class all fields, and add to the field list
		Field[] superFields = getAllFields(targetClazz.getSuperclass());
		if (ArrayUtils.isNotEmpty(superFields)) {
			fieldList.addAll(Arrays.asList(superFields));
		}

		// list to array
		Field[] resultFields;
		if (!fieldList.isEmpty()) {
			resultFields = fieldList.toArray(EMPTY_FIELD_ARRAY);
		} else {
			// reuse the EMPTY_FIELD_ARRAY
			resultFields = EMPTY_FIELD_ARRAY;
		}

		// set cache
		CLASS_FIELDS_CACHE.put(targetClazz, resultFields);

		return resultFields;
	}

	/**
	 * has Field
	 *
	 * @param clazz     the class
	 * @param fieldName the field name
	 * @return the boolean
	 */
	public static boolean hasField(Class<?> clazz, String fieldName) {
		try {
			getField(clazz, fieldName);
			return true;
		} catch (NoSuchFieldException e) {
			return false;
		}
	}

	/**
	 * get Field
	 *
	 * @param clazz     the class
	 * @param fieldName the field name
	 * @return the field
	 * @throws IllegalArgumentException if {@code clazz} or {@code fieldName} is null
	 * @throws NoSuchFieldException     if the field named {@code fieldName} does not exist
	 * @throws SecurityException        the security exception
	 */
	@NonNull
	public static Field getField(final Class<?> clazz, final String fieldName) throws NoSuchFieldException, SecurityException {
		Assert.notNull(clazz, "'clazz' must not be null");
		Assert.notNull(fieldName, "'fieldName' must not be null");

		Map<String, Field> fieldMap = MapUtils.computeIfAbsent(FIELD_CACHE, clazz, k -> new ConcurrentHashMap<>(4));

		Field field;
		if (fieldName.contains(StrPool.DOT)) {
			field = fieldMap.get(fieldName);
			if (field == null) {
				String[] fieldNameArr = fieldName.split("\\.");
				Class<?> currentClass = clazz;
				Field currentField = getField(clazz, fieldNameArr[0]);
				for (int i = 1; i < fieldNameArr.length; ++i) {
					currentField = getField(currentClass, fieldNameArr[i]);
					currentClass = currentField.getType();
				}
				field = currentField;

				// 设置缓存
				fieldMap.put(fieldName, field);
			}
		} else {
			field = MapUtils.computeIfAbsent(fieldMap, fieldName, k -> {
				Class<?> cl = clazz;
				while (cl != null && cl != Object.class && !cl.isInterface()) {
					try {
						return cl.getDeclaredField(fieldName);
					} catch (NoSuchFieldException e) {
						cl = cl.getSuperclass();
					}
				}

				// 未找到Field，返回NULL_FIELD常量，避免相同的fieldName重复执行当前函数
				return NULL_FIELD;
			});
		}

		if (field == NULL_FIELD) {
			throw new NoSuchFieldException("field not found: " + clazz.getName() + ", field: " + fieldName);
		}

		setAccessible(field);

		return field;
	}

	/**
	 * get field value
	 *
	 * @param target the target
	 * @param field  the field of the target
	 * @param <T>    the field type
	 * @return field value
	 * @throws IllegalArgumentException if {@code target} is {@code null}
	 * @throws SecurityException        the security exception
	 * @throws ClassCastException       if the type of the variable receiving the field value is not equals to the field type
	 */
	@Nullable
	public static <T> T getFieldValue(Object target, Field field)
			throws IllegalArgumentException, SecurityException {
		Assert.notNull(target, "'target' must not be null");

		while (true) {
			setAccessible(field);
			try {
				return (T)field.get(target);
			} catch (IllegalAccessException ignore) {
				// avoid other threads executing `field.setAccessible(false)`
			}
		}
	}

	/**
	 * get field value
	 *
	 * @param target    the target
	 * @param fieldName the field name
	 * @param <T>       the field type
	 * @return field value
	 * @throws IllegalArgumentException if {@code target} or {@code fieldName} is {@code null}
	 * @throws NoSuchFieldException     if the field named {@code fieldName} does not exist
	 * @throws SecurityException        the security exception
	 * @throws ClassCastException       if the type of the variable receiving the field value is not equals to the field type
	 */
	@Nullable
	public static <T> T getFieldValue(Object target, String fieldName)
			throws IllegalArgumentException, NoSuchFieldException, SecurityException {
		Assert.notNull(target, "'target' must not be null");
		Assert.notNull(fieldName, "'fieldName' must not be null");

		if (target instanceof Map) {
			return (T)((Map<?, ?>)target).get(fieldName);
		} else if (target instanceof Annotation) {
			return getAnnotationValue((Annotation)target, fieldName);
		} else {
			if (fieldName.contains(StrPool.DOT)) {
				String[] fieldNameArr = fieldName.split("\\.");
				Object currentFieldValue = target;
				for (String fn : fieldNameArr) {
					currentFieldValue = getFieldValue(currentFieldValue, fn);
					if (currentFieldValue == null) {
						return null;
					}
				}
				return (T)currentFieldValue;
			} else {
				// get field
				Field field = getField(target.getClass(), fieldName);
				// get field value
				return getFieldValue(target, field);
			}
		}
	}

	/**
	 * set field value
	 *
	 * @param target     the target
	 * @param field      the field of the target
	 * @param fieldValue the field value
	 * @throws IllegalArgumentException if {@code target} is {@code null}
	 * @throws SecurityException        the security exception
	 */
	public static void setFieldValue(Object target, Field field, Object fieldValue)
			throws IllegalArgumentException, SecurityException {
		Assert.notNull(target, "'target' must not be null");

		while (true) {
			setAccessible(field);
			try {
				field.set(target, fieldValue);
				return;
			} catch (IllegalAccessException ignore) {
				// avoid other threads executing `field.setAccessible(false)`
			}
		}
	}

	/**
	 * get field value
	 *
	 * @param target     the target
	 * @param fieldName  the field name
	 * @param fieldValue the field value
	 * @throws IllegalArgumentException if {@code target} or {@code fieldName} is {@code null}
	 * @throws NoSuchFieldException     if the field named {@code fieldName} does not exist
	 * @throws SecurityException        the security exception
	 */
	public static void setFieldValue(Object target, String fieldName, final Object fieldValue)
			throws IllegalArgumentException, NoSuchFieldException, SecurityException {
		Assert.notNull(target, "'target' must not be null");
		Assert.notNull(fieldName, "'fieldName' must not be null");

		if (fieldName.contains(StrPool.DOT)) {
			String[] fieldNameArr = fieldName.split("\\.");
			Object parentTarget = target;
			Field currentField;
			Object currentTarget = null;
			for (int i = 0; i < fieldNameArr.length - 1; ++i) {
				currentField = getField(parentTarget.getClass(), fieldNameArr[i]);
				currentTarget = getFieldValue(parentTarget, currentField);
				if (currentTarget == null) {
					try {
						currentTarget = currentField.getType().newInstance();
					} catch (InstantiationException | IllegalAccessException e) {
						throw new IllegalArgumentException("new instance failed, class: " + fieldToString(currentField));
					}
					setFieldValue(parentTarget, currentField, currentTarget);
				}
				parentTarget = currentTarget;
			}
			setFieldValue(currentTarget, fieldNameArr[fieldNameArr.length - 1], fieldValue);
		} else {
			if (target instanceof Map) {
				((Map<String, Object>)target).put(fieldName, fieldValue);
			} else {
				// get field
				Field field = getField(target.getClass(), fieldName);
				// set new value
				setFieldValue(target, field, fieldValue);
			}
		}
	}

	/**
	 * set `static` field value
	 *
	 * @param staticField the static field
	 * @param newValue    the new value
	 * @throws IllegalArgumentException if {@code staticField} is {@code null} or not a static field
	 * @throws NoSuchFieldException     if the class of the staticField has no `modifiers` field
	 * @throws IllegalAccessException   the illegal access exception
	 */
	public static void setStaticFieldValue(Field staticField, Object newValue)
			throws IllegalAccessException {
		Assert.notNull(staticField, "'staticField' must not be null");

		// check is static field
		if (!Modifier.isStatic(staticField.getModifiers())) {
			throw new IllegalArgumentException("the `" + fieldToString(staticField) + "` is not a static field, cannot modify value.");
		}

		// set new value
		setAccessible(staticField);
		staticField.set(staticField.getDeclaringClass(), newValue);
	}

	/**
	 * set `static` field value
	 *
	 * @param targetClass     the target class
	 * @param staticFieldName the static field name
	 * @param newValue        the new value
	 * @throws IllegalArgumentException if {@code targetClass} or {@code staticFieldName} is {@code null}
	 * @throws NullPointerException     if {@code staticFieldName} is {@code null}
	 * @throws NoSuchFieldException     if the field named {@code modifyFieldName} does not exist
	 * @throws IllegalAccessException   the illegal access exception
	 */
	public static void setStaticFieldValue(Class<?> targetClass, String staticFieldName, Object newValue)
			throws NoSuchFieldException, IllegalAccessException {
		Assert.notNull(targetClass, "'targetClass' must not be null");
		Assert.notNull(staticFieldName, "'staticFieldName' must not be null");

		// get field
		Field field = targetClass.getDeclaredField(staticFieldName);

		// modify static field value
		setStaticFieldValue(field, newValue);
	}

	//endregion


	//region Method

	/**
	 * get method
	 *
	 * @param clazz          the class
	 * @param methodName     the method name
	 * @param parameterTypes the parameter types
	 * @return the method
	 * @throws IllegalArgumentException if {@code clazz} is {@code null}
	 * @throws NullPointerException     if {@code methodName} is {@code null}
	 * @throws NoSuchMethodException    if the method named {@code methodName} does not exist
	 * @throws SecurityException        the security exception
	 */
	@NonNull
	public static Method getMethod(final Class<?> clazz, final String methodName, final Class<?>... parameterTypes)
			throws NoSuchMethodException, SecurityException {
		Assert.notNull(clazz, "'clazz' must not be null");

		Map<String, Method> methodMap = MapUtils.computeIfAbsent(METHOD_CACHE, clazz, k -> new ConcurrentHashMap<>(4));

		String cacheKey = generateMethodCacheKey(methodName, parameterTypes);
		Method method = MapUtils.computeIfAbsent(methodMap, cacheKey, k -> {
			Class<?> cl = clazz;
			while (cl != null) {
				try {
					return cl.getDeclaredMethod(methodName, parameterTypes);
				} catch (NoSuchMethodException e) {
					cl = cl.getSuperclass();
				}
			}
			// 未找到Method，返回NULL_METHOD常量，避免相同的参数重复执行当前函数
			return NULL_METHOD;
		});

		if (method == NULL_METHOD) {
			throw new NoSuchMethodException("method not found: " + methodToString(clazz, methodName, parameterTypes));
		}

		setAccessible(method);

		return method;
	}

	private static String generateMethodCacheKey(String methodName, Class<?>[] parameterTypes) {
		StringBuilder key = new StringBuilder(methodName);
		if (parameterTypes != null && parameterTypes.length > 0) {
			key.append("|");
			for (Class<?> parameterType : parameterTypes) {
				key.append(parameterType.getName()).append(",");
			}
		}
		return key.toString();
	}

	/**
	 * get method
	 *
	 * @param clazz      the class
	 * @param methodName the method name
	 * @return the method
	 * @throws IllegalArgumentException if {@code clazz} is {@code null}
	 * @throws NullPointerException     if {@code methodName} is {@code null}
	 * @throws NoSuchMethodException    if the method named {@code methodName} does not exist
	 * @throws SecurityException        the security exception
	 */
	@NonNull
	public static Method getMethod(final Class<?> clazz, final String methodName)
			throws NoSuchMethodException, SecurityException {
		return getMethod(clazz, methodName, EMPTY_CLASS_ARRAY);
	}

	/**
	 * invoke Method
	 *
	 * @param target the target
	 * @param method the method
	 * @param args   the args
	 * @return the result of the underlying method
	 * @throws InvocationTargetException if the underlying method throws an exception.
	 * @throws IllegalArgumentException  the illegal argument exception
	 * @throws SecurityException         the security exception
	 */
	public static Object invokeMethod(Object target, Method method, Object... args)
			throws InvocationTargetException, IllegalArgumentException, SecurityException {
		while (true) {
			setAccessible(method);
			try {
				return method.invoke(target, args);
			} catch (IllegalAccessException ignore) {
				// avoid other threads executing `method.setAccessible(false)`
			}
		}
	}

	/**
	 * invoke Method
	 *
	 * @param target the target
	 * @param method the method
	 * @return the result of the underlying method
	 * @throws InvocationTargetException if the underlying method throws an exception.
	 * @throws IllegalArgumentException  the illegal argument exception
	 * @throws SecurityException         the security exception
	 */
	public static Object invokeMethod(Object target, Method method)
			throws InvocationTargetException, IllegalArgumentException, SecurityException {
		return invokeMethod(target, method, EMPTY_OBJECT_ARRAY);
	}

	/**
	 * invoke Method
	 *
	 * @param target         the target
	 * @param methodName     the method name
	 * @param parameterTypes the parameter types
	 * @param args           the args
	 * @return the result of the underlying method
	 * @throws IllegalArgumentException  if {@code target} is {@code null}
	 * @throws NullPointerException      if {@code methodName} is {@code null}
	 * @throws NoSuchMethodException     if the method named {@code methodName} does not exist
	 * @throws InvocationTargetException if the underlying method throws an exception.
	 * @throws SecurityException         the security exception
	 */
	public static Object invokeMethod(Object target, String methodName, Class<?>[] parameterTypes, Object... args)
			throws NoSuchMethodException, InvocationTargetException, IllegalArgumentException, SecurityException {
		Assert.notNull(target, "'target' must not be null");

		// get method
		Method method = getMethod(target.getClass(), methodName, parameterTypes);

		// invoke method
		return invokeMethod(target, method, args);
	}

	/**
	 * invoke Method
	 *
	 * @param target     the target
	 * @param methodName the method name
	 * @return the result of the underlying method
	 * @throws NoSuchMethodException     the no such method exception
	 * @throws InvocationTargetException if the underlying method throws an exception.
	 * @throws IllegalArgumentException  the illegal argument exception
	 * @throws SecurityException         the security exception
	 */
	public static Object invokeMethod(Object target, String methodName)
			throws NoSuchMethodException, InvocationTargetException, IllegalArgumentException, SecurityException {
		return invokeMethod(target, methodName, EMPTY_CLASS_ARRAY, EMPTY_OBJECT_ARRAY);
	}

	/**
	 * invoke static Method
	 *
	 * @param staticMethod the static method
	 * @param args         the args
	 * @return the result of the static method
	 * @throws IllegalArgumentException  if {@code staticMethod} is {@code null} or not a static method
	 * @throws InvocationTargetException if the underlying method throws an exception.
	 * @throws SecurityException         the security exception
	 */
	public static Object invokeStaticMethod(Method staticMethod, Object... args)
			throws IllegalArgumentException, InvocationTargetException, SecurityException {
		Assert.notNull(staticMethod, "'staticMethod' must not be null");

		if (!Modifier.isStatic(staticMethod.getModifiers())) {
			throw new IllegalArgumentException("`" + methodToString(staticMethod) + "` is not a static method");
		}

		return invokeMethod(null, staticMethod, args);
	}

	/**
	 * invoke static Method
	 *
	 * @param staticMethod the static method
	 * @return the result of the static method
	 * @throws InvocationTargetException if the underlying method throws an exception.
	 * @throws IllegalArgumentException  the illegal argument exception
	 * @throws SecurityException         the security exception
	 */
	public static Object invokeStaticMethod(Method staticMethod)
			throws InvocationTargetException, IllegalArgumentException, SecurityException {
		return invokeStaticMethod(staticMethod, EMPTY_OBJECT_ARRAY);
	}

	/**
	 * invoke static Method
	 *
	 * @param targetClass      the target class
	 * @param staticMethodName the static method name
	 * @param parameterTypes   the parameter types
	 * @param args             the args
	 * @return the result of the static method
	 * @throws IllegalArgumentException  if {@code targetClass} is {@code null}
	 * @throws NullPointerException      if {@code methodName} is {@code null}
	 * @throws NoSuchMethodException     the no such method exception
	 * @throws InvocationTargetException if the underlying method throws an exception.
	 * @throws SecurityException         the security exception
	 */
	public static Object invokeStaticMethod(Class<?> targetClass, String staticMethodName,
											Class<?>[] parameterTypes, Object... args)
			throws IllegalArgumentException, NoSuchMethodException, InvocationTargetException, SecurityException {
		Assert.notNull(targetClass, "'targetClass' must not be null");

		// get method
		Method staticMethod = getMethod(targetClass, staticMethodName, parameterTypes);
		if (!Modifier.isStatic(staticMethod.getModifiers())) {
			throw new NoSuchMethodException("static method not found: "
					+ methodToString(targetClass, staticMethodName, parameterTypes));
		}

		return invokeStaticMethod(staticMethod, args);
	}

	/**
	 * invoke static Method
	 *
	 * @param targetClass      the target class
	 * @param staticMethodName the static method name
	 * @return the result of the static method
	 * @throws IllegalArgumentException  if {@code targetClass} is {@code null}
	 * @throws NullPointerException      if {@code methodName} is {@code null}
	 * @throws NoSuchMethodException     the no such method exception
	 * @throws InvocationTargetException if the underlying method throws an exception.
	 * @throws SecurityException         the security exception
	 */
	public static Object invokeStaticMethod(Class<?> targetClass, String staticMethodName)
			throws IllegalArgumentException, NoSuchMethodException, SecurityException, InvocationTargetException {
		return invokeStaticMethod(targetClass, staticMethodName, EMPTY_CLASS_ARRAY, EMPTY_OBJECT_ARRAY);
	}

	/**
	 * Equals method
	 *
	 * @param m1 the method 1
	 * @param m2 the method 2
	 * @return the boolean
	 */
	public static boolean equalsMethod(Method m1, Method m2) {
		if (m1 == null) {
			return m2 == null;
		} else if (m2 == null) {
			return false;
		}

		if (m1.equals(m2)) {
			return true;
		}

		if (!m1.getName().equals(m2.getName()) || m1.getParameterCount() != m2.getParameterCount()) {
			return false;
		}

		for (int i = 0; i < m1.getParameterCount(); ++i) {
			if (!m1.getParameterTypes()[i].equals(m2.getParameterTypes()[i])) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Contains method
	 *
	 * @param methods the methods
	 * @param method  the method
	 * @return the boolean
	 */
	public static boolean containsMethod(Collection<Method> methods, Method method) {
		if (methods.contains(method)) {
			return true;
		}

		for (Method m : methods) {
			if (equalsMethod(m, method)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Contains method
	 *
	 * @param methodMap the method map
	 * @param method    the method
	 * @return the boolean
	 */
	public static boolean containsMethod(Map<Method, ?> methodMap, Method method) {
		if (methodMap.isEmpty()) {
			return false;
		}

		return containsMethod(methodMap.keySet(), method);
	}

	/**
	 * Has method
	 *
	 * @param clazz  the class
	 * @param method the method
	 * @return the boolean
	 */
	public static boolean hasMethod(Class<?> clazz, Method method) {
		if (method.getDeclaringClass() == clazz) {
			return true;
		}

		try {
			clazz.getMethod(method.getName(), method.getParameterTypes());
			return true;
		} catch (NoSuchMethodException e) {
			return false;
		}
	}

	//endregion


	//region Annotation

	/**
	 * get annotation from the method or super class method
	 *
	 * @param method          the method
	 * @param annotationClass the annotation class
	 * @param <T>             the annotation type
	 * @return the annotation
	 */
	@Nullable
	public static <T extends Annotation> T getAnnotation(final Method method, final Class<T> annotationClass) {
		T annotation = method.getAnnotation(annotationClass);
		if (annotation == null) {
			Class<?> superClass = method.getDeclaringClass().getSuperclass();
			Method m;
			while (superClass != null && superClass != Object.class) {
				try {
					m = superClass.getMethod(method.getName(), method.getParameterTypes());
					annotation = m.getAnnotation(annotationClass);
					if (annotation != null) {
						return annotation;
					}
				} catch (NoSuchMethodException ignore) {
					// do nothing
				}
				superClass = superClass.getSuperclass();
			}
		}
		return annotation;
	}

	/**
	 * get annotation values
	 *
	 * @param annotation the annotation
	 * @return annotationValues the annotation values
	 * @throws IllegalArgumentException if {@code annotation} is {@code null}
	 * @throws NoSuchFieldException     the no such field exception
	 */
	@NonNull
	@SuppressWarnings("all")
	public static Map<String, Object> getAnnotationValues(Annotation annotation) throws NoSuchFieldException {
		Assert.notNull(annotation, "'annotation' must not be null");

		InvocationHandler h = Proxy.getInvocationHandler(annotation);
		return getFieldValue(h, "memberValues");
	}

	/**
	 * get annotation value
	 *
	 * @param annotation the annotation
	 * @param fieldName  the field name
	 * @param <T>        the type of the field
	 * @return annotationValue the field value
	 * @throws IllegalArgumentException if {@code annotation} or {@code fieldName} is {@code null}
	 * @throws NoSuchFieldException     the no such field exception
	 */
	@Nullable
	public static <T> T getAnnotationValue(Annotation annotation, String fieldName) throws NoSuchFieldException {
		Assert.notNull(fieldName, "'fieldName' must not be null");
		Map<String, Object> annotationValues = getAnnotationValues(annotation);
		return (T)annotationValues.get(fieldName);
	}

	//endregion


	//region Instance

	/**
	 * get singleton for the class
	 *
	 * @param clazz the clazz
	 * @param <T>   the type
	 * @return the singleton
	 * @throws IllegalArgumentException if {@code clazz} is null
	 */
	@NonNull
	public static <T> T getSingleton(Class<T> clazz) {
		Assert.notNull(clazz, "'clazz' must not be null");

		if (clazz.isInterface()) {
			throw new IllegalArgumentException("'clazz' must be not an interface: " + clazz);
		}

		return (T)MapUtils.computeIfAbsent(SINGLETON_CACHE, clazz, key -> {
			try {
				return clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new IllegalArgumentException("new instance failed, class: " + clazz, e);
			}
		});
	}

	//endregion


	//region toString

	/**
	 * class to string
	 *
	 * @param clazz the class
	 * @return the string
	 */
	public static String classToString(Class<?> clazz) {
		return "Class<" + clazz.getSimpleName() + ">";
	}

	/**
	 * field to string
	 *
	 * @param clazz     the clazz
	 * @param fieldName the field name
	 * @param fieldType the field type
	 * @return the string
	 */
	public static String fieldToString(Class<?> clazz, String fieldName, Class<?> fieldType) {
		return "Field<" + clazz.getSimpleName() + ".(" + fieldType.getSimpleName() + " " + fieldName + ")>";
	}

	/**
	 * field to string
	 *
	 * @param field the field
	 * @return the string
	 */
	public static String fieldToString(Field field) {
		return fieldToString(field.getDeclaringClass(), field.getName(), field.getType());
	}

	/**
	 * method to string
	 *
	 * @param clazz          the clazz
	 * @param methodName     the method name
	 * @param parameterTypes the parameter types
	 * @return the string
	 */
	public static String methodToString(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
		return "Method<" + clazz.getSimpleName() + "." + methodName + parameterTypesToString(parameterTypes) + ">";
	}

	/**
	 * method to string
	 *
	 * @param method the method
	 * @return the string
	 */
	public static String methodToString(Method method) {
		String methodStr = method.getDeclaringClass().getSimpleName() + "." + method.getName()
				+ parameterTypesToString(method.getParameterTypes());
		if (Modifier.isStatic(method.getModifiers())) {
			methodStr = "static " + methodStr;
		}
		return "Method<" + methodStr + ">";
	}

	/**
	 * annotatio to string
	 *
	 * @param annotation the annotation
	 * @return the string
	 */
	public static String annotationToString(Annotation annotation) {
		if (annotation == null) {
			return "null";
		}

		String annoStr = annotation.toString();
		String annoValueStr = annoStr.substring(annoStr.indexOf('('));
		return "@" + annotation.annotationType().getSimpleName() + annoValueStr;
	}

	/**
	 * parameter types to string
	 *
	 * @param parameterTypes the parameter types
	 * @return the string
	 */
	public static String parameterTypesToString(Class<?>[] parameterTypes) {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		if (parameterTypes != null) {
			for (int i = 0; i < parameterTypes.length; i++) {
				if (i > 0) {
					sb.append(", ");
				}
				Class<?> c = parameterTypes[i];
				sb.append((c == null) ? "null" : c.getSimpleName());
			}
		}
		sb.append(")");
		return sb.toString();
	}

	//endregion
}
