public class ModuleResults<String,Boolean> {

  private final String name;
  private final Boolean result;

  public ModuleResults(String name, Boolean result) {
    this.name = name;
    this.result = result;
  }

  public String getName() { return name; }
  public Boolean getResult() { return result; }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ModuleResults)) return false;
    ModuleResults pairo = (ModuleResults) o;
    return this.name.equals(pairo.getName()) &&
           this.result.equals(pairo.getResult());
  }

}