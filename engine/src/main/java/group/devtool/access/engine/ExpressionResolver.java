/*
 * The Access Access Control Engine, 
 * unlike the RBAC model that utilizes static data to implement access control, 
 * realizes API access control by evaluating if dynamic data satisfies the access control rules.
 *
 * License: GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 * See the license.txt file in the root directory or see <http://www.gnu.org/licenses/>.
 */
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
