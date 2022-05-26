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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import icu.easyj.maven.plugin.mojo.SimplifyPomMojoConfig;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import static icu.easyj.maven.plugin.mojo.utils.ObjectUtils.isEmpty;
import static icu.easyj.maven.plugin.mojo.utils.ObjectUtils.isNotEmpty;

/**
 * 抽象POM 简化器
 *
 * @author wangliang181230
 * @since 0.4.0
 */
public abstract class AbstractPomSimplifier implements IPomSimplifier {

	protected final Log log;

	protected final SimplifyPomMojoConfig config;


	protected final MavenProject project;
	protected final MavenProject parent;

	protected final Model originalModel;
	protected final Parent originalModelParent;
	protected final List<Dependency> originalDependencies;

	protected final Model model;
	protected final Parent modelParent;


	private boolean isCopiedParentItems = false;
	private boolean isCopiedParentItemsForOpenSourceProject = false;
	private boolean isResetDependencies = false;


	protected AbstractPomSimplifier(MavenProject project, SimplifyPomMojoConfig config, Log log) {
		this.project = project;
		this.parent = project.getParent();
		this.originalModel = project.getOriginalModel();
		this.originalModelParent = this.originalModel.getParent();
		this.originalDependencies = originalModel.getDependencies();
		this.model = project.getModel();
		this.modelParent = this.model.getParent();

		this.config = config;

		this.log = log;
	}


	@Override
	public void afterSimplify() {
		this.replaceParentRevision();
		this.optimizeDependencies();
	}

	/**
	 * 根据配置进行一些操作
	 */
	@Override
	public void doSimplifyByConfig() {
		this.copyProjectInfoFromParentForOpenSourceProject();
	}


	private void copyParentItems(String... itemNameArr) {
		for (String itemName : itemNameArr) {
			try {
				itemName = itemName.substring(0, 1).toUpperCase() + itemName.substring(1);

				Method getMethod = Model.class.getMethod("get" + itemName);

				Object originalValue = getMethod.invoke(this.originalModel);
				Object value = this.findParentValue(this.project.getParent(), getMethod);

				if (isEmpty(originalValue) && isNotEmpty(value)) {
					this.log.info("   Copy " + itemName + ".");
					Method setMethod = Model.class.getMethod("set" + itemName, value instanceof List ? List.class : (value instanceof Map ? Map.class : value.getClass()));
					setMethod.invoke(this.originalModel, value);
				}
			} catch (Exception e) {
				this.log.warn("   Copy " + itemName + " failed:", e);
			}
		}
	}

	private Object findParentValue(MavenProject parent, Method getMethod) throws InvocationTargetException, IllegalAccessException {
		if (parent == null) {
			return null;
		}
		Object value = getMethod.invoke(parent.getOriginalModel());
		if (isEmpty(value)) {
			return findParentValue(parent.getParent(), getMethod);
		}
		return value;
	}

	private int getDependenciesSize(DependencyManagement dm) {
		if (dm == null) {
			return 0;
		}
		return getDependenciesSize(dm.getDependencies());
	}

	private int getDependenciesSize(List<Dependency> dependencies) {
		if (dependencies == null) {
			return 0;
		}
		return dependencies.size();
	}

	protected void printLine() {
		this.log.info("-------------------------------------------");
	}


	//region # 对POM中各元素的操作


	//region --- Parent ---

	/**
	 * 移除Parent
	 */
	public void removeParent() {
		if (this.originalModel.getParent() != null) {
			this.log.info("Remove Parent.");
			this.originalModel.setParent(null);
		}
	}

	public void removeParentByConfig() {
		if (this.config.needRemoveParent()) {
			removeParent();
		}
	}

	/**
	 * 替换Parent的版本号表达式 '${revision}' 为具体的版本号
	 */
	public void replaceParentRevision() {
		if (this.originalModel.getParent() != null && this.originalModelParent != null && REVISION.equals(this.originalModelParent.getVersion())) {
			this.log.info("Set parent version from '" + this.originalModelParent.getVersion() + "' to '" + this.modelParent.getVersion() + "'.");
			this.originalModelParent.setVersion(this.modelParent.getVersion());
		}
	}

	public void removeParentRelativePath() {
		if (this.originalModel.getParent() != null && isNotEmpty(this.originalModel.getParent().getRelativePath())) {
			this.log.info("Remove Parent RelativePath.");
			this.originalModel.getParent().setRelativePath(null);
		}
	}

	//endregion ##


	//region -------------------- GroupId、ArtifactId、Version、Packaging --------------------

