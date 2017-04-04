package com.example.zaizai.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.example.zaizai.MainActivity;
import com.example.zaizai.R;
import com.example.zaizai.Task;
import com.example.zaizai.adapter.TaskAdapter;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class TaskhallFragment extends Fragment {
	
	private ViewPager mViewPaper;  
	private List<ImageView> images;  
	private List<View> dots;  
	private int currentItem;  
	//��¼��һ�ε��λ��  
	private int oldPosition = 0;  
	//���ͼƬ��id  
	private int[] imageIds = new int[]{  
	        R.drawable.j,  
	        R.drawable.k,  
	        R.drawable.l,  
	        R.drawable.m,  
	        R.drawable.n  
	};  
	//���ͼƬ�ı���  
	private String[]  titles = new String[]{  
	        "2017 ��ϲ����",   
	        "������������������һ�������ߴ���",    
	        "���˽ڿ��֣�",     
	        "�����Ƽ���ѧ��ի525�������ʸ߷�",      
	        "��˾���ĳ�Ҫ����"  
	    };  
	private TextView title;  
	private ViewPagerAdapter adapter;  
	private ScheduledExecutorService scheduledExecutorService; 
	
	private List<Task> taskList = new ArrayList<Task>();
	
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        //����ֲ�����
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();  
        scheduledExecutorService.scheduleWithFixedDelay(  
                new ViewPageTask(),   
                2,   
                2,   
                TimeUnit.SECONDS);
                         
    }  
           
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View view = inflater.inflate(R.layout.taskhall_layout, container, false);
        //����Task��listview
        initTasks();//��ʼ�������������
        
        
        
        
        //��λ����
        /*Button getLocat = (Button)view.findViewById(R.id.get_location);
        getLocat.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		Intent intent = new Intent(getActivity(),com.example.zaizai.activity.Location.class);
        		startActivity(intent);
        	}
        });*/
        
       //ʵ�ֹ���ֲ��ؼ�
        mViewPaper = (ViewPager)view.findViewById(R.id.vp);
      //��ʾ��ͼƬ  
        images = new ArrayList<ImageView>();  
        for(int i = 0; i < imageIds.length; i++){  
            ImageView imageView = new ImageView(getActivity());//����mark  
            imageView.setBackgroundResource(imageIds[i]);  
            images.add(imageView);  
        }  
        //��ʾ��С��  
        dots = new ArrayList<View>();  
        dots.add(view.findViewById(R.id.dot_0));  
        dots.add(view.findViewById(R.id.dot_1));  
        dots.add(view.findViewById(R.id.dot_2));  
        dots.add(view.findViewById(R.id.dot_3));  
        dots.add(view.findViewById(R.id.dot_4));  
          
        title = (TextView) view.findViewById(R.id.title_cycle);  
        title.setText(titles[0]);  
          
        adapter = new ViewPagerAdapter();  
        mViewPaper.setAdapter(adapter); 
        
        mViewPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {  
            
        	  
            @Override  
            public void onPageSelected(int position) {  
                title.setText(titles[position]);  
                dots.get(position).setBackgroundResource(R.drawable.dot_focused);  
                dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);  
                  
                oldPosition = position;  
                currentItem = position;  
            }  
              
            @Override  
            public void onPageScrolled(int arg0, float arg1, int arg2) {  
                  
            }  
              
            @Override  
            public void onPageScrollStateChanged(int arg0) {  
                  
            }  
        });
        
        return view;  
    }  
    
    /** 
     * �Զ���Adapter 
     * @author baizechen 
     * 
     */  
    private class ViewPagerAdapter extends PagerAdapter{  
      
        @Override  
        public int getCount() {  
            return images.size();  
        }  
      
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {  
            return arg0 == arg1;  
        }  
      
        @Override  
        public void destroyItem(ViewGroup view, int position, Object object) {  
            // TODO Auto-generated method stub  
//              super.destroyItem(container, position, object);  
//              view.removeView(view.getChildAt(position));  
//              view.removeViewAt(position);  
            view.removeView(images.get(position));  
        }  
      
        @Override  
        public Object instantiateItem(ViewGroup view, int position) {  
            // TODO Auto-generated method stub  
            view.addView(images.get(position));  
            return images.get(position);  
        }  
          
    } 
    
    /** 
     * ͼƬ�ֲ����� 
     * @author baizechen 
     * 
     */  
    private class ViewPageTask implements Runnable{  
  
        @Override  
        public void run() {  
            currentItem = (currentItem + 1) % imageIds.length;  
            mHandler.sendEmptyMessage(0);  
        }  
    }  
      
    /** 
     * �������̴߳��ݹ��������� 
     */  
    private Handler mHandler = new Handler(){  
        public void handleMessage(android.os.Message msg) {  
            mViewPaper.setCurrentItem(currentItem);  
        };  
    };
    
    
    private void initTasks(){
    	Task haochen = new Task("�Ƴ�","�����Ƽ���ѧ9#525","15:33","6.00Ԫ","��",R.drawable.haochen);
    	taskList.add(haochen);
    	Task bai = new Task("С��","�廪��ѧ","12:55","10.00Ԫ","����˧",R.drawable.bai);
    	taskList.add(bai);
    	Task yuan = new Task("JYY","�㽭���ݽ���Ƥ�ﳧ","10.21","2.22Ԫ","�����",R.drawable.yuan);
    	taskList.add(yuan);
    }
      
    @Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
    }  
      
    @Override  
    public void onPause() {  
        super.onPause(); 
        if(scheduledExecutorService != null){  
            scheduledExecutorService.shutdown();  
            scheduledExecutorService = null;  
        }  
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	    	        
    	TaskAdapter taskAdapter = new TaskAdapter(getActivity(),R.layout.task_item,taskList);
        ListView listView = (ListView)getView().findViewById(R.id.list_task);
        if(((MainActivity)getActivity()).getIsNewpo() == 1){
        	Task hei = new Task("С��",((MainActivity)getActivity()).getSpotNewpo(),"13:40",
        			((MainActivity)getActivity()).getRewardNewpo().toString()+"Ԫ",
        			((MainActivity)getActivity()).getPsNewpo(),R.drawable.bai);
	    	taskList.add(hei);
        }
        listView.setAdapter(taskAdapter);
        
        
      //ListView����¼�
        listView.setOnItemClickListener(new OnItemClickListener(){
        	@Override
        	public void onItemClick(AdapterView<?>parent, View view, 
        			int position, long id){
        		Task task = taskList.get(position);
        		Intent intent = new Intent(getActivity(),com.example.zaizai.activity.DetailActivity.class);
        		intent.putExtra("headId",task.getImageId());
        		intent.putExtra("spot", task.getSpot());
        		intent.putExtra("time", task.getTime());
        		intent.putExtra("reward", task.getReward());
        		intent.putExtra("username", task.getUsername());
        		intent.putExtra("ps", task.getPs());
        		startActivity(intent);
        	}
        });
    }
}
