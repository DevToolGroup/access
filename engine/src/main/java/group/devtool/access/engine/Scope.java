package group.devtool.access.engine;

/**
 * 访问控制查询范围
 */
public interface Scope {

  /**
   * @return 匹配条件
   */
  Condition condition();

  /**
   * @return 查询约束条件
   */
  void specification(Specification specification);


}
