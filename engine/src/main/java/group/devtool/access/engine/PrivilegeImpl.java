/*
 * The Access Access Control Engine,
 * unlike the RBAC model that utilizes static data to implement access control,
 * realizes API access control by evaluating if dynamic data satisfies the access control rules.
 *
 * License: GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 * See the license.txt file in the root directory or see <http://www.gnu.org/licenses/>.
 */
package group.devtool.access.engine;

/**
 * {@link Privilege} 实现类
 */
public class PrivilegeImpl implements Privilege {

	private Condition condition;

	public PrivilegeImpl() {

	}

	public PrivilegeImpl(String expression) throws ExpressionException {
		condition = new ConditionImpl(expression);
	}

	public PrivilegeImpl(Condition condition) {
		this.condition = condition;
	}

	@Override
	public Condition getCondition() {
		return condition;
	}

	public void setCondition(String expression) throws ExpressionException {
		this.condition = new ConditionImpl(expression);
	}

}
