package com.example.administrator.dectionary;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class AutoCompleteDemo extends AppCompatActivity implements TextWatcher {

    String[] Game = new String[]{"game1", "game2", "game3", "game4"};
    private final String DATABASE_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/dictionary";
    private AutoCompleteTextView word;
    private final String DATAVBASE_FILENAME = "dictionary.db";
    private SQLiteDatabase database;
    private Button searchWord;
    private TextView showResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_complete_demo);
        word = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        searchWord = (Button) findViewById(R.id.searchWord);
        showResult = (TextView) findViewById(R.id.result);
      /*  ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list_item_view,Game);
        word.setAdapter(adapter);*/
        Log.d("Autocpmplete", "openDatabase-before");
        database = openDatabase();
        Log.d("Autocpmplete", "openDatabase-after");
//        Log.d("Autocpmplete", (database==null)+"---111");

        searchWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sql = "select chiness from t_word where english=?";
                Cursor cursor = database.rawQuery(sql,new String[]{word.getText().toString()});
                String result = "未找到该单词";
                if(cursor.getCount() > 0){
                    cursor.moveToFirst();
                    result = cursor.getString(cursor.getColumnIndex("chinese")).replace("&amp;","&");
                    showResult.setText(word.getText() + "\n" + result.toString());
                }
            }
        });
        word.addTextChangedListener(this);
    }

    private SQLiteDatabase openDatabase() {
        Log.d("Autocpmplete", "openDatabase");
        try{
            String databaseFileName = DATABASE_PATH + "/" + DATAVBASE_FILENAME;
            File dir = new File(DATABASE_PATH);
            if(!dir.exists()){
                dir.mkdir();
            }
            Log.d("Autocpmplete",( !(new File(databaseFileName).exists()))+"---file");
            if(!(new File(databaseFileName).exists())){
                Log.d("Autocpmplete","new file");
                InputStream is = getResources().openRawResource(R.raw.dictionary);//获取项目中的数据库文件，复制到SD卡中
                FileOutputStream fos = new FileOutputStream(databaseFileName);
                byte[] by = new byte[1024];
                int count = 0;
                int i=0;
                while((count = is.read(by)) > 0){
                    fos.write(by,0,count);
                    i++;
                }
                Log.d("Autocpmplete", String.valueOf(i));
                fos.close();
                is.close();
            }
            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFileName,null);
            return database;
        }catch (Exception e ){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.d("Autocpmplete","afterTextChanged");
        Log.d("Autocpmplete",database.toString());
        Cursor cursor = database.rawQuery("select english as _id from t_word where english like ?",
                new String[]{s.toString() + "%"});
        Log.d("Autocpmplete",cursor.toString());
        DictionaryAdapter adapter = new DictionaryAdapter(this,cursor,true);
        word.setAdapter(adapter);
    }

}
