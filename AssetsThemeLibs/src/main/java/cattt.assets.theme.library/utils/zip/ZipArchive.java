package cattt.assets.theme.library.utils.zip;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.util.concurrent.Executors;

import cattt.assets.theme.library.utils.zip.callback.OnUnzipListener;
import cattt.assets.theme.library.utils.zip.model.Zipable;

public class ZipArchive implements Zipable {

    @Override
    public void zip(String targetPath, String destinationFilePath, String password) throws Exception {
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

        if (password.length() > 0) {
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
            parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
            parameters.setPassword(password);
        }

        ZipFile zipFile = new ZipFile(destinationFilePath);
        File targetFile = new File(targetPath);
        if (targetFile.isFile()) {
            zipFile.addFile(targetFile, parameters);
        } else if (targetFile.isDirectory()) {
            zipFile.addFolder(targetFile, parameters);
        }
    }

    @Override
    public void unzip(String targetZipFilePath, String destinationFolderPath, OnUnzipListener listener) throws Exception {
        this.unzip(targetZipFilePath, destinationFolderPath, null, listener);
    }

    @Override
    public void unzip(String targetZipFilePath, String destinationFolderPath, String password, OnUnzipListener listener) throws Exception {
        ZipFile zipFile = new ZipFile(targetZipFilePath);
        zipFile.setFileNameCharset("UTF-8");
        if (!zipFile.isValidZipFile()) {
            throw new ZipException("The compressed file is illegal and may be corrupted.");
        }
        zipFile.setRunInThread(true);
        if (zipFile.isEncrypted()) {
            zipFile.setPassword(password);
        }
        zipFile.extractAll(destinationFolderPath);
        if (listener != null) {
            Executors.newCachedThreadPool().execute(new ZipProgressRunnable(zipFile.getProgressMonitor(), new UnzipProgressMonitor(new File(targetZipFilePath), new File(destinationFolderPath), listener)));
        }
    }
}
