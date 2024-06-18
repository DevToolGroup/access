package group.devtool.access.engine;

/**
 * 访问权限
 */
public interface Privilege {

  /**
   * @return 匹配条件
   */
  Condition condition();

}
