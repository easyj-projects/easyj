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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import icu.easyj.maven.plugin.mojo.utils.IOUtils;
import icu.easyj.maven.plugin.mojo.utils.ObjectUtils;
import icu.easyj.maven.plugin.mojo.utils.ZipUtils;
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

	@Parameter(defaultValue = "${project.basedir}")
	private File outputDirectory;


	@Parameter(defaultValue = "false")
	private boolean skipInstall;

	@Parameter(defaultValue = "false")
	private boolean skipDeploy;


	/**
	 * 由于springboot官方不提供该功能，只提供 excludeGroupIds，所以这里提供该功能
	 */
	@Parameter(property = "maven.spring-boot-extend.includeGroupIds")
	private String includeGroupIds;

	/**
	 * 是否将排除掉的lib打包进lib.zip中。
	 * {@link #includeGroupIds} 不为空时，才有作用。
	 */
	@Parameter(property = "maven.spring-boot-extend.zipLib", defaultValue = "true")
	private boolean zipLib;


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
		String includeGroupIdsStr = includeGroupIds.toString();
		// 打印 includeGroupIds
		getLog().info("");
		getLog().info("The includeGroupIds: " + includeGroupIdsStr.substring(1, includeGroupIdsStr.length() - 1).replaceAll("^|\\s*,\\s*", "\r\n[INFO]   - "));

		// 因为spring-boot:repackage没有includeGroupIds，所以反过来使用excludeGroupIds来达到include的效果
		Set<String> excludeGroupIds = new TreeSet<>(String::compareTo); // 使用TreeSet，为了下面的日志按groupId顺序打印
		// 设置过滤器
		AtomicInteger includeCount = new AtomicInteger(0);
		project.setArtifactFilter(artifact -> {
			if (this.isRuntimeScope(artifact.getScope())) {
				if (!includeGroupIds.contains(artifact.getGroupId())) {
					return true;
				} else {
					includeCount.incrementAndGet();
				}
			}
			return false;
		});
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

			// 设置 'spring-boot.repackage.layout = ZIP'
			if (!"ZIP".equals(properties.getProperty("spring-boot.repackage.layout"))) {
				properties.put("spring-boot.repackage.layout", "ZIP");
				getLog().info("");
				getLog().info("Put property 'spring-boot.repackage.layout' = 'ZIP' for the goal 'spring-boot:repackage'.");
			}

			// 从artifact中获取file
			List<File> excludeJarFiles = new ArrayList<>(excludeArtifacts.size());
			for (Artifact excludeArtifact : excludeArtifacts) {
				excludeJarFiles.add(excludeArtifact.getFile());
			}

			// 将依赖复制到 /target/lib 目录下
			getLog().info("");
			File libDir = new File(outputDirectory.getPath() + "\\target\\lib");
			if (!libDir.exists()) {
				getLog().info("Create directory: " + libDir.getPath());
				if (!libDir.mkdir()) {
					throw new RuntimeException("Create directory failed: " + libDir.getPath());
				}
			}
			getLog().info("Copy " + excludeArtifacts.size() + " JARs to the directory: " + libDir.getPath());
			for (Artifact excludeArtifact : excludeArtifacts) {
				try {
					IOUtils.copy(excludeArtifact.getFile(), new File(libDir, excludeArtifact.getFile().getName()));
				} catch (IOException e) {
					throw new RuntimeException("Copy '" + excludeArtifact.getFile().getName() + "' to the directory '" + libDir.getPath() + "' failed.", e);
				}
			}

			// 将依赖打包进lib.zip中
			if (zipLib) {
				FileOutputStream fos;
				try {
					fos = new FileOutputStream(outputDirectory.getPath() + "\\target\\lib--" + excludeJarFiles.size() + "-JARs.zip");
				} catch (FileNotFoundException e) {
					throw new RuntimeException("New FileOutputStream of 'lib.zip' failed.", e);
				}

				try {
					ZipUtils.toZip(excludeJarFiles, fos, false, "lib");
				} catch (IOException e) {
					throw new RuntimeException("Package 'lib.zip' failed.", e);
				}

				getLog().info("Package 'lib.zip' succeeded, contains " + excludeJarFiles.size() + " JARs.");
				getLog().info("Total JARs: " + (includeCount.get() + excludeJarFiles.size()));
			}

			// 创建startup.bat文件
			try {
				IOUtils.createFile(new File(outputDirectory.getPath() + "\\target\\startup.bat"), "chcp 65001\r\njava -jar -Dloader.path=lib " + project.getBuild().getFinalName() + ".jar\r\ncmd");
				getLog().info("Create '/target/startup.bat'.");
			} catch (IOException e) {
				getLog().error("Create startup.bat failed", e);
			}
			// 创建startup.sh文件
			try {
				IOUtils.createFile(new File(outputDirectory.getPath() + "\\target\\startup.sh"), "#!/bin/sh\r\njava -jar -Dloader.path=lib " + project.getBuild().getFinalName() + ".jar");
				getLog().info("Create '/target/startup.sh'.");
			} catch (IOException e) {
				getLog().error("Create startup.sh failed", e);
			}
		} else {
			getLog().info("The 'excludeGroupIds' is empty, do not put the property 'spring-boot.excludeGroupIds'.");

			// 创建startup.bat文件
			try {
				IOUtils.createFile(new File(outputDirectory.getPath() + "\\target\\startup.bat"), "chcp 65001\r\njava -jar " + project.getBuild().getFinalName() + ".jar\r\ncmd");
				getLog().info("Create '/target/startup.bat'.");
			} catch (IOException e) {
				getLog().error("Create startup.bat failed", e);
			}
			// 创建startup.sh文件
			try {
				IOUtils.createFile(new File(outputDirectory.getPath() + "\\target\\startup.sh"), "#!/bin/sh\r\njava -jar " + project.getBuild().getFinalName() + ".jar");
				getLog().info("Create '/target/startup.sh'.");
			} catch (IOException e) {
				getLog().error("Create startup.sh failed", e);
			}
		}
	}

	private Set<String> convertIncludeGroupIds(String includeGroupIds) {
		Set<String> result = new TreeSet<>(String::compareTo);

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
