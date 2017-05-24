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
        //����7�����ݵ㣨��x���꣩
        LineData mLineData = makeLineData(7, num);
        setCharStyle(mLineChart, mLineData, Color.WHITE);
      }
    
    private LineData makeLineData(int count, int[] num){
    	//x����ʾ����������
    	ArrayList<String> x = new ArrayList<String>();
    	for(int i=0; i<count;i++){
    		x.add(i+" ");
    	}
    	//y����ʾ��������
    	ArrayList<Entry> y = new ArrayList<Entry>();
    	for(int i=0;i<count;i++){
    		float val = (float)(num[i]);
    		Entry entry = new Entry(val,i);
    		y.add(entry);
    	}
    	
    	//y�����ݼ�
    	LineDataSet mLineDataSet = new LineDataSet(y, "��������");
    	//��y��ļ��������ò���
    	//�߿�
    	mLineDataSet.setLineWidth(3.0f);
    	//��ʾ��Բ�δ�С
    	mLineDataSet.setCircleSize(5.0f);
    	//���ֵ���ɫ
    	mLineDataSet.setCircleColor(Color.RED);
    	//Բ�����ɫ
    	mLineDataSet.setCircleColor(Color.BLUE);
    	//������������ݵ㣬Բ��������������Ŀհ״�����ɫ
    	mLineDataSet.setCircleColorHole(Color.GREEN);
    	
    	//����mLineDataSet.setDrawHighlightIndicators(false)��
    	//HighLight��ʮ�ֽ�����ݺ��߽�������ʾ
    	//ͬʱ��mLineDataSet.setHighLightColor(Color.RED)����ʧЧ
    	mLineDataSet.setDrawHighlightIndicators(true);
    	//������ʮ�ֽ����ߵ���ɫ
    	mLineDataSet.setHighLightColor(Color.RED);
    	//������������ʾ���ݵ�������С����ɫ
    	mLineDataSet.setValueTextSize(15.0f);
    	mLineDataSet.setValueTextColor(Color.BLUE);
    	mLineDataSet.setDrawCircleHole(true);
    
    	//�ı�������ʽΪ����
    	mLineDataSet.setDrawCubic(true);
    	//���ߵ�ƽ���ȣ�ֵԽ��Խƽ��
    	mLineDataSet.setCubicIntensity(0.3f);
    	
    	//��������·����������ã���ɫ��͸��
    	//mLineDataSet.setDrawFilled(true);
    	//mLineDataSet.setFillColor(Color.YELLOW);
    	//mLineDataSet.setFillAlpha(128);
    	
    	//������������ʾ���ݵĸ�ʽ����������ã���������ʾfloat���ݸ�ʽ
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
    	//�Ƿ�������ͼ����ӱ߿�
    	mLineChart.setDrawBorders(true);
    	
    	//��������
    	mLineChart.setDescription("������������");
    	mLineChart.setDescriptionTextSize(30.0f);
    	mLineChart.setDescriptionColor(Color.BLUE);
    	
    	//���û�����ݵ�ʱ�򣬻���ʾ���������listview��emtpyview
    	mLineChart.setNoDataTextDescription("�������MPAndroidchart������Ϊ�գ���ô�㽫�����������");
    	
    	//�Ƿ���Ʊ���ڱ�����ɫ
    	//���mLineChart.setDrawGridBackground(false)��
    	//��mLineChart.setGridBackgroundColor(Color.GRAY)��ʧЧ
    	mLineChart.setDrawGridBackground(false);
    	mLineChart.setGridBackgroundColor(Color.GRAY);
    	
    	//����
    	mLineChart.setTouchEnabled(true);
    	//��ҷ
    	mLineChart.setDragEnabled(true);
    	//����
    	mLineChart.setScaleEnabled(true);
    	mLineChart.setPinchZoom(true);
    	//���ñ���
    	mLineChart.setBackgroundColor(color);
    	//����x,y������
    	mLineChart.setData(mLineData);
    	
    	//���ñ���ͼ��ʾ�������Ǹ�һ��y��value��
    	Legend mLegend =mLineChart.getLegend();
    	mLegend.setPosition(LegendPosition.BELOW_CHART_CENTER);
    	mLegend.setForm(LegendForm.CIRCLE);//��ʽĬ��Ϊ����
    	mLegend.setFormSize(20.0f);//��ʽͼ���С
    	mLegend.setTextColor(Color.BLUE);
    	mLegend.setTextSize(20.0f);//���������С
    	//��x�ᶯ����ʱ��2000����
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
