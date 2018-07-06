package cattt.gst.library.base;

import android.util.ArrayMap;

import java.util.Vector;

import cattt.gst.library.base.model.GTData;

public class SingleGlobalThemeArrayMap {

    private ArrayMap<String, Vector<GTData>> map = new ArrayMap<>();

    public ArrayMap<String, Vector<GTData>> getGlobalThemeResourcesMap(){
        return map;
    }

    private static final class Helper {
        private static final SingleGlobalThemeArrayMap INSTANCE = new SingleGlobalThemeArrayMap();
    }

    protected static SingleGlobalThemeArrayMap get() {
        return Helper.INSTANCE;
    }
}
