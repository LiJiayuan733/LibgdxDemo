package com.thzs.app.datacoplite.ui.view.nView.base.handle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.thzs.app.datacoplite.core.Game;
import com.thzs.app.datacoplite.core.Log;
import com.thzs.app.datacoplite.ui.view.View;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzButton;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzEditArea2;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzImage;
import com.thzs.app.datacoplite.ui.widget.yzcover.YzWindowTopBar;
import com.thzs.app.datacoplite.ui.widget.yzcover.base.YzGdxQuery;
import com.thzs.app.datacoplite.util.Native.MutApiSupport;
import com.thzs.app.datacoplite.util.stm32utils.Led1306Picture;

import java.util.Properties;

public class STM32ViewHandle extends BaseViewHandle{
    public SerialPort port;
    public YzGdxQuery ActorGroup;
    public YzWindowTopBar Actor_WindowBar;
    public Table Table_Center,Table_Left,Table_Right;
    public YzButton Actor_Button1,Actor_Button2,Actor_Button3,Actor_Button4,Actor_Button5,Actor_Button6;
    public YzEditArea2 Edit_PortName,Edit_PortBaudRate,Edit_PortDataBits,Edit_PortStopBits,Edit_PicturePath;
    public YzImage image;
    public STM32ViewHandle(View view) {
        super(view);
        ActorGroup=new YzGdxQuery();
    }

    @Override
    public void ViewInit() {
        instance.stage= Game.stage();

        ActorGroup.add(Actor_WindowBar=new YzWindowTopBar(instance));
        Actor_WindowBar.setTitle("STM32 功能测试");

        Table_Center=new Table();Table_Left=new Table();Table_Right=new Table();
        Table_Center.setBounds(0,0,Game.STAGE_WIDTH,Game.STAGE_HEIGHT-Actor_WindowBar.getHeight());
        Table_Center.pad(8,8,8,8);
        Table_Left.setTransform(true);
        Table_Right.setTransform(true);
        Table_Center.top();

        ActorGroup.add(Actor_Button1=new YzButton("检索串口"),Actor_Button2=new YzButton("打开串口"),Actor_Button3=new YzButton("读取数据"),Actor_Button4=new YzButton("关闭串口"),Actor_Button5=new YzButton("选择图片"),Actor_Button6=new YzButton("发送图片"));
        ActorGroup.add(Edit_PortName=new YzEditArea2("串口名称"),Edit_PortBaudRate=new YzEditArea2("波特率"),Edit_PortDataBits=new YzEditArea2("数据位"),Edit_PortStopBits=new YzEditArea2("停止位"),Edit_PicturePath=new YzEditArea2("图片路径"));
        ActorGroup.add(image=new YzImage(128*2,64*2));
        ActorGroup.find(YzEditArea2.class).width(Game.STAGE_WIDTH/2f);

        ActorGroup.find(YzButton.class).to(Table_Left).each(gdxQuery -> gdxQuery.cell().pad(5));
        ActorGroup.find(YzEditArea2.class).to(Table_Right).each(gdxQuery -> gdxQuery.cell().pad(5));
        ActorGroup.find(YzImage.class).width(128*2).height(64*2).to(Table_Right);

        Table_Center.add(Table_Left);
        Table_Center.add(Table_Right);
        instance.stage.addActor(Table_Center);
        instance.stage.addActor(Actor_WindowBar);
        ActorGroup.loadYzFocus();
    }

    @Override
    public void dispose() {
        super.dispose();
        ActorGroup.dispose();
        if(port!=null&&port.isOpen()){
            port.closePort();
        }
    }

