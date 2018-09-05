package cattt.assets.theme.library.base.storage;

import java.io.File;

public class ChoiceBean {
    private String sha1;
    private File folder;

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public File getFolder() {
        return folder;
    }

    public void setFolder(File folder) {
        this.folder = folder;
    }
}
