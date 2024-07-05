/*
 * The Access Access Control Engine, 
 * unlike the RBAC model that utilizes static data to implement access control, 
 * realizes API access control by evaluating if dynamic data satisfies the access control rules.
 *
 * License: GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 * See the license.txt file in the root directory or see <http://www.gnu.org/licenses/>.
 */
package group.devtool.access.engine;

public class UnexpectedExpressionResultExpression extends AccessControlException {

  public UnexpectedExpressionResultExpression() {
    super("访问权限表达式返回结果不符合预期，预期类型为Boolean");
  }

}
