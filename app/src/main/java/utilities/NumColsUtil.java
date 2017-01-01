package utilities;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by John Vehikite on 12/31/2016.
 * Class that contains utility method that dynamically calculates
 * number of columns that should be used in our grid layout
 *
 * Source: http://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns/38472370
 */

public class NumColsUtil {
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }
}
