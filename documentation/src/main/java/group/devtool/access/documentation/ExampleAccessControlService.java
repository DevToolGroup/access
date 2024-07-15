/*
 * The Access Access Control Engine,
 * unlike the RBAC model that utilizes static data to implement access control,
 * realizes API access control by evaluating if dynamic data satisfies the access control rules.
 *
 * License: GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 * See the license.txt file in the root directory or see <http://www.gnu.org/licenses/>.
 */
package group.devtool.access.documentation;

import group.devtool.access.documentation.entity.ExampleAccessControlEntity;
import group.devtool.access.documentation.entity.ExampleAccessControlMetaData;
import group.devtool.access.documentation.entity.ExampleAccessControlResponse;
import group.devtool.access.engine.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


public class ExampleAccessControlService {

	public static void main(String[] args) {
		SessionFactory factory = getEntityManagerFactory();
		// 初始化数据
		init(factory);

		EntityManager manager = factory.createEntityManager();
		List<ExampleAccessControlEntity> entities = query(manager);

		// 原始数据校验，
		System.out.println("原始数据量：" + entities.size());
		System.out.println("entity 1 name：" + entities.get(0).getName());
		System.out.println("entity 1 married：" + entities.get(0).getMarried());
		System.out.println("entity 2 name：" + entities.get(1).getName());
		System.out.println("entity 2 married：" + entities.get(1).getMarried());

		// 加载规则策略
		AccessControlDefinitionService definitionService = new ResourcesAccessControlDefinitionService();

		// 执行访问控制
		AccessControlServiceImpl control = new AccessControlServiceImpl(definitionService);
		try {
			List<ExampleAccessControlResponse> result = control.doControl(() -> {
				ExampleAccessControlMetaData metadata = new ExampleAccessControlMetaData(10, "F", "get");
				metadata.setKey("access.yml");
				return metadata;
			}, (consumer) -> {
				CriteriaBuilder builder = manager.getCriteriaBuilder();;
				// 查询范围修改
				return doOperation(manager, builder, consumer);
			});
			System.out.println("过滤后数据量：" + result.size());
			System.out.println("忽略返回字段：" + result.get(0).getMarried());
			System.out.println("返回数据的实体名称：" + result.get(0).getName());

		} catch (Exception e) {
			// do nothing
		}
	}


	private static  List<ExampleAccessControlResponse> doOperation(EntityManager manager, CriteriaBuilder builder,
																												 AccessControlService.AccessSpecificationBuilder consumer) throws Exception {
		CriteriaQuery<ExampleAccessControlEntity> query = builder.createQuery(ExampleAccessControlEntity.class);
		Root<ExampleAccessControlEntity> root = query.from(ExampleAccessControlEntity.class);

		// 构建JPA Query
		ExampleSpecification testSpecification = new ExampleSpecification(builder, root);
		consumer.apply(testSpecification);

		Predicate predicate;
		if (null != testSpecification.getPredicate()) {
			predicate = builder.and(testSpecification.getPredicate(), builder.ge(root.get("age"), 0));
		} else {
			predicate = builder.ge(root.get("age"), 0);
		}
		query.where(predicate);

		TypedQuery<ExampleAccessControlEntity> dbQuery = manager.createQuery(query);
		List<ExampleAccessControlEntity> entities = dbQuery.getResultList();

		List<ExampleAccessControlResponse> responses = new ArrayList<>();
		for (ExampleAccessControlEntity entity : entities) {
			responses.add(new ExampleAccessControlResponse(entity.getId(), entity.getAge(), entity.getGender(),
							entity.getMarried(), entity.getName()));
		}

		return responses;
	}

	private static List<ExampleAccessControlEntity> query(EntityManager manager) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ExampleAccessControlEntity> query = builder.createQuery(ExampleAccessControlEntity.class);
		Root<ExampleAccessControlEntity> root = query.from(ExampleAccessControlEntity.class);
		Predicate predicate = builder.ge(root.get("age"), 0);
		query.where(predicate);

		TypedQuery<ExampleAccessControlEntity> dbQuery = manager.createQuery(query);
		return dbQuery.getResultList();
	}

	private static void init(SessionFactory factory) {
		try (Session session = factory.openSession()) {
			Transaction transaction = session.beginTransaction();

			ExampleAccessControlEntity entity1 = new ExampleAccessControlEntity();
			entity1.setAge(10);
			entity1.setMarried(true);
			entity1.setGender("F");
			entity1.setName("entity1");
			session.save(entity1);

			ExampleAccessControlEntity entity2 = new ExampleAccessControlEntity();
			entity2.setAge(1);
			entity2.setMarried(true);
			entity2.setGender("F");
			entity2.setName("entity2");
			session.save(entity2);

			transaction.commit();
		}

	}

	private static SessionFactory getEntityManagerFactory() {

		Configuration cfg = new Configuration();
		cfg.configure();
		// 添加映射文件或注解的实体类
		cfg.addAnnotatedClass(ExampleAccessControlEntity.class); // 替换为你的实体类

		// 创建SessionFactory
		SessionFactory sessionFactory = cfg.buildSessionFactory();
		return sessionFactory;
	}




}
