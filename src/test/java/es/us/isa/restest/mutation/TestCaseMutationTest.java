package es.us.isa.restest.mutation;

import es.us.isa.restest.mutation.operators.InvalidParameterValue;
import es.us.isa.restest.mutation.operators.RemoveRequiredParameter;
import es.us.isa.restest.specification.OpenAPISpecification;
import es.us.isa.restest.testcases.TestCase;
import io.swagger.models.HttpMethod;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;

import static es.us.isa.restest.mutation.TestCaseMutation.makeTestCaseFaulty;
import static org.junit.Assert.*;

public class TestCaseMutationTest {

    @Test
    public void testCaseMutationTest() {
        OpenAPISpecification spec = new OpenAPISpecification("src/test/resources/Comments/swagger.yaml");
        TestCase tc = new TestCase("dfgsdfg", true, "getComments", "/comments", HttpMethod.GET);
        tc.addQueryParameter("type", "Review");
        tc.addQueryParameter("limit", "2");

        TestCase oldTc = SerializationUtils.clone(tc);
        assertTrue("The test case should be mutated", makeTestCaseFaulty(tc, spec.getSpecification().getPath("/comments").getGet()));
        assertNotEquals("The two test cases should be different", tc, oldTc);
    }

    @Test
    public void testCaseMutationImpossibleTest() {
        OpenAPISpecification spec = new OpenAPISpecification("src/test/resources/Comments/swagger.yaml");
        TestCase tc = new TestCase("dfgsdfg", true, "getComment", "/comments/{id}", HttpMethod.GET);
        tc.addPathParameter("id", "c1");

        TestCase oldTc = SerializationUtils.clone(tc);
        assertFalse("The test case should NOT be mutated", makeTestCaseFaulty(tc, spec.getSpecification().getPath("/comments/{id}").getGet()));
        assertEquals("Both test cases should be equal", tc, oldTc);
    }

    @Test
    public void removeRequiredParameterTest() {
        OpenAPISpecification spec = new OpenAPISpecification("src/test/resources/Comments/swagger.yaml");
        TestCase tc = new TestCase("dfgsdfg", true, "postComment", "/comments", HttpMethod.POST);
        tc.setBodyParameter("{\"randomBody\": \"randomValue\"}");

        TestCase oldTc = SerializationUtils.clone(tc);
        assertTrue("The test case should be mutated", RemoveRequiredParameter.mutate(tc, spec.getSpecification().getPath("/comments").getPost()));
        assertNotEquals("The two test cases should be different", tc, oldTc);
    }

    @Test
    public void removeRequiredParameterNotPossibleTest() {
        OpenAPISpecification spec = new OpenAPISpecification("src/test/resources/Comments/swagger.yaml");
        TestCase tc = new TestCase("dfgsdfg", true, "getComments", "/comments", HttpMethod.GET);
        tc.addQueryParameter("type", "Review");
        tc.addQueryParameter("limit", "2");

        TestCase oldTc = SerializationUtils.clone(tc);
        assertFalse("The test case should NOT be mutated", RemoveRequiredParameter.mutate(tc, spec.getSpecification().getPath("/comments").getGet()));
        assertEquals("Both test cases should be equal", tc, oldTc);
    }

    @Test
    public void invalidParameterValueTest() {
        OpenAPISpecification spec = new OpenAPISpecification("src/test/resources/Comments/swagger_forTestSuite.yaml");
        TestCase tc = new TestCase("dfgsdfg", true, "getComment", "/comments/{id}", HttpMethod.GET);
        tc.addPathParameter("id", "c1");

        TestCase oldTc = SerializationUtils.clone(tc);
        assertTrue("The test case should be mutated", InvalidParameterValue.mutate(tc, spec.getSpecification().getPath("/comments/{id}").getGet()));
        assertTrue("The length of the mutated 'id' parameter should be greater than 4", tc.getPathParameters().get("id").length() > 4);
        assertNotEquals("The two test cases should be different", tc, oldTc);
    }

    @Test
    public void invalidParameterValueNotPossibleTest() {
        OpenAPISpecification spec = new OpenAPISpecification("src/test/resources/Comments/swagger.yaml");
        TestCase tc = new TestCase("dfgsdfg", true, "putComment", "/comments", HttpMethod.PUT);

        TestCase oldTc = SerializationUtils.clone(tc);
        assertFalse("The test case should NOT be mutated", InvalidParameterValue.mutate(tc, spec.getSpecification().getPath("/comments").getPut()));
        assertEquals("Both test cases should be equal", tc, oldTc);
    }
}