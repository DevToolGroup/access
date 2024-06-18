package group.devtool.access.engine;

public class UnexpectedExpressionResultExpression extends AccessControlException {

  public UnexpectedExpressionResultExpression() {
    super("访问权限表达式返回结果不符合预期，预期类型为Boolean");
  }

}
