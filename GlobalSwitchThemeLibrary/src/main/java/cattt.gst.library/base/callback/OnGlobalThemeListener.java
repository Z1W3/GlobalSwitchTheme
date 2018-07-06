package cattt.gst.library.base.callback;


import android.util.ArrayMap;

import java.util.Vector;

import cattt.gst.library.base.model.GTData;

public interface OnGlobalThemeListener {

    void onGlobalThemeResources(ArrayMap<String, Vector<GTData>> map);
}
