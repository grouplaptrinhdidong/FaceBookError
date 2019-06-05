package facebook.socialnetwork.nhom3.facebook;

public class Comments {
    public String comment, date, time, user;

    public Comments(String comment, String date, String time, String user) {
        this.comment = comment;
        this.date = date;
        this.time = time;
        this.user = user;
    }
    public Comments(){

    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
