package icu.easyj.core.util;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link CycleDependencyHandler} 测试类
 *
 * @author wangliang181230
 */
class CycleDependencyHandlerTest {

	@Test
	void testGetUniqueSubstituteObject() {
		Set<Object> objSet = new HashSet<>();

		Assertions.assertDoesNotThrow(() -> {
			long startNanoTime = System.nanoTime();
			int count = 10 * 10000;
			int i = count;
			Object obj;
			while (i-- > 0) {
				obj = CycleDependencyHandler.getUniqueSubstituteObject(new Object());
				if (objSet.contains(obj)) {
					throw new RuntimeException("\r\n注意：getUniqueSubstituteObject方法执行了" + (count - i) + "次后，出现了重复对象，请重写此方法的实现，避免出现重复的对象，重复对象为：" + obj);
				}
				objSet.add(obj);
			}
			long cost = System.nanoTime() - startNanoTime;
			System.out.println("getUniqueSubstituteObject方法测试通过，执行了" + count + "次，没有出现重复对象。耗时：" + (cost / 1000000) + " ms。");
		});
	}
}
