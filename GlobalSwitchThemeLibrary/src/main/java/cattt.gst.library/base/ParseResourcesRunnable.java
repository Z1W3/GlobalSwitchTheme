package cattt.gst.library.base;


import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Vector;

import cattt.gst.library.base.model.GTData;
import cattt.gst.library.base.model.emdata.ViewType;


public class ParseResourcesRunnable implements Runnable {
    private static final Vector<String> FOLDER = new Vector<>(Arrays.asList(
            "background", "image"
    ));

    private static final Vector<String> TYPE_COLOR = new Vector<>(Arrays.asList(
            "background", "text", "hint"
    ));

    private static final Vector<String> IMAGE_SUFFIX_NAME = new Vector<>(Arrays.asList(
            ".jpg", ".png"
    ));

    private static final Vector<String> COLOR_SUFFIX_NAME = new Vector<>(Arrays.asList(
            ".xml"
    ));

    private File target;
    private EventHandler handler = new EventHandler();

    public ParseResourcesRunnable(File target) {
        this.target = target;
    }

    @Override
    public void run() {
        ArrayMap<String, Vector<GTData>> map = new ArrayMap<>();
        for (String path : getTargetPaths(target)) {
            String parentPath = new File(path).getParentFile().getAbsolutePath();
            String parentFolderName = parentPath.substring(parentPath.lastIndexOf("/") + 1);
            String suffixName = path.substring(path.lastIndexOf("."));
            if (IMAGE_SUFFIX_NAME.contains(suffixName) && FOLDER.contains(parentFolderName)) {
                String name = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
                Vector<GTData> mVector = getVector(name, map);
                //FOLDER.get(0) = "background"
                //FOLDER.get(1) = "image"
                int type = FOLDER.get(0).equalsIgnoreCase(parentFolderName) ? ViewType.TYPE_BACKGROUND_DRAWABLE : ViewType.TYPE_IMAGE_DRAWABLE;
                mVector.add(new GTData(type, path));
                map.put(name, mVector);
            }
            if (COLOR_SUFFIX_NAME.contains(suffixName)) {
                try {
                    map.putAll(xmlParser(new FileInputStream(new File(path))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        handler.obtainMessage(0, map).sendToTarget();
    }

    private Vector<GTData> getVector(String key, ArrayMap<String, Vector<GTData>> map) {
        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            return new Vector<>();
        }
    }

    private String[] getTargetPaths(File target) {
        Vector<String> vector = new Vector<>();
        recursionFetchFiles(target, vector);
        return vector.toArray(new String[vector.size()]);
    }

    private void recursionFetchFiles(File dir, Vector<String> vector) {
        if (dir.exists()) {
            if (dir.isDirectory()) {
                File[] files = dir.listFiles();
                for (File file : files) {
                    recursionFetchFiles(file, vector);
                }
            } else {
                vector.add(dir.getAbsolutePath());
            }
        }
    }

    public ArrayMap<String, Vector<GTData>> xmlParser(InputStream in) throws XmlPullParserException, IOException {
        ArrayMap<String, Vector<GTData>> map = new ArrayMap<>();
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in, "utf-8");
        int eventType = parser.getEventType();
        while (XmlPullParser.END_DOCUMENT != eventType) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    try {
                        String name = parser.getAttributeValue(0);
                        String typeAttr = parser.getAttributeValue(1);
                        Vector<GTData> mVector = getVector(name, map);
                        if (TYPE_COLOR.contains(typeAttr)) {
                            // TYPE_COLOR.get(0) = "background",
                            // TYPE_COLOR.get(1) = "text",
                            // TYPE_COLOR.get(2) = "hint"
                            int type = TYPE_COLOR.get(0).equalsIgnoreCase(typeAttr) ?
                                    ViewType.TYPE_BACKGROUND_COLOR : TYPE_COLOR.get(1).equalsIgnoreCase(typeAttr) ?
                                    ViewType.TYPE_TEXT_COLOR : ViewType.TYPE_HINT_COLOR;
                            mVector.add(new GTData(type, parser.nextText()));
                            map.put(name, mVector);
                        }
                    } catch (IndexOutOfBoundsException ex) {
                        ex.printStackTrace();
                    }
                    break;
                case XmlPullParser.END_TAG:
                    break;
            }
            eventType = parser.next();
        }
        return map;
    }

    private static class EventHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            SingleGlobalThemeArrayMap.get().setGlobalThemeResourcesMap((ArrayMap<String, Vector<GTData>>) msg.obj);
            GlobalThemeMonitor.get().onSwitchResourcesOfMessage();
        }
    }
}
