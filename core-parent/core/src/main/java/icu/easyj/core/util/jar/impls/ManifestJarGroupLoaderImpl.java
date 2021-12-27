///*
// * Copyright 2021 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package icu.easyj.core.util.jar.impls;
//
//import icu.easyj.core.loader.LoadLevel;
//import icu.easyj.core.util.StringUtils;
//import icu.easyj.core.util.jar.IJarGroupLoader;
//import icu.easyj.core.util.jar.JarContext;
//
//import static icu.easyj.core.util.jar.JarUtils.AUTOMATIC_MODULE_NAME;
//import static icu.easyj.core.util.jar.JarUtils.BUNDLE_SYMBOLIC_NAME;
//
///**
// * 从JAR中的 '/META-INF/MANIFEST.MF' 读取组名
// *
// * @author wangliang181230
// */
//@LoadLevel(name = "manifest", order = 10)
//public class ManifestJarGroupLoaderImpl implements IJarGroupLoader {
//
//	@Override
//	public String load(JarContext jarContext) {
//		String automaticModuleName = jarContext.getAttribute(AUTOMATIC_MODULE_NAME);
//		if (this.isEffectiveGroup(automaticModuleName)) {
//			return automaticModuleName;
//		}
//
//		String bundleSymbolicName = getBundleSymbolicName(jarContext);
//		if (this.isEffectiveGroup(bundleSymbolicName)) {
//			return bundleSymbolicName;
//		}
//
//		return null;
//	}
//
//	private String getBundleSymbolicName(JarContext jarContext) {
//		String bundleSymbolicName = jarContext.getAttribute(BUNDLE_SYMBOLIC_NAME);
//		if (StringUtils.isNotBlank(bundleSymbolicName)) {
//			bundleSymbolicName = bundleSymbolicName.split(";")[0];
//			if (StringUtils.isNotBlank(bundleSymbolicName)) {
//				return bundleSymbolicName.trim();
//			}
//		}
//		return null;
//	}
//
//	private boolean isEffectiveGroup(String group) {
//		return StringUtils.isNotBlank(group)
////				&& StringUtils.contains(group, '.')
////				&& StringUtils.indexOf(group, '.', 1) < 4
//				&& (
//				group.startsWith("org.")
//						|| group.startsWith("com.")
//						|| group.startsWith("cn.")
//		);
//	}
//}
