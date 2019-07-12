package sample;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class Patient implements Serializable {

    private final String cod;
    private final String name;
    private final String surname;
    private final LocalDate birthDate;
    private final String birthTown;
    private String diagnosis = "";
    private ArrayList<Prescription> prescriptions;
    private ArrayList<Administration> administrations;
    private ArrayList<HeartBeat> HeartBeats = new ArrayList<>();
    private ArrayList<Temperature> Temperatures = new ArrayList<>();
    private ArrayList<Pressure> Pressures = new ArrayList<>();
    private boolean hospitalized;

    public Patient(String cod, String name, String surname, LocalDate birthDate, String birthTown) {
        this.cod = cod;
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.birthTown = birthTown;
        this.prescriptions = new ArrayList<Prescription>();
        this.administrations = new ArrayList<Administration>();
        this.hospitalized = true;
    }

    public void addPrescription(Prescription current){
        prescriptions.add(current);
    }

    public void addAdministration(Administration current){
        administrations.add(current);
    }

    public void addHeartBeat(int heartbeat){ HeartBeats.add(new HeartBeat(heartbeat)); }

    public void addTemperature(int temp) { Temperatures.add(new Temperature(temp)); }

    public void addPressure(int min, int max) { Pressures.add(new Pressure(min, max)); }

    public void setDiagnosis(String diagnosis){
        this.diagnosis = diagnosis;
    }

    public String getDiagnosis() { return diagnosis; }

    public void setHospitalization(boolean state){ hospitalized = state; }

    public String getFullName(){ return (surname + " " + name); }

    public String getCodFis(){ return cod; }

    public LocalDate getDate(){ return birthDate; }

    public String getBirthTown(){ return birthTown; }

    public boolean getHospitalization(){ return hospitalized; }

    public ArrayList<Prescription> getPrescriptions(){ return prescriptions; }

    @Override
    public boolean equals(Object other){
        return (other instanceof Patient) && (cod.equals(((Patient)other).cod)) && (name.equals(((Patient)other).name)) &&
                (surname.equals(((Patient)other).surname)) && (birthDate.equals(((Patient)other).birthDate)) &&
                (birthTown.equals(((Patient)other).birthTown));
    }

    @Override
    public String toString() {
        return "Patient [cod=" + cod + ", name=" + name + ", surname=" + surname + ", birthDate=" + birthDate.toString() + ", birthTown=" + birthTown + ", diagnosis=" + diagnosis + ",prescription=" + prescriptions.toString() + ", administrations=" + administrations.toString() + "]";
    }

    public void generateFakeData(){
        //pressione 2min ,temp 3 min, hb 5 min
        long current = new Date().getTime();
        long start = current - 604800000; //una settimana indietro

        for(current=current; current>start; current-=60000){
            long thiscurrent = current /1000;
            if (thiscurrent % 5 == 0)
                HeartBeats.add(new HeartBeat(randomData("hb"), current));
            if (thiscurrent % 3 == 0)
                Temperatures.add(new Temperature(randomData("temp"), current));
            if (thiscurrent % 2 == 0)
                Pressures.add(new Pressure(randomData("pressMin"), randomData("pressMax"), current));
        }

    }

    private int randomData(String type){
        Random rand = new Random();
        switch(type){
            case "hb": return rand.nextInt(70)+50;
            case "temp": return rand.nextInt(16)+25;
            case "pressMin": return rand.nextInt(110)+50;
            case "pressMax": return rand.nextInt(110)+70;
        }
        return 0;
    }
}
