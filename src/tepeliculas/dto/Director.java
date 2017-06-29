package tepeliculas.dto;

import java.sql.Date;
import java.util.Objects;

/**
 *
 * @author igor
 */
public class Director extends SimpleDTO{
    private Gender gender;
    private java.sql.Date dob;
    private int country;
    
    public Director() {
    }

    public Director(String name, Gender gender, Date dob, int country) {
        super(name);
        this.gender = gender;
        this.dob = dob;
        this.country = country;
    }

    public Director(int id, String name, Gender gender, Date dob, int country) {
        super(id, name);
        this.gender = gender;
        this.dob = dob;
        this.country = country;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + this.id + Objects.hashCode(this.name) +
               this.country + Objects.hashCode(this.gender) +
               Objects.hashCode(this.dob); 
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Director other = (Director) obj;
//        System.out.println("this.id == other.getId() = " + (this.id == other.getId()));
//        boolean b1 = this.id == other.getId();
//        System.out.println("this.name.equalsIgnoreCase(other.getName()) = " + this.name.equalsIgnoreCase(other.getName()));
//        boolean b2 = this.name.equalsIgnoreCase(other.getName());
//        System.out.println("this.country == other.getCountry() = " + (this.country == other.getCountry()));
//        boolean b3 = this.country == other.getCountry();
//        System.out.println("Objects.equals(this.gender, other.getGender()) = " + (Objects.equals(this.gender, other.getGender())));
//        boolean b4 = Objects.equals(this.gender, other.getGender());
//        System.out.println("Objects.equals(this.dob, other.getDob()) = " + (Objects.equals(this.dob, other.getDob())));
//        boolean b5 = Objects.equals(this.dob, other.getDob());
//        boolean result = b1 && b2 && b3 && b4 && b5;
//        System.out.println("result = " + result);
//        return result;
        return this.id == other.getId() &&
               this.name.equalsIgnoreCase(other.getName()) &&
               this.country == other.getCountry() &&
               Objects.equals(this.gender, other.getGender()) &&
               Objects.equals(this.dob, other.getDob());
    }
}
