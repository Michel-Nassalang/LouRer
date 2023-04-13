package sn.flexzone.lourer;


public class Objet {
    private int id;
    private String nom;

    private String description;
    private String statut; // perdu, retrouvé, Sur récompense
    private String username;
    private String adresse;

    private String email;
    private String tel;

    private String img;

    public Objet(int id, String nom, String statut, String username, String adresse, String tel) {
        this.id = id;
        this.nom = nom;
        this.statut = statut;
        this.username = username;
        this.adresse = adresse;
        this.tel = tel;
    }

    public Objet(String nom, String statut, String username, String adresse, String tel) {
        this.nom = nom;
        this.statut = statut;
        this.username = username;
        this.adresse = adresse;
        this.tel = tel;
    }
    public Objet(String nom, String statut, String username, String adresse) {
        this.nom = nom;
        this.statut = statut;
        this.username = username;
        this.adresse = adresse;
    }

    public Objet() {
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getStatut() {
        return statut;
    }

    public String getUser() {
        return username;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getTel() {
        return tel;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setUser(String username) {
        this.username = username;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "id: " + id + '\n' +
                "nom: " + nom + '\n' +
                "description: "+ description + '\n' +
                "statut: " + statut + '\n' +
                "user: " + username + '\n' +
                "adresse: " + adresse + '\n' +
                "email: " + email + '\n' +
                "tel: " + tel + '\n';
    }
}
