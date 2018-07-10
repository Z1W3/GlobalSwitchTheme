package cattt.gst.library.base.model;

import android.os.Parcel;
import android.os.Parcelable;

import cattt.gst.library.base.model.em.MatchViewType;

public class GTData implements Parcelable {

    @MatchViewType
    private int type;
    private String content;

    public GTData(@MatchViewType int type, String content){
        this.type = type;
        this.content = content;
    }

    protected GTData(Parcel in) {
        type = in.readInt();
        content = in.readString();
    }

    public int getType() {
        return type;
    }

    public void setType(@MatchViewType int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static final Creator<GTData> CREATOR = new Creator<GTData>() {
        @Override
        public GTData createFromParcel(Parcel in) {
            return new GTData(in);
        }

        @Override
        public GTData[] newArray(int size) {
            return new GTData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(content);
    }
}
