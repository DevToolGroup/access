package group.devtool.access.engine;

/**
 * 基于规则的动态访问控制
 * 
 * <code>
 * public class Controller {
 * 
 * public ResponseEntity<String> get(Request request) {
 * return AccessControlService.control(
 * ()-> {return attribute},
 * (ctx) -> { return get(request);}
 * )
 * }
 * }
 * <code>
 */
public interface AccessControlService {

  public final static AccessControlService defaultService = new AccessControlServiceImpl();

  /**
   * 根据定义的动态访问控制元数据，实现对接口的访问控制
   * 
   * @param <T>       返回类型
   * @param supplier  访问控制元数据初始化
   * @param operation 业务操作
   * @return 实际业务响应
   * @throws AccessControlException  访问控制限制异常
   * @throws JsonProcessingException 序列化异常
   * @throws ExpressionException
   * @throws Exception
   */
  public <T> T doControl(AccessMetaDataSupplier supplier, AccessControlOperation<T> operation)
      throws Exception;

  public static <T> T control(AccessMetaDataSupplier supplier, AccessControlOperation<T> operation)
      throws Exception {
    return defaultService.doControl(supplier, operation);
  }

  /**
   * 业务实际操作接口
   */
  public interface AccessControlOperation<T> {

    /**
     * 根据实际情况获取当前请求对数据可视范围的约束要求，并应用于后续的查询
     * 
     * @param builder 消费查询条件构造器
     * @return 业务查询结果
     */
    public T run(AccessSpecificationBuilder builder) throws Exception;

  }

  /**
   * 元数据提供接口
   */
  public interface AccessMetaDataSupplier {

    /**
     * @return 访问控制元数据
     * @throws Exception 元数据构造异常
     */
    public MetaData get() throws Exception;

  }

  /**
   * 查询限制条件构造器
   */
  public interface AccessSpecificationBuilder {

    /**
     * 根据配置参数构造查询限制条件
     * 
     * @throws Exception 查询条件构造异常
     */
    public void apply(Specification specification) throws Exception;

  }
}
