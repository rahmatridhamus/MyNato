package my.mynato.rahmatridham.mynato.Model;

/**
 * Created by rahmatridham on 2/21/2017.
 */

public class SubtitleThematik {
    String id_sub_content_list,sub_title,content,url;

    public SubtitleThematik(String id_sub_content_list, String sub_title, String content, String url) {
        this.id_sub_content_list = id_sub_content_list;
        this.sub_title = sub_title;
        this.content = content;
        this.url = url;
    }

    public String getId_sub_content_list() {
        return id_sub_content_list;
    }

    public void setId_sub_content_list(String id_sub_content_list) {
        this.id_sub_content_list = id_sub_content_list;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}