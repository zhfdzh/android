package com.example.administrator.dectionary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.CursorAdapter;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/7/12 0012.
 */

public class DictionaryAdapter extends CursorAdapter {

    private LayoutInflater layoutInflater;

    @Override
    public CharSequence convertToString(Cursor cursor) {
        return cursor == null ? "" : cursor.getString(cursor.getColumnIndex("_id"));
    }

    //单词显示到列表中
    private void setView(View view, Cursor cursor){
        TextView tvWordItem = (TextView) view;
        tvWordItem.setText(cursor.getString(cursor.getColumnIndex("_id")));
    }

    public DictionaryAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //生成新的选项
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.list_item_view,null);
        setView(view,cursor);
        return view;
    }

    //绑定选项到列表
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
            setView(view,cursor);
    }
}
