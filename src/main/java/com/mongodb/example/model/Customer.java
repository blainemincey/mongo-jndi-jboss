package com.mongodb.example.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private int version;

    @NotNull
    private String fullName;

    @Min(0)
    private int age;

    /**
     *
     */
    public Customer() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (version != customer.version) return false;
        if (age != customer.age) return false;
        if (!id.equals(customer.id)) return false;
        return fullName.equals(customer.fullName);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + version;
        result = 31 * result + fullName.hashCode();
        result = 31 * result + age;
        return result;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", version=" + version +
                ", fullName='" + fullName + '\'' +
                ", age=" + age +
                '}';
    }
}