package group.devtool.access.engine;

/**
 * {@link Privilege} 实现类
 */
public class PrivilegeImpl implements Privilege {

  private Condition condition;

  public PrivilegeImpl(String expression) throws ExpressionException {
    condition = new ConditionImpl(expression);
  }

  public PrivilegeImpl(Condition condition) {
    this.condition = condition;
  }

  @Override
  public Condition condition() {
    return condition;
  }

}
