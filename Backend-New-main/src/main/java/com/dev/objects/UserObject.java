package com.dev.objects;

public class UserObject {
    @Entity
    @Table(name = "users")
    public class UserObject {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)

        @Column
        public int id;

        @Column
        private String username;

        @Column
        private String password;

        @Column
        private String token;

        @Column
        private boolean firstLogin;

        @ManyToMany
        private Set<OrganizationObject> organizations = new HashSet<>();

        public void addOrganization(OrganizationObject o) {
            organizations.add(o);
        }

        public void deleteOrganization(OrganizationObject o) {
            organizations.delete(o);
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isFirstLogin() {
            return firstLogin;
        }

        public void setFirstLogin(boolean firstLogin) {
            this.firstLogin = firstLogin;
        }

        public Set<OrganizationObject> getOrganizations() {
            return organizations;
        }

        public void setOrganizations(Set<OrganizationObject> organizations) {
            this.organizations = organizations;
        }
    }