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
package icu.easyj.core.util.version;

import java.io.Serializable;
import java.util.Objects;

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
		boolean unknownVersion = VersionUtils.isUnknownVersion(version);
		this.version = unknownVersion ? VersionUtils.UNKNOWN_VERSION : version;

		long versionLong;
		try {
			versionLong = VersionUtils.toLong(version);
		} catch (IncompatibleVersionException e) {
			unknownVersion = true;
			versionLong = -1;
		}

		this.unknownVersion = unknownVersion;
		this.versionLong = versionLong;
		this.snapshotVersion = VersionUtils.isSnapshotVersion(version);
	}


	@Override
	public int compareTo(@NonNull VersionInfo otherVersionInfo) {
		if (otherVersionInfo == null) {
			return 1;
		}
		if (otherVersionInfo == this) {
			return 0;
		}
		if (Objects.equals(this.version, otherVersionInfo.version)) {
			return 0;
		}

		return this.compare(otherVersionInfo.getVersion(), otherVersionInfo.getVersionLong());
	}

	public int compareTo(String otherVersion) {
		if (VersionUtils.isUnknownVersion(otherVersion)) {
			return unknownVersion ? 0 : 1;
		} else if (unknownVersion) {
			return -1;
		}

		if (this.version.equals(otherVersion)) {
			return 0;
		}

		long otherVersionLong = VersionUtils.toLong(otherVersion);
		return this.compare(otherVersion, otherVersionLong);
	}

	private int compare(String otherVersion, long otherVersionLong) {
		int result = Long.compare(this.versionLong, otherVersionLong);
		if (result == 0) {
			// 如果long值相等，则再比较一下String值
			return this.version.compareTo(otherVersion);
		} else {
			return result;
		}
	}

	/**
	 * 判断是否介于两个版本号之间，即：{@code this.version >= startVersion && this.version <= toVersion}
	 *
	 * @param startVersion 起始版本号
	 * @param toVersion    截止版本号
	 * @return true=介于 | false=不介于
	 */
	public boolean between(String startVersion, String toVersion) {
		return compareTo(startVersion) >= 0 && compareTo(toVersion) <= 0;
	}

	public boolean notBetween(String startVersion, String toVersion) {
		return !between(startVersion, toVersion);
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		VersionInfo versionInfo = (VersionInfo)o;
		return versionLong == versionInfo.versionLong
				&& unknownVersion == versionInfo.unknownVersion
				&& snapshotVersion == versionInfo.snapshotVersion
				&& Objects.equals(version, versionInfo.version);
	}

	@Override
	public int hashCode() {
		return Objects.hash(version, versionLong, unknownVersion, snapshotVersion);
	}
}
