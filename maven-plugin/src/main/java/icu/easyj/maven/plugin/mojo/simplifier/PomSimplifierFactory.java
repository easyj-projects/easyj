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
import icu.easyj.maven.plugin.mojo.SimplifyPomMojoConfig;
import icu.easyj.maven.plugin.mojo.simplifier.jar.JarPomSimplifier;
import icu.easyj.maven.plugin.mojo.simplifier.jar.ShadeJarPomSimplifier;
import icu.easyj.maven.plugin.mojo.simplifier.mavenplugin.MavenPluginPomSimplifier;
import icu.easyj.maven.plugin.mojo.simplifier.noop.NoopPomSimplifier;
import icu.easyj.maven.plugin.mojo.simplifier.pom.BomPomSimplifier;
import icu.easyj.maven.plugin.mojo.simplifier.pom.DependenciesPomSimplifier;
import icu.easyj.maven.plugin.mojo.simplifier.pom.PomSimplifier;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import static icu.easyj.maven.plugin.mojo.simplifier.IPomSimplifier.AUTO;
import static icu.easyj.maven.plugin.mojo.simplifier.IPomSimplifier.BOM;
import static icu.easyj.maven.plugin.mojo.simplifier.IPomSimplifier.DEPENDENCIES;
import static icu.easyj.maven.plugin.mojo.simplifier.IPomSimplifier.JAR;
import static icu.easyj.maven.plugin.mojo.simplifier.IPomSimplifier.MAVEN_PLUGIN;
import static icu.easyj.maven.plugin.mojo.simplifier.IPomSimplifier.POM;
import static icu.easyj.maven.plugin.mojo.simplifier.IPomSimplifier.SHADE;
import static icu.easyj.maven.plugin.mojo.simplifier.IPomSimplifier.WAR;

/**
 * POM简化器工厂类
 *
 * @author wangliang181230
 * @since 0.4.0
 */
public abstract class PomSimplifierFactory {

	public static AbstractPomSimplifier create(MavenProject project, String modeStr, SimplifyPomMojoConfig config, Log log) {
		SimplifyMode mode = null;

		// auto模式时，自动根据构建标识判断
		if (AUTO.equalsIgnoreCase(modeStr)) {
			modeStr = null;
			String artifactId = project.getArtifactId().toLowerCase();
			if (POM.equals(project.getPackaging()) && artifactId.endsWith("-bom")) {
				modeStr = BOM;
				log.info("Set mode to '" + modeStr + "'," +
						" because the artifactId \"" + project.getArtifactId() + "\".endsWith(\"-bom\").");
			} else if (JAR.equals(project.getPackaging()) && artifactId.endsWith("-all")) {
				modeStr = SHADE;
				log.info("Set mode to '" + modeStr + "'," +
						" because the artifactId \"" + project.getArtifactId() + "\".endsWith(\"-all\").");
			}
		}

		if (modeStr == null || modeStr.isEmpty()) {
			modeStr = project.getPackaging();
		} else {
			switch (modeStr.toLowerCase().replace('_', '-')) {
				case POM:
				case JAR:
				case WAR:
				case MAVEN_PLUGIN:
					if (!modeStr.equalsIgnoreCase(project.getPackaging())) {
						log.warn("The mode '" + modeStr + "' can't used for packaging '" + project.getPackaging() + "'.");
						modeStr = project.getPackaging();
					}
					break;
				case SHADE:
					if (!"jar".equals(project.getPackaging())) {
						log.warn("The mode '" + modeStr + "' can't used for packaging '" + project.getPackaging() + "'.");
						modeStr = project.getPackaging();
					}
					break;
				case DEPENDENCIES:
				case BOM:
					if (!"pom".equals(project.getPackaging())) {
						log.warn("The mode '" + modeStr + "' can't used for packaging '" + project.getPackaging() + "'.");
						modeStr = project.getPackaging();
					}
					break;
				default:
					break;
			}
		}

		try {
			mode = SimplifyMode.getByModeStr(modeStr);
		} catch (Exception e) {
			log.warn("Get the mode by string '" + modeStr + "' failed: " + e);
		}

		if (mode == null) {
			mode = SimplifyMode.NOOP;
		}

		log.info("The simplify mode is: " + mode);

		config.setMode(mode);
		return createInternal(project, config, log);
	}


	private static AbstractPomSimplifier createInternal(MavenProject project, SimplifyPomMojoConfig config, Log log) {
		switch (config.getMode()) {
			case JAR:
			case WAR:
				return new JarPomSimplifier(project, config, log);
			case SHADE:
				return new ShadeJarPomSimplifier(project, config, log);

			case MAVEN_PLUGIN:
				return new MavenPluginPomSimplifier(project, config, log);

			case POM:
				return new PomSimplifier(project, config, log);
			case DEPENDENCIES:
				return new DependenciesPomSimplifier(project, config, log);
			case BOM:
				return new BomPomSimplifier(project, config, log);

			case NOOP:
			default:
				return new NoopPomSimplifier(project, config, log);
		}
	}
}
