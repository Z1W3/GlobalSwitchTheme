package cattt.assets.theme.library.base.parse;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.ArrayMap;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.Executors;

import cattt.assets.theme.library.base.enums.DrawableStates;
import cattt.assets.theme.library.base.enums.ParseAssetsType;
import cattt.assets.theme.library.base.model.BackgroundsBean;
import cattt.assets.theme.library.base.model.ColorsBean;
import cattt.assets.theme.library.base.model.SelectorsBean;
import cattt.assets.theme.library.base.model.StateDrawableBean;
import cattt.assets.theme.library.base.storage.AssetsStorage;
import cattt.assets.theme.library.utils.logger.Log;

public class ParseAssetsHelper {
    private Log logger = Log.getLogger(ParseAssetsHelper.class);
    private static final String FIXED_COLOR_LABEL = "@color/";
    private static final String FIXED_DRAWABLE_LABEL = "@drawable/";
    private static final String DRAWABLE_FOLDER = "/drawable/";
    private static final ArrayMap<String, Integer> STATE_DRAWABLE_MAP = new ArrayMap<>();


    /**
     * 所有资产文件的文件名
     */
    public static final String[] ASSETS_FILE_NAMES = {
            "/colors.xml",
            "/selectors.xml",
            "/backgrounds.xml",
    };

    private static final String[][] ASSETS_TAGS = {
            /*colors.xml*/
            {"color", "color-array", "item"},
            /*selectors.xml*/
            {"selector-array", "item"},
            /*backgrounds.xml*/
            {"background"},
    };

    private static final String ID_ATTRIBUTE_NAME = "id";


    private static final String COLOR_ATTRIBUTE_NAME = "name";

    private static final String COLOR_ARRAY_ITEM_ATTRIBUTE_NAME = "type";

    private static final String[] COLOR_ARRAY_ITEM_ATTRIBUTE_VALUES = {
            "backgroundColor",
            "textColor",
            "hintColor"
    };

    public ParseAssetsHelper() {
        initStateDrawableMap();
    }

    private void initStateDrawableMap() {
        STATE_DRAWABLE_MAP.put(DrawableStates.STATE_ABOVE_ANCHOR, android.R.attr.state_above_anchor);
        STATE_DRAWABLE_MAP.put(DrawableStates.STATE_ACCELERATED, android.R.attr.state_accelerated);
        STATE_DRAWABLE_MAP.put(DrawableStates.STATE_ACTIVATED, android.R.attr.state_activated);
        STATE_DRAWABLE_MAP.put(DrawableStates.STATE_ACTIVE, android.R.attr.state_active);
        STATE_DRAWABLE_MAP.put(DrawableStates.STATE_CHECKABLE, android.R.attr.state_checkable);
        STATE_DRAWABLE_MAP.put(DrawableStates.STATE_CHECKED, android.R.attr.state_checked);
        STATE_DRAWABLE_MAP.put(DrawableStates.STATE_DRAG_CAN_ACCEPT, android.R.attr.state_drag_can_accept);
        STATE_DRAWABLE_MAP.put(DrawableStates.STATE_DRAG_HOVERED, android.R.attr.state_drag_hovered);
        STATE_DRAWABLE_MAP.put(DrawableStates.STATE_EMPTY, android.R.attr.state_empty);
        STATE_DRAWABLE_MAP.put(DrawableStates.STATE_ENABLED, android.R.attr.state_enabled);
        STATE_DRAWABLE_MAP.put(DrawableStates.STATE_EXPANDED, android.R.attr.state_expanded);
        STATE_DRAWABLE_MAP.put(DrawableStates.STATE_FIRST, android.R.attr.state_first);
        STATE_DRAWABLE_MAP.put(DrawableStates.STATE_FOCUSED, android.R.attr.state_focused);
        STATE_DRAWABLE_MAP.put(DrawableStates.STATE_HOVERED, android.R.attr.state_hovered);
        STATE_DRAWABLE_MAP.put(DrawableStates.STATE_LAST, android.R.attr.state_last);
        STATE_DRAWABLE_MAP.put(DrawableStates.STATE_LONG_PRESSABLE, android.R.attr.state_long_pressable);
        STATE_DRAWABLE_MAP.put(DrawableStates.STATE_MIDDLE, android.R.attr.state_middle);
        STATE_DRAWABLE_MAP.put(DrawableStates.STATE_MULTILINE, android.R.attr.state_multiline);
        STATE_DRAWABLE_MAP.put(DrawableStates.STATE_PRESSED, android.R.attr.state_pressed);
        STATE_DRAWABLE_MAP.put(DrawableStates.STATE_SELECTED, android.R.attr.state_selected);
        STATE_DRAWABLE_MAP.put(DrawableStates.STATE_SINGLE, android.R.attr.state_single);
        STATE_DRAWABLE_MAP.put(DrawableStates.STATE_WINDOW_FOCUSED, android.R.attr.state_window_focused);
    }

