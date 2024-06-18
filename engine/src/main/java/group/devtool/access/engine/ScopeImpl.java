package group.devtool.access.engine;

/**
 * 访问控制查询范围
 */
public class ScopeImpl implements Scope {

  private Condition condition;

  private SQL sql;

  public ScopeImpl(String expression, SQL sql) throws ExpressionException {
    this.condition = new ConditionImpl(expression);
    this.sql = sql;
  }

  @Override
  public Condition condition() {
    return condition;
  }

  @Override
  public void specification(Specification specification) {
    doSpecification(sql, specification);
  }

  private Specification doSpecification(SQL element, Specification specification) {
    switch (element.op()) {
      case AND:
        return specification.and(doSpecification(element.left(), specification),
            doSpecification(element.right(), specification));
      case OR:
        return specification.or(doSpecification(element.left(), specification),
            doSpecification(element.right(), specification));
      case GE:
        return specification.ge(sql.getStatement());
      case GT:
        return specification.gt(sql.getStatement());
      case LT:
        return specification.lt(sql.getStatement());
      case LE:
        return specification.le(sql.getStatement());
      case LIKE:
        return specification.like(sql.getStatement());
      case NOTLIKE:
        return specification.notLike(sql.getStatement());
      case LEFTLIKE:
        return specification.leftLike(sql.getStatement());
      case RIGHTLIKE:
        return specification.rightLike(sql.getStatement());
      case EQ:
        return specification.equal(sql.getStatement());
      case NE:
        return specification.notEqual(sql.getStatement());
      case BETWEEN:
        return specification.between(sql.getStatement());
      default:
        throw new UnsupportedOperationException("暂不支持当前");
    }
  }

  public static enum OP {
    AND,
    OR,
    GE,
    GT,
    LE,
    LT,
    EQ,
    NE,
    LIKE,
    NOTLIKE,
    LEFTLIKE,
    RIGHTLIKE,
    IN,
    NOTIN,
    BETWEEN,
    ;

  }

  public static interface SQL {

    OP op();

    SQL left();

    SQL right();

    Statement getStatement();

  }

  public static class SQLImpl implements SQL {

    private OP op;

    private SQL left;

    private SQL right;

    private String entity;

    private String column;

    private Object[] parameters;

    public SQLImpl(OP op, String entity, String column, Object... parameters) {
      this.op = op;
      this.entity = entity;
      this.column = column;
      this.parameters = parameters;
    }

    public SQLImpl(OP op, SQL left, SQL right) {
      this.op = op;
      this.left = left;
      this.right = right;
    }

    @Override
    public OP op() {
      return op;
    }

    @Override
    public SQL left() {
      return left;
    }

    @Override
    public SQL right() {
      return right;
    }

    @Override
    public Statement getStatement() {
      return new StatementImpl(entity, column, parameters);
    }

  }

}
