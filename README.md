[ ![Download](https://api.bintray.com/packages/jzoom/maven/jzoom-android-alert/images/download.svg) ](https://bintray.com/jzoom/maven/jzoom-android-alert/_latestVersion)
[![](https://travis-ci.org/jzoom/jzoom-android-alert.svg?branch=master)](https://travis-ci.org/jzoom/jzoom-android-alert)


# 概述
  android上原生的弹出框和ios原生的弹出框视觉差距较大，为了与ios的视觉效果类似，所以封装了这个库。


![效果](http://upload-images.jianshu.io/upload_images/2112529-491cb6ea2f124a41.gif?imageMogr2/auto-orient/strip)


# 特性
1、alert

普通弹出框，一般只有一个按钮

2、confirm

确认弹出框，一般带两个按钮，一个取消，一个确认

3、select

选择列表

4、showWait/hideWait

等待框

# 使用


compile 'com.jzoom:android-alert:1.2'


# 示例代码
```


public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.activity_main);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,getData()));
        listView.setOnItemClickListener(this);
    }

    private List<String> getData(){
        List<String> data = new ArrayList<String>();
        data.add("标题alert");
        data.add("标题内容alert");
        data.add("定制按钮alert");
        data.add("等待框");
        data.add("等待框无文字");
        data.add("确认");
        data.add("选择");
        return data;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i){
            case 0:
                Alert.alert(this, "我是一个弹出框", new Alert.AlertListener() {
                    @Override
                    public void onAlert(int buttonId) {
                        if(buttonId==Alert.OK){
                            Alert.toast(MainActivity.this,"点击了ok");
                        }else{
                            Alert.toast(MainActivity.this,"取消了");
                        }
                    }
                });
                break;
            case 1:
                Alert.alert(this,"标题", "我是一个弹出框", new Alert.AlertListener() {
                    @Override
                    public void onAlert(int buttonId) {
                        if(buttonId==Alert.OK){
                            Alert.toast(MainActivity.this,"点击了ok");
                        }else{
                            Alert.toast(MainActivity.this,"取消了");
                        }
                    }
                });
                break;

            case 2:
                Alert.dialog(this).setButtons("我知道了")
                        .setButtonStyles(R.style.jzoom_button_danger).setCanceledOnTouchOutside(true).setTitle("弹出框")
                        .setAlertListener(new Alert.AlertListener() {
                    @Override
                    public void onAlert(int buttonId) {
                        if(buttonId==Alert.OK){
                            Alert.toast(MainActivity.this,"点击了ok");
                        }else{
                            Alert.toast(MainActivity.this,"取消了");
                        }
                    }
                }).show();
                break;
            case 3:
                Alert.showWait(this,"请稍等...");
                listView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Alert.hidelWait();
                    }
                },2000);
                break;
            case 4:
                Alert.showWait(this,null);
                listView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Alert.hidelWait();
                    }
                },2000);
                break;
            case 5:
                Alert.confirm(this, "提示", "是否要退出登录?", new Alert.AlertListener() {
                    @Override
                    public void onAlert(int buttonId) {
                        if(buttonId==Alert.OK){
                            Alert.toast(MainActivity.this,"点击了ok");
                        }else{
                            Alert.toast(MainActivity.this,"点击了cancel");
                        }
                    }
                });
                break;
            case 6:
                Alert.select(this, "请选择",Arrays.asList("1","2","3","1","2","3","1","2","3","1",
                        "2","3","1","2","3","1","2","3","1","2","3"),0,new Alert.SelectListener(){

                    @Override
                    public void onSelect(int buttonId, int index) {
                        if(buttonId==Alert.OK){
                            Alert.toast(MainActivity.this,"选中下标为"+index);
                        }else{
                            Alert.toast(MainActivity.this,"取消");
                        }
                    }
                });
                break;
        }
    }
}


```



