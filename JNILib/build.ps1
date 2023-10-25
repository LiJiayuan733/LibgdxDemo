cd C:\Users\32827\Desktop\AppStart
./JNILib/createH.ps1
#javac -h ./JNILib/include "core/src/com/thzs/app/datacoplite/util/Native/windows/NativeWindowsSupportUtils.java" -d ./JNILib/classes
cmake ./JNILib/ -B .\JNILib\Build -G "Visual Studio 17 2022"
cmake --build .\JNILib\Build --target JNILib
cp ./JNILib/Build/Debug/JNILib.dll ./JNILib/output/