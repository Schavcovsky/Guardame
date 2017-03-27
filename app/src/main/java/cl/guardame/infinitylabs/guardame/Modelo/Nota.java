package cl.guardame.infinitylabs.guardame.Modelo;

/**
 * Created by aleja on 29/07/2016.
 */
public class Nota {

    private String note_id;
    private String title;
    private String content;
    private String note_update_date;

    public Nota(String note_id, String title, String content, String note_update_date) {
        this.note_id = note_id;
        this.title = title;
        this.content = content;
        this.note_update_date = note_update_date;
    }

    public String getNote_id() {
        return note_id;
    }

    public void setNote_id(String note_id) {
        this.note_id = note_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNote_update_date() {
        return note_update_date;
    }

    public void setNote_update_date(String note_update_date) {
        this.note_update_date = note_update_date;
    }
}
