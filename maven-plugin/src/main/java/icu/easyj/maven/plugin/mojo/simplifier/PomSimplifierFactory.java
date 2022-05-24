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
package icu.easyj.maven.plugin.mojo.simplifier;

import icu.easyj.maven.plugin.mojo.SimplifyMode;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

/**
 * POM简化器工厂类
 *
 * @author wangliang181230
 * @since 0.4.0
 */
public abstract class PomSimplifierFactory {

	public static AbstractPomSimplifier create(MavenProject project, String modeStr, Log log) {
		SimplifyMode mode = null;

		if (modeStr == null || modeStr.isEmpty() || "auto".equalsIgnoreCase(modeStr)) {
			modeStr = project.getPackaging();
		}

		try {
			mode = SimplifyMode.getByModeStr(modeStr);
		} catch (Exception e) {
			log.warn("Get the mode by string '" + modeStr + "' failed: " + e);
		}

		if (mode == null) {
			mode = SimplifyMode.NOOP;
		}

		return createInternal(project, mode, log);
	}


	private static AbstractPomSimplifier createInternal(MavenProject project, SimplifyMode mode, Log log) {
		log.info("The mode is " + mode);

		switch (mode) {
			case JAR:
				return new JarPomSimplifier(project, log);

			case SHADE:
				return new ShadeJarPomSimplifier(project, log);

			case BOM:
				return new BomPomSimplifier(project, log);

			case POM:
			case MAVEN_PLUGIN:
			case NOOP:
			case WAR:
			default:
				return new NoopPomSimplifier(project, log);
		}
	}
}
