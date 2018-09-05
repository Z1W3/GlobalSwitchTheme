package cattt.assets.theme.library.base.parse;


import java.io.File;
import java.io.IOException;
import java.util.Vector;

import cattt.assets.theme.library.base.enums.ParseAssetsType;
import cattt.assets.theme.library.base.enums.ParseAssetsTypeClub;
import cattt.assets.theme.library.base.model.BackgroundsBean;
import cattt.assets.theme.library.base.model.ColorsBean;
import cattt.assets.theme.library.base.model.SelectorsBean;
import cattt.assets.theme.library.utils.logger.Log;


public class ParseAssetsRunnable implements Runnable {
    private Log logger = Log.getLogger(ParseAssetsRunnable.class);
    private File mParseFile;
    @ParseAssetsTypeClub
    private int mToken;
    private ParseAssetsHelper mHelper;

    public ParseAssetsRunnable(File outFile, @ParseAssetsTypeClub int token) {
        mHelper = new ParseAssetsHelper();
        this.mToken = token;
        this.mParseFile = new File(new StringBuffer().append(outFile.getPath()).append(token2Path(token)).toString());
    }

    @Override
    public void run() {
        switch (mToken) {
            case ParseAssetsType.COLORS:
                onParseColorsXml();
                break;
            case ParseAssetsType.SELECTORS:
                onParseSelectorsXml();
                break;
            case ParseAssetsType.BACKGROUNDS:
                onParseBackgroundsXml();
                break;
        }
    }

    private static String token2Path(@ParseAssetsTypeClub int token) {
        switch (token) {
            case ParseAssetsType.COLORS:
                return ParseAssetsHelper.ASSETS_FILE_NAMES[0];
            case ParseAssetsType.SELECTORS:
                return ParseAssetsHelper.ASSETS_FILE_NAMES[1];
            case ParseAssetsType.BACKGROUNDS:
                return ParseAssetsHelper.ASSETS_FILE_NAMES[2];
        }
        return "";
    }

    private void onParseColorsXml() {
        try {
            final Vector<ColorsBean> beans = mHelper.parseColorsXml(mParseFile);
            ParseAssetsMonitor.get().onColorAssets(beans);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.e("Finished onParseColorsXml");
    }

    private void onParseSelectorsXml() {
        try {
            final Vector<SelectorsBean> beans = mHelper.parseSelectorsXml(mParseFile);
            ParseAssetsMonitor.get().onSelectorAssets(beans);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.e("Finished onParseSelectorsXml");
    }

    private void onParseBackgroundsXml() {
        try {
            final Vector<BackgroundsBean> beans = mHelper.parseBackgroundsXml(mParseFile);
            ParseAssetsMonitor.get().onBackgroundAssets(beans);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.e("Finished onParseBackgroundsXml");
    }
}