    /**
     * 解析colors.xml中的内容
     */
    public Vector<ColorsBean> parseColorsXml(File target) throws IOException {
        if (target == null) {
            throw new NullPointerException("Can't be null.");
        }
        return this.parseColorsXml(new FileInputStream(target));
    }

    /**
     * 解析colors.xml中的内容
     */
    private Vector<ColorsBean> parseColorsXml(FileInputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new NullPointerException("Can't be null.");
        }
        final Vector<ColorsBean> beans = new Vector<>();
        try {
            final XmlPullParser xml = getXmlPullParser(inputStream);
            int eventType = xml.getEventType();
            final ArrayMap<String, String> colorsMap = new ArrayMap<>();
            ColorsBean colorsBean = null;
            while (XmlPullParser.END_DOCUMENT != eventType) {
                final String tag = xml.getName() != null ? xml.getName() : "";
                if (eventType == XmlPullParser.START_TAG && /*Node is <color>*/tag.equals(ASSETS_TAGS[0][0])) {
                    final String name = xml.getAttributeValue(null, COLOR_ATTRIBUTE_NAME);
                    final String colorStr = xml.nextText();
                    colorsMap.put(name, colorStr);
                }
                if (eventType == XmlPullParser.START_TAG && /*Node is <color-array>*/tag.equals(ASSETS_TAGS[0][1])) {
                    final String id = xml.getAttributeValue(null, ID_ATTRIBUTE_NAME);
                    colorsBean = new ColorsBean();
                    colorsBean.setId(id);
                }
                if (eventType == XmlPullParser.START_TAG && /*Node is <item>*/tag.equals(ASSETS_TAGS[0][2])) {
                    final String type = xml.getAttributeValue(null, COLOR_ARRAY_ITEM_ATTRIBUTE_NAME);
                    final String colorText = xml.nextText();
                    final String colorKey = colorText.contains(FIXED_COLOR_LABEL) ? colorText.substring(colorText.lastIndexOf(FIXED_COLOR_LABEL) + FIXED_COLOR_LABEL.length()) : "";
                    final Object colorValue = colorsMap.get(colorKey);
                    final int color = colorValue != null ? Color.parseColor((String) colorValue) : 0;
                    if (/*type of value is backgroundColor*/ type.equals(COLOR_ARRAY_ITEM_ATTRIBUTE_VALUES[0])) {
                        colorsBean.setBackgroundColor(color);
                    }
                    if (/*type of value is textColor*/ type.equals(COLOR_ARRAY_ITEM_ATTRIBUTE_VALUES[1])) {
                        colorsBean.setTextColor(color);
                    }
                    if (/*type of value is hintColor*/type.equals(COLOR_ARRAY_ITEM_ATTRIBUTE_VALUES[2])) {
                        colorsBean.setHintColor(color);
                    }
                }
                if (eventType == XmlPullParser.END_TAG
                        && /*Node is <color-array>*/tag.equals(ASSETS_TAGS[0][1])) {
                    beans.add(colorsBean);
                }
                eventType = xml.next();
            }
        } catch (XmlPullParserException ex) {
            ex.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return beans;
    }


    /**
     * 解析selectors.xml中的内容
     */
    public Vector<SelectorsBean> parseSelectorsXml(File target) throws IOException {
        if (target == null) {
            throw new NullPointerException("Can't be null.");
        }
        return this.parseSelectorsXml(new FileInputStream(target), target.getPath());
    }