	public void resetArtifactIdentification() {
		if (!this.model.getGroupId().equals(this.originalModel.getGroupId())) {
			this.log.info("Set GroupId from '" + this.originalModel.getGroupId() + "' to '" + this.model.getGroupId() + "'.");
			this.originalModel.setGroupId(this.model.getGroupId());
		}
		if (!this.model.getArtifactId().equals(this.originalModel.getArtifactId())) {
			this.log.info("Set ArtifactId from '" + this.originalModel.getArtifactId() + "' to '" + this.model.getArtifactId() + "'.");
			this.originalModel.setArtifactId(this.model.getArtifactId());
		}
		if (!this.model.getVersion().equals(this.originalModel.getVersion())) {
			this.log.info("Set Version from '" + this.originalModel.getVersion() + "' to '" + this.model.getVersion() + "'.");
			this.originalModel.setVersion(this.model.getVersion());
		}
		if (isNotEmpty(this.model.getPackaging()) && !this.model.getPackaging().equals(this.originalModel.getPackaging()) && !JAR.equalsIgnoreCase(this.model.getPackaging())) {
			this.log.info("Set Packaging from '" + this.originalModel.getPackaging() + "' to '" + this.model.getPackaging() + "'.");
			this.originalModel.setPackaging(this.model.getPackaging());
		}
	}

	public void resetVersion() {
		if (isNotEmpty(this.originalModel.getVersion()) && !this.model.getVersion().equals(this.originalModel.getVersion())) {
			this.log.info("Set Version from '" + this.originalModel.getVersion() + "' to '" + this.model.getVersion() + "'.");
			this.originalModel.setVersion(this.model.getVersion());
		}
	}

	//endregion ##


	//region -------------------- Name、Description --------------------

	public void resetNameAndDescription() {
		if (isNotEmpty(this.model.getName()) && isNotEmpty(this.originalModel.getName()) && !this.model.getName().equals(this.originalModel.getName())) {
			this.log.info("Set Name from '" + this.originalModel.getName() + "' to '" + this.model.getName() + "'.");
			this.originalModel.setName(this.model.getName());
		}
		if (isNotEmpty(this.model.getDescription()) && isNotEmpty(this.originalModel.getDescription()) && !this.model.getDescription().equals(this.originalModel.getDescription())) {
			this.log.info("Set Description from '" + this.originalModel.getDescription() + "' to '" + this.model.getDescription() + "'.");
			this.originalModel.setDescription(this.model.getDescription());
		}
	}

	//endregion ##


	//region -------------------- Organization、Url、Licenses、Developers、Scm、IssueManagement --------------------

	public void copyProjectInfoFromParentForOpenSourceProject() {
		if (this.isCopiedParentItemsForOpenSourceProject || !this.config.isOpenSourceProject() || this.originalModel.getParent() != null) {
			return;
		}
		this.isCopiedParentItemsForOpenSourceProject = true;

		printLine();
		this.log.info("Copy project info from parent for the open source project:");

		String[] itemNameArr = new String[]{
				// 开源必须
				"Url",
				"Licenses",
				"Developers",
				"Scm",

				// 开源非必须，但加着比较好
				"Organization",
				"IssueManagement"
		};
		this.copyParentItems(itemNameArr);
		printLine();
	}

	//endregion ##


	//region -------------------- InceptionYear、Contributors、MailingLists、CiManagement --------------------

	public void copyProjectInfoFromParent() {
		if (this.isCopiedParentItems || !this.config.isOpenSourceProject() || this.originalModel.getParent() != null) {
			return;
		}
		this.isCopiedParentItems = true;

		printLine();
		this.log.info("Copy project info from parent:");

		String[] itemNameArr = new String[]{
				"InceptionYear",
				"Contributors",
				"MailingLists",
				"CiManagement"
		};
		this.copyParentItems(itemNameArr);

		this.copyProjectInfoFromParentForOpenSourceProject();
		printLine();
	}

	//endregion ##


	//region -------------------- DependencyManagement、Dependencies --------------------

	public void removeDependencyManagement() {
		if (this.originalModel.getDependencyManagement() != null) {
			this.resetDependencies();
			this.log.info("Remove DependencyManagement.");
			this.originalModel.setDependencyManagement(null);
		} else {
			this.resetDependencies();
		}
	}

	public void resetDependencyManagement() {
		if (isNotEmpty(this.model.getDependencyManagement()) && this.model.getDependencyManagement() != this.originalModel.getDependencyManagement()) {
			int originalSize = this.getDependenciesSize(this.originalModel.getDependencyManagement());
			int size = this.getDependenciesSize(this.model.getDependencyManagement());
			this.log.info("Reset DependencyManagement: " + originalSize + " -> " + size);
			this.originalModel.setDependencyManagement(this.model.getDependencyManagement());
		}
	}

	public void removeDependencies() {
		if (isNotEmpty(this.originalModel.getDependencies())) {
			this.log.info("Remove Dependencies.");
			this.originalModel.setDependencies(null);
		}
	}

