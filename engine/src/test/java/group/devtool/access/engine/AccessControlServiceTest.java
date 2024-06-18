package group.devtool.access.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.FixMethodOrder;
import org.junit.Test;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import group.devtool.access.engine.AccessControlService.AccessSpecificationBuilder;
import group.devtool.access.engine.ScopeImpl.SQLImpl;
import group.devtool.access.engine.entity.TestAccessControlEntity;
import group.devtool.access.engine.entity.TestAccessControlMetaData;
import group.devtool.access.engine.entity.TestAccessControlResponse;
import group.devtool.access.engine.entity.TestSpecification;

@FixMethodOrder
public class AccessControlServiceTest {

  @Test
  public void testControl1() {
    SessionFactory factory = getEntityManagerFactory();
    // 初始化数据
    init(factory);

    EntityManager manager = factory.createEntityManager();
    List<TestAccessControlEntity> entities = query(manager);

    // 原始数据校验，
    assertEquals(2, entities.size());
    assertEquals("entity1", entities.get(0).getName());
    assertEquals(true, entities.get(0).getMarried());
    assertEquals("entity2", entities.get(1).getName());
    assertEquals(true, entities.get(1).getMarried());

    // mock 访问控制定义服务
    AccessControlDefinitionService definitionService = mock(AccessControlDefinitionService.class);
    when(definitionService.load(any())).thenReturn(new TestAccessControlDefinition());

    // 执行访问控制的业务操作
    AccessControlServiceImpl control = new AccessControlServiceImpl(definitionService);
    try {
      List<TestAccessControlResponse> result = control.doControl(() -> {
        return new TestAccessControlMetaData(10, "F", "get");
      }, (consumer) -> {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        // 查询范围修改
        return doOperation(manager, builder, consumer);
      });

      assertEquals(1, result.size());
      assertNull(result.get(0).getMarried());
      assertEquals("entity1", result.get(0).getName());

    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testControl2() {
    SessionFactory factory = getEntityManagerFactory();
    // 初始化数据
    init(factory);

    EntityManager manager = factory.createEntityManager();
    List<TestAccessControlEntity> entities = query(manager);

    // 原始数据校验，
    assertEquals(2, entities.size());
    assertEquals("entity1", entities.get(0).getName());
    assertEquals(true, entities.get(0).getMarried());
    assertEquals("entity2", entities.get(1).getName());
    assertEquals(true, entities.get(1).getMarried());

    // mock 访问控制定义服务
    AccessControlDefinitionService definitionService = mock(AccessControlDefinitionService.class);
    when(definitionService.load(any())).thenReturn(new NoPrivilegeTestAccessControlDefinition());

    // 执行访问控制的业务操作
    AccessControlServiceImpl control = new AccessControlServiceImpl(definitionService);
    try {
      control.doControl(() -> {
        return new TestAccessControlMetaData(10, "F", "get");
      }, (consumer) -> {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        // 查询范围修改
        return doOperation(manager, builder, consumer);
      });
      fail("限制失效");
    } catch (Exception e) {
      assertTrue(e instanceof NoPrivilegeException);
    }
  }

  @Test
  public void testControl3() {
    SessionFactory factory = getEntityManagerFactory();
    // 初始化数据
    init(factory);

    EntityManager manager = factory.createEntityManager();
    List<TestAccessControlEntity> entities = query(manager);

    // 原始数据校验，
    assertEquals(2, entities.size());
    assertEquals("entity1", entities.get(0).getName());
    assertEquals(true, entities.get(0).getMarried());
    assertEquals("entity2", entities.get(1).getName());
    assertEquals(true, entities.get(1).getMarried());

    // mock 访问控制定义服务
    AccessControlDefinitionService definitionService = mock(AccessControlDefinitionService.class);
    when(definitionService.load(any())).thenReturn(new NoScopeTestAccessControlDefinition());

    // 执行访问控制的业务操作
    AccessControlServiceImpl control = new AccessControlServiceImpl(definitionService);
    try {
      List<TestAccessControlResponse> result = control.doControl(() -> {
        return new TestAccessControlMetaData(10, "F", "get");
      }, (consumer) -> {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        // 查询范围修改
        return doOperation(manager, builder, consumer);
      });

      assertEquals(2, result.size());
      assertEquals("entity1", result.get(0).getName());
      assertNull(result.get(0).getMarried());
      assertEquals("entity2", result.get(1).getName());
      assertNull(result.get(1).getMarried());

    } catch (Exception e) {
      assertTrue(e instanceof NoPrivilegeException);
    }
  }

  @Test
  public void testControl4() {
    SessionFactory factory = getEntityManagerFactory();
    // 初始化数据
    init(factory);

    EntityManager manager = factory.createEntityManager();
    List<TestAccessControlEntity> entities = query(manager);

    // 原始数据校验，
    assertEquals(2, entities.size());
    assertEquals("entity1", entities.get(0).getName());
    assertEquals(true, entities.get(0).getMarried());
    assertEquals("entity2", entities.get(1).getName());
    assertEquals(true, entities.get(1).getMarried());

    // mock 访问控制定义服务
    AccessControlDefinitionService definitionService = mock(AccessControlDefinitionService.class);
    when(definitionService.load(any())).thenReturn(new NoFieldTestAccessControlDefinition());

    // 执行访问控制的业务操作
    AccessControlServiceImpl control = new AccessControlServiceImpl(definitionService);
    try {
      List<TestAccessControlResponse> result = control.doControl(() -> {
        return new TestAccessControlMetaData(10, "F", "get");
      }, (consumer) -> {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        // 查询范围修改
        return doOperation(manager, builder, consumer);
      });

      assertEquals(2, result.size());
      assertEquals("entity1", result.get(0).getName());
      assertTrue(result.get(0).getMarried());
      assertEquals("entity2", result.get(1).getName());
      assertTrue(result.get(1).getMarried());

    } catch (Exception e) {
      assertTrue(e instanceof NoPrivilegeException);
    }
  }

  private List<TestAccessControlResponse> doOperation(EntityManager manager, CriteriaBuilder builder,
      AccessSpecificationBuilder consumer) throws Exception {
    CriteriaQuery<TestAccessControlEntity> query = builder.createQuery(TestAccessControlEntity.class);
    Root<TestAccessControlEntity> root = query.from(TestAccessControlEntity.class);

    // 构建JPA Query
    TestSpecification testSpecification = new TestSpecification(builder, root);
    consumer.apply(testSpecification);
    
    Predicate predicate;
    if (null != testSpecification.getPredicate()) {
      predicate = builder.and(testSpecification.getPredicate(), builder.ge(root.get("age"), 0));
    } else {
      predicate = builder.ge(root.get("age"), 0);
    }
    query.where(predicate);

    TypedQuery<TestAccessControlEntity> dbQuery = manager.createQuery(query);
    List<TestAccessControlEntity> entities = dbQuery.getResultList();

    List<TestAccessControlResponse> responses = new ArrayList<>();
    for (TestAccessControlEntity entity : entities) {
      responses.add(new TestAccessControlResponse(entity.getId(), entity.getAge(), entity.getGender(),
          entity.getMarried(), entity.getName()));
    }

    return responses;
  }

  private List<TestAccessControlEntity> query(EntityManager manager) {
    CriteriaBuilder builder = manager.getCriteriaBuilder();
    CriteriaQuery<TestAccessControlEntity> query = builder.createQuery(TestAccessControlEntity.class);
    Root<TestAccessControlEntity> root = query.from(TestAccessControlEntity.class);
    Predicate predicate = builder.ge(root.get("age"), 0);
    query.where(predicate);

    TypedQuery<TestAccessControlEntity> dbQuery = manager.createQuery(query);
    return dbQuery.getResultList();
  }

  private void init(SessionFactory factory) {
    try (Session session = factory.openSession()) {
      Transaction transaction = session.beginTransaction();

      TestAccessControlEntity entity1 = new TestAccessControlEntity();
      entity1.setAge(10);
      entity1.setMarried(true);
      entity1.setGender("F");
      entity1.setName("entity1");
      session.save(entity1);

      TestAccessControlEntity entity2 = new TestAccessControlEntity();
      entity2.setAge(1);
      entity2.setMarried(true);
      entity2.setGender("F");
      entity2.setName("entity2");
      session.save(entity2);

      transaction.commit();
    }

  }

  private SessionFactory getEntityManagerFactory() {

    Configuration cfg = new Configuration();
    cfg.configure();
    // 添加映射文件或注解的实体类
    cfg.addAnnotatedClass(TestAccessControlEntity.class); // 替换为你的实体类

    // 创建SessionFactory
    SessionFactory sessionFactory = cfg.buildSessionFactory();
    return sessionFactory;
  }

  public class TestAccessControlDefinition implements AccessControlDefinition {

    @Override
    public List<Privilege> getPrivileges() {
      try {
        return Arrays.asList(
            new PrivilegeImpl("\"@(/age)\" > 5 && \"@(/gender)\" == \"F\""),
            new PrivilegeImpl("\"@(/age)\" > 10 && \"@(/gender)\" == \"F\""));
      } catch (ExpressionException e) {
        return new ArrayList<>();
      }
    }

    @Override
    public List<Scope> getScopes() {
      SQLImpl sql = new ScopeImpl.SQLImpl(ScopeImpl.OP.GE, "user", "age", 5);
      try {
        return Arrays.asList(
            new ScopeImpl("\"@(/age)\" > 5 && \"@(/gender)\" == \"F\"", sql),
            new ScopeImpl("\"@(/age)\" < 5 && \"@(/gender)\" == \"F\"", sql));
      } catch (ExpressionException e) {
        return new ArrayList<>();
      }
    }

    @Override
    public List<View> getFields() {
      List<String> ignores = Arrays.asList("/married");
      List<String> full = new ArrayList<>();
      try {
        return Arrays.asList(
            new ViewImpl("\"@(/age)\" > 5 && \"@(/gender)\" == \"F\"", ignores),
            new ViewImpl("\"@(/age)\" > 10 && \"@(/gender)\" == \"F\"", full));
      } catch (ExpressionException e) {
        return new ArrayList<>();
      }
    }

  }

  public class NoScopeTestAccessControlDefinition implements AccessControlDefinition {

    @Override
    public List<Privilege> getPrivileges() {
      try {
        return Arrays.asList(
            new PrivilegeImpl("\"@(/age)\" > 5 && \"@(/gender)\" == \"F\""),
            new PrivilegeImpl("\"@(/age)\" < 5 && \"@(/gender)\" == \"F\""));
      } catch (ExpressionException e) {
        return new ArrayList<>();
      }
    }

    @Override
    public List<Scope> getScopes() {
      return new ArrayList<>();
    }

    @Override
    public List<View> getFields() {
      List<String> ignores = Arrays.asList("/married");
      List<String> full = new ArrayList<>();
      try {
        return Arrays.asList(
            new ViewImpl("\"@(/age)\" > 5 && \"@(/gender)\" == \"F\"", ignores),
            new ViewImpl("\"@(/age)\" < 5 && \"@(/gender)\" == \"F\"", full));
      } catch (ExpressionException e) {
        return new ArrayList<>();
      }
    }

  }

  public class NoPrivilegeTestAccessControlDefinition implements AccessControlDefinition {

    @Override
    public List<Privilege> getPrivileges() {
      return new ArrayList<>();
    }

    @Override
    public List<Scope> getScopes() {
      SQLImpl sql = new ScopeImpl.SQLImpl(ScopeImpl.OP.GE, "user", "age", 5);
      try {
        return Arrays.asList(
            new ScopeImpl("\"@(/age)\" > 5 && \"@(/gender)\" == \"F\"", sql),
            new ScopeImpl("\"@(/age)\" < 5 && \"@(/gender)\" == \"F\"", sql));
      } catch (ExpressionException e) {
        return new ArrayList<>();
      }
    }

    @Override
    public List<View> getFields() {
      List<String> ignores = Arrays.asList("/married");
      List<String> full = new ArrayList<>();
      try {
        return Arrays.asList(
            new ViewImpl("\"@(/age)\" > 5 && \"@(/gender)\" == \"F\"", ignores),
            new ViewImpl("\"@(/age)\" > 10 && \"@(/gender)\" == \"F\"", full));
      } catch (ExpressionException e) {
        return new ArrayList<>();
      }
    }

  }

  public class NoFieldTestAccessControlDefinition implements AccessControlDefinition {

    @Override
    public List<Privilege> getPrivileges() {
      return new ArrayList<>();
    }

    @Override
    public List<Scope> getScopes() {
      SQLImpl sql = new ScopeImpl.SQLImpl(ScopeImpl.OP.GE, "user", "age", 5);
      try {
        return Arrays.asList(
            new ScopeImpl("\"@(/age)\" > 5 && \"@(/gender)\" == \"F\"", sql),
            new ScopeImpl("\"@(/age)\" < 5 && \"@(/gender)\" == \"M\"", sql));
      } catch (ExpressionException e) {
        return new ArrayList<>();
      }
    }

    @Override
    public List<View> getFields() {
      return new ArrayList<>();
    }

  }
}
