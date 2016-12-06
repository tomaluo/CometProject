package com.mazing.com.cavasdemo;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;

import com.mazing.com.cavasdemo.itemtouchhelp.RecyclerListAdapter;
import com.mazing.com.cavasdemo.itemtouchhelp.SPItemTouchHelperCallBack;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by toma on 16/6/16.
 */
public class ItemTouchActivity extends AppCompatActivity {

    private RecyclerView Itemrv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_itemtouch);

        Itemrv = (RecyclerView) findViewById(R.id.item_rv);
        Itemrv.setLayoutManager(new LinearLayoutManager(this));

                String[] strs = {
                "The",
                "Canvas",
                "class",
                "holds",
                "the",
                "draw",
                "calls",
                ".",
                "To",
                "draw",
                "something,",
                "you",
                "need",
                "4 basic",
                "components",
                "Bitmap",
        };

        List<String> list = new ArrayList<>();
        for(String str : strs){
            list.add(str);
        }
        RecyclerListAdapter adapter = new RecyclerListAdapter(this,list);
        Itemrv.setAdapter(adapter);

        SPItemTouchHelperCallBack callBack = new SPItemTouchHelperCallBack(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callBack);
        helper.attachToRecyclerView(Itemrv);
    }
}
