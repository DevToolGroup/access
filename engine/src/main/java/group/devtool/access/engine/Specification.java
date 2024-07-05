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
 * 查询范围约束条件
 */
public interface Specification {

  public Specification and(Specification... specifications);

  public Specification or(Specification... specifications);

  public Specification isNull(Statement statement);

  public Specification isNotNull(Statement statement);

  public Specification equal(Statement statement);

  public Specification notEqual(Statement statement);

  public Specification between(Statement statement);

  public Specification gt(Statement statement);

  public Specification ge(Statement statement);

  public Specification lt(Statement statement);

  public Specification le(Statement statement);

  public Specification leftLike(Statement statement);

  public Specification rightLike(Statement statement);

  public Specification like(Statement statement);

  public Specification notLike(Statement statement);

  public Specification in(Statement statement);
  
  public Specification notIn(Statement statement);

}
