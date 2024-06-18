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
