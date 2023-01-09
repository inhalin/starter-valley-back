package startervalley.backend.entity;

public enum InquiryTarget {
    DEVELOPERS("개발자"),
    MANAGERS("운영진");

    private final String name;

    InquiryTarget(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}


//
//public enum Gender {
//    MALE(Constants.MALE_VALUE), FEMALE(Constants.FEMALE_VALUE);
//
//    Gender(String genderString) {
//    }
//
//    public static class Constants {
//        public static final String MALE_VALUE = "MALE";
//        public static final String FEMALE_VALUE = "FEMALE";
//    }
//}