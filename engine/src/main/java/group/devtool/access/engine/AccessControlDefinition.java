package group.devtool.access.engine;

import java.util.List;

/**
 * 访问控制权限
 */
public interface AccessControlDefinition {

  /**
   * @return 访问控制权限
   */
  List<Privilege> getPrivileges();

  /**
   * @return 访问范围控制
   */
  List<Scope> getScopes();

  /**
   * @return 访问内容限制
   */
  List<View> getFields();

  
}
