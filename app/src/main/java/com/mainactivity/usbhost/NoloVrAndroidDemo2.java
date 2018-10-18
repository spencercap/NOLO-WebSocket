package com.mainactivity.usbhost;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.caliber.Nolo_ControllerStates;
import com.caliber.Nolo_Pose;
import com.caliber.Nolo_Vector3;
import com.watchdata.usbhostconn.UsbCustomTransfer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xu.huang.zf on 2017/5/16.
 */

public class NoloVrAndroidDemo2 extends Activity implements View.OnClickListener {

    private TextView tv_api;
    private Button bt_init;
    private TextView tv_init;
    private Button bt_conn;
    private TextView tv_conn;
    private Button bt_setDisconnectedCallback;
    private TextView tv_setDisconnectedCallback;
    private Button bt_startShowData;
    private HorizontalScrollView sl_frame;
    private Button bt_leftSendData;
    private Button bt_rightSendData;
    private Button bt_finish;
    private TextView tv_finish;
    private UsbCustomTransfer usbCustomTransfer;
    private UsbCustomTransfer.DisconnectedCallback mDisconnectedCallback;
    private final int NoloDeviceConnSuccess = 1;
    private final int NoloDeviceDisconnceted = 2;
    private Timer mtime;
    //    private TimerTask timerTask;
//    private FrameLayout fl_frame;
    private TextView tv_H_P_X;
    private TextView tv_H_P_Y;
    private TextView tv_H_P_Z;
    private TextView tv_H_R_X;
    private TextView tv_H_R_Y;
    private TextView tv_H_R_Z;
    private TextView tv_H_R_W;
    private TextView tv_L_P_X;
    private TextView tv_L_P_Y;
    private TextView tv_L_P_Z;
    private TextView tv_L_R_X;
    private TextView tv_L_R_Y;
    private TextView tv_L_R_Z;
    private TextView tv_L_R_W;
    private TextView tv_L_T_X;
    private TextView tv_L_T_Y;
    private TextView tv_R_P_X;
    private TextView tv_R_P_Y;
    private TextView tv_R_P_Z;
    private TextView tv_R_R_X;
    private TextView tv_R_R_Y;
    private TextView tv_R_R_Z;
    private TextView tv_R_R_W;
    private TextView tv_R_T_X;
    private TextView tv_R_T_Y;
    private TextView tv_L_E_X;
    private TextView tv_R_E_X;
    private Boolean CONNECTED_FLAG = false;
    private Boolean START_SHOW_DATA_FLAG = false;
    private Boolean SYNC_SHOW_DATA_FLAG = false;
    //    private LinearLayout fl_frame;
    private Button bt_getVersion;
    private TextView tv_getVersion;
    private Button bt_getHmdInitPosition;
    private TextView tv_getHmdInitPosition;
    private Button bt_getHmdCalibration;
    private TextView tv_getHmdCalibration;
    private TextView tv_L_T_Bt;
    private TextView tv_L_T_T;
    private TextView tv_R_T_Bt;
    private TextView tv_R_T_T;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.nolovrandroiddemo2);
        initView();
        usbCustomTransfer = UsbCustomTransfer.getInstance(this);
        mtime = new Timer();

    }

    private void initView() {
        tv_api = (TextView) findViewById(R.id.tv_api);
        bt_init = (Button) findViewById(R.id.bt_init);
        tv_init = (TextView) findViewById(R.id.tv_init);
        bt_conn = (Button) findViewById(R.id.bt_conn);
        tv_conn = (TextView) findViewById(R.id.tv_conn);
        bt_setDisconnectedCallback = (Button) findViewById(R.id.bt_setDisconnectedCallback);
        tv_setDisconnectedCallback = (TextView) findViewById(R.id.tv_setDisconnectedCallback);
        bt_startShowData = (Button) findViewById(R.id.bt_startShowData);
        sl_frame = (HorizontalScrollView) findViewById(R.id.sl_frame);
        bt_leftSendData = (Button) findViewById(R.id.bt_leftSendData);
        bt_rightSendData = (Button) findViewById(R.id.bt_rightSendData);
        bt_finish = (Button) findViewById(R.id.bt_finish);
        tv_finish = (TextView) findViewById(R.id.tv_finish);

        bt_init.setOnClickListener(this);
        bt_conn.setOnClickListener(this);
        bt_setDisconnectedCallback.setOnClickListener(this);
        bt_startShowData.setOnClickListener(this);
        bt_leftSendData.setOnClickListener(this);
        bt_rightSendData.setOnClickListener(this);
        bt_finish.setOnClickListener(this);
//        fl_frame = (FrameLayout) findViewById(R.id.fl_frame);
//        fl_frame.setOnClickListener(this);
        mDisconnectedCallback = new UsbCustomTransfer.DisconnectedCallback() {
            @Override
            public void setUsbDeviceConnState(int mstate) {

                if (mstate == NoloDeviceConnSuccess) {
                    // 设备正常连接
                    tv_setDisconnectedCallback.setText("Inserted");

                } else if (mstate == NoloDeviceDisconnceted) {
                    // 设备断开
                    tv_setDisconnectedCallback.setText("Disconnected");
                    CONNECTED_FLAG = false;

                }
            }
        };
/*        fl_frame = (LinearLayout) findViewById(R.id.fl_frame);
        fl_frame.setOnClickListener(this);*/
        bt_getVersion = (Button) findViewById(R.id.bt_getVersion);
        bt_getVersion.setOnClickListener(this);
        tv_getVersion = (TextView) findViewById(R.id.tv_getVersion);
        tv_getVersion.setOnClickListener(this);
        bt_getHmdInitPosition = (Button) findViewById(R.id.bt_getHmdInitPosition);
        bt_getHmdInitPosition.setOnClickListener(this);
        tv_getHmdInitPosition = (TextView) findViewById(R.id.tv_getHmdInitPosition);
/*        bt_getHmdCalibration = (Button) findViewById(R.id.bt_getHmdCalibration);
        tv_getHmdCalibration = (TextView) findViewById(R.id.tv_getHmdCalibration);*/

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        usbCustomTransfer.usb_finish();
    }

    TimerTask timerTask = new TimerTask() {
        public void run() {
            //每次需要执行的代码放到这里面。
//            Log.i("NoloVrAndroidDemo2", "TimerTask");
            final Nolo_Pose mnolo_pose_Hmd = usbCustomTransfer.getPoseByDeviceType(0);
            final Nolo_Pose mnolo_pose_Left = usbCustomTransfer.getPoseByDeviceType(1);
            final Nolo_Pose mnolo_pose_Right = usbCustomTransfer.getPoseByDeviceType(2);
            final int left_EN = usbCustomTransfer.getElectricityByDeviceType(1);
            final int right_EN = usbCustomTransfer.getElectricityByDeviceType(2);
            final Nolo_ControllerStates mnolo_controllerStates_Left = usbCustomTransfer.getControllerStatesByDeviceType(1);
            final Nolo_ControllerStates mnolo_controllerStates_Right = usbCustomTransfer.getControllerStatesByDeviceType(2);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mnolo_pose_Hmd != null) {
                        String posx = String.valueOf(mnolo_pose_Hmd.getPos().getX());
                        tv_H_P_X.setText(posx);
                        String posy = String.valueOf(mnolo_pose_Hmd.getPos().getY());
                        tv_H_P_Y.setText(posy);
                        String posz = String.valueOf(mnolo_pose_Hmd.getPos().getZ());
                        tv_H_P_Z.setText(posz);
                        String quaternionx = String.valueOf(mnolo_pose_Hmd.getNolo_Quaternion().getX());
                        tv_H_R_X.setText(quaternionx);
                        String quaterniony = String.valueOf(mnolo_pose_Hmd.getNolo_Quaternion().getY());
                        tv_H_R_Y.setText(quaterniony);
                        String quaternionz = String.valueOf(mnolo_pose_Hmd.getNolo_Quaternion().getZ());
                        tv_H_R_Z.setText(quaternionz);
                        String quaternionw = String.valueOf(mnolo_pose_Hmd.getNolo_Quaternion().getW());
                        tv_H_R_W.setText(quaternionw);
                    }
                    if (mnolo_pose_Left != null) {
                        String posx = String.valueOf(mnolo_pose_Left.getPos().getX());
                        tv_L_P_X.setText(posx);
                        String posy = String.valueOf(mnolo_pose_Left.getPos().getY());
                        tv_L_P_Y.setText(posy);
                        String posz = String.valueOf(mnolo_pose_Left.getPos().getZ());
                        tv_L_P_Z.setText(posz);
                        String quaternionx = String.valueOf(mnolo_pose_Left.getNolo_Quaternion().getX());
                        tv_L_R_X.setText(quaternionx);
                        String quaterniony = String.valueOf(mnolo_pose_Left.getNolo_Quaternion().getY());
                        tv_L_R_Y.setText(quaterniony);
                        String quaternionz = String.valueOf(mnolo_pose_Left.getNolo_Quaternion().getZ());
                        tv_L_R_Z.setText(quaternionz);
                        String quaternionw = String.valueOf(mnolo_pose_Left.getNolo_Quaternion().getW());
                        tv_L_R_W.setText(quaternionw);
                    }
                    if (mnolo_pose_Right != null) {
                        String posx = String.valueOf(mnolo_pose_Right.getPos().getX());
                        tv_R_P_X.setText(posx);
                        String posy = String.valueOf(mnolo_pose_Right.getPos().getY());
                        tv_R_P_Y.setText(posy);
                        String posz = String.valueOf(mnolo_pose_Right.getPos().getZ());
                        tv_R_P_Z.setText(posz);
                        String quaternionx = String.valueOf(mnolo_pose_Right.getNolo_Quaternion().getX());
                        tv_R_R_X.setText(quaternionx);
                        String quaterniony = String.valueOf(mnolo_pose_Right.getNolo_Quaternion().getY());
                        tv_R_R_Y.setText(quaterniony);
                        String quaternionz = String.valueOf(mnolo_pose_Right.getNolo_Quaternion().getZ());
                        tv_R_R_Z.setText(quaternionz);
                        String quaternionw = String.valueOf(mnolo_pose_Right.getNolo_Quaternion().getW());
                        tv_R_R_W.setText(quaternionw);
                    }
                    if (mnolo_controllerStates_Left != null) {
                        String buttons = String.valueOf(mnolo_controllerStates_Left.getButtons());
                    //    tv_L_T_Bt.setText(mnolo_controllerStates_Left.getButtons());
                        tv_L_T_Bt.setText(buttons);
                        String touches = String.valueOf(mnolo_controllerStates_Left.getTouches());
                    //    tv_L_T_T.setText(mnolo_controllerStates_Left.getTouches());
                        tv_L_T_T.setText(touches);
                        String touchpadaxisX = String.valueOf(mnolo_controllerStates_Left.getTouchpadAxis().getX());
                        tv_L_T_X.setText(touchpadaxisX);
                        String touchpadaxisY = String.valueOf(mnolo_controllerStates_Left.getTouchpadAxis().getY());
                        tv_L_T_Y.setText(touchpadaxisY);
                    }
                    if (mnolo_controllerStates_Right != null) {
                        String buttons = String.valueOf(mnolo_controllerStates_Right.getButtons());
                    //    tv_R_T_Bt.setText(mnolo_controllerStates_Right.getButtons());
                        tv_R_T_Bt.setText(buttons);
                        String touches = String.valueOf(mnolo_controllerStates_Right.getTouches());
                    //    tv_R_T_T.setText(mnolo_controllerStates_Right.getTouches());
                        tv_R_T_T.setText(touches);
                        String touchpadaxisX = String.valueOf(mnolo_controllerStates_Right.getTouchpadAxis().getX());
                        tv_R_T_X.setText(touchpadaxisX);
                        String touchpadaxisY = String.valueOf(mnolo_controllerStates_Right.getTouchpadAxis().getY());
                        tv_R_T_Y.setText(touchpadaxisY);
                    }
                    // 电量
                    tv_L_E_X.setText(String.valueOf(left_EN));
                    tv_R_E_X.setText(String.valueOf(right_EN));

                }
            });


        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_init:
                usbCustomTransfer.usb_init();
                break;
            case R.id.bt_conn:
                int res = usbCustomTransfer.usb_conn();
                if (res == 1) {
                    tv_conn.setText("Connect Success");
                    CONNECTED_FLAG = true;
                } else if (res == 0) {
                    tv_conn.setText("Connect Failed");
                    CONNECTED_FLAG = false;
                }
                break;
            case R.id.bt_setDisconnectedCallback:
                usbCustomTransfer.setDisconnectedCallback(mDisconnectedCallback);
                tv_setDisconnectedCallback.setText("Ok");
                break;
            case R.id.bt_startShowData:
                if(CONNECTED_FLAG) {
                    bt_startShowData.setVisibility(View.GONE);
                    View table = View.inflate(this, R.layout.demo2_fragment, null);
                    tv_H_P_X = (TextView) table.findViewById(R.id.tv_H_P_X);
                    tv_H_P_Y = (TextView) table.findViewById(R.id.tv_H_P_Y);
                    tv_H_P_Z = (TextView) table.findViewById(R.id.tv_H_P_Z);
                    tv_H_R_X = (TextView) table.findViewById(R.id.tv_H_R_X);
                    tv_H_R_Y = (TextView) table.findViewById(R.id.tv_H_R_Y);
                    tv_H_R_Z = (TextView) table.findViewById(R.id.tv_H_R_Z);
                    tv_H_R_W = (TextView) table.findViewById(R.id.tv_H_R_W);
                    tv_L_P_X = (TextView) table.findViewById(R.id.tv_L_P_X);
                    tv_L_P_Y = (TextView) table.findViewById(R.id.tv_L_P_Y);
                    tv_L_P_Z = (TextView) table.findViewById(R.id.tv_L_P_Z);
                    tv_L_R_X = (TextView) table.findViewById(R.id.tv_L_R_X);
                    tv_L_R_Y = (TextView) table.findViewById(R.id.tv_L_R_Y);
                    tv_L_R_Z = (TextView) table.findViewById(R.id.tv_L_R_Z);
                    tv_L_R_W = (TextView) table.findViewById(R.id.tv_L_R_W);
                    tv_L_T_X = (TextView) table.findViewById(R.id.tv_L_T_X);
                    tv_L_T_Y = (TextView) table.findViewById(R.id.tv_L_T_Y);
                    tv_R_P_X = (TextView) table.findViewById(R.id.tv_R_P_X);
                    tv_R_P_Y = (TextView) table.findViewById(R.id.tv_R_P_Y);
                    tv_R_P_Z = (TextView) table.findViewById(R.id.tv_R_P_Z);
                    tv_R_R_X = (TextView) table.findViewById(R.id.tv_R_R_X);
                    tv_R_R_Y = (TextView) table.findViewById(R.id.tv_R_R_Y);
                    tv_R_R_Z = (TextView) table.findViewById(R.id.tv_R_R_Z);
                    tv_R_R_W = (TextView) table.findViewById(R.id.tv_R_R_W);
                    tv_R_T_X = (TextView) table.findViewById(R.id.tv_R_T_X);
                    tv_R_T_Y = (TextView) table.findViewById(R.id.tv_R_T_Y);
                    tv_L_E_X = (TextView) table.findViewById(R.id.tv_L_E_X);
                    tv_R_E_X = (TextView) table.findViewById(R.id.tv_R_E_X);
                    tv_L_T_Bt = (TextView) table.findViewById(R.id.tv_L_T_Bt);
                    tv_L_T_T = (TextView) table.findViewById(R.id.tv_L_T_T);
                    tv_R_T_Bt = (TextView) table.findViewById(R.id.tv_R_T_Bt);
                    tv_R_T_T = (TextView) table.findViewById(R.id.tv_R_T_T);
                    sl_frame.addView(table);
                    sl_frame.setVisibility(View.VISIBLE);
                    mtime.schedule(timerTask, 2, 30); // hx-- 开启一个新的线程
                }
                break;
            case R.id.bt_leftSendData:
                byte[] leftEN = {(byte) 0xAA, (byte) 0x66, (byte) 0x64, (byte) 0x00};
                usbCustomTransfer.usb_sendData(leftEN);
                break;
            case R.id.bt_rightSendData:
                byte[] rightEN = {(byte) 0xAA, (byte) 0x66, (byte) 0x00, (byte) 0x64};
                usbCustomTransfer.usb_sendData(rightEN);
                break;
            case R.id.bt_finish:
                usbCustomTransfer.usb_finish();
                break;
            case R.id.bt_getVersion:
                int version = usbCustomTransfer.getVersionByDeviceType(1);
                tv_getVersion.setText("LeftCON:" + String.valueOf(version));
                break;
            case R.id.bt_getHmdInitPosition:
                Nolo_Vector3 hmdInitPosition = usbCustomTransfer.getHmdInitPosition();
                if(hmdInitPosition != null) {
                    String hmdI_X = String.valueOf(hmdInitPosition.getX());
                    String hmdI_Y = String.valueOf(hmdInitPosition.getY());
                    String hmdI_Z = String.valueOf(hmdInitPosition.getZ());
                    tv_getHmdInitPosition.setText("HmdInitP"+ " " + "X:"+ hmdI_X + " "+"Y:" + hmdI_Y + " " + "Z:"+ hmdI_Z);
                }
                break;
/*            case R.id.bt_getHmdCalibration:
                int hmdC = usbCustomTransfer.getHmdCalibration();
                tv_getHmdCalibration.setText(String.valueOf(hmdC));
                break;*/
        }
    }

}
