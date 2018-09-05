package cattt.assets.theme.library.base.callback;

import java.util.Vector;

import cattt.assets.theme.library.base.model.BackgroundsBean;
import cattt.assets.theme.library.base.model.ColorsBean;
import cattt.assets.theme.library.base.model.SelectorsBean;

public interface OnAssetsListener {
    void onColorAssets(Vector<ColorsBean> beans);
    void onSelectorAssets(Vector<SelectorsBean> beans);
    void onBackgroundAssets(Vector<BackgroundsBean> beans);
}
