/**
 * Created by himon on 16-12-12.
 */
public class Page {
    public String version;
    public String url;
    public String date;
    public String ip;
    public int unzip_length;
    public int length;
    public String body;

    public Page(String version, String url, String date, String ip, int unzip_length, int length, String body) {
        this.version = version;
        this.url = url;
        this.date = date;
        this.ip = ip;
        this.unzip_length = unzip_length;
        this.length = length;
        this.body = body;
    }

    public Page() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getUnzip_length() {
        return unzip_length;
    }

    public void setUnzip_length(int unzip_length) {
        this.unzip_length = unzip_length;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
