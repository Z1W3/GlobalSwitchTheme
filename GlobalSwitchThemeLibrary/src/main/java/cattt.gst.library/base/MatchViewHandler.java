package cattt.gst.library.base;

import android.os.Handler;
import android.os.Message;

import cattt.gst.library.base.model.MatchView;
import cattt.gst.library.base.model.MatchViewData;
import cattt.gst.library.utils.logger.Log;


public class MatchViewHandler extends Handler {
    private static Log logger = Log.getLogger(MatchViewHandler.class);
    private MatchView root;

    protected MatchViewHandler(MatchView root) {
        this.root = root;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case GlobalThemeWorker.MSG_CODE_BACKGROUND_DRAWABLE:
                MatchViewData m1 = (MatchViewData) msg.obj;
                root.setBackground(m1.getView(), m1.getDrawable());
                break;
            case GlobalThemeWorker.MSG_CODE_BACKGROUND_COLOR:
                MatchViewData m2 = (MatchViewData) msg.obj;
                root.setBackgroundColor(m2.getView(), m2.getColor());
                break;
            case GlobalThemeWorker.MSG_CODE_IMAGE_DRAWABLE:
                MatchViewData m3 = (MatchViewData) msg.obj;
                root.setImageByInstanceofView(m3.getView(), m3.getDrawable());
                break;
            case GlobalThemeWorker.MSG_CODE_TEXT_COLOR:
                MatchViewData m4 = (MatchViewData) msg.obj;
                root.setTextColorByInstanceofView(m4.getView(), m4.getColor());
                break;
            case GlobalThemeWorker.MSG_CODE_HINT_COLOR:
                MatchViewData m5 = (MatchViewData) msg.obj;
                root.setTextHintColorByInstanceofView(m5.getView(), m5.getColor());
                break;
            case GlobalThemeWorker.MSG_CODE_MATCH_VIEW_VISIBILITY:
                root.setMatchViewVisibility((Integer) msg.obj);
                break;
        }
    }
}