package cattt.gst.library.base;

import android.os.Handler;
import android.os.Message;

import cattt.gst.library.base.model.LoadingView;
import cattt.gst.library.base.model.MatchViewData;
import cattt.gst.library.utils.logger.Log;


public class MatchingViewHandler extends Handler {
    private static Log logger = Log.getLogger(MatchingViewHandler.class);
    private LoadingView root;

    protected MatchingViewHandler(LoadingView root) {
        this.root = root;
    }

    @Override
    public void handleMessage(Message msg) {
        final MatchViewData data = (MatchViewData) msg.obj;
        switch (msg.what) {
            case GlobalThemeWorker.MSG_CODE_BACKGROUND_DRAWABLE:
                root.setBackground(data.getResId(), data.getDrawable());
                break;
            case GlobalThemeWorker.MSG_CODE_BACKGROUND_COLOR:
                root.setBackgroundColor(data.getResId(), data.getColor());
                break;
            case GlobalThemeWorker.MSG_CODE_IMAGE_DRAWABLE:
                root.setImageByInstanceofView(data.getResId(), data.getDrawable());
                break;
            case GlobalThemeWorker.MSG_CODE_TEXT_COLOR:
                root.setTextColorByInstanceofView(data.getResId(), data.getColor());
                break;
            case GlobalThemeWorker.MSG_CODE_HINT_COLOR:
                root.setTextHintColorByInstanceofView(data.getResId(), data.getColor());
                break;
        }
    }
}