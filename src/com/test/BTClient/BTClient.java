package com.test.BTClient;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


import com.test.BTClient.view.RoundProgressBar;
import com.test.BTClient.DeviceListActivity;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "NewApi", "HandlerLeak", "InflateParams" }) //��ע����ָ������
public class BTClient extends Activity  {
	
	private final static int REQUEST_CONNECT_DEVICE = 1;    //�궨���ѯ�豸���
	
	private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";   //SPP����UUID��
	
	private InputStream is;    //������������������������
	//private TextView text0;    //��ʾ������
  //  private EditText edit0;    //��������������
 //   private TextView dis;
 //   private ScrollView sv;      //��ҳ���
    private String smsg = "";    //��ʾ�����ݻ���
    private String fmsg = "";    //���������ݻ���
    private TextView counting;
    private TextView dongzuo;    //������������
   
	private TextView tvHint;        //��ҳ�����֡��ϻ����Խ�����ҳ��
	
	RoundProgressBar rbar_1;        //Բ�ν�����
	Button btn_3;
	private int progress = 0;
	
	RatingBar ratingBar;              //�Ǽ���
	float rat;

    public String filename=""; //��������洢���ļ���
    BluetoothDevice _device = null;     //�����豸
    BluetoothSocket _socket = null;      //����ͨ��socket
    boolean _discoveryFinished = false;    
    boolean bRun = true;
    boolean bThread = false;
 
    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();    //��ȡ�����������������������豸
	
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);   //���û���Ϊ������ main.xml
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        
        initView();
        initView_1();
        initView_3();
        
        
     //   text0 = (TextView)findViewById(R.id.Text0);  //�õ���ʾ�����
     //   edit0 = (EditText)findViewById(R.id.Edit0);   //�õ��������
    //    sv = (ScrollView)findViewById(R.id.ScrollView01);  //�õ���ҳ���
  //      dis = (TextView) findViewById(R.id.in);      //�õ�������ʾ���
        counting = (TextView) findViewById(R.id.counting);
		dongzuo = (TextView) findViewById(R.id.dongzuo);

       //����򿪱��������豸���ɹ�����ʾ��Ϣ����������
        if (_bluetooth == null){
        	Toast.makeText(this, "�޷����ֻ���������ȷ���ֻ��Ƿ����������ܣ�", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        // �����߳������豸���Ա�����  
       new Thread(){
    	   public void run(){
    		   if(_bluetooth.isEnabled()==false){   
        		_bluetooth.enable();
    		   }
    	   }   	   
       }.start();      
  
    }
	
    //���Ͱ�����Ӧ
  /*  public void onSendButtonClicked(View v){
    //	Button btn = (Button)findViewById(R.id.Button02);
    	try{
    		OutputStream os = _socket.getOutputStream();   //�������������
    		byte[] bos_new = new byte[2];
    		bos_new[0]='0';
    		bos_new[1]='0';
    		if(btn.getText().toString().equals("����")){
        		bos_new[1]='0';
        		btn.setText("����");
    		}else{
        		bos_new[1]='1';
        		btn.setText("����");
    		}
    		os.write(bos_new);	
    	}catch(IOException e){  		
    	}  	
    }*/
    
    //���ջ�������ӦstartActivityForResult()
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch(requestCode){
    	case REQUEST_CONNECT_DEVICE:     //���ӽ������DeviceListActivity���÷���
    		// ��Ӧ���ؽ��
            if (resultCode == Activity.RESULT_OK) {   //���ӳɹ�����DeviceListActivity���÷���
                // MAC��ַ����DeviceListActivity���÷���
                String address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                // �õ������豸���      
                _device = _bluetooth.getRemoteDevice(address);
 
                // �÷���ŵõ�socket
                try{
                	_socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
                }catch(IOException e){
                	Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
                }
                //����socket
            	Button btn = (Button) findViewById(R.id.Button03);
                try{
                	_socket.connect();
                	Toast.makeText(this, "����"+_device.getName()+"�ɹ���", Toast.LENGTH_SHORT).show();
                	btn.setText("�Ͽ�");
                }catch(IOException e){
                	try{
                		Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
                		_socket.close();
                		_socket = null;
                	}catch(IOException ee){
                		Toast.makeText(this, "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
                	}
                	
                	return;
                }
                
                //�����쳣
                try{
            		is = _socket.getInputStream();   //�õ���������������
            		}catch(IOException e){
            			Toast.makeText(this, "��������ʧ�ܣ�", Toast.LENGTH_SHORT).show();
            			return;
            		}
            		if(bThread==false){
            			ReadThread.start();
            			bThread=true;
            		}else{
            			bRun = true;
            		}
            }
    		break;
    	default:break;
    	}
    }
    
    //���������߳�
    Thread ReadThread=new Thread(){
    	
    	public void run(){
    		int num = -1;
    		byte[] buffer = new byte[1024];
    		byte[] buffer_new = new byte[1024];
    		int i = 0;
    		int n = 0;
    		bRun = true;
    	   
    		//�����߳�
    		while(true){
    			try{
    				while(is.available()==0){     //�ȴ�����
    					while(bRun == false){}
    					
    				}
    				while(true){
    					n=0;
    					while((num=is.read(buffer))== -1);          //��������
    					 String  st = new String(buffer,0,num);
    					
    					fmsg+=st;    //�����յ�����
    					for(i=0;i<num;i++){
    						if((buffer[i] == 0x0d)&&(buffer[i+1]==0x0a)){
    							buffer_new[n] = 0x0a;
    							i++;
    						}else{
    							buffer_new[n] = buffer[i];
    						}
    						n++;
    					}
    				   String s = new String(buffer_new,0,n);
    					smsg+=s;   //д����ջ���
    					if(is.available()==0)break;  //��ʱ��û�����ݲ�����������ʾ
    					 
    				}
    				//������ʾ��Ϣ��������ʾˢ��
    					handler.sendMessage(handler.obtainMessage());       	    		
    	    		}catch(IOException e){
    	    		}
    		}
    	}
    };
    
    //��Ϣ�������
    Handler handler= new Handler(){
    	public void handleMessage(Message msg){
    		
    		super.handleMessage(msg);
   // 		dis.setText(smsg);   //��ʾ���� 
   // 		sv.scrollTo(0,dis.getMeasuredHeight()); //�����������һҳ
    	
    		if(smsg.charAt(0)=='a'){
    			dongzuo.setText("���ֻ���");
    		}
    		else if(smsg.charAt(0)=='b'){
    			dongzuo.setText("���ֻ���");
    		}
    		else if(smsg.charAt(0)=='c'){
    			dongzuo.setText("ǰ�Ļ���");
    		}    
    		else{
    			dongzuo.setText("");
    		}
			
    		counting.setText(smsg.substring(3, 6));
    	//	angle.setText(smsg);
    	//	strength.setText(fmsg);
    		rat = Float.parseFloat(smsg.substring(2, 4));
    		ratingBar.setRating((float)(0.5+(rat - 10)*0.05));
    	}
    };
    
    //�رճ�����ô�����
    public void onDestroy(){
    	super.onDestroy();
    	if(_socket!=null)  //�ر�����socket
    	try{
    		_socket.close();
    	}catch(IOException e){}
        _bluetooth.disable();  //�ر���������
    }
    
    
 
    
    //���Ӱ�����Ӧ����
    public void onConnectButtonClicked(View v){ 
    	if(_bluetooth.isEnabled()==false){  //����������񲻿�������ʾ
    		Toast.makeText(this, " ��������...", Toast.LENGTH_LONG).show();
    		return;
    	}
    	
    	
        //��δ�����豸���DeviceListActivity�����豸����
    	Button btn = (Button) findViewById(R.id.Button03);
    	if(_socket==null){
    		Intent serverIntent = new Intent(this, DeviceListActivity.class); //��ת��������
    		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);  //���÷��غ궨��
    	}
    	else{
    		 //�ر�����socket
    	    try{
    	    	
    	    	is.close();            //�ر�������
    	    	_socket.close();        //�ر�����ͨ��
    	    	_socket = null;
    	    	bRun = false;
    	    	btn.setText("����");
    	    }catch(IOException e){}   
    	}
    	return;
    }
    //����ͼ�껭��------ax
    public void onChartClickedAx(View v){
    	int[] num = new int[7];
    	for(int i=6;i<13;i++){
    		num[i] = Integer.parseInt(smsg.substring(i,i+1));
    	}
    	Intent intent = new Intent(BTClient.this, Chart.class);
    	intent.putExtra("extra_data", num);
    	startActivity(intent);
    	overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    
    public void onChartClickedAy(View v){
    	int[] num = new int[7];
    	for(int i=13;i<20;i++){
    		num[i] = Integer.parseInt(smsg.substring(i,i+1));
    	}
    	Intent intent = new Intent(BTClient.this, Chart_ay.class);
    	intent.putExtra("extra_data_2", num);
    	startActivity(intent);
    	overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public void onChartClickedAz(View v){
    	int[] num = new int[7];
    	for(int i=20;i<27;i++){
    		num[i] = Integer.parseInt(smsg.substring(i,i+1));
    	}
    	Intent intent = new Intent(BTClient.this, Chart_az.class);
    	intent.putExtra("extra_data_3", num);
    	startActivity(intent);
    	overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public void onChartClickedGx(View v){
    	int[] num = new int[7];
    	for(int i=27;i<34;i++){
    		num[i] = Integer.parseInt(smsg.substring(i,i+1));
    	}
    	Intent intent = new Intent(BTClient.this, Chart_gx.class);
    	intent.putExtra("extra_data_4", num);
    	startActivity(intent);
    	overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public void onChartClickedGy(View v){
    	int[] num = new int[7];
    	for(int i=34;i<41;i++){
    		num[i] = Integer.parseInt(smsg.substring(i,i+1));
    	}
    	Intent intent = new Intent(BTClient.this, Chart_gy.class);
    	intent.putExtra("extra_data_5", num);
    	startActivity(intent);
    	overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    public void onChartClickedGz(View v){
    	int[] num = new int[7];
    	for(int i=41;i<48;i++){
    		num[i] = Integer.parseInt(smsg.substring(i,i+1));
    	}
    	Intent intent = new Intent(BTClient.this, Chart_gz.class);
    	intent.putExtra("extra_data_6", num);
    	startActivity(intent);
    	overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }
    
    //���水����Ӧ����
    public void onSaveButtonClicked(View v){
    	Save();
    }
    
    //���������Ӧ����
    public void onClearButtonClicked(View v){
    	smsg="";
    	fmsg="";
  //  	dis.setText(smsg);
		counting.setText(smsg);
		dongzuo.setText(smsg);
	//	angle.setText(smsg);
	//	strength.setText(smsg); 
    	return;
    }
    
    //�˳�������Ӧ����
    public void onQuitButtonClicked(View v){
    	finish();
    }
    
    //���湦��ʵ��
	private void Save() {
		//��ʾ�Ի��������ļ���
		LayoutInflater factory = LayoutInflater.from(BTClient.this);  //ͼ��ģ�����������
		final View DialogView =  factory.inflate(R.layout.sname, null);  //��sname.xmlģ��������ͼģ��
		new AlertDialog.Builder(BTClient.this)
								.setTitle("�ļ���")
								.setView(DialogView)   //������ͼģ��
								.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() //ȷ��������Ӧ����
								{
									public void onClick(DialogInterface dialog, int whichButton){
										EditText text1 = (EditText)DialogView.findViewById(R.id.sname);  //�õ��ļ����������
										filename = text1.getText().toString();  //�õ��ļ���
										
										try{
											if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //���SD����׼����
												
												filename =filename+".txt";   //���ļ���ĩβ����.txt										
												File sdCardDir = Environment.getExternalStorageDirectory();  //�õ�SD����Ŀ¼
												File BuildDir = new File(sdCardDir, "/data");   //��dataĿ¼���粻����������
												if(BuildDir.exists()==false)BuildDir.mkdirs();
												File saveFile =new File(BuildDir, filename);  //�½��ļ���������Ѵ������½��ĵ�
												FileOutputStream stream = new FileOutputStream(saveFile);  //���ļ�������
												stream.write(fmsg.getBytes());
												stream.close();
												Toast.makeText(BTClient.this, "�洢�ɹ���", Toast.LENGTH_SHORT).show();
											}else{
												Toast.makeText(BTClient.this, "û�д洢����", Toast.LENGTH_LONG).show();
											}
										
										}catch(IOException e){
											return;
										}
										
										
										
									}
								})
								.setNegativeButton("ȡ��",   //ȡ��������Ӧ����,ֱ���˳��Ի������κδ��� 
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) { 
									}
								}).show();  //��ʾ�Ի���
	} 
	//��ҳ�����¼�
	public void initView(){
		tvHint   = (TextView)this.findViewById(R.id.tv_hint);
		
		Button btn = (Button)findViewById(R.id.main_jintian);
		Animation animation = AnimationUtils.loadAnimation(BTClient.this, R.anim.flip_horizontal_in);
       
		
		Animation ani = new AlphaAnimation(0f,1f);
		ani.setDuration(1500);
		ani.setRepeatMode(Animation.REVERSE);
		ani.setRepeatCount(Animation.INFINITE);
		tvHint.startAnimation(ani);
		 btn.startAnimation(animation);
		
		
	}
	//Բ�ν���������¼�(׼ȷ��)
	public void initView_1(){
		rbar_1 = (RoundProgressBar) findViewById(R.id.roundProgressBar_1);
		btn_3 = (Button)findViewById(R.id.btn_3);
		btn_3.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				progress = 0;
		    	new Thread(new Runnable(){
		    		@Override
		    		public void run(){
		    			while(progress <= Integer.parseInt(smsg.substring(1,3))){
		    				progress +=3;
		    				System.out.println(progress);
		    				rbar_1.setProgresss(progress);
		    				try{
		    					Thread.sleep(100);
		    				}catch(InterruptedException e){
		    					e.printStackTrace();
		    				}
		    			}
		    		}
		    	}).start();
			}
		});
	}
	//������������ָ����
	public void initView_3(){
		ratingBar = (RatingBar) findViewById(R.id.rating);
		ratingBar.setRating(0.5f); 
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				
			 ratingBar.setRating(rating);
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.clock:
			Toast.makeText(this, "clock", Toast.LENGTH_SHORT).show();
			break;
		case R.id.search: 
			Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
			break;
		case R.id.example_icon: 
			Intent intent = new Intent(BTClient.this,ExamplePlay.class);
  		    startActivity(intent);
  		    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
  		    break;
		case R.id.about: 
			Intent intent1 = new Intent(BTClient.this,About.class);
  		    startActivity(intent1);
  		    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
			break;
		default: break;
		}
		return super.onOptionsItemSelected(item);
	}
	
    
}