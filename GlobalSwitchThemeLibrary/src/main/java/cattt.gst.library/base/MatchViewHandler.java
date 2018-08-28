package cattt.gst.library.base;

import android.os.Handler;
import android.os.Message;

import cattt.gst.library.base.model.IMatchViewable;
import cattt.gst.library.base.model.MatchViewData;
import cattt.gst.library.utils.logger.Log;


public class MatchViewHandler extends Handler {
    private static Log logger = Log.getLogger(MatchViewHandler.class);
    private IMatchViewable root;

    protected MatchViewHandler(IMatchViewable root) {
        this.root = root;
    }

    @Override
    public void handleMessage(Message msg) {
        MatchViewData m = (MatchViewData) msg.obj;
        switch (msg.what) {
            case GlobalThemeWorker.MSG_CODE_BACKGROUND_DRAWABLE:
                root.setBackground(m.getView(), m.getDrawable());
                break;
            case GlobalThemeWorker.MSG_CODE_BACKGROUND_COLOR:
                root.setBackgroundColor(m.getView(), m.getColor());
                break;
            case GlobalThemeWorker.MSG_CODE_IMAGE_DRAWABLE:
                root.setImageByInstanceofView(m.getView(), m.getDrawable());
                break;
            case GlobalThemeWorker.MSG_CODE_TEXT_COLOR:
                root.setTextColorByInstanceofView(m.getView(), m.getColor());
                break;
            case GlobalThemeWorker.MSG_CODE_HINT_COLOR:
                root.setTextHintColorByInstanceofView(m.getView(), m.getColor());
                break;
        }
    }
}