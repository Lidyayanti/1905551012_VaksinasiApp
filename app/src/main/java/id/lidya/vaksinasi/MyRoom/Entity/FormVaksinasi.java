package id.lidya.vaksinasi.MyRoom.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FormVaksinasi {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String fullname;
    public String username;
    public String email;
    public String kebutuhan;
    public boolean hamil = false;
    public boolean ispa = false;
    public boolean alergi = false;
    public boolean jantung = false;
    public boolean ginjal = false;
    public String gender;
    public String usia;
    public String image;
    public int rating;
    public String password;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUsia() {
        return usia;
    }

    public void setUsia(String usia) {
        this.usia = usia;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isHamil() {
        return hamil;
    }

    public void setHamil(boolean hamil) {
        this.hamil = hamil;
    }

    public boolean isIspa() {
        return ispa;
    }

    public void setIspa(boolean ispa) {
        this.ispa = ispa;
    }

    public boolean isAlergi() {
        return alergi;
    }

    public void setAlergi(boolean alergi) {
        this.alergi = alergi;
    }

    public boolean isJantung() {
        return jantung;
    }

    public void setJantung(boolean jantung) {
        this.jantung = jantung;
    }

    public boolean isGinjal() {
        return ginjal;
    }

    public void setGinjal(boolean ginjal) {
        this.ginjal = ginjal;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKebutuhan() {
        return kebutuhan;
    }

    public void setKebutuhan(String kebutuhan) {
        this.kebutuhan = kebutuhan;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
