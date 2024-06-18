package group.devtool.access.engine;

public enum OperationProvider {

  NOT("!", new Operation.NOT()),
  AND("&&", new Operation.AND()),
  OR("||", new Operation.OR()),
  GT(">", new Operation.GT()),
  GE(">=", new Operation.GE()),
  LT("<", new Operation.LT()),
  LE("<=", new Operation.LE()),
  EQ("==", new Operation.EQ()),
  NE("!=", new Operation.NE()),
  IN("~", new Operation.IN()),
  BG("^", new Operation.BG()),
  EG("$", new Operation.EG()),
  ;

  private String name;

  private Operation operation;

  private OperationProvider(String name, Operation operation) {
    this.name = name;
    this.operation = operation;
  }

  public Operation getOperation() {
    return operation;
  }

  public static Operation getOperation(String operation) throws ExpressionResolveException {
    OperationProvider[] values = values();
    for (OperationProvider provider : values) {
      if (provider.name.equals(operation)) {
        return provider.operation;
      }
    }
    throw new ExpressionResolveException("暂不支持当前操作类型，操作：" + operation);
  }
}
