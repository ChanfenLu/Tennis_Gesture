package com.test.BTClient;


import java.util.ArrayList;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;



public class Chart extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mpandroidchart);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        
        Intent intent = getIntent();
        int[] num = intent.getIntArrayExtra("extra_data");
        
        LineChart mLineChart = (LineChart) findViewById(R.id.chart_ax);
        //制作7个数据点（沿x坐标）
        LineData mLineData = makeLineData(7, num);
        setCharStyle(mLineChart, mLineData, Color.WHITE);
      }
    
    private LineData makeLineData(int count, int[] num){
    	//x轴显示的数据设置
    	ArrayList<String> x = new ArrayList<String>();
    	for(int i=0; i<count;i++){
    		x.add(i+" ");
    	}
    	//y轴显示数据设置
    	ArrayList<Entry> y = new ArrayList<Entry>();
    	for(int i=0;i<count;i++){
    		float val = (float)(num[i]);
    		Entry entry = new Entry(val,i);
    		y.add(entry);
    	}
    	
    	//y轴数据集
    	LineDataSet mLineDataSet = new LineDataSet(y, "测试数据");
    	//用y轴的集合来设置参数
    	//线宽
    	mLineDataSet.setLineWidth(3.0f);
    	//显示的圆形大小
    	mLineDataSet.setCircleSize(5.0f);
    	//折现的颜色
    	mLineDataSet.setCircleColor(Color.RED);
    	//圆珠的颜色
    	mLineDataSet.setCircleColor(Color.BLUE);
    	//填充折现上数据点，圆球里面包裹的中心空白处的颜色
    	mLineDataSet.setCircleColorHole(Color.GREEN);
    	
    	//设置mLineDataSet.setDrawHighlightIndicators(false)后
    	//HighLight的十字交叉的纵横线将不会显示
    	//同时，mLineDataSet.setHighLightColor(Color.RED)将会失效
    	mLineDataSet.setDrawHighlightIndicators(true);
    	//按击后，十字交叉线的颜色
    	mLineDataSet.setHighLightColor(Color.RED);
    	//设置这项上显示数据点的字体大小和颜色
    	mLineDataSet.setValueTextSize(15.0f);
    	mLineDataSet.setValueTextColor(Color.BLUE);
    	mLineDataSet.setDrawCircleHole(true);
    
    	//改变折现样式为曲线
    	mLineDataSet.setDrawCubic(true);
    	//曲线的平滑度，值越大越平滑
    	mLineDataSet.setCubicIntensity(0.3f);
    	
    	//填充曲线下方的区域设置，黄色和透明
    	//mLineDataSet.setDrawFilled(true);
    	//mLineDataSet.setFillColor(Color.YELLOW);
    	//mLineDataSet.setFillAlpha(128);
    	
    	//设置折现上显示数据的格式，如果不设置，将设置显示float数据格式
    	mLineDataSet.setValueFormatter(new ValueFormatter(){
			@Override
			public String getFormattedValue(float value,
					com.github.mikephil.charting.data.Entry entry,
					int dataSetIndex, ViewPortHandler viewPortHandler) {
				int n = (int) value;
    			String s = "y=" + n;
    			return s;
			}
    	});
    	
    	ArrayList<LineDataSet> mLineDataSets = new ArrayList<LineDataSet>();
    	mLineDataSets.add(mLineDataSet);
    	LineData mLineData = new LineData(x, mLineDataSets);
    	return mLineData;
    }
    private void setCharStyle(LineChart mLineChart, LineData mLineData, int color){
    	//是否在折现图上添加边框
    	mLineChart.setDrawBorders(true);
    	
    	//数据描述
    	mLineChart.setDescription("动作类型数量");
    	mLineChart.setDescriptionTextSize(30.0f);
    	mLineChart.setDescriptionColor(Color.BLUE);
    	
    	//如果没有数据的时候，会显示这个，类似listview的emtpyview
    	mLineChart.setNoDataTextDescription("如果传给MPAndroidchart的数据为空，那么你将看到这段文字");
    	
    	//是否绘制表格内背景颜色
    	//如果mLineChart.setDrawGridBackground(false)，
    	//则mLineChart.setGridBackgroundColor(Color.GRAY)将失效
    	mLineChart.setDrawGridBackground(false);
    	mLineChart.setGridBackgroundColor(Color.GRAY);
    	
    	//触摸
    	mLineChart.setTouchEnabled(true);
    	//拖曳
    	mLineChart.setDragEnabled(true);
    	//缩放
    	mLineChart.setScaleEnabled(true);
    	mLineChart.setPinchZoom(true);
    	//设置背景
    	mLineChart.setBackgroundColor(color);
    	//设置x,y的数据
    	mLineChart.setData(mLineData);
    	
    	//设置比例图标示，就是那个一组y的value的
    	Legend mLegend =mLineChart.getLegend();
    	mLegend.setPosition(LegendPosition.BELOW_CHART_CENTER);
    	mLegend.setForm(LegendForm.CIRCLE);//样式默认为方形
    	mLegend.setFormSize(20.0f);//样式图标大小
    	mLegend.setTextColor(Color.BLUE);
    	mLegend.setTextSize(20.0f);//设置字体大小
    	//沿x轴动画，时间2000毫米
    	mLineChart.animateX(2000);
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		  switch (item.getItemId()) {
	        case android.R.id.home:
	        	Intent intent = new Intent(Chart.this, BTClient.class);
	        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        	startActivity(intent);
	        	overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	        	break;
	        case R.id.example_icon:
	       	    Intent intent1 = new Intent(Chart.this, ExamplePlay.class);
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
	        case R.id.about:
	        	Intent intent2 = new Intent(Chart.this, About.class);
	       	    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	  		    startActivity(intent2);
	  		    overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
	        	break;
	        default: break;   
		}
		return super.onOptionsItemSelected(item);
	}

    
}
