package group.devtool.access.engine;

public class NoPrivilegeException extends AccessControlException {

  public NoPrivilegeException() {
    super("暂无访问权限");
  }

}