	public void resetDependencies() {
		if (isResetDependencies) {
			return;
		}
		isResetDependencies = true;

		if (!isEmpty(this.originalModel.getDependencies())) {
			int originalSize = getDependenciesSize(this.originalModel.getDependencies());
			printLine();
			this.log.info("Reset dependency groupId and version and exclusions (" + this.originalModel.getDependencies().size() + "):");
			for (int i = 0, n = 0; i < this.model.getDependencies().size(); i++, n++) {
				Dependency dependency = this.model.getDependencies().get(i);
				if (i < originalSize) {
					Dependency originalDependency = this.originalModel.getDependencies().get(n);

					// 重置groupId、version和exclusions
					if (dependency.getArtifactId().equals(originalDependency.getArtifactId())) {
						//region 判断是否需要移除

						if (!this.config.isKeepProvidedAndOptionalDependencies()) {
							if ("provided".equalsIgnoreCase(dependency.getScope())) {
								this.removeOneDependencies(dependency, n--, "scope=provided");
								continue;
							}
							if (dependency.isOptional()) {
								this.removeOneDependencies(dependency, n--, "optional=true");
								continue;
							}
						}

						if (!this.config.isKeepTestDependencies()) {
							if ("test".equalsIgnoreCase(dependency.getScope())) {
								this.removeOneDependencies(dependency, n--, "scope=test");
								continue;
							}
						}

						if (this.config.isExcludeDependency(dependency)) {
							this.removeOneDependencies(dependency, n--, "isExclude=true");
							continue;
						}

						//endregion

						this.log.info("  Reset dependency groupId and version and exclusions: " + dependency + " -> " + originalDependency);
						originalDependency.setGroupId(dependency.getGroupId());
						originalDependency.setVersion(dependency.getVersion());
						originalDependency.setExclusions(dependency.getExclusions());
					} else {
						this.log.warn("  Reset dependency groupId and version and exclusions failed: " + dependency + " != " + originalDependency);
					}
				} else if (!isNeedRemoved(dependency)) {
					this.log.info("  Add dependency: " + dependency);
					this.originalModel.getDependencies().add(dependency);
				}
			}
			this.log.info("End: " + this.originalModel.getDependencies().size());
			printLine();
		}
	}

	private void optimizeDependencies() {
		if (isEmpty(this.originalModel.getDependencies())) {
			return;
		}

		for (Dependency dependency : this.originalModel.getDependencies()) {
			if ("compile".equalsIgnoreCase(dependency.getScope())) {
				dependency.setScope(null);
			}
		}
	}

	private boolean isNeedRemoved(Dependency dependency) {
		if (!this.config.isKeepProvidedAndOptionalDependencies()) {
			if ("provided".equalsIgnoreCase(dependency.getScope())) {
				return true;
			}
			if (dependency.isOptional()) {
				return true;
			}
		}

		if (!this.config.isKeepTestDependencies()) {
			if ("test".equalsIgnoreCase(dependency.getScope())) {
				return true;
			}
		}

		if (this.config.isExcludeDependency(dependency)) {
			return true;
		}

		return false;
	}

	private void removeOneDependencies(Dependency dependency, int i, String cause) {
		this.originalModel.getDependencies().remove(i);
		this.log.info("  Remove dependency by " + cause + ": " + dependency);
	}

	//endregion ##


	//region -------------------- Properties --------------------

	public void removeProperties() {
		if (isNotEmpty(this.originalModel.getProperties())) {
			this.log.info("Remove Properties.");
			this.originalModel.setProperties(null);
			this.resetDependencies();
		}
	}

	//endregion


	//region -------------------- Prerequisites、Build、Reporting --------------------

	public void removePrerequisites() {
		if (this.originalModel.getPrerequisites() != null) {
			this.log.info("Remove Prerequisites.");
			this.originalModel.setPrerequisites(null);
		}
	}

	public void removeBuild() {
		if (this.originalModel.getBuild() != null) {
			this.log.info("Remove Build.");
			this.originalModel.setBuild(null);
		}
	}

	public void removeReporting() {
		if (this.originalModel.getReporting() != null) {
			this.log.info("Remove Reporting.");
			this.originalModel.setReporting(null);
		}
	}

	//endregion ##


	//region -------------------- Repositories、PluginRepositories、DistributionManagement --------------------

	public void removeRepositories() {
		if (isNotEmpty(this.originalModel.getRepositories())) {
			this.log.info("Remove Repositories.");
			this.originalModel.setRepositories(null);
		}
	}

	public void removePluginRepositories() {
		if (isNotEmpty(this.originalModel.getPluginRepositories())) {
			this.log.info("Remove PluginRepositories.");
			this.originalModel.setPluginRepositories(null);
		}
	}

	public void removeDistributionManagement() {
		if (this.originalModel.getDistributionManagement() != null) {
			this.log.info("Remove DistributionManagement.");
			this.originalModel.setDistributionManagement(null);
		}
	}

	//endregion ##


	//region -------------------- Profiles --------------------

	public void removeProfiles() {
		if (isNotEmpty(this.originalModel.getProfiles())) {
			this.log.info("Remove Profiles.");
			this.originalModel.setProfiles(null);
		}
	}

	//endregion ##


	//endregion #
}
