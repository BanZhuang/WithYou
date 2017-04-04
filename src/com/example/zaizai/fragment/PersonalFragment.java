package com.example.zaizai.fragment;


import java.util.ArrayList;
import com.example.zaizai.MainActivity;
import com.example.zaizai.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class PersonalFragment extends Fragment {
	
	/*private String[] personal_item = {"�ҵķ���","�ҵ�����",
			"�����˻�","����","����"};*/
	private ArrayList<String> listP = new ArrayList<String>();
	ImageButton login;
	TextView loginText;
	
	Button logVid,logPic;
	
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
          
    }  
           
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
        View view = inflater.inflate(R.layout.personal_layout, container, false);

		//loginText = (TextView)view.findViewById(R.id.login_text);
        //��¼ͷ�����¼�����
        login = (ImageButton)view.findViewById(R.id.login_head);
        /*login.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        			//ʹ�øú����ص�loginactivity�����ݣ��ж��Ƿ��½
                	Intent intent = new Intent(getActivity(),com.example.zaizai.activity.VideoVerify.class);
                    startActivityForResult(intent,2);

        	}
        }); */
        
        logVid = (Button)view.findViewById(R.id.log_vid);
        logPic = (Button)view.findViewById(R.id.log_pic);
        logVid.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		Intent intent = new Intent(getActivity(),com.example.zaizai.activity.VideoVerify.class);
                startActivityForResult(intent,2);
        	}
        });
        
        logPic.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		Intent intent2 = new Intent(getActivity(),com.example.zaizai.activity.OnlineFace.class);
                startActivityForResult(intent2,2);
        	}
        });
        
        
        //����ListView
        listP.add("�ҵķ���");
        listP.add("�ҵ�����");
        listP.add("�����˻�");
        listP.add("����");
        listP.add("����");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(),//��ȡcontext
        		android.R.layout.simple_list_item_1,listP);
        ListView listPersonal = (ListView)view.findViewById(R.id.list_personal);//ע����fragment�л�ȡ�ؼ�ʹ�õķ���
        listPersonal.setAdapter(adapter);
        
        //����ÿ��item����¼�
        listPersonal.setOnItemClickListener(new OnItemClickListener(){
        	@Override
        	public void onItemClick(AdapterView<?>parent, View view, 
        			int position, long id){
        		if(listP.get(position).equals("�ҵķ���")){
        			MainActivity parentActivity = (MainActivity)getActivity();
        			parentActivity.changeTab(R.id.lymyissue);
        		}
        		if(listP.get(position).equals("�ҵ�����")){
        			Intent intent = new Intent(getActivity(),com.example.zaizai.activity.MygrabActivity.class);
                    startActivity(intent);
        		}
        		if(listP.get(position).equals("�����˻�")){
        			Intent intent = new Intent(getActivity(),com.example.zaizai.activity.MyaccActivity.class);
                    startActivity(intent);
        		}
        		if(listP.get(position).equals("����")){
        			Intent intent = new Intent(getActivity(),com.example.zaizai.activity.FeedbackActivity.class);
                    startActivity(intent);
        		}
        		if(listP.get(position).equals("����")){
        			Intent intent = new Intent(getActivity(),com.example.zaizai.activity.AboutActivity.class);
                    startActivity(intent);
        		}
        	}
        });
        
        
        return view;  
    }  
    
      
    @Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
      
    }  
      
    @Override  
    public void onPause() {  
        super.onPause();  
    }
    
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
    	super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		case 2:
			if (resultCode == Activity.RESULT_OK) {
				((MainActivity)getActivity()).setCurrentLaunch(1);
				//loginText.setText("С��");
	        	login.setBackgroundResource(R.drawable.bai);
			}
			break;
		default:
		}
	}
}
