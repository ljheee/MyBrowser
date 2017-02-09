package com.ljheee.mybrowser;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.http.SslError;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnKeyListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {

	WebView webView;
	WebSettings webSettings;
	GridView gridView;
	
	/*Toolbar底部菜单选项下标*/
	private final int TOOLBAR_ITEM_BACK = 0;// 退后
	private final int TOOLBAR_ITEM_FORWARD = 1;// 前进
	private final int TOOLBAR_ITEM_PAGEHOME = 2;// 首页
	private final int TOOLBAR_ITEM_MENU = 3;// 菜单
	
	/** 底部菜单图片 **/
	int[] menu_toolbar_image_array = { 
			R.drawable.controlbar_backward_enable,
			R.drawable.controlbar_forward_enable, 
			R.drawable.controlbar_homepage,
			R.drawable.controlbar_menu };
	
	
	GridView menuGrid;
	private AlertDialog menuDialog;
	private View menuView;
	
	/*-- MENU菜单选项下标 --*/
	private final int ITEM_0 = 0;
	private final int ITEM_1 = 1;
	private final int ITEM_2 = 2;
	private final int ITEM_3 = 3;
	private final int ITEM_4 = 4;
	private final int ITEM_5_ABOUT = 5;
	private final int ITEM_6_EXIT = 6;
	private final int ITEM_7_SETTING = 7;
	/** 菜单图片 **/
	int[] menu_image_array = { R.drawable.menu_add_to_bookmark,
			R.drawable.menu_refresh, 
			R.drawable.menu_day,
			R.drawable.menu_nightmode, 
			R.drawable.menu_checknet,
			R.drawable.menu_about, 
			R.drawable.menu_quit,
			R.drawable.menu_syssettings };
	/** 菜单文字 **/
	String[] menu_name_array = { "软件下载", "刷新页面", "日间", "夜间", "检查网络", "相关信息",
			"退出", "设置" };
	
	static boolean isNight = false;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initTheme();
        setContentView(R.layout.activity_main);
        
        webView = (WebView) findViewById(R.id.webView);
        webViewSetting();
        gridView = (GridView) findViewById(R.id.gridView);
        
        gridView.setBackgroundResource(R.drawable.channelgallery_bg);
		gridView.setNumColumns(4);
		gridView.setGravity(Gravity.CENTER);
		gridView.setVerticalSpacing(10);
		gridView.setHorizontalSpacing(10);
		gridView.setAdapter(getMenuAdapter(null,menu_toolbar_image_array));
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				switch (arg2) {
				case TOOLBAR_ITEM_PAGEHOME:
					webView.clearHistory();
					webView.loadUrl("http://hao.360.cn/");
					break;
				case TOOLBAR_ITEM_BACK:
					if (webView.canGoBack()) {
						webView.goBack();
					} else {
						Toast.makeText(MainActivity.this, "不能再后退了！",
								Toast.LENGTH_SHORT).show();
					}
					break;
				case TOOLBAR_ITEM_FORWARD:
					if (webView.canGoForward()) {
						webView.goForward();
					} else {
						Toast.makeText(MainActivity.this, "不能前进噢！",
								Toast.LENGTH_SHORT).show();
					}
					break;
				case TOOLBAR_ITEM_MENU:
					menuDialog.show();
					break;
				}
			}
		});
        
		initMenuDialog();
    }
    
    private void initTheme() {
    	if(isNight){
    		setTheme(R.style.NightTheme);
    	} else{
    		setTheme(R.style.AppTheme);
    	}
		
	}

	/**
     * 初始化-菜单对话框
     */
    private void initMenuDialog() {
    	
    	menuView = View.inflate(this, R.layout.gridview_menu, null);
		// 创建AlertDialog
		menuDialog = new AlertDialog.Builder(this).create();
		menuDialog.setView(menuView);
		menuGrid = (GridView) menuView.findViewById(R.id.gridview);
		menuGrid.setAdapter(getMenuAdapter(menu_name_array, menu_image_array));
		menuGrid.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {//返回事件
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					menuDialog.dismiss();//清除对话框
				}
				return false;
			}
		});
		menuGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case ITEM_0:
					webView.loadUrl("http://app.qq.com/?rootvia=YYBH5.STORE.NEWS_990190&sid=ATHViNAjM1760G69aqTVlvMV#id=app_cate");
					break;
				case ITEM_1://刷新
					webView.loadUrl(webView.getUrl());
					break;
				case ITEM_2: //日间模式
					isNight = true;
