package com.library.view.dialog.pop;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        PopVerticalPosition.CENTER,
        PopVerticalPosition.ABOVE,
        PopVerticalPosition.BELOW,
        PopVerticalPosition.ALIGN_TOP,
        PopVerticalPosition.ALIGN_BOTTOM,
})
@Retention(RetentionPolicy.SOURCE)
public @interface PopVerticalPosition {
    int CENTER = 0;
    int ABOVE = 1;
    int BELOW = 2;
    int ALIGN_TOP = 3;
    int ALIGN_BOTTOM = 4;
}
