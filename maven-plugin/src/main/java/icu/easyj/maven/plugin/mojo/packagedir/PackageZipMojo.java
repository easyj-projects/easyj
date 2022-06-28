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
package icu.easyj.maven.plugin.mojo.packagedir;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import icu.easyj.maven.plugin.mojo.utils.ObjectUtils;
import icu.easyj.maven.plugin.mojo.utils.ZipUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * 将指定目录打包成zip 的 Goal
 *
 * @author wangliang181230
 * @since 0.6.9
 */
@Mojo(name = "package-zip", defaultPhase = LifecyclePhase.PACKAGE, threadSafe = true)
public class PackageZipMojo extends AbstractMojo {

	@Parameter
	private List<String> directories;

	@Parameter
	private String outputFilePathname;


	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (ObjectUtils.isEmpty(directories)) {
			getLog().warn("directories 不能为空");
			return;
		}

		List<File> files = new ArrayList<>();
		for (String directory : directories) {
			if (directory != null && !directory.trim().isEmpty()) {
				files.add(new File(directory.trim()));
			}
		}

		FileOutputStream fos;
		try {
			fos = new FileOutputStream(outputFilePathname);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("创建文件流失败", e);
		}

		try {
			ZipUtils.toZip(files, fos, true);
		} catch (IOException e) {
			throw new RuntimeException("压缩文件流失败", e);
		}
	}
}
