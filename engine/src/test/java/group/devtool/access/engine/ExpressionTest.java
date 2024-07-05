/*
 * The Access Access Control Engine, 
 * unlike the RBAC model that utilizes static data to implement access control, 
 * realizes API access control by evaluating if dynamic data satisfies the access control rules.
 *
 * License: GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 * See the license.txt file in the root directory or see <http://www.gnu.org/licenses/>.
 */
package group.devtool.access.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import group.devtool.access.engine.Expression.LogicExpression;
import group.devtool.access.engine.Expression.ReferExpression;

public class ExpressionTest {

  @Test
  public void testReference() {
    ReferExpression literal = new Expression.ReferExpression("\"@(/name)\"");
    try {
      Object result = literal.getValue((path) -> {
        return path;
      });
      assertTrue(result instanceof String);

      assertEquals("/name", result);;
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testLiteral() {
    ReferExpression literal = new Expression.ReferExpression("\"name\"");
    try {
      Object result = literal.getValue((path) -> {
        return path;
      });
      assertTrue(result instanceof String);

      assertEquals("name", result);;
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGtIsTrue() {
    ReferExpression left = new Expression.ReferExpression("6");
    ReferExpression right = new Expression.ReferExpression("5");
    LogicExpression expression = new Expression.LogicExpression(left, right, OperationProvider.GT.getOperation());
    try {
      Object value = expression.getValue((path) -> {
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertTrue((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGtIsFalse() {
    ReferExpression left = new Expression.ReferExpression("5");
    ReferExpression right = new Expression.ReferExpression("6");
    LogicExpression expression = new Expression.LogicExpression(left, right, OperationProvider.GT.getOperation());
    try {
      Object value = expression.getValue((path) -> {
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertFalse((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testLtIsTrue() {
    ReferExpression left = new Expression.ReferExpression("5");
    ReferExpression right = new Expression.ReferExpression("6");
    LogicExpression expression = new Expression.LogicExpression(left, right, OperationProvider.LT.getOperation());
    try {
      Object value = expression.getValue((path) -> {
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertTrue((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testLtIsFalse() {
    ReferExpression left = new Expression.ReferExpression("6");
    ReferExpression right = new Expression.ReferExpression("5");
    LogicExpression expression = new Expression.LogicExpression(left, right, OperationProvider.LT.getOperation());
    try {
      Object value = expression.getValue((path) -> {
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertFalse((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGeIsTrue() {
    ReferExpression left = new Expression.ReferExpression("6");
    ReferExpression right = new Expression.ReferExpression("5");
    LogicExpression expression = new Expression.LogicExpression(left, right, OperationProvider.GE.getOperation());
    try {
      Object value = expression.getValue((path) -> {
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertTrue((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }

    ReferExpression eqLeft = new Expression.ReferExpression("6");
    ReferExpression eqRight = new Expression.ReferExpression("5");
    LogicExpression expression2 = new Expression.LogicExpression(eqLeft, eqRight, OperationProvider.GE.getOperation());
    try {
      Object value = expression2.getValue((path) -> {
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertTrue((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testGeIsFalse() {
    ReferExpression left = new Expression.ReferExpression("5");
    ReferExpression right = new Expression.ReferExpression("6");
    LogicExpression expression = new Expression.LogicExpression(left, right, OperationProvider.GE.getOperation());
    try {
      Object value = expression.getValue((path) -> {
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertFalse((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testLeIsTrue() {
    ReferExpression left = new Expression.ReferExpression("5");
    ReferExpression right = new Expression.ReferExpression("6");
    LogicExpression expression = new Expression.LogicExpression(left, right, OperationProvider.LE.getOperation());
    try {
      Object value = expression.getValue((path) -> {
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertTrue((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }

    ReferExpression eqLeft = new Expression.ReferExpression("5");
    ReferExpression eqRight = new Expression.ReferExpression("6");
    LogicExpression expression2 = new Expression.LogicExpression(eqLeft, eqRight, OperationProvider.LE.getOperation());
    try {
      Object value = expression2.getValue((path) -> {
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertTrue((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testLeIsFalse() {
    ReferExpression left = new Expression.ReferExpression("5");
    ReferExpression right = new Expression.ReferExpression("6");
    LogicExpression expression = new Expression.LogicExpression(left, right, OperationProvider.GE.getOperation());
    try {
      Object value = expression.getValue((path) -> {
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertFalse((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testEqIsTrue() {
    ReferExpression left = new Expression.ReferExpression("5");
    ReferExpression right = new Expression.ReferExpression("5");
    LogicExpression expression = new Expression.LogicExpression(left, right, OperationProvider.EQ.getOperation());
    try {
      Object value = expression.getValue((path) -> {
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertTrue((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }

  }

  @Test
  public void testEqIsFalse() {
    ReferExpression left = new Expression.ReferExpression("5");
    ReferExpression right = new Expression.ReferExpression("6");
    LogicExpression expression = new Expression.LogicExpression(left, right, OperationProvider.EQ.getOperation());
    try {
      Object value = expression.getValue((path) -> {
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertFalse((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testNeIsTrue() {
    ReferExpression left = new Expression.ReferExpression("4");
    ReferExpression right = new Expression.ReferExpression("5");
    LogicExpression expression = new Expression.LogicExpression(left, right, OperationProvider.NE.getOperation());
    try {
      Object value = expression.getValue((path) -> {
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertTrue((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }

  }

  @Test
  public void testNeIsFalse() {
    ReferExpression left = new Expression.ReferExpression("5");
    ReferExpression right = new Expression.ReferExpression("5");
    LogicExpression expression = new Expression.LogicExpression(left, right, OperationProvider.NE.getOperation());
    try {
      Object value = expression.getValue((path) -> {
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertFalse((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testBgIsTrue() {
    ReferExpression left = new Expression.ReferExpression("bg");
    ReferExpression right = new Expression.ReferExpression("b");
    LogicExpression expression = new Expression.LogicExpression(left, right, OperationProvider.BG.getOperation());
    try {
      Object value = expression.getValue((path) -> {
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertTrue((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }

  }

  @Test
  public void testBgIsFalse() {
    ReferExpression left = new Expression.ReferExpression("gb");
    ReferExpression right = new Expression.ReferExpression("b");
    LogicExpression expression = new Expression.LogicExpression(left, right, OperationProvider.BG.getOperation());
    try {
      Object value = expression.getValue((path) -> {
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertFalse((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testEgIsTrue() {
    ReferExpression left = new Expression.ReferExpression("bg");
    ReferExpression right = new Expression.ReferExpression("g");
    LogicExpression expression = new Expression.LogicExpression(left, right, OperationProvider.EG.getOperation());
    try {
      Object value = expression.getValue((path) -> {
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertTrue((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }

  }

  @Test
  public void testEgIsFalse() {
    ReferExpression left = new Expression.ReferExpression("gb");
    ReferExpression right = new Expression.ReferExpression("g");
    LogicExpression expression = new Expression.LogicExpression(left, right, OperationProvider.EG.getOperation());
    try {
      Object value = expression.getValue((path) -> {
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertFalse((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testCharInIsTrue() {
    ReferExpression left = new Expression.ReferExpression("g");
    ReferExpression right = new Expression.ReferExpression("gb");
    LogicExpression expression = new Expression.LogicExpression(left, right, OperationProvider.IN.getOperation());
    try {
      Object value = expression.getValue((path) -> {
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertTrue((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testCharInIsFalse() {
    ReferExpression left = new Expression.ReferExpression("gb");
    ReferExpression right = new Expression.ReferExpression("a");
    LogicExpression expression = new Expression.LogicExpression(left, right, OperationProvider.IN.getOperation());
    try {
      Object value = expression.getValue((path) -> {
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertFalse((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testListInIsTrue() {
    ReferExpression left = new Expression.ReferExpression("b");
    ReferExpression right = new Expression.ReferExpression("a,b,c");
    LogicExpression expression = new Expression.LogicExpression(left, right, OperationProvider.IN.getOperation());
    try {
      Object value = expression.getValue((path) -> {
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertTrue((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testListInIsFalse() {
    ReferExpression left = new Expression.ReferExpression("d");
    ReferExpression right = new Expression.ReferExpression("a,b,c");
    LogicExpression expression = new Expression.LogicExpression(left, right, OperationProvider.IN.getOperation());
    try {
      Object value = expression.getValue((path) -> {
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertFalse((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testReferListInIsTrue() {
    ReferExpression l2 = new Expression.ReferExpression("b");
    ReferExpression r2 = new Expression.ReferExpression("\"@(/sons)\"");
    LogicExpression e2 = new Expression.LogicExpression(l2, r2, OperationProvider.IN.getOperation());
    try {
      Object value = e2.getValue((path) -> {
        if (path.equals("/sons")) {
          return Arrays.asList("a", "b", "c");
        }
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertTrue((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testReferListInIsFalse() {
    ReferExpression l2 = new Expression.ReferExpression("d");
    ReferExpression r2 = new Expression.ReferExpression("\"@(/sons)\"");
    LogicExpression e2 = new Expression.LogicExpression(l2, r2, OperationProvider.IN.getOperation());
    try {
      Object value = e2.getValue((path) -> {
        if (path.equals("/sons")) {
          return Arrays.asList("a", "b", "c");
        }
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertFalse((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testReferObjectListInIsTrue() {
    ReferExpression l2 = new Expression.ReferExpression("\"@(/son)\"");
    ReferExpression r2 = new Expression.ReferExpression("\"@(/sons)\"");
    LogicExpression e2 = new Expression.LogicExpression(l2, r2, OperationProvider.IN.getOperation());
    try {
      Object value = e2.getValue((path) -> {
        if (path.equals("/sons")) {
          return Arrays.asList(new A("a"), new A("b"), new A("c"));
        }
        if (path.equals("/son")) {
          return new A("a");
        }
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertTrue((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testReferObjectListInIsFalse() {
    ReferExpression l2 = new Expression.ReferExpression("\"@(/son)\"");
    ReferExpression r2 = new Expression.ReferExpression("\"@(/sons)\"");
    LogicExpression e2 = new Expression.LogicExpression(l2, r2, OperationProvider.IN.getOperation());
    try {
      Object value = e2.getValue((path) -> {
        if (path.equals("/sons")) {
          return Arrays.asList(new A("a"), new A("b"), new A("c"));
        }
        if (path.equals("/son")) {
          return new A("d");
        }
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertFalse((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testReferMapInIsTrue() {
    ReferExpression l3 = new Expression.ReferExpression("b");
    ReferExpression r3 = new Expression.ReferExpression("\"@(/sons)\"");
    LogicExpression e3 = new Expression.LogicExpression(l3, r3, OperationProvider.IN.getOperation());
    try {
      Object value = e3.getValue((path) -> {
        if (path.equals("/sons")) {
          Map<String, String> maps = new HashMap<>();
          maps.put("a", "a");
          maps.put("b", "b");
          maps.put("c", "c");
          return maps;
        }
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertTrue((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testReferMapInIsFalse() {
    ReferExpression l3 = new Expression.ReferExpression("d");
    ReferExpression r3 = new Expression.ReferExpression("\"@(/sons)\"");
    LogicExpression e3 = new Expression.LogicExpression(l3, r3, OperationProvider.IN.getOperation());
    try {
      Object value = e3.getValue((path) -> {
        if (path.equals("/sons")) {
          Map<String, String> maps = new HashMap<>();
          maps.put("a", "a");
          maps.put("b", "b");
          maps.put("c", "c");
          return maps;
        }
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertFalse((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }

  }

  @Test
  public void testReferObjectMapInIsTrue() {
    ReferExpression l3 = new Expression.ReferExpression("\"@(/son)\"");
    ReferExpression r3 = new Expression.ReferExpression("\"@(/sons)\"");
    LogicExpression e3 = new Expression.LogicExpression(l3, r3, OperationProvider.IN.getOperation());
    try {
      Object value = e3.getValue((path) -> {
        if (path.equals("/sons")) {
          Map<A, String> maps = new HashMap<>();
          maps.put(new A("a"), "a");
          maps.put(new A("b"), "b");
          maps.put(new A("c"), "c");
          return maps;
        }
        if (path.equals("/son")) {
          return new A("a");
        }
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertTrue((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testReferObjectMapInIsFalse() {
    ReferExpression l3 = new Expression.ReferExpression("\"@(/son)\"");
    ReferExpression r3 = new Expression.ReferExpression("\"@(/sons)\"");
    LogicExpression e3 = new Expression.LogicExpression(l3, r3, OperationProvider.IN.getOperation());
    try {
      Object value = e3.getValue((path) -> {
        if (path.equals("/sons")) {
          Map<A, String> maps = new HashMap<>();
          maps.put(new A("a"), "a");
          maps.put(new A("b"), "b");
          maps.put(new A("c"), "c");
          return maps;
        }
        if (path.equals("/son")) {
          return new A("d");
        }
        return null;
      });
      assertTrue(value instanceof Boolean);
      assertFalse((Boolean) value);
    } catch (ExpressionException e) {
      fail(e.getMessage());
    }

  }

  class A {

    private String name;

    public A() {
      
    }

    public A(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    @Override
    public int hashCode() {
      return name.hashCode();
    }

    @Override
    public boolean equals(Object other) {
      if (!(other instanceof A)) {
        return false;
      }
      return name.equals(((A)other).name);
    }

  }
}
