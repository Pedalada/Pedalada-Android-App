package pedalada.pedalapp;

public class Rider {

    String name;
    int age;
    boolean female;
    
    public Rider() {}

    public Rider(String name, int age, boolean female) {
        this.name = name;
        this.age = age;
        this.female = female;
    }

    public int getAge() {return age;}

    public String getName() {return name;}

    public boolean isFemale() {return female;}

    public void setAge(int newAge) {this.age = newAge;}

    public void setName(String newName) {this.name = newName;}

}