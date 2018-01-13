package in.ac.gndec.tnp;

/**
 * Created by mrdis on 1/11/2018.
 */

public class Guardian {

    String name,occupation,contact;

    public Guardian() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }


    @Override
    public String toString() {
        return "Guardian{" +
                "\tname='" + name + '\'' +
                ",\n\t occupation='" + occupation + '\'' +
                ", \n\tcontact='" + contact + '\'' +
                '}';
    }
}
