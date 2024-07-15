/*
 * The Access Access Control Engine,
 * unlike the RBAC model that utilizes static data to implement access control,
 * realizes API access control by evaluating if dynamic data satisfies the access control rules.
 *
 * License: GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 * See the license.txt file in the root directory or see <http://www.gnu.org/licenses/>.
 */
package group.devtool.access.engine;

import java.util.*;

import group.devtool.access.engine.Path.State;

/**
 * 可访问内容
 */
public class ViewImpl implements View {

	private Condition condition;

	private List<String> paths;

	public ViewImpl() {
	}

	public ViewImpl(String expression, List<String> paths) throws ExpressionException {
		this.condition = new ConditionImpl(expression);
		this.paths = paths;
	}

	/**
	 * 修剪视图
	 *
	 * @param <T>      返回结果类型
	 * @param response 返回结果参数
	 * @throws AccessControlException
	 */
	public <T> void clip(T response) throws AccessControlException {
		if (response instanceof Collection) {
			Collection<?> collection = (Collection<?>) response;
			doClipCollection(collection);
		} else if (response instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) response;
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
		for (Object value : map.values()) {
			doClip(value);
		}
	}

	private void doClipCollection(Collection<?> collection) throws AccessControlException {
		for (Object value : collection) {
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
	public Condition getCondition() {
		return condition;
	}

	public void setCondition(String expression) throws ExpressionException {
		this.condition = new ConditionImpl(expression);
	}

	public List<String> getPaths() {
		return paths;
	}

	public void setPaths(List<String> paths) {
		this.paths = paths;
	}
}
