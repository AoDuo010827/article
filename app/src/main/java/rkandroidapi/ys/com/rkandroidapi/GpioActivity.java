package rkandroidapi.ys.com.rkandroidapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.ys.rkapi.MyManager;

public class GpioActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner spinner_gpio;
    private TextView inValidText;
    private String gpioValue = "";

    private int index = 1;
    private MyManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = MyManager.getInstance(this);
        setContentView(R.layout.activity_gpio);
        spinner_gpio = findViewById(R.id.spinner_gpio);
        inValidText = findViewById(R.id.invalid);
        findViewById(R.id.get_io_status).setOnClickListener(this);
        findViewById(R.id.set_input).setOnClickListener(this);
        findViewById(R.id.set_output).setOnClickListener(this);
        findViewById(R.id.get_io_value).setOnClickListener(this);
        findViewById(R.id.set_highvalue).setOnClickListener(this);
        findViewById(R.id.set_lowvalue).setOnClickListener(this);
        findViewById(R.id.checkio).setOnClickListener(this);
        spinner_gpio.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //标准gpio
            case R.id.checkio:
                    if (Integer.valueOf(manager.getFirmwareVersion()) < 5) {
                        inValidText.setText("此api版本不支持，请更新版本");
                    } else {
                        String status = manager.getGpioDirection(index);
                        if ("".equals(status))
                            inValidText.setText("无效的GPIO");
                        else
                            inValidText.setText("有效的GPIO");
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
                if(manager.getGpioDirection(index).trim().equals("in"))
                    ToastUtils.showToast(this, "输入口不能拉高");
                else {
                    if (manager.writeGpioValue(index, "1"))
                        Toast.makeText(this, "成功设置该io高电平", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.set_lowvalue:
                if(manager.getGpioDirection(index).trim().equals("in"))
                    ToastUtils.showToast(this, "输入口不能拉低");
                else {
                    if (manager.writeGpioValue(index, "0"))
                        Toast.makeText(this, "成功设置该io低电平", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String[] value = getResources().getStringArray(R.array.spinner_val);
        index = Integer.parseInt(value[i]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
