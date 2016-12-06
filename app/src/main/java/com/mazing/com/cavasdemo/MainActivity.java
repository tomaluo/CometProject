package com.mazing.com.cavasdemo;


import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import com.mazing.com.cavasdemo.loadingdrawble.LoadingDrawable;
import com.mazing.com.cavasdemo.refresh.RefreshView;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback,MediaPlayer.OnPreparedListener,View.OnClickListener {

    private RecyclerView recyclerView;
    private RefreshView refreshView;
    private ListView mList;
    public static final int REFRESH_DELAY = 2000;

    public static final String KEY_ICON = "icon";
    public static final String KEY_COLOR = "color";

    protected List<Map<String, Integer>> mSampleList;

    private LoadingDrawable mGearDrawable;

    private SensorManager mSensormanager;  //传感器管理器
    private Sensor mStepCount;
    private Sensor mStepDetector;

    private float mCount;//步行总数
    private float mDetector;//步行探测器

    private static final int sensorTypeD=Sensor.TYPE_STEP_DETECTOR;
    private static final int sensorTypeC=Sensor.TYPE_STEP_COUNTER;

    private TextView stepTv;

    private ViewPager autoVp;

    public View view1;
    public View view2;
    public View view3;

    private SurfaceView sf1;
    private SurfaceView sf2;
    private SurfaceView sf3;

    private SurfaceHolder holder1;
    private SurfaceHolder holder2;
    private SurfaceHolder holder3;

    private MediaPlayer player;

    private List<View> viewcontaniers;

    private String Path;

    private VideoView autoVV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
//        Button btnstar = (Button)findViewById(R.id.stare);
//        btnstar.setOnClickListener(this);

//        autoVV = (VideoView)findViewById(R.id.auto_videoview);
//
////        Path = "file://" + Environment.getExternalStorageDirectory().getPath()+ "/1.mp4";
//
//        Path = "android.resource://" + getPackageName() + "/" + R.raw.sf1;
//
////        autoVV.setVideoURI(Uri.parse(Path));
//        autoVV.setVideoPath(Path);
//        autoVV.start();
//
//        autoVV.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.start();
//                mp.setLooping(true);
//            }
//        });
//
//        // 获取SurfaceHolder实例
        holder1 = sf1.getHolder();
        holder2 = sf2.getHolder();
        holder3 = sf3.getHolder();
//
//        // 实现接口
        holder1.addCallback(this);
        holder2.addCallback(this);
        holder3.addCallback(this);

        autoVp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewcontaniers.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View specView = viewcontaniers.get(position);
                ViewGroup parent = (ViewGroup) specView.getParent();
                if (parent != null) {
                    parent.removeAllViews();
                }
                container.addView(specView);
                return viewcontaniers.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(viewcontaniers.get(position));
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });

//        MoveCircle  circle3 = (MoveCircle)findViewById(R.id.circle3);
//        circle3.startAnimation();

//        mGearDrawable = new LoadingDrawable(new LevelLoadingRenderer(this));
//
//        ImageView iv = (ImageView) findViewById(R.id.whorl_view);
//        iv.setImageDrawable(mGearDrawable);
//        mGearDrawable.start();
//        Map<String, Integer> map;
//        mSampleList = new ArrayList<>();
//
//        int[] icons = {
//                R.drawable.icon_1,
//                R.drawable.icon_2,
//                R.drawable.icon_3};
//
//        int[] colors = {
//                R.color.saffron,
//                R.color.eggplant,
//                R.color.sienna};
//
//        for (int i = 0; i < 20; i++) {
//            map = new HashMap<>();
//            map.put(KEY_ICON, icons[sf2]);
//            map.put(KEY_COLOR, colors[sf2]);
//            mSampleList.add(map);
//        }
//
//        recyclerView = (RecyclerView) findViewById(R.id.re_rv);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new SampleAdapter());
//        String[] strs = {
//                "The",
//                "Canvas",
//                "class",
//                "holds",
//                "the",
//                "draw",
//                "calls",
//                ".",
//                "To",
//                "draw",
//                "something,",
//                "you",
//                "need",
//                "4 basic",
//                "components",
//                "Bitmap",
//        };
//
//        mList = (ListView) findViewById(R.id.list);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strs);
//        mList.setAdapter(adapter);

