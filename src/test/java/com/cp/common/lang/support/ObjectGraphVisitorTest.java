/*
 * ObjectGraphVisitorTest.java (c) 2 December 2007
 *
 * Copyright (c) 2003, Code Primate
 * All Rights Reserved
 * @author John J. Blum
 * @version 2008.4.30
 * @see com.cp.common.lang.support.CommonSupportTestCase
 * @see com.cp.common.lang.support.ObjectGraphVisitor
 */

package com.cp.common.lang.support;

import com.cp.common.enums.State;
import java.util.HashMap;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestSuite;

public class ObjectGraphVisitorTest extends CommonSupportTestCase {

    public ObjectGraphVisitorTest(final String testName) {
        super(testName);
    }

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(ObjectGraphVisitorTest.class);
        //suite.addTest(new ObjectGraphVisitorTest("testName"));
        return suite;
    }

    protected void assertEqualObjectGraph(final Map<Class, Map<Integer, Object>> expectedObjectGraph,
                                          final Map<Class, Map<Integer, Object>> actualObjectGraph) {
        assertNotNull("The expected object graph cannot be null!", expectedObjectGraph);
        assertNotNull("The actual object graph cannot be null!", actualObjectGraph);
        assertEquals(expectedObjectGraph.size(), actualObjectGraph.size());

        for (Class key : expectedObjectGraph.keySet()) {
            assertTrue(actualObjectGraph.containsKey(key));
            assertEqualObjectSubGraph(expectedObjectGraph.get(key), actualObjectGraph.get(key));
        }
    }

    protected void assertEqualObjectSubGraph(final Map<Integer, Object> expectedObjectSubGraph,
                                             final Map<Integer, Object> actualObjectSubGraph) {
        assertNotNull("The expected object subgraph cannot be null!", expectedObjectSubGraph);
        assertNotNull("The actual object subgraph cannot be null!", actualObjectSubGraph);
        assertEquals(expectedObjectSubGraph.size(), actualObjectSubGraph.size());

        for (Integer key : expectedObjectSubGraph.keySet()) {
            assertTrue(actualObjectSubGraph.containsKey(key));
            assertEquals(expectedObjectSubGraph.get(key), actualObjectSubGraph.get(key));
        }
    }

    public void testVisit() throws Exception {
        final Household household = getHousehold(1, "Bloom Household", 7799);

        final Address jonBloomAddress = getAddress(1, "5276 Washington Ave.", null, "Seattle", State.WASHINGTON, "31279");
        final Address sarahBloomAddress = getAddress(2, "5276 Washington Ave.", null, "Seattle", State.WASHINGTON, "31279");

        final PhoneNumber jonBloomPhoneNumber = getPhoneNumber(1, "501", "555", "1234", null);
        final PhoneNumber sarahBloomPhoneNumber = getPhoneNumber(2, "501", "555", "2244", null);

        final Person jonBloom = getPerson(1, "Jon", "Bloom");
        jonBloom.setAddress(jonBloomAddress);
        jonBloom.setPhoneNumber(jonBloomPhoneNumber);
        household.addMember(jonBloom);

        final Person sarahBloom = getPerson(2, "Sarah", "Bloom");
        sarahBloom.setAddress(sarahBloomAddress);
        sarahBloom.setPhoneNumber(sarahBloomPhoneNumber);
        household.addMember(sarahBloom);

        final Map<Class, Map<Integer, Object>> expectedObjectGraph = new HashMap<Class, Map<Integer, Object>>(3);

        final Map<Integer, Object> expectedObjectSubGraphAddress = new HashMap<Integer, Object>(2);
        expectedObjectSubGraphAddress.put(1, jonBloomAddress);
        expectedObjectSubGraphAddress.put(2, sarahBloomAddress);
        expectedObjectGraph.put(jonBloomAddress.getClass(), expectedObjectSubGraphAddress);

        final Map<Integer, Object> expectedObjectSubGraphHousehold = new HashMap<Integer, Object>(1);
        expectedObjectSubGraphHousehold.put(1, household);
        expectedObjectGraph.put(household.getClass(), expectedObjectSubGraphHousehold);

        final Map<Integer, Object> expectedObjectSubGraphPerson = new HashMap<Integer, Object>(2);
        expectedObjectSubGraphPerson.put(1, jonBloom);
        expectedObjectSubGraphPerson.put(2, sarahBloom);
        expectedObjectGraph.put(jonBloom.getClass(), expectedObjectSubGraphPerson);

        final Map<Integer, Object> expectedObjectSubGraphPhoneNumber = new HashMap<Integer, Object>(2);
        expectedObjectSubGraphPhoneNumber.put(1, jonBloomPhoneNumber);
        expectedObjectSubGraphPhoneNumber.put(2, sarahBloomPhoneNumber);
        expectedObjectGraph.put(jonBloomPhoneNumber.getClass(), expectedObjectSubGraphPhoneNumber);

        final ObjectGraphVisitor visitor = new ObjectGraphVisitor();
        household.accept(visitor);

        final Map<Class, Map<Integer, Object>> actualObjectGraph = visitor.getObjectGraph();

        assertNotNull(actualObjectGraph);
        assertEquals(expectedObjectGraph, actualObjectGraph);
//        assertEqualObjectGraph(expectedObjectGraph, actualObjectGraph);
    }

}
