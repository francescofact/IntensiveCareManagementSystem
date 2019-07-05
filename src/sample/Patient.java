package sample;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class Patient implements Serializable {

    private final String cod;
    private final String name;
    private final String surname;
    private final Date birthDate;
    private final String birthTown;
    private final String diagnosis;
    private ArrayList<Prescription> prescriptions;
    private ArrayList<Administration> administrations;

    public Patient(String cod, String name, String surname, Date birthDate, String birthTown, String diagnosis) {
        this.cod = cod;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.birthTown = birthTown;
        this.diagnosis = diagnosis;
        this.prescriptions = new ArrayList<Prescription>();
        this.administrations = new ArrayList<Administration>();
    }

    public void addPrescription(Prescription current){
        prescriptions.add(current);
    }

    public void addAdministration(Administration current){
        administrations.add(current);
    }

    @Override
    public boolean equals(Object other){
        return (other instanceof Patient) && (cod.equals(((Patient)other).cod)) && (name.equals(((Patient)other).name)) &&
                (surname.equals(((Patient)other).surname)) && (birthDate.equals(((Patient)other).birthDate)) &&
                (birthTown.equals(((Patient)other).birthTown));
    }

    @Override
    public String toString() {
        return "Patient [cod=" + cod + ", name=" + name + ", surname=" + surname + ", birthDate=" + birthDate.toString() + ", birthTown=" + birthTown + ", diagnosis=" + diagnosis + ", administrations=" + administrations.toString() + "]";
    }
}
