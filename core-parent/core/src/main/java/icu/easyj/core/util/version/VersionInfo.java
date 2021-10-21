/*
 * Copyright 2021 the original author or authors.
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
package icu.easyj.core.util.version;

import java.io.Serializable;

import icu.easyj.core.util.StringUtils;
import org.springframework.lang.NonNull;

/**
 * 版本信息
 *
 * @author wangliang181230
 */
public class VersionInfo implements Comparable<VersionInfo>, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 版本号
	 */
	private final String version;

	/**
	 * 版本号-长整形值
	 *
	 * @see VersionUtils#toLong(String)
	 */
	private final long versionLong;

	/**
	 * 是否为未知版本
	 *
	 * @see VersionUtils#isUnknownVersion(String)
	 */
	private final boolean unknownVersion;

	/**
	 * 是否为快照版本
	 *
	 * @see VersionUtils#isSnapshotVersion(String)
	 */
	private final boolean snapshotVersion;


	public VersionInfo(String version) {
		this.unknownVersion = VersionUtils.isUnknownVersion(version);
		this.version = this.unknownVersion ? VersionUtils.UNKNOWN_VERSION : version;
		this.versionLong = VersionUtils.toLong(version);
		this.snapshotVersion = VersionUtils.isSnapshotVersion(version);
	}


	@Override
	public int compareTo(VersionInfo otherVersionInfo) {
		if (otherVersionInfo == null) {
			return 1;
		}

		if (version.equals(otherVersionInfo.version)) {
			return 0;
		}

		return Long.compare(this.versionLong, otherVersionInfo.getVersionLong());
	}

	public int compareTo(String otherVersion) {
		if (StringUtils.isBlank(otherVersion)) {
			return unknownVersion ? 0 : 1;
		}

		if (version.equals(otherVersion)) {
			return 0;
		}

		long otherVersionLong = VersionUtils.toLong(otherVersion);
		return Long.compare(this.versionLong, otherVersionLong);
	}


	@NonNull
	public String getVersion() {
		return version;
	}

	public long getVersionLong() {
		return versionLong;
	}

	public boolean isUnknownVersion() {
		return unknownVersion;
	}

	public boolean isSnapshotVersion() {
		return snapshotVersion;
	}
}
