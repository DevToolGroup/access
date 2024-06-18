package group.devtool.access.engine;

/**
 * 可访问内容
 */
public interface View {

  /**
   * @return 匹配条件
   */
  public Condition condition();

  /**
   * 修剪视图
   * 
   * @param <T>
   * @param response
   * @throws AccessControlException
   */
  public <T> void clip(T response) throws AccessControlException;

}
