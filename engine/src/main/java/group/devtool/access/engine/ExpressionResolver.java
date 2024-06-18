package group.devtool.access.engine;

/**
 * 访问权限表达式解析接口
 */
public interface ExpressionResolver {

  /**
   * 解析权限表达式字符串，返回表达式
   * 
   * @param expression 权限表达式
   * @return 权限表达式
   * @throws ExpressionException 表达式异常
   */
  Expression resolve(String expression) throws ExpressionException;

}
