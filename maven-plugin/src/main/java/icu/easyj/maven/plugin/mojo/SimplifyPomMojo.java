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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.Arrays;

import icu.easyj.maven.plugin.mojo.simplifier.AbstractPomSimplifier;
import icu.easyj.maven.plugin.mojo.simplifier.PomSimplifierFactory;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * 简化 POM 的 Goal
 *
 * @author wangliang181230
 * @since 0.4.0
 */
@Mojo(name = "simplify-pom", defaultPhase = LifecyclePhase.PREPARE_PACKAGE, threadSafe = true)
public class SimplifyPomMojo extends AbstractSimplifyPomMojo {

	private static final int POM_WRITER_SIZE = 4096;


	@Parameter(defaultValue = "${project}", readonly = true, required = true)
	private MavenProject project;

	@Parameter(defaultValue = "false")
	private boolean skip;


	/**
	 * 简化模式
	 */
	@Parameter(defaultValue = "AUTO", property = "maven.simplify.mode")
	private String simplifyMode;


	/**
	 * 是否复制父POM的部分信息
	 */
	@Parameter(defaultValue = "false")
	private boolean copyParentItems;

	/**
	 * 是否移除Properties
	 */
	@Parameter(defaultValue = "false")
	private boolean removeProperties;

	/**
	 * 是否移除Parent
	 */
	@Parameter(defaultValue = "true")
	private boolean removeParent;

	/**
	 * 是否用于开源项目
	 */
	@Parameter(defaultValue = "true")
	private boolean isOpenSourceProject;

	/**
	 * 是否更新POM文件
	 */
	@Parameter(defaultValue = "true")
	private boolean updatePomFile;


	@Override
	public void execute() throws MojoExecutionException {
		if (skip) {
			getLog().info("Simplify-POM has been skipped.");
			return;
		}

		getLog().info("Create PomSimplifier by mode: " + this.simplifyMode);
		AbstractPomSimplifier pomSimplifier = PomSimplifierFactory.create(this.project, this.simplifyMode, getLog());
		getLog().info("Do simplify by the POM simplifier: " + pomSimplifier.getClass().getSimpleName());


		//region 处理POM

		pomSimplifier.doSimplify();

		if (copyParentItems) {
			pomSimplifier.copyParent();
		}

		if (removeProperties) {
			pomSimplifier.removeProperties();
		}

		if (removeParent) {
			pomSimplifier.removeParent();
		} else {
			pomSimplifier.revertParent();
		}

		if (isOpenSourceProject) {
			pomSimplifier.copyParentForOpenSourceProject();
		}

		//endregion


		// Create simplified POM file
		getLog().info("Update the POM file to '" + this.simplifiedPomFileName + "'.");

		File simplifiedPomFile = getSimplifiedPomFile();
		writePom(this.project.getOriginalModel(), simplifiedPomFile);

		if (updatePomFile) {
			project.setFile(simplifiedPomFile);
		}
	}

	private void writePom(Model model, File pomFile) throws MojoExecutionException {
		// Create dir
		File parentFile = pomFile.getParentFile();
		if (!parentFile.exists()) {
			boolean success = parentFile.mkdirs();
			if (!success) {
				throw new MojoExecutionException("Failed to create directory " + pomFile.getParent());
			}
		}

		// Write POM
		MavenXpp3Writer pomWriter = new MavenXpp3Writer();
		StringWriter stringWriter = new StringWriter(POM_WRITER_SIZE);
		try {
			pomWriter.write(stringWriter, model);
		} catch (IOException e) {
			throw new MojoExecutionException("Internal I/O error!", e);
		}
		StringBuffer buffer = stringWriter.getBuffer();
		writeStringToFile(buffer.toString(), pomFile, model.getModelEncoding());
	}

	protected void writeStringToFile(String data, File file, String encoding)
			throws MojoExecutionException {
		if (System.getProperty("os.name").contains("Windows")) {
			data = data.replaceAll("\r?\n", "\r\n");
		}
		byte[] binaryData;

		try {
			binaryData = data.getBytes(encoding);
			if (file.isFile() && file.canRead() && file.length() == binaryData.length) {
				try (InputStream inputStream = Files.newInputStream(file.toPath())) {
					byte[] buffer = new byte[binaryData.length];
					inputStream.read(buffer);
					if (Arrays.equals(buffer, binaryData)) {
						getLog().debug("Arrays.equals( buffer, binaryData ) ");
						return;
					}
					getLog().debug("Not Arrays.equals( buffer, binaryData ) ");
				} catch (IOException e) {
					// ignore those exceptions, we will overwrite the file
					getLog().debug("Issue reading file: " + file.getPath(), e);
				}
			} else {
				getLog().debug("file: " + file + ",file.length(): " + file.length() + ", binaryData.length: " + binaryData.length);
			}
		} catch (IOException e) {
			throw new MojoExecutionException("cannot read String as bytes", e);
		}
		try (OutputStream outStream = Files.newOutputStream(file.toPath())) {
			outStream.write(binaryData);
		} catch (IOException e) {
			throw new MojoExecutionException("Failed to write to " + file, e);
		}
	}
}
