package group.devtool.access.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import group.devtool.access.engine.Path.State;

/**
 * 可访问内容
 */
public class ViewImpl implements View {

  private Condition condition;

  private List<String> paths;

  public ViewImpl(String expression, List<String> paths) throws ExpressionException {
    this.condition = new ConditionImpl(expression);
    this.paths = paths;
  }

  /**
   * 修剪视图
   * 
   * @param <T>
   * @param response
   * @throws AccessControlException
   */
  public <T> void clip(T response) throws AccessControlException {
    if (response instanceof Collection) {
      Collection<?> collection = (Collection<?>)response;
      doClipCollection(collection);
    } else if (response instanceof Map) {
      Map<?, ?> map = (Map<?, ?>)response;
      doClipMap(map);
    } else {
      doClip(response);
    }
  }

  private <T> void doClip(T response) throws AccessControlException {
    List<Path> replacePaths = doResolve(response);
    for (Path replacePath : replacePaths) {
      replacePath.replace(response);
    }
  }

  private void doClipMap(Map<?, ?> map) throws AccessControlException {
    for (Object value: map.values()) {
      doClip(value);
    }
  }

  private void doClipCollection(Collection<?> collection) throws AccessControlException {
    for (Object value: collection) {
      doClip(value);
    }
  }

  private List<Path> doResolve(Object object) throws AccessControlException {
    State state = Path.state();
    List<Path> result = new ArrayList<>();
    for (String path : paths) {
      result.add(Path.of(path, object, state));
    }
    return result;
  }

  @Override
  public Condition condition() {
    return condition;
  }

}
