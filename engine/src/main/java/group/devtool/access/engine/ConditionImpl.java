/*
 * The Access Access Control Engine, 
 * unlike the RBAC model that utilizes static data to implement access control, 
 * realizes API access control by evaluating if dynamic data satisfies the access control rules.
 *
 * License: GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 * See the license.txt file in the root directory or see <http://www.gnu.org/licenses/>.
 */
package group.devtool.access.engine;

import group.devtool.access.engine.Path.State;

/**
 * {@link Condition} 实现类
 */
public class ConditionImpl implements Condition {

  private Expression expression;

  public ConditionImpl(Expression expression) {
    this.expression = expression;
  }

  public ConditionImpl(String expression) throws ExpressionException {
    this.expression = new ExpressionResolverImpl().resolve(expression);
  }

  @Override
  public boolean match(MetaData meta) throws AccessControlException, ExpressionException {
    if (null == expression) {
      return false;
    }
    State state = Path.state();
    Object result = expression.getValue((path) -> {
      Path po = Path.of(path, meta, state);
      return po.get(meta);
    });
    if (!(result instanceof Boolean)) {
      throw new UnexpectedExpressionResultExpression();
    }
    return (Boolean) result;
  }

}
