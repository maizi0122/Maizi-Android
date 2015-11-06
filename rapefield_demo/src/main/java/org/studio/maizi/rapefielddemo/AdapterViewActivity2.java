/*
 *  $HomePage: https://github.com/maizi0122/ $
 *  $Revision: 000001 $
 *  $Date: 2015-10-18 09:05:31 -0000 (Sun, 18 Oct 2015) $
 *
 *  ====================================================================
 *  Copyright (C) 2015 The Maizi-Studio Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *  ====================================================================
 *
 *  This project powered by Maizi-Studio, but works with the
 *  license of apache,so you should abide by this license.
 *  Any question contacting with email below:
 *  maizi0122@gmail.com
 */

package org.studio.maizi.rapefielddemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.studio.maizi.rapefield.IRapeField;
import org.studio.maizi.rapefield.anno.Adapter;
import org.studio.maizi.rapefield.anno.Anim;
import org.studio.maizi.rapefield.anno.ContentView;
import org.studio.maizi.rapefield.anno.EventTarget;
import org.studio.maizi.rapefield.anno.RegistListener;
import org.studio.maizi.rapefield.anno.ResId;
import org.studio.maizi.rapefield.impl.RapeField;

import java.util.ArrayList;
import java.util.List;

/**
 * Powered by Maizi-Studio.<br />
 * Design by maizi.<br />
 * Created on 15-11-5.
 */
@SuppressWarnings("all")
@ContentView(R.layout.activity_sec)
public class AdapterViewActivity2 extends Activity implements AdapterView.OnItemClickListener {

    private List<String> list = new ArrayList<String>() {
        {
            for (int i = 0; i < 20; i++) {
                this.add("(<-press img)...hello maizi...(<-long press tv)" + i);
            }
        }
    };

    @ResId(R.id.ac_sec_lv)
    @Adapter(MyAdapter.class)
    @RegistListener(listeners = {AdapterViewActivity2.class})
    private ListView ac_sec_lv;

    @ResId(R.id.ac_ava1_root)
    @Anim(animResId = R.anim.slide_in_right, duration = 1000, interpolator = android.R.interpolator.decelerate_cubic)
    private RelativeLayout ac_ava1_root;

    private IRapeField rapeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //you adapter class have no empty-params constructor,you should make instance manually and pass it like : inject(this, new MyAdapter("maizi"));
        rapeField = new RapeField().inject(this, new MyAdapter("maizi"));

    }

    @Override
    @EventTarget(targets = {R.id.ac_sec_lv})
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String[] arrays = TextView.class.cast(view.findViewById(R.id.ac_sec_lv_item_tv)).getText().toString().split("\\(<\\-([a-z[\\p{Space}]]+)\\)");
        Toast.makeText(this, new StringBuilder("item ").append(arrays[1]).append(arrays[2]).append("\n have been clicked").toString(), Toast.LENGTH_SHORT).show();
    }

    private class MyAdapter extends BaseAdapter implements View.OnLongClickListener {

        private String name;

        //you have no empty-params constructor,you should make instance manually and pass it like : inject(this, new MyAdapter("maizi"));
        public MyAdapter(String name) {
            this.name = name;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = null;
            MyHolder holder = null;
            if (convertView != null) {
                itemView = convertView;
                holder = MyHolder.class.cast(itemView.getTag());
            } else {
                itemView = LayoutInflater.from(AdapterViewActivity2.this).inflate(R.layout.ac_sec_lv_item, parent, false);
                holder = new MyHolder();
                //-------------------------------------------------
                rapeField.inject(this, itemView, holder, this);//-----------------------------attention last this,MyAdapter have been make instance auto,
                //-------------------------------------------------                         //because of annotation @Adapter(MyAdapter.class),we have helped you
                holder.ac_sec_lv_item_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);       //setAdapter automatic with instance creating, if your Adapter class
                itemView.setTag(holder);                                                    //have no empty-param constructor,you should pass the instance in
            }                                                                               //inject(Object... obj) manually,because we don't know what object
            holder.ac_sec_lv_item_iv.setImageResource(R.mipmap.ic_launcher);                //in params to create instance...
            holder.ac_sec_lv_item_tv.setText(list.get(position));
            return itemView;
        }

        @Override
        @EventTarget(targets = {R.id.ac_sec_lv_item_tv})
        public boolean onLongClick(View v) {
            String[] arrays = TextView.class.cast(v.findViewById(R.id.ac_sec_lv_item_tv)).getText().toString().split("\\(<\\-([a-z[\\p{Space}]]+)\\)");
            Toast.makeText(AdapterViewActivity2.this, new StringBuilder("TextView ").append(arrays[1]).append(arrays[2]).append("\n have been long clicked").toString(), Toast.LENGTH_SHORT).show();
            return false;
        }

        private class MyHolder {
            @ResId(R.id.ac_sec_lv_item_iv)
            ImageView ac_sec_lv_item_iv;
            @ResId(R.id.ac_sec_lv_item_tv)
            @RegistListener(listeners = {MyAdapter.class})
            TextView ac_sec_lv_item_tv;
            @ResId(R.id.ac_sec_lv_item_root)
            View itemView;
        }

    }
}