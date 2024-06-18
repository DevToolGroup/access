package group.devtool.access.engine;

import java.io.Serializable;

public interface Condition extends Serializable {

  /**
   * 判断是否匹配元数据
   * 
   * @param meta 接口元数据
   * @return true-匹配，false-不匹配
   * @throws AccessControlException 
   * @throws ExpressionException 
   */
  public boolean match(MetaData meta) throws AccessControlException, ExpressionException;
  
}
