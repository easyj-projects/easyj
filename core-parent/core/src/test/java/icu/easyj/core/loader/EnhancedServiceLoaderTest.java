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
package icu.easyj.core.loader;

import java.util.List;

import icu.easyj.core.loader.model.ChineseHello;
import icu.easyj.core.loader.model.EnglishHello;
import icu.easyj.core.loader.model.FrenchHello;
import icu.easyj.core.loader.model.Hello;
import icu.easyj.core.loader.model.LatinHello;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The type Enhanced service loader test.
 * 注：从阿里的seata项目中复制过来的。
 *
 * @author Otis.z
 */
public class EnhancedServiceLoaderTest {

	/**
	 * Test load by class and class loader.
	 */
	@Test
	public void testLoadByClassAndClassLoader() {
		Hello load = EnhancedServiceLoader.load(Hello.class, Hello.class.getClassLoader());
		assertEquals(load.say(), "Olá.");
	}

	/**
	 * Test load exception.
	 */
	@Test
	public void testLoadException() {
		assertThrows(EnhancedServiceNotFoundException.class, () -> {
			EnhancedServiceLoader.load(EnhancedServiceLoaderTest.class);
		});
	}

	/**
	 * Test load by class.
	 */
	@Test
	public void testLoadByClass() {
		Hello load = EnhancedServiceLoader.load(Hello.class);
		assertEquals("Olá.", load.say());
	}

	/**
	 * Test load by class and activate name.
	 */
	@Test
	public void testLoadByClassAndActivateName() {
		Hello englishHello = EnhancedServiceLoader.load(Hello.class, "EnglishHello");
		assertEquals("hello!", englishHello.say());
	}

	/**
	 * Test load by class and class loader and activate name.
	 */
	@Test
	public void testLoadByClassAndClassLoaderAndActivateName() {
		Hello englishHello = EnhancedServiceLoader
				.load(Hello.class, "EnglishHello", EnhancedServiceLoaderTest.class.getClassLoader());
		assertEquals("hello!", englishHello.say());
	}

	/**
	 * Gets all extension class.
	 */
	@Test
	public void getAllExtensionClass() {
		List<Class<?>> allExtensionClass = EnhancedServiceLoader.getAllExtensionClass(Hello.class);
		Assertions.assertEquals(4, allExtensionClass.size());
		assertEquals(LatinHello.class, allExtensionClass.get(0));
		assertEquals(FrenchHello.class, allExtensionClass.get(1));
		assertEquals(EnglishHello.class, allExtensionClass.get(2));
		assertEquals(ChineseHello.class, allExtensionClass.get(3));
	}

	/**
	 * Gets all extension class 1.
	 */
	@Test
	public void getAllExtensionClass1() {
		List<Class<?>> allExtensionClass = EnhancedServiceLoader
				.getAllExtensionClass(Hello.class, ClassLoader.getSystemClassLoader());
		assertNotEquals(0, allExtensionClass.size());
	}

	@Test
	public void getSingletonExtensionInstance() {
		Hello hello1 = EnhancedServiceLoader.load(Hello.class, "ChineseHello");
		Hello hello2 = EnhancedServiceLoader.load(Hello.class, "ChineseHello");
		assertEquals(hello1, hello2);
	}

	@Test
	public void getMultipleExtensionInstance() {
		Hello hello1 = EnhancedServiceLoader.load(Hello.class, "LatinHello");
		Hello hello2 = EnhancedServiceLoader.load(Hello.class, "LatinHello");
		assertNotEquals(hello1, hello2);
	}

	@Test
	public void getAllInstances() {
		List<Hello> hellows1 = EnhancedServiceLoader.loadAll(Hello.class);
		List<Hello> hellows2 = EnhancedServiceLoader.loadAll(Hello.class);
		for (Hello hello : hellows1) {
			if (!hello.say().equals("Olá.")) {
				assertTrue(hellows2.contains(hello));
			} else {
				assertFalse(hellows2.contains(hello));
			}
		}
	}
}
