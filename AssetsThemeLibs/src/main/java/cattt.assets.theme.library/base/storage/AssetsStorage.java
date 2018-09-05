package cattt.assets.theme.library.base.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class AssetsStorage {
    private static final String LOCAL_NAME = "assets_theme_libs";
    private static final String KEY_CHOICE = "key_choice";

    private static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(LOCAL_NAME, Context.MODE_PRIVATE);
    }

    public static void saveChoiceBean(Context context, String sha1, File outFile) {
        try {
            final JSONObject root = new JSONObject();
            root.put("sha1", sha1);
            root.put("folder", outFile.getPath());
            getEditor(context).putString(KEY_CHOICE, root.toString()).commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ChoiceBean getChoiceBean(Context context) {
        final String json = getSharedPreferences(context).getString(KEY_CHOICE, "");
        if (!TextUtils.isEmpty(json)) {
            try {
                final JSONObject root = new JSONObject(json);
                final ChoiceBean bean = new ChoiceBean();
                bean.setSha1(root.getString("sha1"));
                bean.setFolder(new File(root.getString("folder")));
                return bean;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
