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
package icu.easyj.maven.plugin.mojo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * ZIP压缩工具类
 *
 * @author wangliang181230
 * @since 0.6.9
 */
public abstract class ZipUtils {

	private static final int BUFFER_SIZE = 2 * 1024;


	/**
	 * 压缩成ZIP 方法1
	 *
	 * @param srcDir           压缩文件夹路径
	 * @param out              压缩文件输出流
	 * @param keepDirStructure 是否保留原来的目录结构
	 * @throws RuntimeException 压缩失败会抛出运行时异常
	 */
	public static void toZip(String srcDir, OutputStream out, boolean keepDirStructure) throws IOException {
		try (ZipOutputStream zos = new ZipOutputStream(out)) {
			File sourceFile = new File(srcDir);
			compress(sourceFile, zos, sourceFile.getName(), keepDirStructure);
		}
	}

	/**
	 * 压缩成ZIP 方法2
	 *
	 * @param srcFiles 需要压缩的文件列表
	 * @param out      压缩文件输出流
	 * @throws IOException 压缩失败会抛出运行时异常
	 */
	public static void toZip(List<File> srcFiles, OutputStream out, boolean keepDirStructure) throws IOException {
		try (ZipOutputStream zos = new ZipOutputStream(out);) {
			for (File srcFile : srcFiles) {
				compress(srcFile, zos, srcFile.getName(), keepDirStructure);
			}
		}
	}

	/**
	 * 递归压缩方法
	 *
	 * @param sourceFile       源文件
	 * @param zos              zip输出流
	 * @param name             压缩后的名称
	 * @param keepDirStructure 是否保留原来的目录结构,true:保留目录结构;
	 *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 * @throws IOException IO异常
	 */
	private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean keepDirStructure) throws IOException {
		byte[] buf = new byte[BUFFER_SIZE];
		if (sourceFile.isFile()) {
			// 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
			zos.putNextEntry(new ZipEntry(name));
			// copy文件到zip输出流中
			int len;
			try (FileInputStream in = new FileInputStream(sourceFile)) {
				while ((len = in.read(buf)) != -1) {
					zos.write(buf, 0, len);
				}
				zos.closeEntry();
			}
		} else {
			File[] listFiles = sourceFile.listFiles();
			if (listFiles == null || listFiles.length == 0) {
				// 需要保留原来的文件结构时,需要对空文件夹进行处理
				if (keepDirStructure) {
					// 空文件夹的处理
					zos.putNextEntry(new ZipEntry(name + "/"));
					// 没有文件，不需要文件的copy
					zos.closeEntry();
				}
			} else {
				for (File file : listFiles) {
					// 判断是否需要保留原来的文件结构
					if (keepDirStructure) {
						// 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
						// 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
						compress(file, zos, name + "/" + file.getName(), keepDirStructure);
					} else {
						compress(file, zos, file.getName(), keepDirStructure);
					}
				}
			}
		}
	}
}
