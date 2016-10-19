package com.manywho.services.huddle.api.entities;

import java.util.List;

public class User {
    private List<Link> links;
    private Profile profile;

    public List<Link> getLinks() {
        return links;
    }

    public Profile getProfile() {
        return profile;
    }

    public static class Profile {
        private Personal personal;
        private List<Contact> contacts;

        public Personal getPersonal() {
            return personal;
        }

        public List<Contact> getContacts() {
            return contacts;
        }

        public static class Personal {
            private String firstName;
            private String surname;
            private String displayName;

            public String getFirstName() {
                return firstName;
            }

            public String getSurname() {
                return surname;
            }

            public String getDisplayName() {
                return displayName;
            }
        }

        public static class Contact {
            private String rel;
            private String value;

            public String getRel() {
                return rel;
            }

            public String getValue() {
                return value;
            }
        }
    }
}
