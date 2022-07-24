package com.aswl.as.setting.utils;

//import org.apache.commons.lang.StringUtils;
//
//import java.io.*;
//import java.util.Enumeration;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.jar.JarEntry;
//import java.util.jar.JarFile;
//import java.util.jar.JarOutputStream;

public class JarTool {

//    /***
//     *
//     * @param jarPath 源jar包路径
//     * @param newJarPath 新生成的jar存放路径
//     * @param confPath 需要修改的配置文件相对路径
//     * @param jarConfChange 配置文件内容替换逻辑
//     * @return
//     * @throws IOException
//     */
//    public static final File change(String jarPath, String newJarPath, String confPath, StringReplace jarConfChange) throws IOException {
//
//        if(StringUtils.isNotBlank(jarPath)) {
//            File file = new File(jarPath);
//            JarFile jarFile = new JarFile(file);
//            confPath = confPath.replace("\\", "/");
//            JarEntry entry = jarFile.getJarEntry(confPath);
//            List<JarEntry> lists = new LinkedList<JarEntry>();
//            for (Enumeration<JarEntry> entrys = jarFile.entries(); entrys.hasMoreElements();) {
//                JarEntry jarEntry = entrys.nextElement();
//                lists.add(jarEntry);
//            }
//
//            String buffer = confContent(jarFile.getInputStream(entry), jarConfChange);
//
//            return write(lists,jarFile,newJarPath, buffer,confPath);
//        }
//        return null;
//    }
//
//    /**
//     * 获取替换后的文件内容
//     * @param inputStream
//     * @param jarConfChange
//     * @return
//     * @throws IOException
//     */
//    private static final String confContent(InputStream inputStream, StringReplace jarConfChange) throws IOException {
//        InputStreamReader isr = new InputStreamReader(inputStream);
//        BufferedReader br = new BufferedReader(isr);
//        StringBuffer buf = new StringBuffer();
//        String line = null;
//        try {
//            while ((line = br.readLine()) != null) {
//                buf.append(jarConfChange.process(line)).append("\r\n");
//            }
//        } finally {
//            br.close();
//            isr.close();
//        }
//        return buf.toString();
//    }
//
//    /**
//     *	 写入到新的jar文件中
//     * @param lists
//     * @param jarFile
//     * @param newJarPath
//     * @param content
//     * @param confPath
//     * @return
//     * @throws IOException
//     */
//    private static final File write(List<JarEntry> lists,JarFile jarFile,
//                                    String newJarPath, String content,String confPath) throws IOException {
//        JarOutputStream jos = null;
//
//        File outFile = new File(newJarPath);
//        if (outFile.exists()) {
//            outFile.delete();
//        }
//        outFile.createNewFile();
//        FileOutputStream fos = new FileOutputStream(outFile);
//        jos = new JarOutputStream(fos);
//        InputStream in = null;
//        JarFile libJarFile = null;
//        String fileName = "";
//        int fileIndex = 0;
//
//        try {
//            for (JarEntry je : lists) {
//                fileName = je.getName();
//                fileIndex++;
////                jos.setMethod(ZipEntry.DEFLATED);
//
//                if(je.getName().endsWith("jar")){
////                    newEntry.setMethod(ZipEntry.STORED);
////                    jos.setMethod(ZipEntry.STORED);
//                }
//
//                JarEntry newEntry = new JarEntry(je.getName());
//                if(je.getLastModifiedTime() != null)
//                    newEntry.setLastModifiedTime(je.getLastModifiedTime());
//                if(je.getLastAccessTime() != null)
//                    newEntry.setLastAccessTime(je.getLastAccessTime());
//                if(je.getCreationTime() != null)
//                    newEntry.setCreationTime(je.getCreationTime());
////                newEntry.setMethod(je.getMethod());
////                newEntry.setComment(je.getComment());
////                newEntry.setTime(je.getTime());
////                newEntry.setExtra(je.getExtra());
//                jos.putNextEntry(newEntry);
//                if (je.getName().equals(confPath)) {
//
//                    jos.write(content.getBytes());
//                    continue;
//                }
//                if (jarFile.getInputStream(je) != null) {
////                    jos.putNextEntry(je);
////                    in = new ZipInputStream(jarFile.getInputStream(je));
////                    if(fileName.endsWith("jar")) {
////                        in = new BufferedInputStream(jarFile.getInputStream(je));
////                        in = new JarInputStream(jarFile.getInputStream(je));
////                    }else{
//                        in = (jarFile.getInputStream(je));
////                    }
//                    byte[] buffer = new byte[32];
//                    int count = -1;
//                    while((count = in.read(buffer)) != -1){
//                        jos.write(buffer, 0, count);
//                    }
////                    byte[] b = new byte[jarFile.getInputStream(je).available()];
////                    jarFile.getInputStream(je).read(b);
////                    jos.write(b);
//                }
////                jos.flush();
//                jos.closeEntry();
//                if(in != null){
//                    in.close();
//                }
//            }
//        } catch (Exception e){
//            System.out.println("fileName index: " + fileIndex + ", name: " + fileName);
//            e.printStackTrace();
//        } finally {
//            // 关闭流
//            if (jos != null) {
//                try {
//                    jos.close();
//                } catch (IOException e) {
//                    jos = null;
//                }
//            }
//            fos.close();
//        }
//
//        return outFile;
//    }
//    public static void main(String[] args) throws IOException {
//        String srcJarPath = "C:\\aswl\\config-service.jar";
//        String newJarPath = "C:\\aswl\\asms\\config-service.jar";
//        String confPath = "BOOT-INF\\classes\\config\\ibrs-service.yml";
//        File fi = change(srcJarPath, newJarPath, confPath, srcConf -> {
//            if (srcConf.contains("output: rtmp:")) {
//                return srcConf.substring(0, srcConf.indexOf("output: rtmp:")) + "output: rtmp://120.24.102.159/oflaDemo/hello/";
//            }
//            return srcConf;
//        });
//        System.out.println(fi.getName());
//    }
//
//    @FunctionalInterface
//    public interface StringReplace {
//
//        String process(String srcConf);
//    }
}
