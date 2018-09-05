package cattt.assets.theme.library.utils.zip.model;

import cattt.assets.theme.library.utils.zip.callback.OnUnzipListener;

public interface Zipable {
    void zip(String targetPath, String destinationFilePath, String password) throws Exception;
    void unzip(String targetZipFilePath, String destinationFolderPath, OnUnzipListener listener) throws Exception;
    void unzip(String targetZipFilePath, String destinationFolderPath, String password, OnUnzipListener listener) throws Exception;
}
