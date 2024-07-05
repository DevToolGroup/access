/*
 * The Access Access Control Engine, 
 * unlike the RBAC model that utilizes static data to implement access control, 
 * realizes API access control by evaluating if dynamic data satisfies the access control rules.
 *
 * License: GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 * See the license.txt file in the root directory or see <http://www.gnu.org/licenses/>.
 */
package group.devtool.access.engine;

public class StatementImpl implements Statement {

  private String entity;

  private String column;

  private Object[] parameters;

  public StatementImpl(String entity, String column, Object... parameters) {
    this.entity = entity;
    this.column = column;
    this.parameters = parameters;
  }

  @Override
  public String entity() {
    return entity;
  }

  @Override
  public String column() {
    return column;
  }

  @Override
  public Object[] parameters() {
    return parameters;
  }

}
