package group.devtool.access.engine;

/**
 * 访问控制规则定义服务
 */
public interface AccessControlDefinitionService {

  /**
   * 根据元数据加载访问控制规则定义
   * 
   * @param metadata 元数据
   * @return 元数据定义
   */
  AccessControlDefinition load(MetaData metadata);

}
