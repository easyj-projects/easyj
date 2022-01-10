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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link AppConfigs} 的测试类
 *
 * @author wangliang181230
 */
public class AppConfigsTest {

	@Test
	public void testSetAndGetProjectConfigs() {
		// case: Area
		AppConfigs.setArea("area1");
		Assertions.assertEquals("area1", AppConfigs.getArea());
		AppConfigs.setAreaName("areaName1");
		Assertions.assertEquals("areaName1", AppConfigs.getAreaName());

		// case: Project
		AppConfigs.setProject("project1");
		Assertions.assertEquals("project1", AppConfigs.getProject());
		AppConfigs.setProjectName("projectName1");
		Assertions.assertEquals("projectName1", AppConfigs.getProjectName());

		// case: Application
		AppConfigs.setApplication("application1");
		Assertions.assertEquals("application1", AppConfigs.getApplication());
		AppConfigs.setApplicationName("applicationName1");
		Assertions.assertEquals("applicationName1", AppConfigs.getApplicationName());
	}
}
