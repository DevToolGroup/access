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
 * 访问控制查询范围
 */
public interface Scope {

  /**
   * @return 匹配条件
   */
  Condition getCondition();

  /**
   * @return 查询约束条件
   */
  void specification(Specification specification);


}
