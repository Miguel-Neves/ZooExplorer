package com.cm.zooexplorer.models;

public class Habitat {
    private String id, binName, consStatus, diet, natHabitat, species, imageUrl, imageName;
    private int f_lifeExpectancy, gestPeriod, m_lifeExpectancy, offsprings;
    private double f_size, f_weight, m_size, m_weight, matAge;
    private Location location;
    //CollectionReference photoCollection;


    public Habitat(String id, String binName, String consStatus, String diet, int f_lifeExpectancy,
                   double f_size, double f_weight, int gestPeriod, Location location,
                   int m_lifeExpectancy, double m_size, double m_weight, double matAge,
                   String natHabitat, int offsprings, String species, String imageUrl, String imageName) {
        this.id = id;
        this.binName = binName;
        this.consStatus = consStatus;
        this.diet = diet;
        this.f_lifeExpectancy = f_lifeExpectancy;
        this.f_size = f_size;
        this.f_weight = f_weight;
        this.gestPeriod = gestPeriod;
        this.location = location;
        this.m_lifeExpectancy = m_lifeExpectancy;
        this.m_size = m_size;
        this.m_weight = m_weight;
        this.matAge = matAge;
        this.natHabitat = natHabitat;
        this.offsprings = offsprings;
        this.species = species;
        this.imageUrl = imageUrl;
        this.imageName = imageName;
    }

    public String getId() {
        return id;
    }

    public String getBinName() {
        return binName;
    }

    public String getConsStatus() {
        return consStatus;
    }

    public String getDiet() {
        return diet;
    }

    public int getF_lifeExpectancy() {
        return f_lifeExpectancy;
    }

    public double getF_size() {
        return f_size;
    }

    public double getF_weight() {
        return f_weight;
    }

    public int getGestPeriod() {
        return gestPeriod;
    }

    public Location getLocation() {
        return location;
    }

    public int getM_lifeExpectancy() {
        return m_lifeExpectancy;
    }

    public double getM_size() {
        return m_size;
    }

    public double getM_weight() {
        return m_weight;
    }

    public double getMatAge() {
        return matAge;
    }

    public String getNatHabitat() {
        return natHabitat;
    }

    public int getOffsprings() {
        return offsprings;
    }

    public String getSpecies() {
        return species;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBinName(String binName) {
        this.binName = binName;
    }

    public void setConsStatus(String consStatus) {
        this.consStatus = consStatus;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public void setF_lifeExpectancy(int f_lifeExpectancy) {
        this.f_lifeExpectancy = f_lifeExpectancy;
    }

    public void setF_size(double f_size) {
        this.f_size = f_size;
    }

    public void setF_weight(double f_weight) {
        this.f_weight = f_weight;
    }

    public void setGestPeriod(int gestPeriod) {
        this.gestPeriod = gestPeriod;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setM_lifeExpectancy(int m_lifeExpectancy) {
        this.m_lifeExpectancy = m_lifeExpectancy;
    }

    public void setM_size(double m_size) {
        this.m_size = m_size;
    }

    public void setM_weight(double m_weight) {
        this.m_weight = m_weight;
    }

    public void setMatAge(double matAge) {
        this.matAge = matAge;
    }

    public void setNatHabitat(String natHabitat) {
        this.natHabitat = natHabitat;
    }

    public void setOffsprings(int offsprings) {
        this.offsprings = offsprings;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
