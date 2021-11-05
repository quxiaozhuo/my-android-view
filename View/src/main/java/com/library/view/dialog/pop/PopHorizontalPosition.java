package com.library.view.dialog.pop;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        PopHorizontalPosition.CENTER,
        PopHorizontalPosition.LEFT,
        PopHorizontalPosition.RIGHT,
        PopHorizontalPosition.ALIGN_LEFT,
        PopHorizontalPosition.ALIGN_RIGHT,
})
@Retention(RetentionPolicy.SOURCE)
public @interface PopHorizontalPosition {
    int CENTER = 0;
    int LEFT = 1;
    int RIGHT = 2;
    int ALIGN_LEFT = 3;
    int ALIGN_RIGHT = 4;
}