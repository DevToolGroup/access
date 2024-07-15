/*
 * The Access Access Control Engine,
 * unlike the RBAC model that utilizes static data to implement access control,
 * realizes API access control by evaluating if dynamic data satisfies the access control rules.
 *
 * License: GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 * See the license.txt file in the root directory or see <http://www.gnu.org/licenses/>.
 */
package group.devtool.access.engine;

import java.util.Arrays;
import java.util.Objects;

/**
 * 访问控制查询范围
 */
public class ScopeImpl implements Scope {

	private Condition condition;

	private Where where;

	public ScopeImpl() {

	}

	public ScopeImpl(String expression, Where where) throws ExpressionException {
		this.condition = new ConditionImpl(expression);
		this.where = where;
	}

	@Override
	public Condition getCondition() {
		return condition;
	}

	public void setCondition(String expression) throws ExpressionException {
		this.condition = new ConditionImpl(expression);
	}

	public Where getWhere() {
		return where;
	}

	public void setWhere(WhereImpl where) {
		this.where = where;
	}

	@Override
	public void specification(Specification specification) {
		doSpecification(where, specification);
	}

	private Specification doSpecification(Where element, Specification specification) {
		switch (element.getOp()) {
			case AND:
				return specification.and(doSpecification(element.getLeft(), specification),
								doSpecification(element.getRight(), specification));
			case OR:
				return specification.or(doSpecification(element.getLeft(), specification),
								doSpecification(element.getRight(), specification));
			case GE:
				return specification.ge(where.getStatement());
			case GT:
				return specification.gt(where.getStatement());
			case LT:
				return specification.lt(where.getStatement());
			case LE:
				return specification.le(where.getStatement());
			case LIKE:
				return specification.like(where.getStatement());
			case NOTLIKE:
				return specification.notLike(where.getStatement());
			case LEFTLIKE:
				return specification.leftLike(where.getStatement());
			case RIGHTLIKE:
				return specification.rightLike(where.getStatement());
			case EQ:
				return specification.equal(where.getStatement());
			case NE:
				return specification.notEqual(where.getStatement());
			case BETWEEN:
				return specification.between(where.getStatement());
			case NOTIN:
				return specification.notIn(where.getStatement());
			case IN:
				return specification.in(where.getStatement());
			case ISNULL:
				return specification.isNull(where.getStatement());
			case ISNOTNULL:
				return specification.isNotNull(where.getStatement());
			default:
				throw new UnsupportedOperationException("暂不支持当前");
		}
	}

	// 过滤条件 操作符
	public enum OP {
		AND,
		OR,
		GE,
		GT,
		LE,
		LT,
		EQ,
		NE,
		LIKE,
		NOTLIKE,
		LEFTLIKE,
		RIGHTLIKE,
		IN,
		NOTIN,
		BETWEEN,
		ISNULL,
		ISNOTNULL,
		;

	}

	// 过滤条件
	public interface Where {

		OP getOp();

		Where getLeft();

		Where getRight();

		Statement getStatement();

	}

	/**
	 * 过滤条件默认实现
	 */
	public static class WhereImpl implements Where {

		private OP op;

		private Where left;

		private Where right;

		private String entity;

		private String column;

		private Object[] parameters;

		public WhereImpl() {
		}

		public WhereImpl(OP op, String entity, String column, Object... parameters) {
			this.op = op;
			this.entity = entity;
			this.column = column;
			this.parameters = parameters;
		}

		public WhereImpl(OP op, Where left, Where right) {
			this.op = op;
			this.left = left;
			this.right = right;
		}

		@Override
		public OP getOp() {
			return op;
		}

		@Override
		public Where getLeft() {
			return left;
		}

		@Override
		public Where getRight() {
			return right;
		}

		@Override
		public Statement getStatement() {
			return new StatementImpl(entity, column, parameters);
		}

		public String getEntity() {
			return entity;
		}

		public String getColumn() {
			return column;
		}

		public Object[] getParameters() {
			return parameters;
		}

		public void setOp(OP op) {
			this.op = op;
		}

		public void setLeft(Where left) {
			this.left = left;
		}

		public void setRight(Where right) {
			this.right = right;
		}

		public void setEntity(String entity) {
			this.entity = entity;
		}

		public void setColumn(String column) {
			this.column = column;
		}

		public void setParameters(Object[] parameters) {
			this.parameters = parameters;
		}

	}

}
