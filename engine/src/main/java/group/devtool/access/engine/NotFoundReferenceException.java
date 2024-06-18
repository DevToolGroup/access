package group.devtool.access.engine;

public class NotFoundReferenceException extends AccessControlException {

  public NotFoundReferenceException(String path) {
    super("引用变量不存在，变量名：" + path);
  }

}
