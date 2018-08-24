package br.com.californiamobile.youfood.model;

public class Categoria {

    private String Name;
    private String Image;

    public Categoria() {
    }

    public Categoria(String Name, String Image) {
        this.Name = Name;
        this.Image = Image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }
}
