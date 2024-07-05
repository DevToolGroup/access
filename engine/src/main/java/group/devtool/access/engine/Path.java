/*
 * The Access Access Control Engine, 
 * unlike the RBAC model that utilizes static data to implement access control, 
 * realizes API access control by evaluating if dynamic data satisfies the access control rules.
 *
 * License: GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 * See the license.txt file in the root directory or see <http://www.gnu.org/licenses/>.
 */
package group.devtool.access.engine;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 字段路径，
 */
public interface Path {

  public static final String SEPARATOR = "/";

  /**
   * 获取对象在指定路径上的值。
   * 
   * @param response 目标对象
   * @return 指定路径上的值。
   * @throws PathException 字段路径异常
   */
  public <T> Object get(T response) throws PathException;

  /**
   * 替换对象在指定路径上的值为空。
   * 
   * @param response 目标对象
   * @throws PathException 字段路径异常
   */
  public <T> void replace(T response) throws PathException;

  /**
   * 字段解析状态，用于优化反射获取字段的性能
   */
  static class State {

    private Map<String, Object> values;

    public State() {
      this.values = new HashMap<>();
    }

    public boolean containsKey(String path) {
      return values.containsKey(path);
    }

    public Object get(String path) {
      return values.get(path);
    }

    public void put(String path, Object value) {
      values.put(path, value);
    }

  }

  /**
   * 字段路径基类
   */
  abstract class AbstractPath implements Path {

    private State state;

    private String path;

    private Path parent;

    public AbstractPath(String path, Path parent) {
      this.path = path;
      this.parent = parent;
      this.state = null;
    }

    @Override
    public <T> Object get(T response) throws PathException {
      if (state.containsKey(path)) {
        return state.get(path);
      }
      Object target = response;
      if (null != parent) {
        target = parent.get(response);
      }
      Object value = doGet(target);
      state.put(path, value);
      return value;
    }

    protected abstract Object doGet(Object response) throws PathException;

    @Override
    public <T> void replace(T response) throws PathException {
      Object target = response;
      if (null != parent) {
        target = parent.get(response);
      }
      doReplace(target);
    }

    protected abstract void doReplace(Object target) throws PathException;

    public void initialized(State state) {
      this.state = state;
    }

  }

  /**
   * 字段顶层路径
   */
  static class RootPath extends AbstractPath {

    public RootPath() {
      super(SEPARATOR, null);
    }

    @Override
    protected Object doGet(Object response) {
      return response;
    }

    @Override
    protected void doReplace(Object target) {
      target = null;
    }
  }

  /**
   * 属性路径
   */
  static class PropertyPath extends AbstractPath {

    private Property property;

    private String name;

    public PropertyPath(String path, Path parent, String name, Class<?> declaredClass) throws PathException {
      super(path, parent);
      this.name = name;
      this.property = new Property(name, declaredClass);
    }

    @Override
    protected Object doGet(Object response) throws PathException {
      try {
        return property.read(response);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        throw new PathException("字段[" + name + "]值获取异常，异常信息：" + e.getMessage());
      }
    }

    @Override
    protected void doReplace(Object target) throws PathException {
      try {
        property.write(target, new Object[] { null });
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        throw new PathException("字段[" + name + "]值替换异常，异常信息：" + e.getMessage());
      }
    }

    static class Property {

      private Class<?> type;

      private Method readMethod;

      private Method writeMethod;

      public Property(String name, Class<?> declaredClass) throws PathException {
        try {
          Field field = getField(name, declaredClass);
          if (null == field) {
            throw new PathException("字段不存在，字段名：" + name + "，类名：" + declaredClass.getSimpleName());
          }
          type = field.getClass();
          readMethod = declaredClass.getMethod(getReadMethodName(name, field.getType() == Boolean.TYPE),
              new Class<?>[] {});
          writeMethod = declaredClass.getMethod(getWriteMethodName(name),
              new Class<?>[] { readMethod.getReturnType() });

        } catch (NoSuchMethodException | SecurityException e) {
          throw new PathException("属性 [" + name + "] get/set方法不存在，不符合一般JavaBean规范");
        }
      }

      private Field getField(String name, Class<?> declaredClass) {
        Field field = null;
        Class<?> target = declaredClass;
        do {
          try {
            field = declaredClass.getDeclaredField(name);
          } catch (NoSuchFieldException e) {
            target = declaredClass.getSuperclass();
          } catch (SecurityException e) {
            return field;
          }
        } while (null == field || null == target);
        return field;
      }

