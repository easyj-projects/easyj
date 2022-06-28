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
package icu.easyj.maven.plugin.mojo.springboot;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import icu.easyj.maven.plugin.mojo.utils.ObjectUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * spring-boot插件的协助插件
 *
 * @author wangliang181230
 * @since 0.6.8
 */
@Mojo(name = "spring-boot-extend", defaultPhase = LifecyclePhase.PREPARE_PACKAGE, threadSafe = true)
public class SpringBootExtendMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", readonly = true, required = true)
	private MavenProject project;


	@Parameter(defaultValue = "false")
	private boolean skipInstall;

	@Parameter(defaultValue = "false")
	private boolean skipDeploy;


	/**
	 * 由于springboot官方不提供该功能，只提供 excludeGroupIds，所以这里提供该功能
	 */
	@Parameter(property = "maven.spring-boot-extend.includeGroupIds")
	private String includeGroupIds;


	@Override
	public void execute() throws MojoExecutionException {
		//region 判断是否为springboot应用

		boolean isSpringBootJar = false;

		if ("jar".equalsIgnoreCase(project.getPackaging())) {
			List<Plugin> plugins = project.getBuildPlugins();
			for (Plugin plugin : plugins) {
				if ("org.springframework.boot".equalsIgnoreCase(plugin.getGroupId())
						&& "spring-boot-maven-plugin".equalsIgnoreCase(plugin.getArtifactId())) {
					isSpringBootJar = true;
					break;
				}
			}
		}

		if (!isSpringBootJar) {
			getLog().info("Skip this goal, cause by this project is not a spring-boot application.");
			return;
		}

		//endregion

		getLog().info("The current project is a springboot application.");


		// skip install or deploy
		this.skipInstallAndDeploy();


		// includeGroupIds
		this.includeDependencies();
	}

	private void skipInstallAndDeploy() {
		Properties properties = project.getProperties();

		if ((skipInstall && !"true".equalsIgnoreCase(properties.getProperty("maven.install.skip")))
				|| (skipDeploy && !"true".equalsIgnoreCase(properties.getProperty("maven.deploy.skip")))) {
			getLog().info("");
			getLog().info("It will skip `install` and `deploy`:");

			if (skipInstall && !"true".equalsIgnoreCase(properties.getProperty("maven.install.skip"))) {
				properties.put("maven.install.skip", "true");
				getLog().info("  - Put properties 'maven.install.skip = true' for skip `install`.");
			}

			if (skipDeploy && !"true".equalsIgnoreCase(properties.getProperty("maven.deploy.skip"))) {
				properties.put("maven.deploy.skip", "true");
				getLog().info("  - Put properties 'maven.deploy.skip = true' for skip `deploy`.");
			}
		}
	}

	private void includeDependencies() {
		if (ObjectUtils.isEmpty(includeGroupIds)) {
			return;
		}

		// string 转为 set
		Set<String> includeGroupIds = this.convertIncludeGroupIds(this.includeGroupIds);
		if (includeGroupIds.isEmpty()) {
			return;
		}
		// 打印 includeGroupIds
		getLog().info("");
		getLog().info("The includeGroupIds: " + includeGroupIds);

		// 因为spring-boot:repackage没有includeGroupIds，所以反过来使用excludeGroupIds来达到include的效果
		Set<String> excludeGroupIds = new HashSet<>();
		// 设置过滤器
		project.setArtifactFilter(artifact -> !includeGroupIds.contains(artifact.getGroupId()) && this.isRuntimeScope(artifact.getScope()));
		// 获取需排除的artifacts
		Set<Artifact> excludeArtifacts = project.getArtifacts();
		// 需排除的artifacts的所有groupId添加到excludeGroupIds
		for (Artifact excludeArtifact : excludeArtifacts) {
			excludeGroupIds.add(excludeArtifact.getGroupId());
		}
		// 清空过滤器
		project.setArtifactFilter(null);

		// 设置 'spring-boot.excludeGroupIds'
		if (!excludeGroupIds.isEmpty()) {
			Properties properties = project.getProperties();

			// 设置 'spring-boot.excludeGroupIds'，用于 spring-boot-maven-plugin:repackage
			{
				// 打印下当前值
				String propertyValue = properties.getProperty("spring-boot.excludeGroupIds");
				if (ObjectUtils.isNotEmpty(propertyValue)) {
					getLog().info("");
					getLog().info("The origin values of the property 'spring-boot.excludeGroupIds' for the goal 'spring-boot:repackage':" + propertyValue.trim().replaceAll("^|\\s*,\\s*", "\r\n[INFO]   - "));
				}

				getLog().info("");
				getLog().info("Put the following values to the property 'spring-boot.excludeGroupIds' for the goal 'spring-boot:repackage': (" + excludeGroupIds.size() + ")");
				// set转string
				StringBuilder sb = new StringBuilder();
				for (String excludeGroupId : excludeGroupIds) {
					getLog().info("  - " + excludeGroupId);
					if (sb.length() > 0) {
						sb.append(",");
					}
					sb.append(excludeGroupId);
				}
				properties.put("spring-boot.excludeGroupIds", sb.toString());
			}

			// 设置 'excludeGroupIds'，用于 'maven-dependency-plugin:copy-dependencies'
			{
				// 打印下当前值
				String propertyValue = properties.getProperty("excludeGroupIds");
				if (ObjectUtils.isNotEmpty(propertyValue)) {
					getLog().info("");
					getLog().info("The origin values of the property 'excludeGroupIds' for the goal 'maven-dependency-plugin:copy-dependencies':" + propertyValue.trim().replaceAll("^|\\s*,\\s*", "\r\n[INFO]   - "));
				}

				getLog().info("Copy the includeGroupIds to the property 'excludeGroupIds' for the goal 'maven-dependency-plugin:copy-dependencies'");
				properties.put("excludeGroupIds", includeGroupIds);
			}

			// 设置 'spring-boot.repackage.layout = ZIP'
			if (!"ZIP".equals(properties.getProperty("spring-boot.repackage.layout"))) {
				properties.put("spring-boot.repackage.layout", "ZIP");
				getLog().info("");
				getLog().info("Put property 'spring-boot.repackage.layout' = 'ZIP' for the goal 'spring-boot:repackage'.");
			}
		} else {
			getLog().info("The 'excludeGroupIds' is empty, do not put the property 'spring-boot.excludeGroupIds'.");
		}
	}

	private Set<String> convertIncludeGroupIds(String includeGroupIds) {
		Set<String> result = new HashSet<>();

		String[] includeGroupIdArr = includeGroupIds.split(",");
		for (String includeGroupId : includeGroupIdArr) {
			includeGroupId = includeGroupId.trim();
			if (includeGroupId.isEmpty()) {
				continue;
			}

			result.add(includeGroupId);
		}

		return result;
	}

	private boolean isRuntimeScope(String scope) {
		return scope == null
				|| scope.trim().isEmpty()
				|| "compile".equalsIgnoreCase(scope)
				|| "runtime".equalsIgnoreCase(scope);
	}
}