//        refreshView = (RefreshView) findViewById(R.id.reView);
//
//        refreshView.setOnRefreshListener(
//                new RefreshView.OnCircleRefreshListener() {
//                    @Override
//                    public void refreshing() {
//                        // do something when refresh starts
//                    }
//
//                    @Override
//                    public void completeRefresh() {
//                        // do something when refresh complete
//                    }
//                });

//        Button btnStop = (Button)findViewById(R.id.btn_stop);
//        btnStop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                refreshView.finishRefreshing();
//            }
//        });
//        PieView view = (PieView)findViewById(R.id.tvPieView);
//
//        List<User> list = new ArrayList<User>();
//        for(int i = 0; i < 10; i ++){
//            User user = new User();
//            user.value = 20;
//            user.name = "test";
//            list.add(user);
//        }
//
//        view.setData(list);
//        WaveView wave = (WaveView) findViewById(R.id.wave_view);

//        PorterDuffXfermodeViewV2 wave = (PorterDuffXfermodeViewV2) findViewById(R.id.wave_view);
//        stepTv = (TextView) findViewById(R.id.step_tv);
//        mSensormanager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
//        mStepCount = mSensormanager.getDefaultSensor(sensorTypeC);
//        mStepDetector = mSensormanager.getDefaultSensor(sensorTypeD);
//        register();
    }

    private void initView(){

        autoVp = (ViewPager) findViewById(R.id.auto_medial_viewPage);
        viewcontaniers = new ArrayList<View>();
        view1 = View.inflate(this, R.layout.layout_vp1, null);
        view2 = View.inflate(this, R.layout.layout_vp2, null);
        view3 = View.inflate(this, R.layout.layout_vp3, null);

        sf1 = (SurfaceView) view1.findViewById(R.id.sf1);
        sf2 = (SurfaceView) view2.findViewById(R.id.sf2);
        sf3 = (SurfaceView) view3.findViewById(R.id.sf3);

        viewcontaniers.add(sf1);
        viewcontaniers.add(sf2);
        viewcontaniers.add(sf3);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 下面开始实例化MediaPlayer对象
        player = new MediaPlayer();
        // 设置播放在surfaceview上
        player.setDisplay(holder);
        player.setOnPreparedListener(this);
//        // 设置循环播放
        player.setLooping(true);

        if (holder1.equals(holder)) {
            Path = "android.resource://" + getPackageName() + "/" + R.raw.sf1;
        }
        if (holder2.equals(holder)) {
            Path = "android.resource://" + getPackageName() + "/" + R.raw.sf2;
        }
        if (holder3.equals(holder)) {
            Path = "android.resource://" + getPackageName() + "/" + R.raw.sf3;
        }

        try {
            Uri url = Uri.parse(Path);
            player.setDataSource(MainActivity.this,url);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 准备播放
        try {
            player.prepareAsync();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.stare)
            player.start();
    }

    private class SampleAdapter extends RecyclerView.Adapter<SampleHolder> {

        @Override
        public SampleHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            return new SampleHolder(view);
        }

        @Override
        public void onBindViewHolder(SampleHolder holder, int pos) {
            Map<String, Integer> data = mSampleList.get(pos);
            holder.bindData(data);
        }

        @Override
        public int getItemCount() {
            return mSampleList.size();
        }
    }

    private class SampleHolder extends RecyclerView.ViewHolder {

        private View mRootView;
        private ImageView mImageViewIcon;

        private Map<String, Integer> mData;

        public SampleHolder(View itemView) {
            super(itemView);

            mRootView = itemView;
            mImageViewIcon = (ImageView) itemView.findViewById(R.id.image_view_icon);
        }

        public void bindData(Map<String, Integer> data) {
            mData = data;

            mRootView.setBackgroundResource(mData.get(KEY_COLOR));
            mImageViewIcon.setImageResource(mData.get(KEY_ICON));
        }
    }
}
