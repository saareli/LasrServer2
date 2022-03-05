package com.dev.objects;
import javax.persistence.*;

@Entity
@Table(name = "organization")
public class OrganizationObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column (name = "id")
    private int id;

    @Column (name = "name", unique = true)
    private String name;

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getName() {return name;}

    public Set<UserObject> getUsers() {return users;}

    public void setName(String name) {this.name = name;}
}