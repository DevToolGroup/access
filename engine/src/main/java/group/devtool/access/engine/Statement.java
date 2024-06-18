package group.devtool.access.engine;

public interface Statement {

  public String entity();

  public String column();

  public Object[] parameters();

}
