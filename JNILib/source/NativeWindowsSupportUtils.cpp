//
// Created by 32827 on 2023/7/20.
//
#include"com_thzs_app_datacoplite_util_Native_windows_NativeWindowsSupportUtils.h"
#include <jni.h>
#include<Windows.h>
#include <d2d1.h>
#include<Shobjidl.h>
#include <atlbase.h>
#include <atlwin.h>
#include <tchar.h>
#include <GLFW/glfw3.h>
#define GLFW_EXPOSE_NATIVE_WIN32
#include <GLFW/glfw3native.h>
#pragma comment(lib, "d2d1.lib")
#pragma comment(lib,"glfw3.lib")
#include<iostream>
#include <codecvt>
#include<atlimage.h>
std::string wstring2utf8string(const std::wstring& str)
{
    static std::wstring_convert<std::codecvt_utf8<wchar_t> > strCnv;
    return strCnv.to_bytes(str);
}

std::string WCharToMByte(LPCWSTR lpcwszStr)
{
	std::string str;
	DWORD dwMinSize = 0;
	LPSTR lpszStr = NULL;
	dwMinSize = WideCharToMultiByte(CP_OEMCP, NULL, lpcwszStr, -1, NULL, 0, NULL, FALSE);
	if (0 == dwMinSize)
	{
		return FALSE;
	}
	lpszStr = new char[dwMinSize];
	WideCharToMultiByte(CP_OEMCP, NULL, lpcwszStr, -1, lpszStr, dwMinSize, NULL, FALSE);
	str = lpszStr;
	delete[] lpszStr;
	return str;

}
char* ConvertLPWSTRToLPSTR(LPWSTR lpwszStrIn)
{
	LPSTR pszOut = NULL;
		if (lpwszStrIn != NULL)
		{
			int nInputStrLen = wcslen(lpwszStrIn);
			// Double NULL Termination  
			int nOutputStrLen = WideCharToMultiByte(CP_ACP, 0, lpwszStrIn, nInputStrLen, NULL, 0, 0, 0) + 2;
			pszOut = new char[nOutputStrLen];
 
			if (pszOut)
			{
				memset(pszOut, 0x00, nOutputStrLen);
				WideCharToMultiByte(CP_ACP, 0, lpwszStrIn, nInputStrLen, pszOut, nOutputStrLen, 0, 0);
			}
		}
 
	return pszOut;
}
JNIEXPORT jstring JNICALL Java_com_thzs_app_datacoplite_util_Native_windows_NativeWindowsSupportUtils_BoxWindow
        (JNIEnv *env, jobject jobject1, jstring jstring1){
    return jstring1;
}
/*JNIEXPORT void JNICALL Java_com_thzs_app_datacoplite_util_NativeWindowsSupportUtils_LongTest
  (JNIEnv *env, jobject jobject1, jstring mhandle){
    const char *str = (*env).GetStringUTFChars(mhandle,0);
    printf(str);
    printf("\n");
    printf("%lld",atoll(str));
    printf("\n");
    long long glfwWndHandle =atoll(str);
    env->ReleaseStringUTFChars(mhandle,str);
}*/
JNIEXPORT void JNICALL Java_com_thzs_app_datacoplite_util_Native_windows_NativeWindowsSupportUtils_LongTest
  (JNIEnv *env, jclass jclazz1, jlong handle){
    GLFWwindow *p=(GLFWwindow *)handle;
    HWND m_hwnd=glfwGetWin32Window(p);
    HRESULT result = CoInitializeEx(NULL, COINIT_APARTMENTTHREADED | COINIT_DISABLE_OLE1DDE);
    if (SUCCEEDED(result)) {
        CComPtr<IFileOpenDialog> pOpenDialog;
        result = pOpenDialog.CoCreateInstance(__uuidof(FileOpenDialog));
        if (SUCCEEDED(result)) {
            result = pOpenDialog->Show(m_hwnd);
            if (SUCCEEDED(result)) {
                CComPtr<IShellItem> pItem;
                result = pOpenDialog->GetResult(&pItem);
                if (SUCCEEDED(result)) {
                    LPWSTR pszFilePath;
                    result = pItem->GetDisplayName(SIGDN_FILESYSPATH, &pszFilePath);
                    if (SUCCEEDED(result)) {
                        MessageBox(m_hwnd, pszFilePath, L"File Path", MB_OK);
                        //return env->NewStringUTF(ConvertLPWSTRToLPSTR(pszFilePath));
                        CoTaskMemFree(pszFilePath);
                    }
                }
            }
        }
    }
};
JNIEXPORT jstring JNICALL Java_com_thzs_app_datacoplite_util_Native_windows_NativeWindowsSupportUtils_FileChooseWindow
  (JNIEnv *env, jclass jclazz1, jlong mhandle){
    GLFWwindow *p=(GLFWwindow *)mhandle;
    HWND m_hwnd=glfwGetWin32Window(p);
    HRESULT result = CoInitializeEx(NULL, COINIT_APARTMENTTHREADED | COINIT_DISABLE_OLE1DDE);
    if (SUCCEEDED(result)) {
        CComPtr<IFileOpenDialog> pOpenDialog;
        result = pOpenDialog.CoCreateInstance(__uuidof(FileOpenDialog));
        if (SUCCEEDED(result)) {
            result = pOpenDialog->Show(m_hwnd);
            if (SUCCEEDED(result)) {
                CComPtr<IShellItem> pItem;
                result = pOpenDialog->GetResult(&pItem);
                if (SUCCEEDED(result)) {
                    LPWSTR pszFilePath;
                    result = pItem->GetDisplayName(SIGDN_FILESYSPATH, &pszFilePath);
                    if (SUCCEEDED(result)) {
                        MessageBox(m_hwnd, pszFilePath, L"File Path", MB_OK);
                        std::string m=wstring2utf8string(pszFilePath);
                        //int pSize = WideCharToMultiByte(CP_OEMCP, 0, pszFilePath, wcslen(pszFilePath), NULL, 0, NULL, NULL);
                        //char* pCStrKey = new char[pSize+2];
                        //WideCharToMultiByte(CP_OEMCP, 0, pszFilePath, wcslen(pszFilePath), pCStrKey, pSize, NULL, NULL);
                        //pCStrKey[pSize+1] = '\0';
                        CoTaskMemFree(pszFilePath);
                        return env->NewStringUTF(m.c_str());
                    }
                }
            }
        }
    }
    return env->NewStringUTF("failure");
}
JNIEXPORT jstring JNICALL Java_com_thzs_app_datacoplite_util_Native_windows_NativeWindowsSupportUtils_DirectoryChooseWindow
        (JNIEnv *env, jclass jclazz1, jlong mhandle){
    GLFWwindow *p=(GLFWwindow *)mhandle;
    HWND m_hwnd=glfwGetWin32Window(p);
    HRESULT result = CoInitializeEx(NULL, COINIT_APARTMENTTHREADED | COINIT_DISABLE_OLE1DDE);
    if (SUCCEEDED(result)) {
        CComPtr<IFileDialog> pFileDialog;
        result = pFileDialog.CoCreateInstance(__uuidof(FileOpenDialog));
        if (SUCCEEDED(result)) {
            DWORD dwOptions;
            result = pFileDialog->GetOptions(&dwOptions);
            if (SUCCEEDED(result)) {
                result = pFileDialog->SetOptions(dwOptions | FOS_PICKFOLDERS);
                if (SUCCEEDED(result)) {
                    result = pFileDialog->Show(m_hwnd);
                    if (SUCCEEDED(result)) {
                        CComPtr<IShellItem> pItem;
                        result = pFileDialog->GetFolder(&pItem);
                        if (SUCCEEDED(result)) {
                            LPWSTR pszFolderPath;
                            result = pItem->GetDisplayName(SIGDN_FILESYSPATH, &pszFolderPath);
                            if (SUCCEEDED(result)) {
                                std::string m=wstring2utf8string(pszFolderPath);
                                CoTaskMemFree(pszFolderPath);
                                return env->NewStringUTF(m.c_str());
                            }
                        }
                    }
                }
            }
        }
    }
    return env->NewStringUTF("failure");
}
JNIEXPORT void JNICALL Java_com_thzs_app_datacoplite_util_Native_windows_NativeWindowsSupportUtils_SetCursorPos
        (JNIEnv *env, jclass jclazz, jint x, jint y){
    int x1=x;
    int y1=y;
    SetCursorPos(x1,y1);
};
JNIEXPORT jint JNICALL Java_com_thzs_app_datacoplite_util_Native_windows_NativeWindowsSupportUtils_GetCursorPos
        (JNIEnv *env, jclass jclazz){
    POINT pt;
    GetCursorPos(&pt);
    long x=pt.x;
    long y=pt.y;
    jint re=(x<<16)+y;
    return re;
};
JNIEXPORT void JNICALL Java_com_thzs_app_datacoplite_util_Native_windows_NativeWindowsSupportUtils_Click
        (JNIEnv *env, jclass jclazz, jint x, jint y){
    int x1=x;
    int y1=y;
    SetCursorPos(x1,y1);
    mouse_event(MOUSEEVENTF_LEFTDOWN, 0, 0, 0, 0); //按下左键
    Sleep(10);
    mouse_event(MOUSEEVENTF_LEFTUP, 0, 0, 0, 0); //松开左键
}
void char_to_wchar(const char* ch, std::wstring& w_str)
{
    wchar_t* wchar;
    int len = MultiByteToWideChar(CP_ACP, 0, ch, strlen(ch), NULL, 0);
    wchar = new wchar_t[len + 1];
    MultiByteToWideChar(CP_ACP, 0, ch, strlen(ch), wchar, len);
    wchar[len] = '\0';
    w_str = wchar;
    delete[]wchar;
}
JNIEXPORT void JNICALL Java_com_thzs_app_datacoplite_util_Native_windows_NativeWindowsSupportUtils_SaveCapture
        (JNIEnv *env, jclass jclazz, jlong mhandle, jstring path){
    const char *strl = (*env).GetStringUTFChars(path,0);
    GLFWwindow *p=(GLFWwindow *)mhandle;
    HWND m_hwnd=glfwGetWin32Window(p);
    BITMAPFILEHEADER bfHeader;
    BITMAPINFOHEADER biHeader;
    BITMAPINFO bInfo;
    HGDIOBJ hTempBitmap;
    HBITMAP hBitmap;
    BITMAP bAllDesktops;
    HDC hDC, hMemDC;
    LONG lWidth, lHeight;
    BYTE *bBits = NULL;
    HANDLE hHeap = GetProcessHeap();
    DWORD cbBits, dwWritten = 0;
    HANDLE hFile;
    INT x = GetSystemMetrics(SM_XVIRTUALSCREEN);
    INT y = GetSystemMetrics(SM_YVIRTUALSCREEN);

    ZeroMemory(&bfHeader, sizeof(BITMAPFILEHEADER));
    ZeroMemory(&biHeader, sizeof(BITMAPINFOHEADER));
    ZeroMemory(&bInfo, sizeof(BITMAPINFO));
    ZeroMemory(&bAllDesktops, sizeof(BITMAP));

    hDC = GetDC(m_hwnd);
    hTempBitmap = GetCurrentObject(hDC, OBJ_BITMAP);
    GetObjectW(hTempBitmap, sizeof(BITMAP), &bAllDesktops);

    lWidth = bAllDesktops.bmWidth;
    lHeight = bAllDesktops.bmHeight;

    DeleteObject(hTempBitmap);

    bfHeader.bfType = (WORD)('B' | ('M' << 8));
    bfHeader.bfOffBits = sizeof(BITMAPFILEHEADER) + sizeof(BITMAPINFOHEADER);
    biHeader.biSize = sizeof(BITMAPINFOHEADER);
    biHeader.biBitCount = 24;
    biHeader.biCompression = BI_RGB;
    biHeader.biPlanes = 1;
    biHeader.biWidth = lWidth;
    biHeader.biHeight = lHeight;

    bInfo.bmiHeader = biHeader;

    cbBits = (((24 * lWidth + 31)&~31) / 8) * lHeight;

    hMemDC = CreateCompatibleDC(hDC);
    hBitmap = CreateDIBSection(hDC, &bInfo, DIB_RGB_COLORS, (VOID **)&bBits, NULL, 0);
    SelectObject(hMemDC, hBitmap);
    BitBlt(hMemDC, 0, 0, lWidth, lHeight, hDC, x, y, SRCCOPY);

    CImage image;
    int bitOfPix = GetDeviceCaps(hDC, BITSPIXEL);
    int width = GetDeviceCaps(hDC, HORZRES);  //获取DC宽度
    int height = GetDeviceCaps(hDC, VERTRES);  //获取DC高度
    UINT dpi = GetDpiForWindow(m_hwnd);
    float fold; //根据dpi计算放大倍数;
    switch (dpi) {
        case 96:
            fold = 1;
            break;
        case 120:
            fold = 1.25;
            break;
        case 144:
            fold = 1.5;
            break;
        case 192:
            fold = 2;
            break;
        case 216:
            fold = 2.25;
            break;
        default:
            fold = 1;
            break;
    }
    image.Create(width*fold, height*fold, bitOfPix);
    BitBlt(image.GetDC(), 0, 0, lWidth, lHeight, hDC, x, y, SRCCOPY);

    int num = MultiByteToWideChar(0,0,strl,-1,NULL,0);
    wchar_t *wide = new wchar_t[num];
    MultiByteToWideChar(0,0,strl,-1,wide,num);
    LPCTSTR str= wide;

    image.Save(str,Gdiplus::ImageFormatPNG);
    image.ReleaseDC();

    DeleteDC(hMemDC);
    ReleaseDC(m_hwnd, hDC);
    DeleteObject(hBitmap);
}
JNIEXPORT jboolean JNICALL Java_com_thzs_app_datacoplite_util_Native_windows_NativeWindowsSupportUtils_SetWindowToTop
        (JNIEnv *env, jclass jclazz, jlong m_handle){
    GLFWwindow *p=(GLFWwindow *)m_handle;
    HWND m_hwnd=glfwGetWin32Window(p);
    bool f= SetWindowPos(m_hwnd,HWND_TOPMOST,0,0,0,0,SWP_NOMOVE|SWP_NOSIZE);
    jboolean re=f?JNI_TRUE:JNI_FALSE;
    return re;
}
JNIEXPORT jlong JNICALL Java_com_thzs_app_datacoplite_util_Native_windows_NativeWindowsSupportUtils_GetLastError
        (JNIEnv *env, jclass clazz){
    DWORD err=GetLastError();
    return err;
}
JNIEXPORT jlong JNICALL Java_com_thzs_app_datacoplite_util_Native_windows_NativeWindowsSupportUtils_GetHWND
        (JNIEnv *env, jclass clazz, jlong m_handle){
    GLFWwindow *p=(GLFWwindow *)m_handle;
    static HWND m_hwnd=glfwGetWin32Window(p);
    HWND *pcs=&m_hwnd;
    return (__int64)pcs;
}
