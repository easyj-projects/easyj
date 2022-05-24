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

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

/**
 * 抽象POM 简化器
 *
 * @author wangliang181230
 * @since 0.4.0
 */
public abstract class AbstractPomSimplifier implements IPomSimplifier {

	protected final Log log;

	protected final MavenProject project;
	protected final MavenProject parent;
	protected final Model originalModel;
	protected final Parent originalModelParent;
	protected final Model model;
	protected final Parent modelParent;

	private boolean isCopiedParentItems = false;
	private boolean isCopiedDependenciesVersion = false;


	protected AbstractPomSimplifier(MavenProject project, Log log) {
		this.project = project;
		this.parent = project.getParent();
		this.originalModel = project.getOriginalModel();
		this.originalModelParent = this.originalModel.getParent();
		this.model = project.getModel();
		this.modelParent = this.model.getParent();

		this.log = log;
	}


	public void copyParent() {
		if (this.isCopiedParentItems) {
			return;
		}

		this.isCopiedParentItems = true;

		this.log.info("Copy parent items:");

		String[] itemNameArr = new String[]{
				"Licenses",
				"InceptionYear",
				"Scm",
				"IssueManagement",
				"CiManagement",
				"Developers",
				"Contributors",
				"Organization",
				"MailingLists",
				"Url"
		};
		this.copyParentItems(itemNameArr);
	}

	private void copyParentItems(String... itemNameArr) {
		for (String itemName : itemNameArr) {
			try {
				itemName = itemName.substring(0, 1).toUpperCase() + itemName.substring(1);

				Method getMethod = Model.class.getMethod("get" + itemName);

				Object originalValue = getMethod.invoke(this.originalModel);
				Object value = getMethod.invoke(this.model);

				if (isNullOrEmpty(originalValue) && !isNullOrEmpty(value)) {
					this.log.info("   Copy " + itemName + ".");
					Method setMethod = Model.class.getMethod("set" + itemName, value instanceof List ? List.class : (value instanceof Map ? Map.class : value.getClass()));
					setMethod.invoke(this.originalModel, value);
				}
			} catch (Exception e) {
				this.log.warn("   Copy " + itemName + " failed:", e);
			}
		}
	}

	private boolean isNullOrEmpty(Object obj) {
		if (obj == null) {
			return true;
		}

		if (obj instanceof String) {
			String str = obj.toString();
			return str.isEmpty();
		} else if (obj instanceof Collection) {
			Collection list = (Collection)obj;
			return list.isEmpty();
		} else if (obj instanceof Map) {
			Map map = (Map)obj;
			return map.isEmpty();
		}

		return false;
	}

	public void copyGroupIdAndVersion() {
		if (!this.model.getGroupId().equals(this.originalModel.getGroupId())) {
			this.log.info("Set groupId from '" + this.originalModel.getGroupId() + "' to '" + this.model.getGroupId() + "'.");
			this.originalModel.setGroupId(this.model.getGroupId());
		}
		if (!this.model.getVersion().equals(this.originalModel.getVersion())) {
			this.log.info("Set version from '" + this.originalModel.getVersion() + "' to '" + this.model.getVersion() + "'.");
			this.originalModel.setVersion(this.model.getVersion());
		}
	}

	public void replaceParentRevision() {
		if (this.originalModelParent != null && REVISION.equals(this.originalModelParent.getVersion())) {
			this.log.info("Set parent version from '" + this.originalModelParent.getVersion() + "' to '" + this.modelParent.getVersion() + "'.");
			this.originalModelParent.setVersion(this.modelParent.getVersion());
		}
	}

	public void copyDependenciesGroupIdAndVersion() {
		if (!isNullOrEmpty(this.originalModel.getDependencies()) && !isCopiedDependenciesVersion) {
			isCopiedDependenciesVersion = true;

			this.log.info("Copy dependencies groupId and version (" + this.originalModel.getDependencies().size() + "):");
			for (int i = 0; i < this.originalModel.getDependencies().size(); i++) {
				Dependency originalDependency = this.originalModel.getDependencies().get(i);
				Dependency dependency = this.model.getDependencies().get(i);
				if (dependency.getArtifactId().equals(originalDependency.getArtifactId())) {
					this.log.info("  Copy dependency groupId and version: " + dependency + " -> " + originalDependency);
					originalDependency.setGroupId(dependency.getGroupId());
					originalDependency.setVersion(dependency.getVersion());
				} else {
					this.log.warn("  Copy dependency groupId and version failed: " + dependency + " != " + originalDependency);
				}
			}
		}
	}

	public void removeParent() {
		if (this.originalModel.getParent() != null) {
			this.log.info("Remove Parent.");
			this.originalModel.setParent(null);
			this.copyGroupIdAndVersion();
			this.copyDependenciesGroupIdAndVersion();
		}
	}

	public void revertParent() {
		if (this.originalModelParent != null && this.originalModel.getParent() == null) {
			this.log.info("Revert Parent.");
			this.originalModel.setParent(this.originalModelParent);
			this.replaceParentRevision();
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

	public void removePrerequisites() {
		if (this.originalModel.getPrerequisites() != null) {
			this.log.info("Remove Prerequisites.");
			this.originalModel.setPrerequisites(null);
		}
	}

	public void removeDistributionManagement() {
		if (this.originalModel.getDistributionManagement() != null) {
			this.log.info("Remove DistributionManagement.");
			this.originalModel.setDistributionManagement(null);
		}
	}

	public void removeRepositories() {
		if (!isNullOrEmpty(this.originalModel.getRepositories())) {
			this.log.info("Remove Repositories.");
			this.originalModel.setRepositories(null);
		}
	}

	public void removePluginRepositories() {
		if (!isNullOrEmpty(this.originalModel.getPluginRepositories())) {
			this.log.info("Remove PluginRepositories.");
			this.originalModel.setPluginRepositories(null);
		}
	}

	public void removeProfiles() {
		if (!isNullOrEmpty(this.originalModel.getProfiles())) {
			this.log.info("Remove Profiles.");
			this.originalModel.setProfiles(null);
		}
	}

	public void removeDependencies() {
		if (!isNullOrEmpty(this.originalModel.getDependencies())) {
			this.log.info("Remove Dependencies.");
			this.originalModel.setDependencies(null);
		}
	}

	public void removeDependencyManagement() {
		if (this.originalModel.getDependencyManagement() != null) {
			this.log.info("Remove DependencyManagement.");
			this.originalModel.setDependencyManagement(null);
		}
	}

	public void removeProperties() {
		if (!isNullOrEmpty(this.originalModel.getProperties())) {
			this.log.info("Remove Properties.");
			this.originalModel.setProperties(null);
		}
	}

	public void resetName() {
		if (!isNullOrEmpty(this.model.getName()) && !this.model.getName().equals(this.originalModel.getName())) {
			this.log.info("Reset Name.");
			this.originalModel.setName(this.model.getName());
		}
	}

	public void resetDependencyManagement() {
		if (!isNullOrEmpty(this.model.getDependencyManagement()) && this.model.getDependencyManagement() != this.originalModel.getDependencyManagement()) {
			this.log.info("Reset DependencyManagement.");
			this.originalModel.setDependencyManagement(this.model.getDependencyManagement());
		}
	}
}
