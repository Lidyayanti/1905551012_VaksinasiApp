package id.winata.vaksinasi.MyRetrofit.ServerResponseObject.SpecificServerResponse;

import id.winata.vaksinasi.MyRetrofit.ServerResponseObject.DeffaultServerResponse;

public class UserDataServerResponse extends DeffaultServerResponse {
    public Data data;

    public static class Data{
        public String name = "";
        public String username = "";
        public String email = "";
        public String password = "";
        public String password_confirmation = "";
        public String user_picture = "";
        public String user_token = "";

        public String getUser_token() {
            return user_token;
        }

        public void setUser_token(String user_token) {
            this.user_token = user_token;
        }

        public String getUser_picture() {
            return user_picture;
        }

        public void setUser_picture(String user_picture) {
            this.user_picture = user_picture;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPassword_confirmation() {
            return password_confirmation;
        }

        public void setPassword_confirmation(String password_confirmation) {
            this.password_confirmation = password_confirmation;
        }
    }
}
