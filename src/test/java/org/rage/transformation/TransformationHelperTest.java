package org.rage.transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.rage.data.DummyVO;
import org.rage.transformation.TransformationHelper;

public class TransformationHelperTest {

	private List<DummyVO> dummyList = null;
	private DummyVO dummyVo = null;

	@Before
	public void configure() {
		dummyList = new ArrayList<DummyVO>();
		dummyList.add(new DummyVO(1, "me", "mepassword"));
		dummyList.add(new DummyVO(2, "you", "youpassword"));

		dummyVo = new DummyVO(1, "someusername", "somepássword");
	}

	/**
	 * Test List<Object> to List<Map> transformation
	 */
	@Test
	public void transformList() {
		final List<Map<String, Map<String, Object>>> transformedList = TransformationHelper
				.transformListToListMap(dummyList);

		Assert.assertEquals(dummyList.size(), transformedList.size());

		Assert.assertEquals(dummyList.get(0).getEmail(), transformedList.get(0).get("email").get("value"));
		Assert.assertEquals(dummyList.get(1).getEmail(), transformedList.get(1).get("email").get("value"));

		Assert.assertEquals(dummyList.get(0).getUsername(), transformedList.get(0).get("username").get("value"));
		Assert.assertEquals(dummyList.get(1).getUsername(), transformedList.get(1).get("username").get("value"));
	}

	/**
	 * Test Object to Map transformation
	 */
	@Test
	public void transformObject() {
		final Map<String, Map<String, Object>> transformedObject = TransformationHelper.transformObjectToMap(dummyVo);

		Assert.assertEquals(dummyVo.getEmail(), transformedObject.get("email").get("value"));
		Assert.assertEquals(dummyVo.getUsername(), transformedObject.get("username").get("value"));
	}

	/**
	 * Test Map to Object transformation
	 */
	@Test
	public void transformMapToObject() {
		final Map<String, Map<String, Object>> transformedObject = TransformationHelper.transformObjectToMap(dummyVo);

		Assert.assertEquals(dummyVo.getEmail(), transformedObject.get("email").get("value"));
		Assert.assertEquals(dummyVo.getUsername(), transformedObject.get("username").get("value"));

		final DummyVO convertedObject = new DummyVO();
		TransformationHelper.populateObjectWithProperties(convertedObject, transformedObject);

		Assert.assertEquals(dummyVo.getEmail(), convertedObject.getEmail());
		Assert.assertEquals(dummyVo.getUsername(), convertedObject.getUsername());
	}

	/**
	 * Test List<Map> to List<Object> transformation
	 */
	@Test
	public void transformMapListToListObject() {
		final List<Map<String, Map<String, Object>>> transformedList = TransformationHelper
				.transformListToListMap(dummyList);

		Assert.assertEquals(dummyList.size(), transformedList.size());
		Assert.assertEquals(dummyList.get(0).getEmail(), transformedList.get(0).get("email").get("value"));
		Assert.assertEquals(dummyList.get(1).getEmail(), transformedList.get(1).get("email").get("value"));

		final List<DummyVO> objectList = new ArrayList<DummyVO>();

		TransformationHelper.populateObjectListWithProperties(objectList, transformedList, DummyVO.class);

		Assert.assertEquals(dummyList.size(), objectList.size());
		Assert.assertEquals(dummyList.get(0).getEmail(), objectList.get(0).getEmail());
		Assert.assertEquals(dummyList.get(1).getEmail(), objectList.get(1).getEmail());
	}

}