      private String getWriteMethodName(String name) {
        return "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
      }

      private String getReadMethodName(String name, boolean useIs) {
        String prefix = useIs ? "is" : "get";
        return prefix + Character.toUpperCase(name.charAt(0)) + name.substring(1);
      }

      public void write(Object target, Object[] objects)
          throws IllegalAccessException, IllegalArgumentException,
          InvocationTargetException, PathException {
        if (type.isPrimitive()) {
          throw new PathException("基本类型的字段不支持替换为NULL");
        }
        writeMethod.invoke(target, objects);
      }

      public Object read(Object target)
          throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return readMethod.invoke(target);
      }

    }

  }

  /**
   * 集合路径
   */
  static class CollectionPath extends AbstractPath {

    private int index;

    public CollectionPath(String path, Path parent, int index) {
      super(path, parent);
      this.index = index;
    }

    @Override
    public void doReplace(Object target) throws PathException {
      if (!(target instanceof Collection)) {
        throw new PathException("参数类型异常，不支持非collection的修改操作");
      }
      Collection<?> collection = (Collection<?>) target;
      Iterator<?> iter = collection.iterator();
      int i = 0;
      while (iter.hasNext()) {
        iter.next();
        if (i == index) {
          iter.remove();
        }
        i += 1;
      }
    }

    @Override
    protected Object doGet(Object target) throws PathException {
      if (!(target instanceof Collection)) {
        throw new PathException("参数类型异常，预期参数类型为Collection");
      }
      Collection<?> collection = (Collection<?>) target;
      int i = 0;
      Iterator<?> iter = collection.iterator();
      while (iter.hasNext()) {
        Object value = iter.next();
        if (i == index) {
          return value;
        }
        i += 1;
      }
      return null;
    }

  }

  /**
   * Map路径
   */
  static class MapPath extends AbstractPath {

    private String key;

    public MapPath(String path, Path parent, String key) {
      super(path, parent);
      this.key = key;
    }

    @Override
    public void doReplace(Object target) throws PathException {
      if (!(target instanceof Map)) {
        throw new PathException("参数类型不符合预期，预期类型为Map");
      }
      Map<?, ?> mapTarget = (Map<?, ?>) target;
      mapTarget.remove(key);
    }

    @Override
    protected Object doGet(Object target) throws PathException {
      if (!(target instanceof Map)) {
        throw new PathException("参数类型不符合预期，预期类型为Map");
      }
      Map<?, ?> mapTarget = (Map<?, ?>) target;
      if (mapTarget.containsKey(key)) {
        return mapTarget.get(key);
      }
      // 如果Map的Key为对象时，根据toString方法判断
      for (Map.Entry<?, ?> entry : mapTarget.entrySet()) {
        if (entry.getKey().toString().equals(key)) {
          return entry.getValue();
        }
      }
      return null;
    }

  }

  /**
   * 路径解析工具类
   */
  static class PathParse {

    private String path;

    private int index = 1;

    private int beginIndex = index;

    private Object response;

    private Object target;

    public PathParse(String path, Object response) {
      this.path = path;
      this.response = response;
      this.target = response;
    }

    public Path resolve(State state) throws PathException {
      AbstractPath po = new RootPath();
      po.initialized(state);

      while (index < path.length()) {
        char character = path.charAt(index);
        if (character == '/') {
          po = resolve(state, po);
          beginIndex = index + 1;
        }
        index += 1;
      }
      if (beginIndex < index) {
        po = resolve(state, po);
        beginIndex = index;
      }
      return po;
    }

    private AbstractPath resolve(State state, AbstractPath po) throws PathException {
      String property = path.substring(beginIndex, index);
      String subPath = path.substring(0, index);
      if (target instanceof Collection) {
        po = new CollectionPath(subPath, po, Integer.parseInt(property));
        po.initialized(state);
        target = po.get(response);

      } else if (target instanceof Map) {
        po = new MapPath(subPath, po, property);
        po.initialized(state);
        target = po.get(response);

      } else if (target.getClass().isArray()) {
        throw new PathException("数组类型不支持修改");

      } else {
        po = new PropertyPath(subPath, po, property, target.getClass());
        po.initialized(state);
        target = po.get(response);
      }
      if (null == target) {
        throw new PathException("字段值不存在");
      }
      return po;
    }

  }

  public static <T> Path of(String path, T response, State state) throws AccessControlException {
    PathParse parse = new PathParse(path, response);
    return parse.resolve(state);
  }

  public static State state() {
    return new State();
  }

}
