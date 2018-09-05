package cattt.theme.toggle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cattt.assets.theme.library.base.AssetsHelper;

public class PlaceHolderFragment extends Fragment {
    private AssetsHelper mHelper;

    private int[] mIds = new int[]{
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

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns mIds new instance of this fragment for the given section
     * number.
     */
    public static PlaceHolderFragment newInstance(int sectionNumber) {
        PlaceHolderFragment fragment = new PlaceHolderFragment();
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
        if (bundle != null) {
            number = bundle.getInt(ARG_SECTION_NUMBER, -1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_demo2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHelper = new AssetsHelper(this, mIds);
        TextView textView = getView().findViewById(R.id.numberTv);
        textView.setText("当前第" + number + "页");
    }

    @Override
    public void onDestroyView() {
        mHelper.destroyAssetsHelper();
        super.onDestroyView();
    }
}
