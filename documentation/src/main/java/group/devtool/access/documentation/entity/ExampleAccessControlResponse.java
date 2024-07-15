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

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "ExampleAccessControlResponse")
public class ExampleAccessControlResponse {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "name")
  private String name;

  @Column(name = "age")
  private Integer age;

  @Column(name = "gender")
  private String gender;

  @Column(name = "married")
  private Boolean married;

  private List<Son> sons;

  private Map<String, String> houses;

  private Map<String, Wife> wifes;

  private String operate;

  public ExampleAccessControlResponse(List<Son> sons) {
    this.sons = sons;
  }

  public ExampleAccessControlResponse(Integer age, String gender, String operate) {
    this.age = age;
    this.gender = gender;
    this.operate = operate;
  }

  public ExampleAccessControlResponse(Integer age, String gender) {
    this.age = age;
    this.gender = gender;
  }

  public ExampleAccessControlResponse(Integer age, String gender, Boolean married) {
    this.age = age;
    this.gender = gender;
    this.married = married;
  }

  public ExampleAccessControlResponse(Integer id, Integer age, String gender, Boolean married, String name) {
    this.id = id;
    this.age = age;
    this.gender = gender;
    this.married = married;
    this.name = name;
  }

  public ExampleAccessControlResponse(Map<String, String> houses) {
    this.houses = houses;
  }

  public ExampleAccessControlResponse(Map<String, Wife> wifes, Boolean married) {
    this.wifes = wifes;
    this.married = married;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public Boolean getMarried() {
    return married;
  }

  public void setMarried(Boolean married) {
    this.married = married;
  }

  public String getOperate() {
    return operate;
  }

  public void setOperate(String operate) {
    this.operate = operate;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map<String, String> getHouses() {
    return houses;
  }

  public void setHouses(Map<String, String> houses) {
    this.houses = houses;
  }

  public Map<String, Wife> getWifes() {
    return wifes;
  }

  public void setWifes(Map<String, Wife> wifes) {
    this.wifes = wifes;
  }

  public List<Son> getSons() {
    return sons;
  }

  public void setSons(List<Son> sons) {
    this.sons = sons;
  }

  public static class Wife {
    private String name;

    public Wife(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

  }

  public static class Son {
    private String name;

    public Son(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}
