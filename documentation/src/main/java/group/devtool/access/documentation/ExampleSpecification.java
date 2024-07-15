/*
 * The Access Access Control Engine, 
 * unlike the RBAC model that utilizes static data to implement access control, 
 * realizes API access control by evaluating if dynamic data satisfies the access control rules.
 *
 * License: GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 * See the license.txt file in the root directory or see <http://www.gnu.org/licenses/>.
 */
package group.devtool.access.documentation;

import group.devtool.access.documentation.entity.ExampleAccessControlEntity;
import group.devtool.access.engine.Specification;
import group.devtool.access.engine.Statement;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class ExampleSpecification implements Specification {

  private Root<ExampleAccessControlEntity> root;

  private CriteriaBuilder builder;

  private Predicate predicate;

  public ExampleSpecification(CriteriaBuilder builder, Root<ExampleAccessControlEntity> root) {
    this.builder = builder;
    this.root = root;
  }

  @Override
  public Specification and(Specification... specifications) {
    Predicate[] predicates = new Predicate[specifications.length];
    for (int i = 0; i < specifications.length; i++) {
      Specification specification = specifications[i];
      predicates[i] = ((ExampleSpecification) specification).getPredicate();
    }
    predicate = builder.and(predicates);
    return this;
  }

  @Override
  public Specification or(Specification... specifications) {
    Predicate[] predicates = new Predicate[specifications.length];
    for (int i = 0; i < specifications.length; i++) {
      Specification specification = specifications[i];
      predicates[i] = ((ExampleSpecification) specification).getPredicate();
    }
    predicate = builder.and(predicates);
    return this;
  }

  @Override
  public Specification isNull(Statement statement) {
    predicate = builder.and(builder.isNull(root.get(statement.column())));
    return this;
  }

  @Override
  public Specification isNotNull(Statement statement) {
    predicate = builder.isNotNull(root.get(statement.column()));
    return this;
  }

  @Override
  public Specification equal(Statement statement) {
    predicate = builder.equal(root.get(statement.column()), statement.parameters()[0]);
    return this;
  }

  @Override
  public Specification notEqual(Statement statement) {
    predicate = builder.notEqual(root.get(statement.column()), statement.parameters()[0]);
    return this;
  }

  @Override
  public Specification gt(Statement statement) {
    predicate = builder.gt(root.get(statement.column()), (Number) statement.parameters()[0]);
    return this;
  }

  @Override
  public Specification ge(Statement statement) {
    predicate = builder.ge(root.get(statement.column()), (Number) statement.parameters()[0]);
    return this;
  }

  @Override
  public Specification lt(Statement statement) {
    predicate = builder.lt(root.get(statement.column()), (Number) statement.parameters()[0]);
    return this;
  }

  @Override
  public Specification le(Statement statement) {
    predicate = builder.le(root.get(statement.column()), (Number) statement.parameters()[0]);
    return this;
  }

  @Override
  public Specification leftLike(Statement statement) {
    predicate = builder.like(root.get(statement.column()), (String) statement.parameters()[0] + "%");
    return this;
  }

  @Override
  public Specification rightLike(Statement statement) {
    predicate = builder.like(root.get(statement.column()), "%" + (String) statement.parameters()[0]);
    return this;
  }

  @Override
  public Specification like(Statement statement) {
    predicate = builder.like(root.get(statement.column()), "%" + (String) statement.parameters()[0] + "%");
    return this;
  }

  @Override
  public Specification notLike(Statement statement) {
    predicate = builder.notLike(root.get(statement.column()), "%" + (String) statement.parameters()[0] + "%");
    return this;
  }

  @Override
  public Specification in(Statement statement) {
    predicate = root.get(statement.column()).in(statement.parameters());
    return this;
  }

  @Override
  public Specification notIn(Statement statement) {
    predicate = root.get(statement.column()).in(statement.parameters());
    return this;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public Specification between(Statement statement) {
    builder.between(root.get(statement.column()), (Comparable) statement.parameters()[0],
        (Comparable) statement.parameters()[1]);
    return this;
  }

  public Predicate getPredicate() {
    return predicate;
  }
}
