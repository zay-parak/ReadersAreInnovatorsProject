package models;

public class Approval {
        private Integer id;
        private String username;
        private String argument;
        private Boolean yesNo;

        public Approval() {}

        public Approval(Integer userID, String username, String argument) {
            this.id = userID;
            this.username = username;
            this.argument = argument;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getArgument() {
            return argument;
        }

        public void setArgument(String argument) {
            this.argument = argument;
        }

        public Boolean getYesNo() {
            return yesNo;
        }

        public void setYesNo(Boolean yesNo) {
            this.yesNo = yesNo;
        }



    }

