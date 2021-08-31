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
package icu.easyj.config;

import java.util.HashMap;
import java.util.Map;

import icu.easyj.core.env.EnvironmentType;
import icu.easyj.core.env.RunMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link GlobalConfigs} 和 {@link GlobalConfigSetter} 的测试类
 *
 * @author wangliang181230
 */
class GlobalConfigsTest {

	@Test
	void testSetAndGetGlobalConfigs() {
		// case: Area
		GlobalConfigSetter.setArea("area1");
		Assertions.assertEquals("area1", GlobalConfigs.getArea());
		GlobalConfigSetter.setAreaName("areaName1");
		Assertions.assertEquals("areaName1", GlobalConfigs.getAreaName());

		// case: Project
		GlobalConfigSetter.setProject("project1");
		Assertions.assertEquals("project1", GlobalConfigs.getProject());
		GlobalConfigSetter.setProjectName("projectName1");
		Assertions.assertEquals("projectName1", GlobalConfigs.getProjectName());

		// case: Application
		GlobalConfigSetter.setApplication("application1");
		Assertions.assertEquals("application1", GlobalConfigs.getApplication());
		GlobalConfigSetter.setApplicationName("applicationName1");
		Assertions.assertEquals("applicationName1", GlobalConfigs.getApplicationName());

		// case: Env
		GlobalConfigSetter.setEnv("env1");
		Assertions.assertEquals("env1", GlobalConfigs.getEnv());
		GlobalConfigSetter.setEnvName("envName1");
		Assertions.assertEquals("envName1", GlobalConfigs.getEnvName());
		this.testSetEnvType(EnvironmentType.DEV);
		this.testSetEnvType(EnvironmentType.TEST);
		this.testSetEnvType(EnvironmentType.SANDBOX);
		this.testSetEnvType(EnvironmentType.PROD);

		// case: Run Mode
		// RELEASE Mode
		GlobalConfigSetter.setRunMode(RunMode.RELEASE);
		Assertions.assertEquals(RunMode.RELEASE, GlobalConfigs.getRunMode());
		Assertions.assertTrue(GlobalConfigs.isReleaseMode());
		Assertions.assertFalse(GlobalConfigs.isDebugMode());
		// DEBUG Mode
		GlobalConfigSetter.setRunMode(RunMode.DEBUG);
		Assertions.assertEquals(RunMode.DEBUG, GlobalConfigs.getRunMode());
		Assertions.assertFalse(GlobalConfigs.isReleaseMode());
		Assertions.assertTrue(GlobalConfigs.isDebugMode());

		// case: In Unit Test
		Assertions.assertFalse(GlobalConfigs.isInUnitTest());
		GlobalConfigSetter.setInUnitTest(true);
		Assertions.assertTrue(GlobalConfigs.isInUnitTest());
		GlobalConfigSetter.setInUnitTest(false);
		Assertions.assertFalse(GlobalConfigs.isInUnitTest());

		// case: Config Map
		// 默认为空Map，不为null
		Assertions.assertNotNull(GlobalConfigs.getConfigs());
		Assertions.assertEquals(0, GlobalConfigs.getConfigs().size());
		// 测试`.addConfig`
		GlobalConfigSetter.addConfig("key", "value");
		Assertions.assertEquals(1, GlobalConfigs.getConfigs().size());
		Assertions.assertEquals("value", GlobalConfigs.getConfig("key"));
		// 测试`.addConfigs`
		Map<String, Object> configMap = new HashMap<>();
		configMap.put("key", "value+");
		configMap.put("key1", "value1");
		configMap.put("key2", "value2");
		GlobalConfigSetter.addConfigs(configMap);
		Assertions.assertEquals(3, GlobalConfigs.getConfigs().size());
		Assertions.assertEquals("value+", GlobalConfigs.getConfig("key"));
		Assertions.assertEquals("value1", GlobalConfigs.getConfig("key1"));
		Assertions.assertEquals("value2", GlobalConfigs.getConfig("key2"));
		// GlobalConfigs.getConfigs()获取到的配置，无法修改
		Assertions.assertThrows(UnsupportedOperationException.class, () -> {
			GlobalConfigs.getConfigs().put("aaa", "bbb");
		});
		Assertions.assertThrows(UnsupportedOperationException.class, () -> {
			GlobalConfigs.getConfigs().remove("aaa");
		});
		Assertions.assertThrows(UnsupportedOperationException.class, () -> {
			GlobalConfigs.getConfigs().putAll(new HashMap<>());
		});
		Assertions.assertThrows(UnsupportedOperationException.class, () -> {
			GlobalConfigs.getConfigs().clear();
		});
	}

	private void testSetEnvType(EnvironmentType envType) {
		GlobalConfigSetter.setEnvType(envType);
		Assertions.assertEquals(envType, GlobalConfigs.getEnvType());

		// DEV
		if (EnvironmentType.DEV == envType) {
			Assertions.assertTrue(GlobalConfigs.isDevEnv());
			Assertions.assertFalse(GlobalConfigs.isNotDevEnv());
		} else {
			Assertions.assertFalse(GlobalConfigs.isDevEnv());
			Assertions.assertTrue(GlobalConfigs.isNotDevEnv());
		}
		// TEST
		if (EnvironmentType.TEST == envType) {
			Assertions.assertTrue(GlobalConfigs.isTestEnv());
			Assertions.assertFalse(GlobalConfigs.isNotTestEnv());
		} else {
			Assertions.assertFalse(GlobalConfigs.isTestEnv());
			Assertions.assertTrue(GlobalConfigs.isNotTestEnv());
		}
		// SANDBOX
		if (EnvironmentType.SANDBOX == envType) {
			Assertions.assertTrue(GlobalConfigs.isSandboxEnv());
			Assertions.assertFalse(GlobalConfigs.isNotSandboxEnv());
		} else {
			Assertions.assertFalse(GlobalConfigs.isSandboxEnv());
			Assertions.assertTrue(GlobalConfigs.isNotSandboxEnv());
		}
		// PROD
		if (EnvironmentType.PROD == envType) {
			Assertions.assertTrue(GlobalConfigs.isProdEnv());
			Assertions.assertFalse(GlobalConfigs.isNotProdEnv());
		} else {
			Assertions.assertFalse(GlobalConfigs.isProdEnv());
			Assertions.assertTrue(GlobalConfigs.isNotProdEnv());
		}
	}
}
