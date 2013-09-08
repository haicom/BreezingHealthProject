package br.com.dina.ui.model;

public class UIHeaderItem {

    private String title;
    private boolean visibility;
    
    public UIHeaderItem(String title, boolean visibility) {
        this.title = title;
        this.visibility = visibility;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVisiable() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }
    
}
