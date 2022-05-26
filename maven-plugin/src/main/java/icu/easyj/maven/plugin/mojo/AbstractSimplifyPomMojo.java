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
package icu.easyj.maven.plugin.mojo;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * 抽象的简化POM的 Goal
 *
 * @author wangliang181230
 * @since 0.4.0
 */
public abstract class AbstractSimplifyPomMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project.basedir}")
	private File outputDirectory;

	@Parameter(property = "maven.simplify.simplifiedPomFileName", defaultValue = ".simplified-pom.xml")
	protected String simplifiedPomFileName;


	protected File getSimplifiedPomFile() {
		return new File(this.outputDirectory, this.simplifiedPomFileName);
	}
}
