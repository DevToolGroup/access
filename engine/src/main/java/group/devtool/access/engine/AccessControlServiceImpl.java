/*
 * The Access Access Control Engine, 
 * unlike the RBAC model that utilizes static data to implement access control, 
 * realizes API access control by evaluating if dynamic data satisfies the access control rules.
 *
 * License: GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 * See the license.txt file in the root directory or see <http://www.gnu.org/licenses/>.
 */
package group.devtool.access.engine;

import java.util.List;

/**
 * {@link AccessControlService} 实现类
 */
public class AccessControlServiceImpl implements AccessControlService {

  private AccessControlDefinitionService definitionService;

  public AccessControlServiceImpl() {
    this.definitionService = new AccessControlDefinitionServiceImpl();
  }

  public AccessControlServiceImpl(AccessControlDefinitionService definitionService) {
    this.definitionService = definitionService;
  }

  @Override
  public <T> T doControl(AccessMetaDataSupplier supplier,
      AccessControlOperation<T> operation) throws Exception {
    MetaData metadata = supplier.get();
    AccessControlDefinition definition = definitionService.load(metadata);
    // 访问限制
    if (!accessible(definition.getPrivileges(), metadata)) {
      throw new NoPrivilegeException();
    }
    // 范围限制
    T response = operation.run((specification) -> {
      Scope scope = findScope(metadata, definition.getScopes());
      if (null != scope) {
        scope.specification(specification);
      }
    });
    // 内容限制
    clipResponse(response, metadata, definition.getFields());
    return response;
  }

  private Scope findScope(MetaData metadata, List<Scope> scopes)
      throws ExpressionException, AccessControlException {
    for (Scope scope : scopes) {
      if (scope.condition().match(metadata)) {
        return scope;
      }
    }
    return null;
  }

  private boolean accessible(List<Privilege> privileges, MetaData metadata)
      throws AccessControlException, ExpressionException {
    for (Privilege privilege : privileges) {
      if (privilege.condition().match(metadata)) {
        return true;
      }
    }
    return false;
  }

  private <T> void clipResponse(T response, MetaData metadata, List<View> views) throws AccessControlException, ExpressionException {
    View fit = null;
    for (View view: views) {
      if (view.condition().match(metadata)) {
        fit = view;
      }
    }
    if (null == fit) {
      return;
    }
    fit.clip(response);
  }
}
