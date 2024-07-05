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
 * 访问控制规则定义服务
 */
public interface AccessControlDefinitionService {

  /**
   * 根据元数据加载访问控制规则定义
   * 
   * @param metadata 元数据
   * @return 元数据定义
   */
  AccessControlDefinition load(MetaData metadata);

}
