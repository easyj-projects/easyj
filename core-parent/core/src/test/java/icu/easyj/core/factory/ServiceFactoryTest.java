package icu.easyj.core.factory;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceFactoryTest {

	@Test
	void test() {
		List<ITestService> serviceList = new ArrayList<>();

		serviceList.add(new TestServiceImplA());
		serviceList.add(new TestServiceImplB1());
		serviceList.add(new TestServiceImplB2());

		ServiceFactory<ITestService> serviceFactory = new ServiceFactory<>(ITestService.class, serviceList);

		// getList
		List<ITestService> aList = serviceFactory.getList("A");
		assertEquals(1, aList.size());
		List<ITestService> bList = serviceFactory.getList("B");
		assertEquals(2, bList.size());

		// default
		assertEquals(serviceList.get(0), serviceFactory.getDefault());
		// A
		assertEquals(serviceList.get(0), serviceFactory.get("A"));
		assertEquals(serviceList.get(0), aList.get(0));
		// B1
		assertEquals(serviceList.get(1), serviceFactory.get("B"));
		assertEquals(serviceList.get(1), bList.get(0));
		// B2
		assertEquals(serviceList.get(2), bList.get(1));
	}

}
