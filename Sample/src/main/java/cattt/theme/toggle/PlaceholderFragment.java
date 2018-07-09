package cattt.theme.toggle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import cattt.gst.library.base.BaseSwitchThemeFragment;

public class PlaceholderFragment extends BaseSwitchThemeFragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private int number;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            number = bundle.getInt(ARG_SECTION_NUMBER, -1);
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_third;
    }

    @Override
    protected void onInit() {
        TextView textView = getView().findViewById(R.id.numberTv);
        textView.setText("当前第" +number+"页");
    }

    @Override
    protected int[] getViewResourcesPendingChangeTheme() {
        return new int[]{
                R.id.wallpaper2,
                R.id.image1,
                R.id.image2,
                R.id.image3,
                R.id.image4,
                R.id.text,
                R.id.app_compat_text,
                R.id.edit_text,
                R.id.app_compat_edit,
                R.id.button,
                R.id.app_compat_button
        };
    }
}
