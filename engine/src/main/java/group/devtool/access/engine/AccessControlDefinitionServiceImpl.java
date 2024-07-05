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
 * {@link AccessControlDefinitionService} 实现类
 */
public class AccessControlDefinitionServiceImpl implements AccessControlDefinitionService {

  public AccessControlDefinitionServiceImpl() {

  }

  @Override
  public AccessControlDefinition load(MetaData metadata) {
    throw new UnsupportedOperationException("Unimplemented method 'load'");
  }

}
