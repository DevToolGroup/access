package group.devtool.access.engine;

import java.io.Serializable;
import java.util.Arrays;

import group.devtool.access.engine.Operation.NOT;

/**
 * 访问权限条件
 */
public interface Expression extends Serializable {

  /**
   * 判断条件是否满足
   * 
   * @param resolve 引用值解析器
   * @return true: 满足条件，false: 不满足条件
   * @throws AccessControlException
   */
  Object getValue(ReferenceResolver resolve) throws ExpressionException;

  /**
   * 条件引用解析接口
   */
  public interface ReferenceResolver {

    public Object resolve(String path) throws Exception;

  }

  public static class ReferExpression implements Expression {

    private String reference;

    public ReferExpression(String reference) {
      this.reference = reference;
    }

    @Override
    public Object getValue(ReferenceResolver resolve) throws ExpressionException {
      if (reference.startsWith("\"@(") && reference.endsWith(")\"")) {
        try {
          return resolve.resolve(reference.substring(3, reference.length() - 2));
        } catch (Exception e) {
          throw new ExpressionOperateException("表达式引用变量取值异常");
        }
      }
      if (!isCharacter(reference)) {
        return reference;
      }
      String value = new String(reference.substring(1, reference.length() - 1));
      String[] arrays = value.split(",");
      if (arrays.length == 1) {
        return arrays[0];
      }
      return Arrays.asList(arrays);
    }

    private boolean isCharacter(String reference) {
      return reference.startsWith("\"") && reference.endsWith("\"");
    }
  }

  public static class LogicExpression implements Expression {

    private Expression left;

    private Expression right;

    private Operation operate;

    public LogicExpression(Expression left, Expression right,
        Operation operate) {
      this.left = left;
      this.right = right;
      this.operate = operate;
    }

    @Override
    public Object getValue(ReferenceResolver resolve) throws ExpressionException {
      if (null == left) {
        throw new ExpressionOperateException("表达式逻辑计算异常，缺少必要的参数");
      }
      if (operate instanceof NOT) {
        return operate.operate(left.getValue(resolve), null);
      } else if (null != right) {
        return operate.operate(left.getValue(resolve), right.getValue(resolve));
      } else {
        throw new ExpressionOperateException("表达式逻辑计算异常，缺少必要的参数");
      }
    }

  }
}
