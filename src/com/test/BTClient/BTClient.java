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

@SuppressLint({ "NewApi", "HandlerLeak", "InflateParams" }) //标注忽略指定警告
public class BTClient extends Activity  {
	
	private final static int REQUEST_CONNECT_DEVICE = 1;    //宏定义查询设备句柄
	
	private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";   //SPP服务UUID号
	
	private InputStream is;    //输入流，用来接收蓝牙数据
	//private TextView text0;    //提示栏解句柄
  //  private EditText edit0;    //发送数据输入句柄
 //   private TextView dis;
 //   private ScrollView sv;      //翻页句柄
    private String smsg = "";    //显示用数据缓存
    private String fmsg = "";    //保存用数据缓存
    private TextView counting;
    private TextView dongzuo;    //描述动作类型
   
	private TextView tvHint;        //首页的文字“上滑可以进入首页”
	
	RoundProgressBar rbar_1;        //圆形进度条
	Button btn_3;
	private int progress = 0;
	
	RatingBar ratingBar;              //星级条
	float rat;

    public String filename=""; //用来保存存储的文件名
    BluetoothDevice _device = null;     //蓝牙设备
    BluetoothSocket _socket = null;      //蓝牙通信socket
    boolean _discoveryFinished = false;    
    boolean bRun = true;
    boolean bThread = false;
 
    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();    //获取本地蓝牙适配器，即蓝牙设备
	
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);   //设置画面为主画面 main.xml
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        
        initView();
        initView_1();
        initView_3();
        
        
     //   text0 = (TextView)findViewById(R.id.Text0);  //得到提示栏句柄
     //   edit0 = (EditText)findViewById(R.id.Edit0);   //得到输入框句柄
    //    sv = (ScrollView)findViewById(R.id.ScrollView01);  //得到翻页句柄
  //      dis = (TextView) findViewById(R.id.in);      //得到数据显示句柄
        counting = (TextView) findViewById(R.id.counting);
		dongzuo = (TextView) findViewById(R.id.dongzuo);

       //如果打开本地蓝牙设备不成功，提示信息，结束程序
        if (_bluetooth == null){
        	Toast.makeText(this, "无法打开手机蓝牙，请确认手机是否有蓝牙功能！", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        // 开启线程设置设备可以被搜索  
       new Thread(){
    	   public void run(){
    		   if(_bluetooth.isEnabled()==false){   
        		_bluetooth.enable();
    		   }
    	   }   	   
       }.start();      
  
    }
	
    //发送按键响应
  /*  public void onSendButtonClicked(View v){
    //	Button btn = (Button)findViewById(R.id.Button02);
    	try{
    		OutputStream os = _socket.getOutputStream();   //蓝牙连接输出流
    		byte[] bos_new = new byte[2];
    		bos_new[0]='0';
    		bos_new[1]='0';
    		if(btn.getText().toString().equals("待机")){
        		bos_new[1]='0';
        		btn.setText("运行");
    		}else{
        		bos_new[1]='1';
        		btn.setText("待机");
    		}
    		os.write(bos_new);	
    	}catch(IOException e){  		
    	}  	
    }*/
    
    //接收活动结果，响应startActivityForResult()
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch(requestCode){
    	case REQUEST_CONNECT_DEVICE:     //连接结果，由DeviceListActivity设置返回
    		// 响应返回结果
            if (resultCode == Activity.RESULT_OK) {   //连接成功，由DeviceListActivity设置返回
                // MAC地址，由DeviceListActivity设置返回
                String address = data.getExtras()
                                     .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                // 得到蓝牙设备句柄      
                _device = _bluetooth.getRemoteDevice(address);
 
                // 用服务号得到socket
                try{
                	_socket = _device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
                }catch(IOException e){
                	Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
                }
                //连接socket
            	Button btn = (Button) findViewById(R.id.Button03);
                try{
                	_socket.connect();
                	Toast.makeText(this, "连接"+_device.getName()+"成功！", Toast.LENGTH_SHORT).show();
                	btn.setText("断开");
                }catch(IOException e){
                	try{
                		Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
                		_socket.close();
                		_socket = null;
                	}catch(IOException ee){
                		Toast.makeText(this, "连接失败！", Toast.LENGTH_SHORT).show();
                	}
                	
                	return;
                }
                
                //捕获异常
                try{
            		is = _socket.getInputStream();   //得到蓝牙数据输入流
            		}catch(IOException e){
            			Toast.makeText(this, "接收数据失败！", Toast.LENGTH_SHORT).show();
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
    
    //接收数据线程
    Thread ReadThread=new Thread(){
    	
    	public void run(){
    		int num = -1;
    		byte[] buffer = new byte[1024];
    		byte[] buffer_new = new byte[1024];
    		int i = 0;
    		int n = 0;
    		bRun = true;
    	   
    		//接收线程
    		while(true){
    			try{
    				while(is.available()==0){     //等待接收
    					while(bRun == false){}
    					
    				}
    				while(true){
    					n=0;
    					while((num=is.read(buffer))== -1);          //读入数据
    					 String  st = new String(buffer,0,num);
    					
    					fmsg+=st;    //保存收到数据
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
    					smsg+=s;   //写入接收缓存
    					if(is.available()==0)break;  //短时间没有数据才跳出进行显示
    					 
    				}
    				//发送显示消息，进行显示刷新
    					handler.sendMessage(handler.obtainMessage());       	    		
    	    		}catch(IOException e){
    	    		}
    		}
    	}
    };
    
    //消息处理队列
    Handler handler= new Handler(){
    	public void handleMessage(Message msg){
    		
    		super.handleMessage(msg);
   // 		dis.setText(smsg);   //显示数据 
   // 		sv.scrollTo(0,dis.getMeasuredHeight()); //跳至数据最后一页
    	
    		if(smsg.charAt(0)=='a'){
    			dongzuo.setText("正手击球");
    		}
    		else if(smsg.charAt(0)=='b'){
    			dongzuo.setText("反手击球");
    		}
    		else if(smsg.charAt(0)=='c'){
    			dongzuo.setText("前拍挥球");
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
    
    //关闭程序掉用处理部分
    public void onDestroy(){
    	super.onDestroy();
    	if(_socket!=null)  //关闭连接socket
    	try{
    		_socket.close();
    	}catch(IOException e){}
        _bluetooth.disable();  //关闭蓝牙服务
    }
    
    
 
    
    //连接按键响应函数
    public void onConnectButtonClicked(View v){ 
    	if(_bluetooth.isEnabled()==false){  //如果蓝牙服务不可用则提示
    		Toast.makeText(this, " 打开蓝牙中...", Toast.LENGTH_LONG).show();
    		return;
    	}
    	
    	
        //如未连接设备则打开DeviceListActivity进行设备搜索
    	Button btn = (Button) findViewById(R.id.Button03);
    	if(_socket==null){
    		Intent serverIntent = new Intent(this, DeviceListActivity.class); //跳转程序设置
    		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);  //设置返回宏定义
    	}
    	else{
    		 //关闭连接socket
    	    try{
    	    	
    	    	is.close();            //关闭输入流
    	    	_socket.close();        //关闭蓝牙通信
    	    	_socket = null;
    	    	bRun = false;
    	    	btn.setText("连接");
    	    }catch(IOException e){}   
    	}
    	return;
    }
    //开启图标画面------ax
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
    
    //保存按键响应函数
    public void onSaveButtonClicked(View v){
    	Save();
    }
    
    //清除按键响应函数
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
    
    //退出按键响应函数
    public void onQuitButtonClicked(View v){
    	finish();
    }
    
    //保存功能实现
	private void Save() {
		//显示对话框输入文件名
		LayoutInflater factory = LayoutInflater.from(BTClient.this);  //图层模板生成器句柄
		final View DialogView =  factory.inflate(R.layout.sname, null);  //用sname.xml模板生成视图模板
		new AlertDialog.Builder(BTClient.this)
								.setTitle("文件名")
								.setView(DialogView)   //设置视图模板
								.setPositiveButton("确定",
								new DialogInterface.OnClickListener() //确定按键响应函数
								{
									public void onClick(DialogInterface dialog, int whichButton){
										EditText text1 = (EditText)DialogView.findViewById(R.id.sname);  //得到文件名输入框句柄
										filename = text1.getText().toString();  //得到文件名
										
										try{
											if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  //如果SD卡已准备好
												
												filename =filename+".txt";   //在文件名末尾加上.txt										
												File sdCardDir = Environment.getExternalStorageDirectory();  //得到SD卡根目录
												File BuildDir = new File(sdCardDir, "/data");   //打开data目录，如不存在则生成
												if(BuildDir.exists()==false)BuildDir.mkdirs();
												File saveFile =new File(BuildDir, filename);  //新建文件句柄，如已存在仍新建文档
												FileOutputStream stream = new FileOutputStream(saveFile);  //打开文件输入流
												stream.write(fmsg.getBytes());
												stream.close();
												Toast.makeText(BTClient.this, "存储成功！", Toast.LENGTH_SHORT).show();
											}else{
												Toast.makeText(BTClient.this, "没有存储卡！", Toast.LENGTH_LONG).show();
											}
										
										}catch(IOException e){
											return;
										}
										
										
										
									}
								})
								.setNegativeButton("取消",   //取消按键响应函数,直接退出对话框不做任何处理 
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) { 
									}
								}).show();  //显示对话框
	} 
	//首页动画事件
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
	//圆形进度条点击事件(准确度)
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
	//星星条（健康指数）
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