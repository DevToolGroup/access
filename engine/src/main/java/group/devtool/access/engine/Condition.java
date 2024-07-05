/*
 * The Access Access Control Engine, 
 * unlike the RBAC model that utilizes static data to implement access control, 
 * realizes API access control by evaluating if dynamic data satisfies the access control rules.
 *
 * License: GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 * See the license.txt file in the root directory or see <http://www.gnu.org/licenses/>.
 */
package group.devtool.access.engine;

import java.io.Serializable;

public interface Condition extends Serializable {

  /**
   * 判断是否匹配元数据
   * 
   * @param meta 接口元数据
   * @return true-匹配，false-不匹配
   * @throws AccessControlException 
   * @throws ExpressionException 
   */
  public boolean match(MetaData meta) throws AccessControlException, ExpressionException;
  
}
