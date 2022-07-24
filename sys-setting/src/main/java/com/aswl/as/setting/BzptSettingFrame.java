package com.aswl.as.setting;

import com.aswl.as.setting.utils.JedisUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BzptSettingFrame extends JFrame {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("jsConfig");

    private static String BASE_URL;
    private static String HTTP_URL;
    private static String ADMIN_URL;
    private static String LNG;
    private static String LAT;
    private static String IS_CLOUD;
    private static String IS_OFFLINE_MAP;
    private static String OUTPUT_PATH;
    private static String ADMIN_OUTPUT_PATH;
    private static String CITY_OUTPUT_PATH;

    private static final String REDIS_VIDEO_STREAM_OUTPUT = "VIDEO_STREAM_OUTPUT";
    private static final String REDIS_VIDEO_STREAM_FLV_OUTPUT = "VIDEO_STREAM_FLV_OUTPUT";

    static {
        BASE_URL = bundle.getString("BaseUrl");
        HTTP_URL = bundle.getString("httpurl");
        ADMIN_URL = bundle.getString("adminUrl");
        LNG = bundle.getString("lng");
        LAT = bundle.getString("lat");
        IS_CLOUD = bundle.getString("isCloud");
        IS_OFFLINE_MAP = bundle.getString("isOfflineMap");
        OUTPUT_PATH = bundle.getString("outputPath");
        ADMIN_OUTPUT_PATH = bundle.getString("adminOutputPath");
        CITY_OUTPUT_PATH = bundle.getString("cityOutputPath");
    }

    JComboBox ipComboBox;
    ButtonGroup mapButtonGroup;
    JTextField lngText;
    JTextField latText;


    public BzptSettingFrame() {
        // 确保一个漂亮的外观风格
        this.setDefaultLookAndFeelDecorated(false);

        // 创建及设置窗口
//        JFrame frame = new JFrame("平台基本配置");
        this.setTitle("平台配置");
        this.setResizable(false);
//        this.setUndecorated(true);
//        this.getRootPane().setWindowDecorationStyle(JRootPane.INFORMATION_DIALOG);
        this.setSize(350, 260);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/images/logo.png")));

        //调用用户定义的方法并添加组件到面板
        JPanel panel = new JPanel();
        buildComponents(panel);
        //添加面板
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(BorderLayout.CENTER, panel);
//        frame.add(panel);

        // 显示窗口
//        frame.pack();
        this.setVisible(true);
    }

    private void buildComponents(JPanel panel) {

        /* 布局部分我们这边不多做介绍
         * 这边设置布局为 null
         */
        panel.setLayout(null);

        // 创建 JLabel
        JLabel userLabel = new JLabel("服务器IP:");
        /* 这个方法定义了组件的位置。
         * setBounds(x, y, width, height)
         * x 和 y 指定左上角的新位置，由 width 和 height 指定新的大小。
         */
        userLabel.setBounds(25,20,90,25);
        panel.add(userLabel);

        ipComboBox = new JComboBox();
        ipComboBox.addItem("请选择");
        List<String> ipList = getLocalIPList();
        if(ipList != null && ipList.size() > 0){
            for(String ip : ipList){
                ipComboBox.addItem(ip);
            }
        }
        ipComboBox.setBounds(135, 20, 180, 25);
        panel.add(ipComboBox);

        //地图
        JLabel mapLabel = new JLabel("地图选择:");
        mapLabel.setBounds(25,60,90,25);
        panel.add(mapLabel);

        mapButtonGroup = new ButtonGroup();
        JRadioButton mapOn = new JRadioButton("在线");
        JRadioButton mapOff = new JRadioButton("离线", true);
        mapOn.setBounds(135, 60, 60, 25);
        mapOff.setBounds(195, 60, 60, 25);
        mapButtonGroup.add(mapOn);
        mapButtonGroup.add(mapOff);
        panel.add(mapOn);
        panel.add(mapOff);

        //地图经度
        JLabel mapLngLabel = new JLabel("地图中心经度:");
        mapLngLabel.setBounds(25,100,90,25);
        panel.add(mapLngLabel);

        lngText = new JTextField("113.250732", 10);
        lngText.setBounds(135,100,180,25);
        panel.add(lngText);

        //地图纬度
        JLabel mapLatLabel = new JLabel("地图中心纬度:");
        mapLatLabel.setBounds(25,140,90,25);
        panel.add(mapLatLabel);

        latText = new JTextField("23.155540", 10);
        latText.setBounds(135,140,180,25);
        panel.add(latText);

        // 创建登录按钮
        JButton saveButton = new JButton("保存并退出");
        saveButton.setBounds(215, 180, 100, 25);
        panel.add(saveButton);
        //保存按钮绑定监听器
        addSaveActionListener(saveButton);
    }

    /**
     * 保存按钮绑定监听器
     * @param saveButton
     */
    private void addSaveActionListener(JButton saveButton) {
        saveButton.addActionListener(e -> {
            String ip = (String) ipComboBox.getSelectedItem();
            if(ip == null || ip.equals("请选择")){
                JOptionPane.showMessageDialog(null, "请选择服务器IP！");
                return;
            }

            String mapOffline = "true";
            Enumeration<AbstractButton> mapBtns = mapButtonGroup.getElements();
            while (mapBtns.hasMoreElements()) {
                AbstractButton btn = mapBtns.nextElement();
                if(btn.isSelected()){
                    if(btn.getText() != null && "离线".equals(btn.getText())){
                        mapOffline = "true";
                    }else if(btn.getText() != null && "在线".equals(btn.getText())){
                        mapOffline = "false";
                    }
                    break;
                }
            }

            String lng = lngText.getText();
            String lat = latText.getText();
            if(lng == null || lng.equals("")){
                JOptionPane.showMessageDialog(null, "请输入经度！");
                return;
            }
            if(!isFloatNumeric(lng)){
                JOptionPane.showMessageDialog(null, "请输入正确经度！");
                return;
            }
            if(lat == null || lat.equals("")){
                JOptionPane.showMessageDialog(null, "请输入纬度！");
                return;
            }
            if(!isFloatNumeric(lat)){
                JOptionPane.showMessageDialog(null, "请输入正确纬度！");
                return;
            }

            //redis videoStreamOutout
            JedisUtil.setKey(REDIS_VIDEO_STREAM_OUTPUT, "rtmp://" + ip + "/live/");
            JedisUtil.setKey(REDIS_VIDEO_STREAM_FLV_OUTPUT, "http://" + ip + ":8768/live?port=1935&app=live&stream=");

            StringBuffer config = new StringBuffer();
            config.append(
                    "window.Global={\n" +
                    "    BaseUrl: 'http://").append(ip).append(":8000',\n" +
                    "    httpurl: 'http://").append(ip).append(":8097', \n" +
                    "    adminUrl: 'http://").append(ip).append(":8081//#/logindup/', \n" +
                    "    lng: ").append(lng).append(", \n" +
                    "    lat: ").append(lat).append(", \n" +
                    "    zoomMap: 9, \n" +
                    "    zoomMapConfig: [ \n" +
                    "        { minZoom: 1, maxZoom: 9, title: '项目' },\n" +
                    "        { minZoom: 10, maxZoom: 12, title: '区域' },\n" +
                    "        { minZoom: 13, maxZoom: 18, title: '设备' }\n" +
                    "    ], \n" +
                    "    isOfflineMap: ").append(mapOffline).append(", \n" +
                    "    isCnZh: true \n" +
                    "}");

            StringBuffer adminConfig = new StringBuffer();
            adminConfig.append(
                    "window.Global={\n" +
                    "    BaseUrl: 'http://").append(ip).append(":8000' \n" +
                    "}");

            StringBuffer cityConfig = new StringBuffer();
            cityConfig.append(
                    "window.Global={\n" +
                    "    BaseUrl: 'http://").append(ip).append(":8000',\n" +
                    "    lng: ").append(lng).append(", \n" +
                    "    lat: ").append(lat).append(", \n" +
                    "    homePageRefreshTime: ").append("10").append(", \n" +
                    "    isOfflineMap: ").append(mapOffline).append(" \n" +
                    "}");

            File filePath = new File(OUTPUT_PATH);
            if(!filePath.exists()){
                filePath.mkdirs();
            }
            File adminFilePath = new File(ADMIN_OUTPUT_PATH);
            if(!adminFilePath.exists()){
                adminFilePath.mkdirs();
            }
            File cityFilePath = new File(CITY_OUTPUT_PATH);
            if(!cityFilePath.exists()){
                cityFilePath.mkdirs();
            }
            File file = new File(OUTPUT_PATH + File.separator + "config.js");
            File adminFile = new File(ADMIN_OUTPUT_PATH + File.separator + "config.js");
            File cityFile = new File(CITY_OUTPUT_PATH + File.separator + "config.js");
            FileOutputStream os = null;
            FileOutputStream adminOs = null;
            FileOutputStream cityOs = null;
            try {
                os = new FileOutputStream(file);
                os.write(config.toString().getBytes(StandardCharsets.UTF_8));
                adminOs = new FileOutputStream(adminFile);
                adminOs.write(adminConfig.toString().getBytes(StandardCharsets.UTF_8));
                cityOs = new FileOutputStream(cityFile);
                cityOs.write(cityConfig.toString().getBytes(StandardCharsets.UTF_8));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }finally {
                if(os != null){
                    try {
                        os.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                if(adminOs != null){
                    try {
                        adminOs.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                if(cityOs != null){
                    try {
                        cityOs.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            this.dispose();
            // 对话框
//            JOptionPane.showMessageDialog(null, "保存成功！");

        });
    }

    public static List<String> getLocalIPList() {
        List<String> ipList = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface;
            Enumeration<InetAddress> inetAddresses;
            InetAddress inetAddress;
            String ip;
            while (networkInterfaces.hasMoreElements()) {
                networkInterface = networkInterfaces.nextElement();
                inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    inetAddress = inetAddresses.nextElement();
                    if (inetAddress != null && inetAddress instanceof Inet4Address) { // IPV4
                        ip = inetAddress.getHostAddress();
                        ipList.add(ip);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipList;
    }

    private static Pattern floatNumericPattern = Pattern.compile("^[0-9\\-\\.]+$");
    /**
     * 判断是否浮点数字表示
     *
     * @param src
     *            源字符串
     * @return 是否数字的标志
     */
    public static boolean isFloatNumeric(String src) {
        boolean return_value = false;
        if (src != null && src.length() > 0) {
            Matcher m = floatNumericPattern.matcher(src);
            if (m.find()) {
                return_value = true;
            }
        }
        return return_value;
    }

    public static void main(String[] args) {
//        // 显示应用 GUI
//        javax.swing.SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
        new BzptSettingFrame();
//            }
//        });
    }
}
