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

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

/**
 * BOM 简化器
 *
 * @author wangliang181230
 * @since 0.4.0
 */
public class BomPomSimplifier extends JarPomSimplifier {

	public BomPomSimplifier(MavenProject project, Log log) {
		super(project, log);
	}


	@Override
	public void doSimplify() {
		this.copyParent();
		this.removeDependencies();
		this.resetDependencyManagement();
		this.removeProperties();
		super.doSimplify();
	}

	@Override
	public void removeDependencyManagement() {
		// do nothing
	}
}
