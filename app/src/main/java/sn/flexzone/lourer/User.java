package sn.flexzone.lourer;

public class User {
    private String id;
    private String prenom;
    private String nom;
    private String pseudo;
    private String adresse;
    private String tel;
    private String email;
    private int points;
    private int age;
    private String adresseplus;
    private String image;
    private String password;

    public User(String id, String prenom, String nom, String pseudo, String adresse, String tel, String email, int points, int age, String image) {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.pseudo = pseudo;
        this.adresse = adresse;
        this.tel = tel;
        this.email = email;
        this.points = points;
        this.age = age;
        this.image = image;
    }

    public User(String prenom, String nom, String pseudo, String adresse, String tel, String email, int age) {
        this.prenom = prenom;
        this.nom = nom;
        this.pseudo = pseudo;
        this.adresse = adresse;
        this.tel = tel;
        this.email = email;
        this.age = age;
    }

    public User(String id, String prenom, String nom, String pseudo, String tel, String email) {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.pseudo = pseudo;
        this.tel = tel;
        this.email = email;
    }
    public User(String prenom, String nom, String pseudo, String tel, String email) {
        this.prenom = prenom;
        this.nom = nom;
        this.pseudo = pseudo;
        this.tel = tel;
        this.email = email;
    }


    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAdresseplus() {
        return adresseplus;
    }

    public void setAdresseplus(String adresseplus) {
        this.adresseplus = adresseplus;
    }
}
