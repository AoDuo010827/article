package rkandroidapi.ys.com.rkandroidapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ys.rkapi.MyManager;
import com.ys.rkapi.Utils.GPIOUtils;
import com.ys.rkapi.product.PX30;

import java.io.File;

public class GpioActivity1 extends AppCompatActivity implements View.OnClickListener {

    private EditText gpioIndex;
    private TextView inValidText;
    private String gpioValue = "";

    private int index;
    private MyManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = MyManager.getInstance(this);
        if (manager.getAndroidModle().contains("px30_e")) {
            setContentView(R.layout.activity_apiopx30);
            findViewById(R.id.btn4).setOnClickListener(this);
            findViewById(R.id.btn5).setOnClickListener(this);
            findViewById(R.id.btn6).setOnClickListener(this);
            findViewById(R.id.btn7).setOnClickListener(this);
            findViewById(R.id.btn8).setOnClickListener(this);
            findViewById(R.id.btn9).setOnClickListener(this);
            findViewById(R.id.btn10).setOnClickListener(this);
            findViewById(R.id.btn11).setOnClickListener(this);
            findViewById(R.id.btn12).setOnClickListener(this);
            findViewById(R.id.btn16).setOnClickListener(this);
            findViewById(R.id.btn19).setOnClickListener(this);
            findViewById(R.id.btn22).setOnClickListener(this);
            findViewById(R.id.btn17).setOnClickListener(this);
            findViewById(R.id.btn25).setOnClickListener(this);
            findViewById(R.id.btn20).setOnClickListener(this);
            findViewById(R.id.btn21).setOnClickListener(this);
            findViewById(R.id.btn26).setOnClickListener(this);
            findViewById(R.id.btn27).setOnClickListener(this);

        } else {
            setContentView(R.layout.activity_gpio);
            manager.upgradeRootPermissionForExport();
            gpioIndex = findViewById(R.id.gpio_index);
            inValidText = findViewById(R.id.invalid);
            findViewById(R.id.get_io_status).setOnClickListener(this);
            findViewById(R.id.set_input).setOnClickListener(this);
            findViewById(R.id.set_output).setOnClickListener(this);
            findViewById(R.id.get_io_value).setOnClickListener(this);
            findViewById(R.id.set_highvalue).setOnClickListener(this);
            findViewById(R.id.set_lowvalue).setOnClickListener(this);
            findViewById(R.id.checkio).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //标准gpio
            case R.id.checkio:
                String indexText = gpioIndex.getText().toString();
                if (!"".equals(indexText)) {
                    index = Integer.parseInt(indexText);
                    if (manager.exportGpio(index)) {
                        manager.upgradeRootPermissionForGpio(index);
                        String status = manager.getGpioDirection(index);
                        if ("".equals(status))
                            inValidText.setText("无效的GPIO");
                        else
                            inValidText.setText("有效的GPIO");
                    }
                }
                break;
            case R.id.get_io_status:
                Toast.makeText(this, "当前io的类型 = " + manager.getGpioDirection(index), Toast.LENGTH_LONG).show();
                break;
            case R.id.set_input:
                if (manager.setGpioDirection(index, 1))
                    Toast.makeText(this, "成功设置该io为输入口", Toast.LENGTH_LONG).show();
                break;
            case R.id.set_output:
                if (manager.setGpioDirection(index, 0))
                    Toast.makeText(this, "成功设置该io为输出口", Toast.LENGTH_LONG).show();
                break;
            case R.id.get_io_value:
                Toast.makeText(this, "当前io的电平 = " + manager.getGpioValue(index), Toast.LENGTH_SHORT).show();
                break;
            case R.id.set_highvalue:
                if (manager.writeGpioValue(index, "1"))
                    Toast.makeText(this, "成功设置该io高电平", Toast.LENGTH_SHORT).show();
                break;
            case R.id.set_lowvalue:
                if (manager.writeGpioValue(index, "0"))
                    Toast.makeText(this, "成功设置该io低电平", Toast.LENGTH_SHORT).show();
                break;

            //px30gpio
            case R.id.btn4:
                ToastUtils.showToast(this, "USB1的状态是: " + GPIOUtils.readGpioPG(PX30.USB1));
                break;
            case R.id.btn5:
                GPIOUtils.writeStringFileFor7(new File(PX30.USB1), "1");
                break;
            case R.id.btn6:
                GPIOUtils.writeStringFileFor7(new File(PX30.USB1), "0");
                break;
            case R.id.btn7:
                ToastUtils.showToast(this, "USB2的状态是: " + GPIOUtils.readGpioPG(PX30.USB2));
                break;
            case R.id.btn8:
                GPIOUtils.writeStringFileFor7(new File(PX30.USB2), "1");
                break;
            case R.id.btn9:
                GPIOUtils.writeStringFileFor7(new File(PX30.USB2), "0");
                break;
            case R.id.btn10:
                ToastUtils.showToast(this, "USB3的状态是: " + GPIOUtils.readGpioPG(PX30.USB3));
                break;
            case R.id.btn11:
                GPIOUtils.writeStringFileFor7(new File(PX30.USB3), "1");
                break;
            case R.id.btn12:
                GPIOUtils.writeStringFileFor7(new File(PX30.USB3), "0");
                break;
            case R.id.btn16:
                ToastUtils.showToast(this, "IO1的状态是: " + GPIOUtils.readGpioPG(PX30.IO1));
                break;
            case R.id.btn19:
                ToastUtils.showToast(this, "IO2的状态是: " + GPIOUtils.readGpioPG(PX30.IO2));
                break;
            case R.id.btn22:
                ToastUtils.showToast(this, "IO3的状态是: " + GPIOUtils.readGpioPG(PX30.IO3));
                break;
            case R.id.btn17:
                ToastUtils.showToast(this, "IO4的状态是: " + GPIOUtils.readGpioPG(PX30.IO4));
                break;
            case R.id.btn25:
                gpioValue = GPIOUtils.readGpioPGForLong(PX30.DIRECTION);
                gpioValue = gpioValue.substring(6, 7);
                ToastUtils.showToast(this, "IO4的输入输出状态: " + gpioValue);
                break;
            case R.id.btn20:
                GPIOUtils.writeStringFileFor7(new File(PX30.DIRECTION), "4 0");
                break;
            case R.id.btn21:
                GPIOUtils.writeStringFileFor7(new File(PX30.DIRECTION), "4 1");
                break;
            case R.id.btn26:
                if (gpioValue.equals("1"))
                    ToastUtils.showToast(this, "输入口不能拉高");
                else
                    GPIOUtils.writeStringFileFor7(new File(PX30.IO4), "1");
                break;
            case R.id.btn27:
                if (gpioValue.equals("1"))
                    ToastUtils.showToast(this, "输入口不能拉低");
                else
                    GPIOUtils.writeStringFileFor7(new File(PX30.IO4), "0");
                break;
            default:
                break;
        }

    }
}