    @Override
    public void ViewListener() {
        super.ViewListener();
        Actor_Button1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                SerialPort[] Serials=SerialPort.getCommPorts();
                for (SerialPort s:Serials) {
                    Log.i(s.getSystemPortName()+"-"+s.getPortDescription()+"-"+s.getDescriptivePortName());
                }
            }
        });
        Actor_Button2.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                String portName=Edit_PortName.getText();
                int baudRate=Integer.parseInt(Edit_PortBaudRate.getText());
                int numDataBits=Integer.parseInt(Edit_PortDataBits.getText());
                int numStopBits=Integer.parseInt(Edit_PortStopBits.getText());
                try {
                    port = SerialPort.getCommPort(portName);
                    port.setBaudRate(baudRate);
                    port.setNumDataBits(numDataBits);
                    port.setNumStopBits(numStopBits);
                    port.setParity(SerialPort.NO_PARITY);
                    if(port.openPort()){
                        Log.i("Open Serial Port "+portName+" Success!");
                    }else {
                        Log.i("Open Serial Port "+portName+" Failed!");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        Actor_Button3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                try {
                    if(port.isOpen()){
                        int len=port.bytesAvailable();
                        byte[] bytes = new byte[len];
                        port.readBytes(bytes,len);
                        String s=new String(bytes,0,len);
                        Log.i(s);
                    }else{
                        Log.i("Please open the port first");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        Actor_Button4.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                String portName=Edit_PortName.getText();
                try {
                    if(port.closePort()){
                        Log.i("Close Serial Port "+portName+" Success!");
                    }else {
                        Log.i("Close Serial Port "+portName+" Failed!");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        Actor_Button5.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String path=MutApiSupport.SystemFileChoose();
                image.setImage(new Texture(Gdx.files.absolute(path)));
                Edit_PicturePath.setText(path);
                super.clicked(event, x, y);
            }
        });
        Actor_Button6.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Led1306Picture led1306Picture=new Led1306Picture(Gdx.files.absolute(Edit_PicturePath.getText()));
                byte[] c=led1306Picture.CreateData();
                //STM32F103VET6只支持最大64位发送，不然会发生未知错误
                for(int i=0;i<c.length/64;i++){
                    port.writeBytes(c,64,64*i);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                Log.i("发送完成");
                //port.writeBytes(led1306Picture.CreateData(),1024);
            }
        });
    }

    @Override
    public void loadData(Properties properties) {
        super.loadData(properties);
        Edit_PortBaudRate.setText(properties.getProperty("PortBaudRate"));
        Edit_PortName.setText(properties.getProperty("PortName"));
        Edit_PortDataBits.setText(properties.getProperty("PortDataBits"));
        Edit_PortStopBits.setText(properties.getProperty("PortStopBits"));
    }

    @Override
    public void SaveData(Properties properties) {
        super.SaveData(properties);
        properties.put("PortBaudRate",Edit_PortBaudRate.getText());
        properties.put("PortName",Edit_PortName.getText());
        properties.put("PortDataBits",Edit_PortDataBits.getText());
        properties.put("PortStopBits",Edit_PortStopBits.getText());
    }

    @Override
    public String getName() {
        return "STM32ViewHandle";
    }
    public static SerialPortDataListener getDefaultSerialPortDataListener() {
        return new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_RECEIVED+SerialPort.LISTENING_EVENT_DATA_WRITTEN+SerialPort.LISTENING_EVENT_PORT_DISCONNECTED+SerialPort.LISTENING_EVENT_TIMED_OUT+SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                switch (event.getEventType()){
                    case SerialPort.LISTENING_EVENT_DATA_RECEIVED:
                        byte[] buffer = event.getReceivedData();
                        String data = new String(buffer);
                        Log.i("DATA :"+data);
                        return;
                    case SerialPort.LISTENING_EVENT_DATA_WRITTEN:
                        Log.i("DATA WRITTEN");
                        return;
                    case SerialPort.LISTENING_EVENT_PORT_DISCONNECTED:
                        Log.i("PORT DISCONNECTED");
                        return;
                    case SerialPort.LISTENING_EVENT_TIMED_OUT:
                        Log.e("SERIAL PORT TIMED OUT");
                        return;
                    case SerialPort.LISTENING_EVENT_DATA_AVAILABLE:
                        Log.i("Hava Data Available Read: "+event.getSerialPort().bytesAvailable());
                        return;
                    default:
                        return;
                }
            }
        };
    }
}