    /**
     * 解析selectors.xml中的内容
     */
    private Vector<SelectorsBean> parseSelectorsXml(FileInputStream inputStream, String path) throws IOException {
        if (inputStream == null || TextUtils.isEmpty(path)) {
            throw new NullPointerException("Can't be null.");
        }
        final Vector<SelectorsBean> beans = new Vector<>();
        final String rootPath = path.substring(0, path.lastIndexOf("/"));
        try {
            final XmlPullParser xml = getXmlPullParser(inputStream);
            int eventType = xml.getEventType();
            SelectorsBean selectorsBean = null;
            ArrayList<StateDrawableBean> stateDrawables = null;
            while (XmlPullParser.END_DOCUMENT != eventType) {
                final String tag = xml.getName() != null ? xml.getName() : "";
                if (eventType == XmlPullParser.START_TAG && /*Node is <selector-array> */ tag.equals(ASSETS_TAGS[1][0])) {
                    final String id = xml.getAttributeValue(null, ID_ATTRIBUTE_NAME);
                    stateDrawables = new ArrayList<>();
                    selectorsBean = new SelectorsBean();
                    selectorsBean.setId(id);
                }
                if (eventType == XmlPullParser.START_TAG && /*Node is <item> */tag.equals(ASSETS_TAGS[1][1])) {
                    final StateDrawableBean stateDrawableBean = new StateDrawableBean();
                    final int attributeCount = xml.getAttributeCount();
                    final ArrayList<Integer> stateSetList = new ArrayList<>(attributeCount);
                    for (int index = 0; index < attributeCount; index++) {
                        final String stateName = xml.getAttributeName(index);
                        if (STATE_DRAWABLE_MAP.get(stateName) != null) {
                            final boolean stateBool = Boolean.valueOf(xml.getAttributeValue(null, stateName));
                            stateSetList.add(stateBool ? STATE_DRAWABLE_MAP.get(stateName) : -STATE_DRAWABLE_MAP.get(stateName));
                        }
                    }
                    stateDrawableBean.setStateSets(ParseAssetsHelper.integerList2IntArray(stateSetList));
                    final String drawText = xml.nextText();
                    if (drawText.contains(FIXED_DRAWABLE_LABEL)) {
                        final String drawName = drawText.substring(drawText.lastIndexOf(FIXED_DRAWABLE_LABEL) + FIXED_DRAWABLE_LABEL.length());
                        final File drawFile = new File(new StringBuffer().append(rootPath).append(DRAWABLE_FOLDER).append(drawName).toString());
                        stateDrawableBean.setDrawable(drawFile);
                        stateDrawables.add(stateDrawableBean);
                    }
                }
                if (eventType == XmlPullParser.END_TAG && /*Node is <selector-array> */ tag.equals(ASSETS_TAGS[1][0])) {
                    selectorsBean.setStateDrawables(stateDrawables != null ? stateDrawables : new ArrayList<StateDrawableBean>(0));
                    beans.add(selectorsBean);
                }
                eventType = xml.next();
            }
        } catch (XmlPullParserException ex) {
            ex.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return beans;
    }

    /**
     * 解析backgrounds.xml中的内容
     */
    public Vector<BackgroundsBean> parseBackgroundsXml(File target) throws IOException {
        if (target == null) {
            throw new NullPointerException("Can't be null.");
        }
        return this.parseBackgroundsXml(new FileInputStream(target), target.getPath());
    }

    /**
     * 解析backgrounds.xml中的内容
     */
    private Vector<BackgroundsBean> parseBackgroundsXml(FileInputStream inputStream, String path) throws IOException {
        if (inputStream == null || TextUtils.isEmpty(path)) {
            throw new NullPointerException("Can't be null.");
        }
        final Vector<BackgroundsBean> beans = new Vector<>();
        final String rootPath = path.substring(0, path.lastIndexOf("/"));
        try {
            final XmlPullParser xml = getXmlPullParser(inputStream);
            int eventType = xml.getEventType();
            BackgroundsBean backgroundBean = null;
            while (XmlPullParser.END_DOCUMENT != eventType) {
                final String tag = xml.getName() != null ? xml.getName() : "";
                if (eventType == XmlPullParser.START_TAG && /*Node is <background> */ tag.equals(ASSETS_TAGS[2][0])) {
                    final String id = xml.getAttributeValue(null, ID_ATTRIBUTE_NAME);
                    final String drawText = xml.nextText();
                    if (drawText.contains(FIXED_DRAWABLE_LABEL)) {
                        final String drawName = drawText.substring(drawText.lastIndexOf(FIXED_DRAWABLE_LABEL) + FIXED_DRAWABLE_LABEL.length());
                        final File drawFile = new File(new StringBuffer().append(rootPath).append(DRAWABLE_FOLDER).append(drawName).toString());
                        backgroundBean = new BackgroundsBean();
                        backgroundBean.setId(id);
                        backgroundBean.setDrawable(drawFile);
                        beans.add(backgroundBean);
                    }
                }
                eventType = xml.next();
            }
        } catch (XmlPullParserException ex) {
            ex.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return beans;
    }

    private XmlPullParser getXmlPullParser(FileInputStream inputStream) throws XmlPullParserException {
        final XmlPullParser xml = XmlPullParserFactory.newInstance().newPullParser();
        xml.setInput(inputStream, "utf-8");
        return xml;
    }

    private static int[] integerList2IntArray(ArrayList<Integer> list) {
        final int[] array = new int[list.size()];
        for (int index = 0; index < list.size(); index++) {
            array[index] = list.get(index);
        }
        return array;
    }

    public static void startAsyncParseAssetsXml(@NonNull Context context, @NonNull String sha1, @NonNull File outFile) {
        if (context == null || TextUtils.isEmpty(sha1) || !(outFile != null && outFile.exists())) {
            throw new NullPointerException("Params cannot be null or directory does not exist.");
        }
        AssetsStorage.saveChoiceBean(context, sha1, outFile);
        Executors.newCachedThreadPool().execute(new ParseAssetsRunnable(outFile, ParseAssetsType.COLORS));
        Executors.newCachedThreadPool().execute(new ParseAssetsRunnable(outFile, ParseAssetsType.SELECTORS));
        Executors.newCachedThreadPool().execute(new ParseAssetsRunnable(outFile, ParseAssetsType.BACKGROUNDS));
    }
}