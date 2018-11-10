package Model;

/**
 * Created by mrdis on 1/11/2018.
 */

public class Student {

    public String name;
    public String branch;
    public String shift;
    public String blood_group;
    public String category;
    public long collegeroll,universityroll;
    public float height,weight;
    public Guardian father,mother;

    public static final String Name="name";
    public static final String Branch ="branch";
    public static final String Shift="shift";
    public static final String Blood_Group="blood_group";
    public static final String Category="category";
    public static final String CRoll="collegeroll";
    public static final String URoll="universityroll";
    public static final String Height="height";
    public static final String Weight="weight";
    public static final String Father="father";
    public static final String Mother="mother";


    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Guardian getFather() {
        return father;
    }

    public void setFather(Guardian father) {
        this.father = father;
    }

    public Guardian getMother() {
        return mother;
    }

    public void setMother(Guardian mother) {
        this.mother = mother;
    }


    public Student() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCollegeroll() {
        return collegeroll;
    }

    public void setCollegeroll(long collegeroll) {
        this.collegeroll = collegeroll;
    }

    public long getUniversityroll() {
        return universityroll;
    }

    public void setUniversityroll(long universityroll) {
        this.universityroll = universityroll;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ",\n branch='" + branch + '\'' +
                ",\n shift='" + shift + '\'' +
                ", \nblood_group='" + blood_group + '\'' +
                ", \ncategory='" + category + '\'' +
                ", \ncollegeroll=" + collegeroll +
                ", \nuniversityroll=" + universityroll +
                ", \nheight=" + height +
                ", \nweight=" + weight +
                ", \nfather=" + father +
                ", \nmother=" + mother +
                '}';
    }
}
