package group.devtool.access.engine;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import group.devtool.access.engine.entity.TestAccessControlMetaData;

public class ConditionImplTest {
  
  @Test
  public void testLiteralExpression() throws ExpressionException, AccessControlException {
    String expression1 = "\"F\" == \"F\"";
    Condition condition = new ConditionImpl(expression1);
    boolean value1 = condition.match(null);
    assertTrue(value1);

    String expression2 = "\"F\" == \"M\"";
    Condition condition2 = new ConditionImpl(expression2);
    boolean value2 = condition2.match(null);
    assertFalse(value2);

    String expression3 = "1 == 1";
    Condition condition3 = new ConditionImpl(expression3);
    boolean value3 = condition3.match(null);
    assertTrue(value3);

    String expression4 = "1 != 1";
    Condition condition4 = new ConditionImpl(expression4);
    boolean value4 = condition4.match(null);
    assertFalse(value4);
  }

  @Test
  public void testSimpleExpression() throws ExpressionException, AccessControlException {
    String expression1 = "\"@(/married)\"";
    Condition condition1 = new ConditionImpl(expression1);
    boolean value1 = condition1.match(new TestAccessControlMetaData(6, "F", true));
    assertTrue(value1);

    boolean value2 = condition1.match(new TestAccessControlMetaData(6, "M", false));
    assertFalse(value2);
  }

  @Test
  public void testGtExpression() throws ExpressionException, AccessControlException {
    String expression1 = "\"@(/age)\" > 5";
    Condition condition1 = new ConditionImpl(expression1);
    boolean value1 = condition1.match(new TestAccessControlMetaData(6, "F"));
    assertTrue(value1);

    boolean value2 = condition1.match(new TestAccessControlMetaData(3, "F"));
    assertFalse(value2);
  }

  @Test
  public void testAndExpression() throws ExpressionException, AccessControlException {
    String expression1 = "\"@(/age)\" > 5 && \"@(/gender)\" == \"F\"";
    Condition condition1 = new ConditionImpl(expression1);
    boolean value1 = condition1.match(new TestAccessControlMetaData(6, "F"));
    assertTrue(value1);

    boolean value2 = condition1.match(new TestAccessControlMetaData(6, "M"));
    assertFalse(value2);
  }

  @Test
  public void testOrExpression() throws ExpressionException, AccessControlException {
    String expression1 = "\"@(/age)\" > 5 || \"@(/gender)\" == \"F\"";
    Condition condition1 = new ConditionImpl(expression1);
    boolean value1 = condition1.match(new TestAccessControlMetaData(6, "F"));
    assertTrue(value1);

    boolean value2 = condition1.match(new TestAccessControlMetaData(6, "M"));
    assertTrue(value2);
  }

  @Test
  public void testNotExpression() throws ExpressionException, AccessControlException {
    String expression1 = "!\"@(/married)\"";
    Condition condition1 = new ConditionImpl(expression1);
    boolean value1 = condition1.match(new TestAccessControlMetaData(6, "F", false));
    assertTrue(value1);
    boolean value2 = condition1.match(new TestAccessControlMetaData(6, "F", true));
    assertFalse(value2);
  }

  @Test
  public void testComplexExpression() throws ExpressionException, AccessControlException {
    String expression1 = "\"@(/married)\" || \"@(/age)\" > 5 && \"@(/gender)\" == \"F\"";
    Condition condition1 = new ConditionImpl(expression1);
    boolean value1 = condition1.match(new TestAccessControlMetaData(6, "F", false));
    assertTrue(value1);
    boolean value2 = condition1.match(new TestAccessControlMetaData(6, "M", true));
    assertTrue(value2);

    String expression2 = "!\"@(/married)\" || \"@(/age)\" > 5 && \"@(/gender)\" == \"F\"";
    Condition condition2 = new ConditionImpl(expression2);
    boolean value21 = condition2.match(new TestAccessControlMetaData(6, "M", false));
    assertTrue(value21);
  }

  @Test
  public void testComplexSubExpression() throws ExpressionException, AccessControlException {
    String expression1 = "\"@(/married)\" || (\"@(/age)\" > 5 && \"@(/gender)\" == \"F\")";
    Condition condition1 = new ConditionImpl(expression1);
    boolean value1 = condition1.match(new TestAccessControlMetaData(6, "F", false));
    assertTrue(value1);
    boolean value2 = condition1.match(new TestAccessControlMetaData(6, "M", false));
    assertFalse(value2);

    String expression2 = "(!\"@(/married)\" || \"@(/age)\" > 5) && \"@(/gender)\" == \"F\"";
    ConditionImpl condition2 = new ConditionImpl(expression2);
    boolean value21 = condition2.match(new TestAccessControlMetaData(6, "M", false));
    assertFalse(value21);

    String expression3 = "(!\"@(/married)\" && \"@(/age)\" < 35 && \"@(/age)\" > 20 ) || (\"@(/married)\" && \"@(/gender)\" == \"F\")";
    ConditionImpl condition3 = new ConditionImpl(expression3);
    boolean value31 = condition3.match(new TestAccessControlMetaData(6, "F", true));
    assertTrue(value31);

    boolean value32 = condition3.match(new TestAccessControlMetaData(22, "F", false));
    assertTrue(value32);

    boolean value33 = condition3.match(new TestAccessControlMetaData(35, "F", false));
    assertFalse(value33);

    String expression4 = "(!\"@(/married)\" && \"@(/age)\" < 35 && \"@(/age)\" > 20 ) || !(\"@(/married)\" && \"@(/gender)\" == \"F\")";
    ConditionImpl condition4 = new ConditionImpl(expression4);
    boolean value41 = condition4.match(new TestAccessControlMetaData(35, "F", true));
    assertFalse(value41);

    boolean value42 = condition4.match(new TestAccessControlMetaData(22, "F", false));
    assertTrue(value42);

    boolean value43 = condition4.match(new TestAccessControlMetaData(35, "F", false));
    assertTrue(value43);
  }
}
