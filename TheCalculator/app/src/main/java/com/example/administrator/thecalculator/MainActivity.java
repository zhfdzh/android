package com.example.administrator.thecalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button[] btn = new Button[10];  //0到9十个按键
    private EditText input; //输入框
    private TextView mem;  //输入框下方的记忆器，用于记录上一次计算的结果
    private TextView _drg;   //三角计算时的标志：角度还是弧度
    private TextView tip;   //最下方的小提示
    private Button
            div, mul, sub, add, equal,      //  /、 x、 -、 +、 =
            sin, cos, tan, log, ln,         // 函数
            sqrt, square, factorial, bksp, //根号、平方、阶乘，退格
            left, right, dot, exit, drg,     // (、 )、 。、 退出、角度控制键
            mc, c;          //mem清屏， input清屏
    private String str_old;     //保存原来的算式样子，为了输出更好看
    private String str_new;     //变换后的式子
    private boolean vbeigin = true; //输入控制，true为重新输入，false为接着输入
    public boolean drg_flag = false;   //true为角度，false为弧度
    public double PI = 4 * Math.atan(1);  // pi
    public boolean tip_lock = true;       //true表示正确，可以继续输入，false表示输入有误，输入被锁定
    public boolean equal_flag = true;   //  判断是否是按=之后的输入，true表示输入在=之前，false反之

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取界面元素
        input = (EditText) findViewById(R.id.input);
        mem = (TextView) findViewById(R.id.mem);
        tip = (TextView) findViewById(R.id.tip);
        _drg = (TextView) findViewById(R.id._drg);
        btn[0] = (Button) findViewById(R.id.zero);
        btn[1] = (Button) findViewById(R.id.one);
        btn[2] = (Button) findViewById(R.id.two);
        btn[3] = (Button) findViewById(R.id.three);
        btn[4] = (Button) findViewById(R.id.four);
        btn[5] = (Button) findViewById(R.id.five);
        btn[6] = (Button) findViewById(R.id.six);
        btn[7] = (Button) findViewById(R.id.seven);
        btn[8] = (Button) findViewById(R.id.eight);
        btn[9] = (Button) findViewById(R.id.nine);
        div = (Button) findViewById(R.id.divide);
        mul = (Button) findViewById(R.id.mul);
        sub = (Button) findViewById(R.id.sub);
        add = (Button) findViewById(R.id.add);
        equal = (Button) findViewById(R.id.equal);
        sin = (Button) findViewById(R.id.sin);
        cos = (Button) findViewById(R.id.cos);
        tan = (Button) findViewById(R.id.tan);
        log = (Button) findViewById(R.id.log);
        ln = (Button) findViewById(R.id.ln);
        sqrt = (Button) findViewById(R.id.sqrt);
        square = (Button) findViewById(R.id.square);
        factorial = (Button) findViewById(R.id.factorical);
        bksp = (Button) findViewById(R.id.bksp);
        left = (Button) findViewById(R.id.left);
        right = (Button) findViewById(R.id.right);
        dot = (Button) findViewById(R.id.dot);
        exit = (Button) findViewById(R.id.exit);
        drg = (Button) findViewById(R.id.drg);
        mc = (Button) findViewById(R.id.mc);
        c = (Button) findViewById(R.id.c);

        //为数字按键绑定监听器
        for(int i=0; i<10;i++){
            btn[i].setOnClickListener(actionPerformed);
        }
        //为其他按键绑定监听器
        div.setOnClickListener(actionPerformed);
        mul.setOnClickListener(actionPerformed);
        sub.setOnClickListener(actionPerformed);
        add.setOnClickListener(actionPerformed);
        equal.setOnClickListener(actionPerformed);
        sin.setOnClickListener(actionPerformed);
        cos.setOnClickListener(actionPerformed);
        tan.setOnClickListener(actionPerformed);
        log.setOnClickListener(actionPerformed);
        ln .setOnClickListener(actionPerformed);
        sqrt.setOnClickListener(actionPerformed);
        square.setOnClickListener(actionPerformed);
        factorial.setOnClickListener(actionPerformed);
        bksp.setOnClickListener(actionPerformed);
        left.setOnClickListener(actionPerformed);
        right.setOnClickListener(actionPerformed);
        dot.setOnClickListener(actionPerformed);
        exit.setOnClickListener(actionPerformed);
        drg.setOnClickListener(actionPerformed);
        mc.setOnClickListener(actionPerformed);
        c.setOnClickListener(actionPerformed);

    }

    String[] Tipcommand = new String[500];    //命令缓存，用于检测输入合法性
    int tip_i = 0;      //Tipcommand 的指令
    private View.OnClickListener actionPerformed = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String  command = ((Button) v).getText().toString();    //获取命令
            String str = input.getText().toString();        //显示器上的字符串
             if(equal_flag == false && "0123456789.()sincostanloglnn!+-x÷^√".indexOf(command) != -1){       //检测输入是否合法
                 if(right(str)){  //检测显示器上的字符串是否合法
                     if("+-x÷^√".indexOf(command) != -1){
                         for(int i=0;i<str.length();i++){
                             Tipcommand[tip_i] = String.valueOf(str.charAt(i));
                             tip_i++;
                         }
                         vbeigin = false;
                     }
                 }else {
                     vbeigin = true;
                     input.setText(0);
                     tip_i = 0;
                     tip_lock = true;
                     tip.setText("欢迎使用");
                 }
                 equal_flag = true;
             }
            if(tip_i > 0){
                TipChecker(Tipcommand[tip_i-1],command);
            }else if(tip_i == 0){
                TipChecker("#",command);
            }
            if("0123456789.()sincostanloglnn!+-x÷^√".indexOf(command) != -1 && tip_lock){
                Tipcommand[tip_i] = command;
                tip_i++;
            }
            if("0123456789.()sincostanloglnn!+-x÷^√".indexOf(command) != -1 && tip_lock){         //检查输入正确，就显示出来
               print(command);
            }else if(command.compareTo("DRG") == 0 && tip_lock){   //如果点击的是DRG实现角度弧度转化
                if(drg_flag == true){
                    drg_flag = false;
                    _drg.setText("RAD");
                }else {
                    drg_flag = true;
                    _drg.setText("DEG");
                }
            }else if (command.compareTo("BKSP") == 0 && equal_flag){   //在按=之前，按下的是退格键
                if(TTO(str) == 3 ){          //一次删除三个字符
                    if(str.length() > 3 ){
                        input.setText(str.substring(0,str.length()-3));
                    }else if(str.length() == 3){
                        input.setText("0");
                        vbeigin = true;
                        tip_i = 0;
                        tip.setText("欢迎使用");
                    }
                }else if (TTO(str) == 2){           //一次删除两个字符
                    if(str.length() > 2 ){
                        input.setText(str.substring(0,str.length()-3));
                    }else if(str.length() == 2){
                        input.setText("0");
                        vbeigin = true;
                        tip_i = 0;
                        tip.setText("欢迎使用");
                    }
                }else if (TTO(str) == 1){           //一次删除一个字符
                    if(right(str)){         //如果之前输入的字符串合法
                        if(str.length() > 1 ){
                            input.setText(str.substring(0,str.length()-3));
                        }else if(str.length() == 1){
                            input.setText("0");
                            vbeigin = true;
                            tip_i = 0;
                            tip.setText("欢迎使用");
                        }
                    }else {         //如果之前输入的字符串不合法，则会全部删除
                        input.setText("0");
                        vbeigin = true;
                        tip_i = 0;
                        tip.setText("欢迎使用");
                    }
                }
                if(input.getText().toString().compareTo("-") == 0 || equal_flag == false){
                    input.setText("0");
                    vbeigin = true;
                    tip_i = 0;
                    tip.setText("欢迎使用");
                }
                tip_lock = true;
                if(tip_i > 0){
                    tip_i--;
                }
            }else if(command.compareTo("BKSP") == 0 && equal_flag == false){        //如果是在按下=之后，输入框置0
                input.setText("0");
                vbeigin = true;
                tip_i = 0;
                tip_lock = true;
                tip.setText("欢迎使用");
            }else if(command.compareTo("C") == 0){
                input.setText("0");
                vbeigin = true;
                tip_i = 0;
                tip_lock = true;
                equal_flag = true;  //表明= 输入之前
                tip.setText("欢迎使用");
            }else if(command.compareTo("MC") == 0){
                mem.setText("0");
            }else if(command.compareTo("EXIT") == 0){
                System.exit(0);
            }else if(command.compareTo("=") == 0 && tip_lock && right(str) && equal_flag ){
                tip_i = 0;
                tip_lock = false ;  //表明不可输入
                equal_flag = false;   //表明输入=之后
                str_old = str;          //保存原来算是的样子
                //替换算式中的运算符便于计算；
                str = str.replaceAll("sin","s");
                str = str.replaceAll("cos","c");
                str = str.replaceAll("tan","t");
                str = str.replaceAll("log","g");
                str = str.replaceAll("ln","l");
                str = str.replaceAll("n!","!");
                vbeigin = true;
                str_new = str.replaceAll("-","-1x");
                new calc().process(str_new);
            }
            tip_lock = true;
        }
    };

    private void print(String command) {
    }

    private int TTO(String str) {
        return 2;
    }

    private void TipChecker(String s, String command) {
    }

    private boolean right(String str) {
        return true;
    }

}
