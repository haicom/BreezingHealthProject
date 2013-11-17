package com.breezing.health.util;

import android.support.v4.widget.CursorAdapter;

public interface OnDataSetChangedListener {
    void onDataSetChanged(CursorAdapter adapter);
    void onContentChanged(CursorAdapter adapter);
}
