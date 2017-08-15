package trizio.ram.com.trizioservireencauche;

/**
 * Created by MONO on 21/06/2017.
 */

public class ItemCheck {

    String code = null;
    String name = null;
    String cant = null;
    String val = null;
    String[] prices;
    boolean selected = false;
    int listaPrice;

    public ItemCheck(String code, String name, boolean selected, String cant, String val, String[] prices) {
        super();
        this.code = code;
        this.name = name;
        this.cant = cant;
        this.val = val;
        this.prices = prices;

        this.selected = selected;
    }

    public String getCode() {
        return code;
    }
    public String getCant() {
        return cant;
    }
    public String getVal() {
        return val;
    }
    public String[] getPrices() {
        return prices;
    }
    public int getListaPrice() {
        return listaPrice + 1;
    }
    public void setListaPrice(int i) {
         this.listaPrice = i;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public void setCant(String cant) {
        this.cant = cant;
    }
    public void setVal(String val) {
        this.val = val;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }


}
