package com.thzs.app.datacoplite.util.process;

public interface RealtimeProcessInterface {
    void onNewStdoutListener(String newStdout);

    void onNewStderrListener(String newStderr);

    void onProcessFinish(int resultCode);
    //void execCommand(String ...commands);
}
