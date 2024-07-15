/*
 * The Access Access Control Engine, 
 * unlike the RBAC model that utilizes static data to implement access control, 
 * realizes API access control by evaluating if dynamic data satisfies the access control rules.
 *
 * License: GNU GENERAL PUBLIC LICENSE, Version 3, 29 June 2007
 * See the license.txt file in the root directory or see <http://www.gnu.org/licenses/>.
 */
package group.devtool.access.documentation.entity;

import group.devtool.access.engine.MetaData;

import java.util.List;
import java.util.Map;

public class ExampleAccessControlMetaData implements MetaData {

  private Integer age;

  private String gender;

  private String operate;

  private String key;

  public ExampleAccessControlMetaData() {

  }

  public ExampleAccessControlMetaData(Integer age, String gender, String operate) {
    this.age = age;
    this.gender = gender;
    this.operate = operate;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getOperate() {
    return operate;
  }

  public void setOperate(String operate) {
    this.operate = operate;
  }

  @Override
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }
}
