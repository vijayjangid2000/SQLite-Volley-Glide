package com.vijayjangid.cubereum;

import java.util.Comparator;

public class ApiData {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String avatarLink;

    public ApiData(String id, String email, String firstName, String lastName, String avatarLink) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatarLink = avatarLink;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAvatarLink() {
        return avatarLink;
    }

    @Override
    public String toString() {
        return "ApiData{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", avatarLink='" + avatarLink + '\'' +
                '}';
    }

    public static Comparator<ApiData> apiDataComparator = new Comparator<ApiData>() {
        @Override
        public int compare(ApiData o1, ApiData o2) {

            String name1 = o1.getFirstName() + " " + o1.getLastName();
            String name2 = o2.getFirstName() + " " + o2.getLastName();

            return name1.compareTo(name2);
        }
    };

}
