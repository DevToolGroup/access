package group.devtool.access.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import group.devtool.access.engine.entity.TestAccessControlMetaData;
import group.devtool.access.engine.entity.TestAccessControlMetaData.Son;
import group.devtool.access.engine.entity.TestAccessControlMetaData.Wife;

public class PathTest {

  @Test
  public void testGetBooleanField() {
    try {
      TestAccessControlMetaData metadata = new TestAccessControlMetaData(10, "F", false);
      Path path = Path.of("/married", metadata, Path.state());
      Object value = path.get(metadata);
      assertTrue(value instanceof Boolean);
      assertFalse((Boolean) value);
    } catch (AccessControlException e) {
      fail("异常：" + e);
    }
  }

  @Test
  public void testGetObjectField() {
    try {
      TestAccessControlMetaData metadata = new TestAccessControlMetaData(10, "F", false);
      Path path = Path.of("/age", metadata, Path.state());
      Object value = path.get(metadata);
      assertTrue(value instanceof Integer);
      assertTrue(((Integer) value) == 10);
    } catch (AccessControlException e) {
      fail("异常：" + e);
    }
  }

  @Test
  public void testGetMapField() {
    try {
      Map<String, String> houses = new HashMap<>();
      houses.put("a", "b");
      TestAccessControlMetaData metadata = new TestAccessControlMetaData(houses);
      Path path = Path.of("/houses/a", metadata, Path.state());
      Object value = path.get(metadata);
      assertTrue(value instanceof String);
      assertTrue(((String) value).equals("b"));
    } catch (AccessControlException e) {
      fail("异常：" + e);
    }
  }

  @Test
  public void testGetMapValueField() {
    try {
      Map<String, Wife> wifes = new HashMap<>();
      wifes.put("a", new Wife("b"));
      TestAccessControlMetaData metadata = new TestAccessControlMetaData(wifes, true);
      Path path = Path.of("/wifes/a/name", metadata, Path.state());
      Object value = path.get(metadata);
      assertTrue(value instanceof String);
      assertTrue(((String) value).equals("b"));
    } catch (AccessControlException e) {
      fail("异常：" + e);
    }
  }

  @Test
  public void testGetListField() {
    try {
      List<Son> sons = new ArrayList<>();
      sons.add(new Son("b"));
      TestAccessControlMetaData metadata = new TestAccessControlMetaData(sons);
      Path path = Path.of("/sons/0/name", metadata, Path.state());
      Object value = path.get(metadata);
      assertTrue(value instanceof String);
      assertTrue(((String) value).equals("b"));
    } catch (AccessControlException e) {
      fail("异常：" + e);
    }
  }

  @Test
  public void testReplaceField() {
    try {
      TestAccessControlMetaData metadata = new TestAccessControlMetaData(10, "F", false);
      Path path = Path.of("/married", metadata, Path.state());
      path.replace(metadata);
      Boolean result = metadata.getMarried();
      assertNull(result);
    } catch (AccessControlException e) {
      e.printStackTrace();
      fail("异常：" + e);
    }
  }

  @Test
  public void testReplaceListElement() {
    try {
      List<Son> sons = new ArrayList<>();
      sons.add(new Son("b"));
      TestAccessControlMetaData metadata = new TestAccessControlMetaData(sons);
      assertTrue(metadata.getSons().size() == 1);

      Path path = Path.of("/sons/0", metadata, Path.state());
      path.replace(metadata);
      assertTrue(metadata.getSons().size() == 0);
    } catch (AccessControlException e) {
      fail("异常：" + e);
    }
  }

  @Test
  public void testReplaceListElementField() {
    try {
      List<Son> sons = new ArrayList<>();
      sons.add(new Son("b"));
      TestAccessControlMetaData metadata = new TestAccessControlMetaData(sons);
      assertEquals("b", metadata.getSons().get(0).getName());

      Path path = Path.of("/sons/0/name", metadata, Path.state());
      path.replace(metadata);
      assertNull(metadata.getSons().get(0).getName());
    } catch (AccessControlException e) {
      fail("异常：" + e);
    }
  }

  @Test
  public void testReplaceMapElement() {
    try {
      Map<String, String> houses = new HashMap<>();
      houses.put("a", "b");
      TestAccessControlMetaData metadata = new TestAccessControlMetaData(houses);
      assertEquals("b", metadata.getHouses().get("a"));

      Path path = Path.of("/houses/a", metadata, Path.state());
      path.replace(metadata);

      assertFalse(metadata.getHouses().containsKey("a"));

    } catch (AccessControlException e) {
      fail("异常：" + e);
    }
  }

  @Test
  public void testReplaceMapValueElement() {
    try {
      Map<String, Wife> wifes = new HashMap<>();
      wifes.put("a", new Wife("b"));
      TestAccessControlMetaData metadata = new TestAccessControlMetaData(wifes, true);
      assertNotNull(metadata.getWifes().get("a").getName());

      Path path = Path.of("/wifes/a/name", metadata, Path.state());
      path.replace(metadata);
      
      assertNull(metadata.getWifes().get("a").getName());
    } catch (AccessControlException e) {
      fail("异常：" + e);
    }
  }

}
