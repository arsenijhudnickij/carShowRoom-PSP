    package main.models.entities;

    import java.time.LocalDate;
    import java.time.Period;

    public class User {
        private int userId;
        private String name;
        private String passportNum;
        private String gmail;
        private int birthDate;
        private int birthMonth;
        private int birthYear;
        private Person person;

        public User() {
        }

        public User(String name, String passportNum, int birthDate, int birthMonth, int birthYear, Person person,String gmail) {
            this.name = name;
            this.passportNum = passportNum;
            this.birthDate = birthDate;
            this.birthMonth = birthMonth;
            this.birthYear = birthYear;
            this.person = person;
            this.gmail = gmail;
        }

        public User(int userId, String name, String passportNum, int birthDate, int birthMonth, int birthYear, Person person,String gmail) {
            this.userId = userId;
            this.name = name;
            this.passportNum = passportNum;
            this.birthDate = birthDate;
            this.birthMonth = birthMonth;
            this.birthYear = birthYear;
            this.person = person;
            this.gmail = gmail;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassportNum() {
            return passportNum;
        }
        public void setGmail(String gmail) {
            this.gmail = gmail;
        }

        public String getGmail() {
            return this.gmail;
        }

        public void setPassportNum(String passportNum) {
            this.passportNum = passportNum;
        }

        public int getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(int birthDate) {
            this.birthDate = birthDate;
        }

        public int getBirthMonth() {
            return birthMonth;
        }

        public void setBirthMonth(int birthMonth) {
            this.birthMonth = birthMonth;
        }

        public int getBirthYear() {
            return birthYear;
        }

        public void setBirthYear(int birthYear) {
            this.birthYear = birthYear;
        }

        public Person getPerson() {
            return person;
        }

        public void setPerson(Person person) {
            this.person = person;
        }
        public String ageString() {
            LocalDate birthDate = LocalDate.of(this.birthYear, this.birthMonth, this.birthDate);
            LocalDate currentDate = LocalDate.now();

            Period period = Period.between(birthDate, currentDate);

            return period.getDays() + "." + period.getMonths() +  "." + period.getYears();
        }
    }