//					webView.loadUrl("javascript:load_day()");
//					webView.loadUrl("javascript:document.bgColor='white'; document.fgColor='black'");
					
					Toast.makeText(MainActivity.this, "日间模式", Toast.LENGTH_SHORT).show();
					break;
				case ITEM_3: //夜间模式
//					webView.loadUrl("javascript:load_night()");
//					webView.loadUrl("javascript:document.bgColor='black'; document.fgColor='white'");

					isNight = true;
					setTheme(R.style.NightTheme);
					refreshUI();
					
					Toast.makeText(MainActivity.this, "夜间模式", Toast.LENGTH_SHORT).show();
					break;
				case ITEM_4:// 检查网络
					
					break;
				case ITEM_5_ABOUT:
					new AlertDialog.Builder(MainActivity.this).setTitle("欢迎光临")
							.setMessage("QQ:554278334\n极简版")
							.setPositiveButton("确定", null).show();
					break;
				case ITEM_6_EXIT:
					finish();
					break;
				case ITEM_7_SETTING:
					Intent intent = new Intent(Settings.ACTION_SETTINGS);
					startActivity(intent);
					break;
				}
				menuDialog.dismiss();
			}
		});
    }

	protected void refreshUI() {

		TypedValue background = new TypedValue();//背景色
        TypedValue textColor = new TypedValue();//字体颜色
        Resources.Theme theme = getTheme();
        theme.resolveAttribute(R.attr.clockBackground, background, true);//获取[当前主题下的]属性值
        theme.resolveAttribute(R.attr.clockTextColor, textColor, true);
        
        webView.setBackgroundResource(background.resourceId);
//        ((View) webView).setTextColor(resources.getColor(textColor.resourceId));
	}

	/**
     * 对webSettings的设置
     */
    private void webViewSetting() {

    	webSettings = webView.getSettings();
    	
    	webSettings.setJavaScriptEnabled(true);
    	webSettings.setTextSize(TextSize.NORMAL);
    	webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    	
    	// 初始化时缩放
    	webSettings.setDatabaseEnabled(true);
    	webSettings.setDomStorageEnabled(true);
    	// 设置加载进来的页面自适应手机屏幕
    	webSettings.setUseWideViewPort(true);
    	webSettings.setLoadWithOverviewMode(true);
    	webSettings.setDefaultTextEncodingName("utf-8");
    	
    	// （主要用于平板，针对特定屏幕代码调整分辨率）
    	DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int mDensity = metrics.densityDpi;

		if (mDensity == 120) {
			webSettings.setDefaultZoom(ZoomDensity.CLOSE);
		} else if (mDensity == 160) {
			webSettings.setDefaultZoom(ZoomDensity.MEDIUM);
		} else if (mDensity == 240) {
			webSettings.setDefaultZoom(ZoomDensity.FAR);
		}
    	
		 webView.loadUrl("file:///android_asset/js.html");
    	 webView.setWebViewClient(new MyWebViewClient());
	}

	private SimpleAdapter getMenuAdapter(String[] menuNameArray,
			int[] imageResourceArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		
		for (int i = 0; i < imageResourceArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", imageResourceArray[i]);
			if(menuNameArray != null){
				map.put("itemText", menuNameArray[i]);
			}
			data.add(map);
		}
		SimpleAdapter simperAdapter = new SimpleAdapter(this, data,
				R.layout.item_menu, 
				new String[] { "itemImage", "itemText"},
				new int[] { R.id.item_image , R.id.item_text});
		return simperAdapter;
	}
    
    
    /**
     *点击链接由自己处理，而不是启动Android的系统browser中响应该链接
     */
    class MyWebViewClient extends WebViewClient{
    	
    	@Override
    	public void onReceivedSslError(WebView view, SslErrorHandler handler,
    			SslError error) {
    		handler.proceed();// 接受证书
    	}

    	/**
    	 * 点击链接由自己处理，而不是启动Android的系统browser中响应该链接
    	 * 对网页中超链接按钮的响应。当按下某个连接时 WebViewClient会调用这个方法，并传递参数
    	 */
		@Override
		public boolean shouldOverrideUrlLoading(WebView webView, String url) {
			if(isNight){
				webView.loadUrl("javascript:document.bgColor='white'; document.fgColor='black'");
			}
			webView.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			view.loadUrl("javascript:window.local_obj.showSource"
					+ "('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
			super.onPageFinished(view, url);
			MainActivity.this.setTitle(view.getTitle());
		}
		

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			Toast.makeText(MainActivity.this, "网络连接失败，请连接网络！",
					Toast.LENGTH_SHORT).show();
		}
    	
    }
    
    
    
    
}
