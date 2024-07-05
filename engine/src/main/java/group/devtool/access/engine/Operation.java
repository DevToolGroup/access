/*
 * The Access Access Control Engine, 
 * unlike the RBAC model that utilizes static data to implement access control, 
 * realizes API access control by evaluating if dynamic data satisfies the access control rules.
 *
 * License: GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 * See the license.txt file in the root directory or see <http://www.gnu.org/licenses/>.
 */
package group.devtool.access.engine;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

/**
 * 判断条件操作
 */
public interface Operation {

  Object operate(Object left, Object right) throws ExpressionOperateException;

  public class GT implements Operation {

    @Override
    public Object operate(Object left, Object right) throws ExpressionOperateException {
      if (null == left || null == right) {
        throw new ExpressionOperateException("缺少必要的参数");
      }
      BigDecimal lb = new BigDecimal(left.toString());
      BigDecimal rb = new BigDecimal(right.toString());
      return lb.compareTo(rb) > 0;
    }

  }

  public class LT implements Operation {

    @Override
    public Object operate(Object left, Object right) throws ExpressionOperateException {
      if (null == left || null == right) {
        throw new ExpressionOperateException("缺少必要的参数");
      }
      BigDecimal lb = new BigDecimal(left.toString());
      BigDecimal rb = new BigDecimal(right.toString());
      return lb.compareTo(rb) < 0;
    }

  }

  public class GE implements Operation {

    @Override
    public Object operate(Object left, Object right) throws ExpressionOperateException {
      if (null == left || null == right) {
        throw new ExpressionOperateException("缺少必要的参数");
      }
      BigDecimal lb = new BigDecimal(left.toString());
      BigDecimal rb = new BigDecimal(right.toString());
      return lb.compareTo(rb) >= 0;
    }

  }

  public class LE implements Operation {

    @Override
    public Object operate(Object left, Object right) throws ExpressionOperateException {
      if (null == left || null == right) {
        throw new ExpressionOperateException("缺少必要的参数");
      }
      BigDecimal lb = new BigDecimal(left.toString());
      BigDecimal rb = new BigDecimal(right.toString());
      return lb.compareTo(rb) <= 0;
    }

  }

  public class EQ implements Operation {

    @Override
    public Object operate(Object left, Object right) throws ExpressionOperateException {
      if (null == left || null == right) {
        throw new ExpressionOperateException("缺少必要的参数");
      }
      return left.equals(right);
    }

  }

  public class NE implements Operation {

    @Override
    public Object operate(Object left, Object right) throws ExpressionOperateException {
      if (null == left || null == right) {
        throw new ExpressionOperateException("缺少必要的参数");
      }
      return !left.equals(right);
    }

  }

  public class IN implements Operation {

    @Override
    public Object operate(Object left, Object right) throws ExpressionOperateException {
      if (null == left || null == right) {
        throw new ExpressionOperateException("缺少必要的参数");
      }
      if (right instanceof Collection) {
        return ((Collection<?>) right).contains(left);
      }
      if (right instanceof Map) {
        return ((Map<?, ?>) right).containsKey(left);
      }
      if (right instanceof CharSequence && left instanceof CharSequence) {
        CharSequence cl = (CharSequence) left;
        CharSequence cr = (CharSequence) right;
        return cr.toString().contains(cl);
      }
      throw new ExpressionOperateException("参数类型仅支持Collection/Map/CharSequence");
    }

  }

  public class BG implements Operation {
    @Override
    public Object operate(Object left, Object right) throws ExpressionOperateException {
      if (null == left || null == right) {
        throw new ExpressionOperateException("缺少必要的参数");
      }
      if (right instanceof CharSequence && left instanceof CharSequence) {
        CharSequence cr = (CharSequence) right;
        CharSequence cl = (CharSequence) left;
        return cl.toString().startsWith(cr.toString());
      }
      throw new ExpressionOperateException("参数类型仅支持CharSequence");
    }

  }

  public class EG implements Operation {

    @Override
    public Object operate(Object left, Object right) throws ExpressionOperateException {
      if (null == left || null == right) {
        throw new ExpressionOperateException("缺少必要的参数");
      }
      if (right instanceof CharSequence && left instanceof CharSequence) {
        CharSequence cr = (CharSequence) right;
        CharSequence cl = (CharSequence) left;
        return cl.toString().endsWith(cr.toString());
      }
      throw new ExpressionOperateException("参数类型仅支持CharSequence");
    }
  }

  public class AND implements Operation {

    @Override
    public Object operate(Object left, Object right) throws ExpressionOperateException {
      if (null == left || null == right) {
        throw new ExpressionOperateException("缺少必要的参数");
      }
      if (Boolean.TRUE.equals(left) && Boolean.TRUE.equals(right)) {
        return true;
      }
      return false;
    }

  }

  public class OR implements Operation {

    @Override
    public Object operate(Object left, Object right) throws ExpressionOperateException {
      if (null == left || null == right) {
        throw new ExpressionOperateException("缺少必要的参数");
      }
      if (Boolean.TRUE.equals(left) || Boolean.TRUE.equals(right)) {
        return true;
      }
      return false;
    }
  }

  public class NOT implements Operation {

    @Override
    public Object operate(Object left, Object right) throws ExpressionOperateException {
      if (null == left) {
        throw new ExpressionOperateException("缺少必要的参数");
      }
      if (left instanceof Boolean) {
        Boolean cl = (Boolean) left;
        return !cl;
      }
      throw new ExpressionOperateException("逻辑非操作参数仅支持Boolean类型");
    }
  }

}
