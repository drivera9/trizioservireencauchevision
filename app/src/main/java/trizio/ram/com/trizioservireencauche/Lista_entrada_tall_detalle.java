package trizio.ram.com.trizioservireencauche;

/*
 * Decompiled with CFR 0_92.
 *
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */

public class Lista_entrada_tall_detalle {
    private int idImagen;
    private String textoEncima;
    private String id ;

    public Lista_entrada_tall_detalle(int n, String string, String id) {
        this.idImagen = n;
        this.textoEncima = string;
        this.id = id;
    }

    public int get_idImagen() {
        return this.idImagen;
    }

    public String get_textoEncima() {
        return this.textoEncima;
    }

    public String getId() {
        return this.id;
    }
}
