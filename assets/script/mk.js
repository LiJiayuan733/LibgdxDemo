Auto.hidWindow();
var path = "C:\\Users\\32827\\Desktop\\AppStart\\assets\\screen.png";
Auto.capture(path);
var result=Auto.Yolo(path)
for(var i=0; i<result.length; i++){
    var il=result[i];
    Auto.log("cls:"+il.cls+" conf:"+il.conf)
    if(il.cls==1){
        Auto.log("x:"+il.center().x+" y:"+il.center().y);
        Auto.click(il.center());
    }
}
Auto.shwWindow();