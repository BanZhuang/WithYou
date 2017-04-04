package com.example.zaizai.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.support.v4.view.PagerAdapter;

public class MyPagerAdapter extends PagerAdapter {
	
	private List mListViews;  
    
    public MyPagerAdapter(List mListViews) {  
        this.mListViews = mListViews;//���췽�������������ǵ�ҳ���������ȽϷ��㡣  
    }  

    @Override  
    public void destroyItem(ViewGroup container, int position, Object object)   {     
        container.removeView((View) mListViews.get(position));//ɾ��ҳ��  
    }  


    @Override  
    public Object instantiateItem(ViewGroup container, int position) {  //�����������ʵ����ҳ��         
         container.addView((View) mListViews.get(position), 0);//���ҳ��  
         return mListViews.get(position);  
    }  

    @Override  
    public int getCount() {           
        return  mListViews.size();//����ҳ��������  
    }  
      
    @Override  
    public boolean isViewFromObject(View arg0, Object arg1) {             
        return arg0==arg1;//�ٷ���ʾ����д  
    }  
}
