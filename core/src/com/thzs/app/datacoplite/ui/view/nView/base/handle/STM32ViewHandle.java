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
import com.thzs.app.datacoplite.util.stm32utils.Led9341Picture;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class STM32ViewHandle extends BaseViewHandle{
    public SerialPort port;
    public float BodyWidth,BodyHeight;
    public YzGdxQuery ActorGroup,ActorLevel2;
    public YzWindowTopBar Actor_WindowBar;
    public Table Table_Center,Table_Left,Table_Right;
    public Table Table_Center2,Table_Left2,Table_Right2;

    //--------------------------- Level 1 Widget -----------------------------
    public YzButton Actor_Button1,Actor_Button2,Actor_Button3,Actor_Button4,Actor_Button5,Actor_Button6,Actor_Button7;
    public YzEditArea2 Edit_PortName,Edit_PortBaudRate,Edit_PortDataBits,Edit_PortStopBits,Edit_PicturePath;
    public YzImage image;

    //--------------------------- Level 2 Widget -----------------------------
    public YzButton Actor_Button8,Actor_Button9,Actor_Button10,Actor_Button11,Actor_Button12,Actor_Button13,Actor_Button14;    //选择图片，发送图片
    public YzEditArea2 Edit_LogText,Edit_PicturePath2;
    public YzImage image2;
    public STM32ViewHandle(View view) {
        super(view);
        ActorGroup=new YzGdxQuery();
        ActorLevel2=new YzGdxQuery();
    }
    //--------------- SerialPort Method -------------------
    public void ClearSerialPortBuffer(boolean isPrint){
        if(port.isOpen()){
            int len=port.bytesAvailable();
            byte[] buffer=new byte[len];
            port.readBytes(buffer,len);
            if(isPrint) {
                Log.i(new String(buffer, 0, len));
            }
        }
    }
    public static byte[] toBytes(int number){
        byte[] bytes = new byte[4];
        bytes[0] = (byte)number;
        bytes[1] = (byte) (number >> 8);
        bytes[2] = (byte) (number >> 16);
        bytes[3] = (byte) (number >> 24);
        return bytes;
    }

    @Override
    public void ViewInit() {
        instance.stage= Game.stage();

        ActorGroup.add(Actor_WindowBar=new YzWindowTopBar(instance));
        Actor_WindowBar.setTitle("STM32 功能测试");

        //可用内容空间
        BodyWidth=Game.STAGE_WIDTH;
        BodyHeight=Game.STAGE_HEIGHT-Actor_WindowBar.getHeight();

        //Table设置
        Table_Center=new Table();Table_Left=new Table();Table_Right=new Table();Table_Center2=new Table();Table_Left2=new Table();Table_Right2=new Table();
        Table_Center.setBounds(0,BodyHeight/2,BodyWidth,BodyHeight/2);Table_Center2.setBounds(0,0,BodyWidth,BodyHeight/2);
        Table_Center.pad(8,8,8,8);Table_Center2.pad(8,8,8,8);
        Table_Left.setTransform(true);Table_Left2.setTransform(true);
        Table_Right.setTransform(true);Table_Right2.setTransform(true);
        Table_Center.top();

        //初始化控件
        ActorGroup.add(Actor_Button1=new YzButton("检索串口"),Actor_Button2=new YzButton("打开串口"),Actor_Button3=new YzButton("读取数据"),Actor_Button4=new YzButton("关闭串口"),Actor_Button5=new YzButton("1608选择图片"),Actor_Button6=new YzButton("1608发送图片"),Actor_Button7=new YzButton("1608初始化"));
        ActorGroup.add(Edit_PortName=new YzEditArea2("串口名称"),Edit_PortBaudRate=new YzEditArea2("波特率"),Edit_PortDataBits=new YzEditArea2("数据位"),Edit_PortStopBits=new YzEditArea2("停止位"),Edit_PicturePath=new YzEditArea2("图片路径"));
        ActorGroup.add(image=new YzImage(128*2,64*2));
        ActorGroup.find(YzEditArea2.class).width(BodyWidth/2f);

        ActorLevel2.add(Actor_Button13=new YzButton("ILI初始化屏幕"),Actor_Button8=new YzButton("ILI选择图片"),Actor_Button9=new YzButton("ILI发送图片"),Actor_Button10=new YzButton("ILI写入图片"),Actor_Button11=new YzButton("ILI读取图片"),Actor_Button12=new YzButton("SD初始化"),Actor_Button13=new YzButton("发送Log"));
        ActorLevel2.add(Edit_PicturePath2=new YzEditArea2("图片路径"),Edit_LogText=new YzEditArea2("Log信息"));
        ActorLevel2.add(image2=new YzImage(240,320));
        ActorLevel2.find(YzEditArea2.class).width(BodyWidth/2f);

        //控件设置与添加
        ActorGroup.find(YzButton.class).to(Table_Left).each(gdxQuery -> gdxQuery.cell().pad(5));
        ActorGroup.find(YzEditArea2.class).to(Table_Right).each(gdxQuery -> gdxQuery.cell().pad(5));
        ActorGroup.find(YzImage.class).width(128*2).height(64*2).to(Table_Right);

        ActorLevel2.find(YzButton.class).to(Table_Left2).each(gdxQuery -> gdxQuery.cell().pad(5));
        ActorLevel2.find(YzEditArea2.class).to(Table_Right2).each(gdxQuery -> gdxQuery.cell().pad(5));
        ActorLevel2.find(YzImage.class).width(240).height(329).to(Table_Right2);

        //布局组合
        Table_Center.add(Table_Left);
        Table_Center.add(Table_Right);
        Table_Center2.add(Table_Left2);
        Table_Center2.add(Table_Right2);
        instance.stage.addActor(Table_Center);
        instance.stage.addActor(Table_Center2);
        instance.stage.addActor(Actor_WindowBar);

//        instance.stage.setDebugAll(true);

        ActorGroup.add(ActorLevel2);
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
                try {
                    Led1306Picture led1306Picture = new Led1306Picture(Gdx.files.absolute(Edit_PicturePath.getText()));
                    byte[] c = led1306Picture.CreateData();
                    ClearSerialPortBuffer(true);
                    byte[] cmd={0x07,0x00,0x00,0x00};
                    port.writeBytes(cmd,4);
                    //STM32F103VET6只支持最大64位发送，不然会发生未知错误
//                    for (int i = 0; i < c.length / 64; i++) {
//                        port.writeBytes(c, 64, 64 * i);
//                    }
                    Thread.sleep(1);
                    port.writeBytes(c,1024);
                    Log.i("发送完成");
                }catch (Exception e){
                    e.printStackTrace();
                }
                //port.writeBytes(led1306Picture.CreateData(),1024);
            }
        });
        Actor_Button7.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                try {
                    byte[] cmd={0x08,0x00,0x00,0x00};
                    port.writeBytes(cmd,4);
                }catch (Exception e){
                    e.printStackTrace();;
                }
            }
        });
        Actor_Button8.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String path=MutApiSupport.SystemFileChoose();
                image2.setImage(new Texture(Gdx.files.absolute(path)));
                Edit_PicturePath2.setText(path);
                super.clicked(event, x, y);
            }
        });
        Actor_Button9.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                try {
                    int index=0;
                    Led9341Picture led9341Picture = new Led9341Picture(Gdx.files.absolute(Edit_PicturePath2.getText()));
                    led9341Picture.PictureConvert();
                    ClearSerialPortBuffer(true);
                    for(;index<((240/20)*(320/20));index++){

                        byte[] cmd={0x09,0x00,0x00,0x00};
                        byte[] reply={0x00,0x00};
                        port.writeBytes(cmd,4);

                        led9341Picture.PictureSend(port,index);


                        while (port.bytesAvailable()!=2){
                            Thread.sleep(10);
                        }
                        int l=port.readBytes(reply,2);
                        if(l==2&&(new String(reply,0,2).equals("OK"))){
                            Log.i("Reply True");
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        Actor_Button10.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Led9341Picture led9341Picture = new Led9341Picture(Gdx.files.absolute(Edit_PicturePath2.getText()));
                led9341Picture.PictureConvert();
                ClearSerialPortBuffer(true);
                int address=0x00026000;
                byte[] cmd={0x07,0x00,0x00,0x00};
                byte[] reply=new byte[128];

                //set picture show address
                byte[] cmd2={0x09,0x00,0x00,0x00};
                byte[] cmd2_data=toBytes(address);
                port.writeBytes(cmd2,4);
                port.writeBytes(cmd2_data,4);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                for(int i=address;i<300+address;i++){
                    byte[] addressByte=toBytes(i);
                    port.writeBytes(cmd,4);//CMD
                    port.writeBytes(addressByte,4);//Address
                    led9341Picture.PictureWrite(port,i-address);//Data

                    while (!(port.bytesAvailable()>=2)){           //Reply Check
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    int l;
                    if(port.bytesAvailable()==2){
                        l=port.readBytes(reply,2);
                    }else{
                        l=port.readBytes(reply,port.bytesAvailable());
                    }
                    if(l==2&&(new String(reply,0,2).equals("OK"))){
                        Log.i("Reply True"+(i-address));
                    }else{
                        Log.i("Reply False"+new String(reply,0,l));
                        return;
                    }
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        Actor_Button11.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                byte[] cmd={0x08,0x00,0x00,0x00};
                port.writeBytes(cmd,4);
                int address=0x00026000;
                byte[] addressByte=toBytes(address);
                port.writeBytes(addressByte,4);
            }
        });
        Actor_Button12.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                byte[] cmd={0x06,0x00,0x00,0x00};
                port.writeBytes(cmd,4);
            }
        });
        Actor_Button13.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                try {
                    byte[] cmd={0x06,0x00,0x00,0x00};
                    port.writeBytes(cmd,4);
                    String text=Edit_LogText.getText();
                    byte[] textByte=text.getBytes(StandardCharsets.UTF_8);
                    int textLen=textByte.length;
                    port.writeBytes(toBytes(textLen),2);
                    port.writeBytes(textByte,textLen);
                }catch (Exception e){
                    e.printStackTrace();
                }
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
