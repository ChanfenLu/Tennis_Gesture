package com.test.BTClient;

import com.test.BTClient.anim.Rotate3DAnimation;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class About extends Activity implements OnClickListener {

	private int mCenterX = 160;   
    private int mCenterY = 0;   
    //A
    private ViewGroup layoutFront; 
    //C
    private ViewGroup layoutBack;
    //B
    private ViewGroup layoutRight; 
    //D
    private ViewGroup layoutLeft;
    
    private Rotate3DAnimation leftAnimation;   
    private Rotate3DAnimation rightAnimation;
    
    private int toLeft = 0;
    private int toRight = 1;
	
	private Button bt_leftBtn;
	private Button bt_rightBtn;
	private ViewGroup layout_viewGroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setView();
		initView();
		setListener();
		initFirst();
		initSecond();
		B2A(rightAnimation);
		D2A(leftAnimation);
		A2D(rightAnimation);
		C2D(leftAnimation);
		C2B(rightAnimation);
		A2B(leftAnimation);
		D2C(rightAnimation);
		B2C(leftAnimation);
		 ActionBar actionBar = getActionBar();
	     actionBar.setDisplayHomeAsUpEnabled(true);
	}
  
	public void setView() {
		setContentView(R.layout.layout_viewgroup);
	}

	
	public void initView() {
		layoutFront = (ViewGroup) findViewById(R.id.layout_viewGroup);
		bt_leftBtn = (Button) findViewById(R.id.bt_leftBtn);
		bt_rightBtn = (Button) findViewById(R.id.bt_rightBtn);
		
		bt_leftBtn.setEnabled(true); 
		bt_rightBtn.setEnabled(true); 
	}


	public void setListener() {
		bt_leftBtn.setOnClickListener(this);
		bt_rightBtn.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_leftBtn:
			frontMoveHandle(toRight);   
            v.setEnabled(false);   
			break;
		case R.id.bt_rightBtn:
			frontMoveHandle(toLeft);   
            v.setEnabled(false);  
			break;

		default:
			break;
		}		
	}
	
	
	 //左旋转
    public void initFirst(){   
        leftAnimation = new Rotate3DAnimation(0, -90, 0.0f, 0.0f, mCenterX, mCenterY);   
        rightAnimation = new Rotate3DAnimation(90, 0, 0.0f, 0.0f, mCenterX, mCenterY);   
        leftAnimation.setFillAfter(true);   
        leftAnimation.setDuration(1000);   
        rightAnimation.setFillAfter(true);   
        rightAnimation.setDuration(1000);   
    }
    
    //右旋转
    public void initSecond(){   
        leftAnimation = new Rotate3DAnimation(-90, 0, 0.0f, 0.0f, mCenterX, mCenterY);   
        rightAnimation = new Rotate3DAnimation(0, 90, 0.0f, 0.0f, mCenterX, mCenterY);   
        leftAnimation.setFillAfter(true);   
        leftAnimation.setDuration(1000);   
        rightAnimation.setFillAfter(true);   
        rightAnimation.setDuration(1000);   
    } 
    
    //B面转到A面所在位置
    public void B2A(Rotate3DAnimation rightAnimation){
    	setContentView(R.layout.layout_right);
    	layoutRight = (ViewGroup)findViewById(R.id.layout_right);
    	layoutRight.startAnimation(rightAnimation);
    	
    	Button leftBtn = (Button)findViewById(R.id.right_leftBtn);
    	Button rightBtn = (Button)findViewById(R.id.right_rightBtn);
    	leftBtn.setEnabled(true); rightBtn.setEnabled(true); 
    	
    	leftBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	rightMoveHandle(toRight);   
                v.setEnabled(false);   
            }   
        }); 
        rightBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	rightMoveHandle(toLeft);   
                v.setEnabled(false);   
            }   
        });	
    }
    
    //D面转到A面所在位置
    public void D2A(Rotate3DAnimation leftAnimation){
    	setContentView(R.layout.layout_left);
    	layoutLeft = (ViewGroup)findViewById(R.id.layout_left);
    	layoutLeft.startAnimation(leftAnimation);
    	
    	Button leftBtn = (Button)findViewById(R.id.left_leftBtn);
    	Button rightBtn = (Button)findViewById(R.id.left_rightBtn);
    	leftBtn.setEnabled(true); rightBtn.setEnabled(true);
    	
    	leftBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	leftMoveHandle(toRight);   
                v.setEnabled(false);   
            }   
        }); 
        rightBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	leftMoveHandle(toLeft);   
                v.setEnabled(false);   
            }   
        });	
    }
    
    //A面转到D面所在位置
    public void A2D(Rotate3DAnimation rightAnimation){
    	setContentView(R.layout.layout_viewgroup);
		layoutFront = (ViewGroup)findViewById(R.id.layout_viewGroup);
		layoutFront.startAnimation(rightAnimation);
		
		Button leftBtn = (Button)findViewById(R.id.bt_leftBtn);
		Button rightBtn = (Button)findViewById(R.id.bt_rightBtn);
		
		leftBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	frontMoveHandle(toRight);   
                v.setEnabled(false);   
            }   
        }); 
        rightBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	frontMoveHandle(toLeft);   
                v.setEnabled(false);   
            }   
        }); 
	}
    
    //C面转到D面所在位置
    public void C2D(Rotate3DAnimation leftAnimation){
    	setContentView(R.layout.layout_back);
    	layoutBack = (ViewGroup)findViewById(R.id.layout_back);
    	layoutBack.startAnimation(leftAnimation);
    	
    	Button leftBtn = (Button)findViewById(R.id.back_leftBtn);
		Button rightBtn = (Button)findViewById(R.id.back_rightBtn);
		
		leftBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	backMoveHandle(toRight);   
                v.setEnabled(false);   
            }   
        }); 
        rightBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	backMoveHandle(toLeft);   
                v.setEnabled(false);   
            }   
        }); 
    }
    
    //C面转到B面所在位置
    public void C2B(Rotate3DAnimation rightAnimation){
    	setContentView(R.layout.layout_back);
    	layoutBack = (ViewGroup)findViewById(R.id.layout_back);
    	layoutBack.startAnimation(rightAnimation);
    	
    	Button leftBtn = (Button)findViewById(R.id.back_leftBtn);
		Button rightBtn = (Button)findViewById(R.id.back_rightBtn);
		
		leftBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	backMoveHandle(toRight);   
                v.setEnabled(false);   
            }   
        }); 
        rightBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	backMoveHandle(toLeft);   
                v.setEnabled(false);   
            }   
        });
    }
    
    //A面转到B面所在位置
    public void A2B(Rotate3DAnimation leftAnimation){
    	setContentView(R.layout.layout_viewgroup);
    	layoutFront = (ViewGroup)findViewById(R.id.layout_viewGroup);
    	layoutFront.startAnimation(leftAnimation);
    	
    	Button leftBtn = (Button)findViewById(R.id.bt_leftBtn);
		Button rightBtn = (Button)findViewById(R.id.bt_rightBtn);
    	
    	leftBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	frontMoveHandle(toRight);   
                v.setEnabled(false);   
            }   
        }); 
        rightBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	frontMoveHandle(toLeft);   
                v.setEnabled(false);   
            }   
        }); 
    }
    
    //D面转到C面所在位置
    public void D2C(Rotate3DAnimation rightAnimation){
    	setContentView(R.layout.layout_left);
    	layoutLeft = (ViewGroup)findViewById(R.id.layout_left);
    	layoutLeft.startAnimation(rightAnimation);
    	
    	Button leftBtn = (Button)findViewById(R.id.left_leftBtn);
		Button rightBtn = (Button)findViewById(R.id.left_rightBtn);
		
		leftBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	leftMoveHandle(toRight);   
                v.setEnabled(false);   
            }   
        }); 
        rightBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	leftMoveHandle(toLeft);   
                v.setEnabled(false);   
            }   
        });
    }
    
    //B面转到C面所在位置
    public void B2C(Rotate3DAnimation leftAnimation){
    	setContentView(R.layout.layout_right);
    	layoutRight = (ViewGroup)findViewById(R.id.layout_right);
    	layoutRight.startAnimation(leftAnimation);
    	
    	Button leftBtn = (Button)findViewById(R.id.right_leftBtn);
		Button rightBtn = (Button)findViewById(R.id.right_rightBtn);
		
		leftBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	rightMoveHandle(toRight);   
                v.setEnabled(false);   
            }   
        }); 
        rightBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	rightMoveHandle(toLeft);   
                v.setEnabled(false);   
            }   
        });
    }
    
    //A在正面时
    public void frontMoveHandle(int to){
    	if(to == toLeft){
    		initFirst();
        	layoutFront.startAnimation(leftAnimation);
        	B2A(rightAnimation);
    	}else if(to == toRight){
    		initSecond();
        	layoutFront.startAnimation(rightAnimation);
        	D2A(leftAnimation);
    	}
    	
    }
    
    //B位于正面时
    public void rightMoveHandle(int to){
    	if(to == toLeft){
    		initFirst();
        	layoutRight.startAnimation(leftAnimation);
        	C2B(rightAnimation);
    	}else if(to == toRight){
    		initSecond();
        	layoutRight.startAnimation(rightAnimation);
        	A2B(leftAnimation);
    	}
    	
    }
    
    //D位于正面时
    public void leftMoveHandle(int to){
    	if(to == toLeft){
    		initFirst();
        	layoutLeft.startAnimation(leftAnimation);
        	A2D(rightAnimation);
    	}else if(to == toRight){
    		initSecond();
        	layoutLeft.startAnimation(rightAnimation);
        	C2D(leftAnimation);
    	}
    	
    }
    
    //C位于正面时
    public void backMoveHandle(int to){
    	if(to == toLeft){
    		initFirst();
        	layoutBack.startAnimation(leftAnimation);
        	D2C(rightAnimation);
    	}else if(to == toRight){
    		initSecond();
        	layoutBack.startAnimation(rightAnimation);
        	B2C(leftAnimation);
    	}
    	
    	
    }

	
	
	//菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		  switch (item.getItemId()) {
	        case android.R.id.home:
	        	Intent intent = new Intent(About.this, BTClient.class);
	        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	startActivity(intent);
	        	overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	        	break;
	        case R.id.example_icon:
	       	    Intent intent1 = new Intent(About.this, ExamplePlay.class);
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
		return super.onOptionsItemSelected(item);
	}

    
}
