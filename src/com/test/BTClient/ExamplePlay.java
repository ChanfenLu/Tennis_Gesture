package com.test.BTClient;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class ExamplePlay extends Activity {

	private ArrayAdapter<String> adapter;
	VideoView vv;
	TextView text;
	Uri rawUri1;
	Button play;
	Button pause;
	Button stop;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.example_layout);
		ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
		
		play = (Button)findViewById(R.id.play);
		pause = (Button)findViewById(R.id.pause);
		stop = (Button)findViewById(R.id.stop);
		vv = (VideoView)findViewById(R.id.videoview);
		vv.setMediaController(new MediaController(this));
		text = (TextView) findViewById(R.id.tips);
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		String[] arr = { "�����շ�","���ֻ���","���ֻ���","����"};
		
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,arr);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		//����ѡ�񣬲��Ų�ͬ����Ƶ
		spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
			public void onItemSelected(AdapterView<?> arg0,View view, int position, long id){
				Toast.makeText(ExamplePlay.this, "����ѡ����"+adapter.getItem(position), Toast.LENGTH_SHORT).show();
				if(adapter.getItem(position) == "�����շ�"){
					text.setText(R.string.tips_1);
					rawUri1 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ab);
					vv.setVideoURI(rawUri1);
					vv.start();
					vv.requestFocus();
					//��������
					play.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v){
							vv.start();
							vv.requestFocus();
						}
					});
					pause.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v){
							vv.pause();
						}
					});
					stop.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v){
							vv.stopPlayback();
						}
					});
				}
				else if(adapter.getItem(position) == "���ֻ���"){
					text.setText(R.string.tips_2);
					rawUri1 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ac);
					vv.setVideoURI(rawUri1);
					//��������
					play.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v){
							vv.start();
							vv.requestFocus();
						}
					});
					pause.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v){
							vv.pause();
						}
					});
					stop.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v){
							vv.stopPlayback();
						}
					});
				}
				else if(adapter.getItem(position) == "���ֻ���" ){
					text.setText(R.string.tips_3);
					rawUri1 = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ad);
					vv.setVideoURI(rawUri1);
					//��������
					play.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v){
							vv.start();
							vv.requestFocus();
						}
					});
					pause.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v){
							vv.pause();
						}
					});
					stop.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v){
							vv.stopPlayback();
						}
					});
				}
				else if(adapter.getItem(position) == "����" ){
					finish();
					overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
				}
				
				//������Ϣ��ʾ����ʾ�������
				vv.setOnCompletionListener(new OnCompletionListener(){
					@Override
					public void onCompletion(MediaPlayer mediaplayer){
						Toast.makeText(ExamplePlay.this, "��Ƶ�������", Toast.LENGTH_SHORT).show();
					}
				});
				arg0.setVisibility(View.VISIBLE);
			}
			public void onNothingSelected(AdapterView<?> arg0){
				Toast.makeText(ExamplePlay.this, "Nothing", Toast.LENGTH_SHORT).show();
				arg0.setVisibility(View.VISIBLE);
			}
		});
		
		
	}
	
	
	 @Override
	public boolean onCreateOptionsMenu(Menu menu) {//�����˵�
	      getMenuInflater().inflate(R.menu.menu_main,menu);
	        return true;
	    }
	
	public boolean onOptionsItemSelected(MenuItem item) { //�˵���Ӧ����
	        switch (item.getItemId()) {
	        case android.R.id.home:
	        	Intent intent = new Intent(ExamplePlay.this, BTClient.class);
	        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	startActivity(intent);
	        	overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	        	break;
	        case R.id.about:
	       	    Intent intent1 = new Intent(ExamplePlay.this,About.class);
	       	    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	  		    startActivity(intent1);
	  		    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	        	break;
	        case R.id.clock:
	        	Toast.makeText(this, "you are good", Toast.LENGTH_SHORT).show();
	        	break;
	        case R.id.search:
	        	Toast.makeText(this, "you are good", Toast.LENGTH_SHORT).show();
	        	break;
	        default: break;   	
	    }
	        return true;
	}
}
