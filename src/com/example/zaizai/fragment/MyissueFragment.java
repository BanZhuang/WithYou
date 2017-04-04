package com.example.zaizai.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.zaizai.MainActivity;
import com.example.zaizai.R;
import com.example.zaizai.Task;
import com.example.zaizai.adapter.TaskAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View.OnClickListener;

public class MyissueFragment extends Fragment {
	
	private Button newPo;
	private TextView loginText;
	private List<Task> taskList = new ArrayList<Task>();
	 
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
          
    }  
           
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View view = inflater.inflate(R.layout.myissue_layout, container, false);  
        initTasks();//���ظ��˷�������
        
        
        
        newPo = (Button)view.findViewById(R.id.newpo);
        newPo.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		
        		//�ж��Ƿ��¼
                if(((MainActivity)getActivity()).getCurrentLaunch() == 0){
                	//δ��¼
                	Toast.makeText(getContext(), "���ȵ�¼��", Toast.LENGTH_SHORT).show();
                }else{
                	Intent intent = new Intent(getActivity(),com.example.zaizai.activity.NewpoActivity.class);
            		startActivityForResult(intent,3);
                }        		
        	}
        });
        
        
        return view;  
    }  
    
    private void initTasks(){
    	Task bai = new Task("С��","�廪��ѧ","12:55","10.00Ԫ","����˧",R.drawable.bai);
    	taskList.add(bai);
    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
    	super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		case 3:
			if (resultCode == Activity.RESULT_OK) {
				//��ûش�intent֮������µ�task
				Task hei = new Task("С��",data.getStringExtra("spotNewpo"),"13:40",
						data.getStringExtra("rewardNewpo").toString()+"Ԫ",
						data.getStringExtra("psNewpo"),R.drawable.bai);
		    	taskList.add(hei);
		    	//ͨ��MainActivity��������
		    	((MainActivity)getActivity()).setSpotNewpo(data.getStringExtra("spotNewpo"));
		    	((MainActivity)getActivity()).setRewardNewpo(data.getStringExtra("rewardNewpo"));
		    	((MainActivity)getActivity()).setPsNewpo(data.getStringExtra("psNewpo"));
		    	//setNewpo(hei);
			}
			break;
		default:
		}
	}
    
    /*//����task��taskhall��listview
    public Task taskTrans;
    public void setNewpo(Task task){
    	this.taskTrans = task;
    }
    public Task getNewpo(){
    	return taskTrans;
    }*/
    
    OnDataTransmissionListener mListener;
    public interface OnDataTransmissionListener {
    	  public void dataTransmission(String data);
    	}
    public void setOnDataTransmissionListener(OnDataTransmissionListener mListener) {
    	  this.mListener = mListener;
    	}
    
    
    @Override
    public void onResume(){
    	//��¼��״̬�ı䣬�ڸú�����ˢ���ҷ�����listview
    	super.onResume();
    	
    	if(((MainActivity)getActivity()).getCurrentLaunch() == 1){
        	//�ҷ�����task��listview
        	
    		TaskAdapter taskAdapter = new TaskAdapter(getActivity(),R.layout.task_item,taskList);
            ListView listView = (ListView)getView().findViewById(R.id.issue_list);
	        listView.setAdapter(taskAdapter);
	        ((MainActivity)getActivity()).setIsNewpo(1);
	        
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
    
    @Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState); 
        //loginText = (TextView)getActivity().findViewById(R.id.login_text);
        //������fragment�л�ȡ�ؼ�
    }  
      
    @Override  
    public void onPause() {  
        super.onPause();  
    }  
}
