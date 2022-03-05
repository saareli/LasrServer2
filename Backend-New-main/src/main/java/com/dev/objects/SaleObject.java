package com.dev.objects;
import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "sales")
public class SaleObject
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column
    private int id;

    @Column
    private Date saleStart;

    @Column
    private Date saleEnd;

    @Column
    private boolean forAllUsers;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "storeId")
    private StoreObject store;

    @ManyToMany
    private Set<OrganizationObject> organizations = new HashSet<>();


    public int getId() {return id;}

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Date getSaleStart() {
        return saleStart;
    }

    public void setSaleStart(Date saleStart) {
        this.saleStart = saleStart;
    }

    public Date getSaleEnd() {
        return saleEnd;
    }

    public void setSaleEnd(Date saleEnd) {
        this.saleEnd = saleEnd;
    }

    public boolean isForAllUsers() {
        return forAllUsers;
    }

    public void setForAllUsers(boolean forAllUsers) {
        this.forAllUsers = forAllUsers;
    }

    public Set<OrganizationObject> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Set<OrganizationObject> organizations) {
        this.organizations = organizations;
    }

    public void addOrganization(OrganizationObject organ) {
        organizations.add(organ);
    }

    public void deleteOrganization(OrganizationObject organ) {
        organizations.delte(organ);
    }

    public StoreObject getStore() {
        return store;
    }

    public void setStore(StoreObject store) {
        this.store = store;
    }

    public StoreObject getStore() {
        return store;
    }

    public void setStore(StoreObject store) {
        this.store = store
    }
}