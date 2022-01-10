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
package icu.easyj.config;

import icu.easyj.core.env.EnvironmentType;
import icu.easyj.core.env.RunMode;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 * {@link EnvironmentConfigs} 的测试类
 *
 * @author wangliang181230
 */
public class EnvironmentConfigsTest {

	@Test
	public void testSetAndGetEnvironmentConfigs() {
		// case: Env
		EnvironmentConfigs.setEnv("env1");
		Assertions.assertEquals("env1", EnvironmentConfigs.getEnv());
		EnvironmentConfigs.setEnvName("envName1");
		Assertions.assertEquals("envName1", EnvironmentConfigs.getEnvName());
		this.testSetEnvType(EnvironmentType.DEV);
		this.testSetEnvType(EnvironmentType.TEST);
		this.testSetEnvType(EnvironmentType.SANDBOX);
		this.testSetEnvType(EnvironmentType.PROD);

		// case: Run Mode
		// RELEASE Mode
		EnvironmentConfigs.setRunMode(RunMode.RELEASE);
		Assertions.assertEquals(RunMode.RELEASE, EnvironmentConfigs.getRunMode());
		Assertions.assertTrue(EnvironmentConfigs.isReleaseMode());
		Assertions.assertFalse(EnvironmentConfigs.isDebugMode());
		// DEBUG Mode
		EnvironmentConfigs.setRunMode(RunMode.DEBUG);
		Assertions.assertEquals(RunMode.DEBUG, EnvironmentConfigs.getRunMode());
		Assertions.assertFalse(EnvironmentConfigs.isReleaseMode());
		Assertions.assertTrue(EnvironmentConfigs.isDebugMode());

		// case: In Unit Test
		Assertions.assertFalse(EnvironmentConfigs.isInUnitTest());
		EnvironmentConfigs.setInUnitTest(true);
		Assertions.assertTrue(EnvironmentConfigs.isInUnitTest());
		EnvironmentConfigs.setInUnitTest(false);
		Assertions.assertFalse(EnvironmentConfigs.isInUnitTest());
	}

	private void testSetEnvType(EnvironmentType envType) {
		EnvironmentConfigs.setEnvType(envType);
		Assertions.assertEquals(envType, EnvironmentConfigs.getEnvType());

		// DEV
		if (EnvironmentType.DEV == envType) {
			Assertions.assertTrue(EnvironmentConfigs.isDevEnv());
			Assertions.assertFalse(EnvironmentConfigs.isNotDevEnv());
		} else {
			Assertions.assertFalse(EnvironmentConfigs.isDevEnv());
			Assertions.assertTrue(EnvironmentConfigs.isNotDevEnv());
		}
		// TEST
		if (EnvironmentType.TEST == envType) {
			Assertions.assertTrue(EnvironmentConfigs.isTestEnv());
			Assertions.assertFalse(EnvironmentConfigs.isNotTestEnv());
		} else {
			Assertions.assertFalse(EnvironmentConfigs.isTestEnv());
			Assertions.assertTrue(EnvironmentConfigs.isNotTestEnv());
		}
		// SANDBOX
		if (EnvironmentType.SANDBOX == envType) {
			Assertions.assertTrue(EnvironmentConfigs.isSandboxEnv());
			Assertions.assertFalse(EnvironmentConfigs.isNotSandboxEnv());
		} else {
			Assertions.assertFalse(EnvironmentConfigs.isSandboxEnv());
			Assertions.assertTrue(EnvironmentConfigs.isNotSandboxEnv());
		}
		// PROD
		if (EnvironmentType.PROD == envType) {
			Assertions.assertTrue(EnvironmentConfigs.isProdEnv());
			Assertions.assertFalse(EnvironmentConfigs.isNotProdEnv());
		} else {
			Assertions.assertFalse(EnvironmentConfigs.isProdEnv());
			Assertions.assertTrue(EnvironmentConfigs.isNotProdEnv());
		}
	}
}
