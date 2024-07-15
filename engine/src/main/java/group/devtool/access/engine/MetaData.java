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
 * 访问控制元数据，用于权限规则的条件判断
 */
public interface MetaData {

	/**
	 * @return 访问控制元数据唯一值
	 */
	public String getKey();

}
