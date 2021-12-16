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

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static icu.easyj.core.util.version.VersionUtils.UNKNOWN_VERSION_LONG;

/**
 * {@link VersionUtils} 测试类
 *
 * @author wangliang181230
 */
public class VersionUtilsTest {

	@Test
	public void testParse() {
		VersionInfo versionInfo = VersionUtils.parse("999");
		Assertions.assertEquals("999", versionInfo.getVersion());
		Assertions.assertEquals(999_000_000_000_000_1L, versionInfo.getVersionLong());
		Assertions.assertFalse(versionInfo.isUnknownVersion());
		Assertions.assertFalse(versionInfo.isSnapshotVersion());

		versionInfo = VersionUtils.parse("999-SNAPSHOT");
		Assertions.assertEquals("999-SNAPSHOT", versionInfo.getVersion());
		Assertions.assertEquals(999_000_000_000_000_0L, versionInfo.getVersionLong());
		Assertions.assertFalse(versionInfo.isUnknownVersion());
		Assertions.assertTrue(versionInfo.isSnapshotVersion());

		versionInfo = VersionUtils.parse(null);
		Assertions.assertEquals(VersionUtils.UNKNOWN_VERSION, versionInfo.getVersion());
		Assertions.assertEquals(UNKNOWN_VERSION_LONG, versionInfo.getVersionLong());
		Assertions.assertTrue(versionInfo.isUnknownVersion());
		Assertions.assertFalse(versionInfo.isSnapshotVersion());
	}

	@Test
	public void testToLong() {
		System.out.println(Long.MAX_VALUE); // 19位
		System.out.println(VersionUtils.toLong("999")); // 16位

		// case: RELEASE版本，个位数为1
		Assertions.assertEquals(999_000_000_000_000_1L, VersionUtils.toLong("999"));
		Assertions.assertEquals(999_002_000_000_000_1L, VersionUtils.toLong("999.2"));
		Assertions.assertEquals(999_002_000_000_000_1L, VersionUtils.toLong("999_2"));
		Assertions.assertEquals(999_002_003_004_005_1L, VersionUtils.toLong("999.2.3.4.5"));
		Assertions.assertThrows(IncompatibleVersionException.class, () -> VersionUtils.toLong("999.2.3.4.5.6"));
		// case: SNAPSHOT版本，个位数为0
		Assertions.assertEquals(999_000_000_000_000_0L, VersionUtils.toLong("999-SNAPSHOT"));
		Assertions.assertEquals(999_002_000_000_000_0L, VersionUtils.toLong("999.2-SNAPSHOT"));
		Assertions.assertEquals(999_002_000_000_000_0L, VersionUtils.toLong("999_2-SNAPSHOT"));
		Assertions.assertEquals(999_002_003_004_005_0L, VersionUtils.toLong("999.2.3.4.5-SNAPSHOT"));
		Assertions.assertThrows(IncompatibleVersionException.class, () -> VersionUtils.toLong("999.2.3.4.5.6-SNAPSHOT"));
		// case: 未知版本
		Assertions.assertEquals(UNKNOWN_VERSION_LONG, VersionUtils.toLong(null));
		Assertions.assertEquals(UNKNOWN_VERSION_LONG, VersionUtils.toLong(""));
		Assertions.assertEquals(UNKNOWN_VERSION_LONG, VersionUtils.toLong("   "));
		Assertions.assertEquals(UNKNOWN_VERSION_LONG, VersionUtils.toLong("unknown"));
	}

	@Test
	public void testCompare() {
		// case: 小于0
		Assertions.assertTrue(VersionUtils.compare("1.0-SNAPSHOT", "1.0-RELEASE") < 0); // SNAPSHOT为快照版本，非RELEASE，版本号相同时，比任何RELEASE版本要小
		Assertions.assertTrue(VersionUtils.compare("1.0-RELEASE", "1.0-SNAPSHO") < 0);
		Assertions.assertTrue(VersionUtils.compare("1.0-RC1", "1.0-RC2") < 0);
		Assertions.assertTrue(VersionUtils.compare("1.0-Aaaa", "1.0-Bbb") < 0);
		Assertions.assertTrue(VersionUtils.compare("   ", "1.0") < 0);

		// case: 等于0
		Assertions.assertEquals(0, VersionUtils.compare("1.0-Aaaa", "1.0-Aaaa"));
		Assertions.assertEquals(0, VersionUtils.compare("   ", " "));

		// case: 大于0
		Assertions.assertTrue(VersionUtils.compare("1.0-RELEASE", "1.0-SNAPSHOT") > 0);
		Assertions.assertTrue(VersionUtils.compare("1.0-SNAPSHO", "1.0-RELEASE") > 0);
		Assertions.assertTrue(VersionUtils.compare("1.0-RC2", "1.0-RC1") > 0);
		Assertions.assertTrue(VersionUtils.compare("1.0-Bbb", "1.0-Aaaa") > 0);
		Assertions.assertTrue(VersionUtils.compare("1.0", "   ") > 0);
	}

	@Test
	public void testIsUnknownVersion() {
		Assertions.assertTrue(VersionUtils.isUnknownVersion(null));
		Assertions.assertTrue(VersionUtils.isUnknownVersion(""));
		Assertions.assertTrue(VersionUtils.isUnknownVersion("   "));
		Assertions.assertTrue(VersionUtils.isUnknownVersion("unknown"));
		Assertions.assertTrue(VersionUtils.isUnknownVersion("UnKnown"));
	}

	@Test
	public void testIsSnapshotVersion() {
		Assertions.assertFalse(VersionUtils.isSnapshotVersion(null));
		Assertions.assertFalse(VersionUtils.isSnapshotVersion(""));
		Assertions.assertFalse(VersionUtils.isSnapshotVersion("   "));
		Assertions.assertFalse(VersionUtils.isSnapshotVersion("unknown"));
		Assertions.assertFalse(VersionUtils.isSnapshotVersion("UnKnown"));
		Assertions.assertTrue(VersionUtils.isSnapshotVersion("UnKnown-SNAPSHOT"));
	}
}
