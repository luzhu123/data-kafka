package com.keruyun.fintech.datakafka;

import com.sun.jmx.snmp.Timestamp;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * @author Tietang Wang 铁汤
 * @Date: 15/11/23 14:00
 * @version 2.0 by fw 2016-7-1 添加对非profile配置文件地支持
 */
public class ZkImport {
    // dev test等环境
  private static final String CONNECT_URL = "testzk1.shishike.com:2181,testzk2.shishike.com:2181,testzk3.shishike.com:2181";
  // loadtest压测环境
// private static final String CONNECT_URL = "172.16.31.67:2181,172.16.31.68:2181,172.16.31.69:2181";
  // zk1.keruyun.com:2181为生产环境慎用 // testzk1/2/3.shishike.com:2181为联调测试 // 120.26.53.156:2181暂时为灰度
  private static final String DEFAULT_NAME_SPACE = "configuratons/on_fintech";

  public static void main(String[] args) throws Exception {
    System.out.println("begin:"+new Timestamp(1458200526768L));
    // importSync();//覆盖 sync.properties
    importProfiles();// 覆盖 profiles下所有配置
    // importRoutes();//覆盖 接口路由配置 on-mobile.properties calm-router.properties
    // calm-router-trade.properties
    System.out.println("end:"+new Timestamp(1458200526768L));
  }

  /**
   * 刷新接口路由 by fw 2016-7-1
   *
   * @throws Exception
   */
  private static void importRoutes() throws Exception {
    // importRoutesProperties("configurations/CalmRouter/dev/CalmRouter_Proxy/routes","calm-router-trade.properties","calm-router-trade",true);
    // importRoutesProperties("configurations/CalmRouter/test/CalmRouter_Proxy/routes","calm-router-trade.properties","calm-router-trade",true);
    // importRoutesProperties("configurations/CalmRouter/gld/CalmRouter_Proxy/routes","calm-router-trade.properties","calm-router-trade",true);
    // importRoutesProperties("configurations/CalmRouter/loadtest/CalmRouter_Proxy/routes","calm-router-trade.properties","calm-router-trade",true);
    // importRoutesProperties("configurations/CalmRouter/prod/CalmRouter_Proxy/routes","calm-router-trade.properties","calm-router-trade",false);
  }

  /**
   * 刷新接口路由属性文件到ZK
   *
   * @param nameSpace
   * @param fileName
   * @param key
   * @param isFlush 是否立即刷新proxy 生产环境必须为false
   * @throws Exception
   */
  private static void importRoutesProperties(String nameSpace, String fileName, String key,
                                             boolean isFlush) throws Exception {
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    CuratorFramework client = CuratorFrameworkFactory.builder().namespace(nameSpace)
            .connectString(CONNECT_URL).retryPolicy(retryPolicy).build();
    client.start();
    client.blockUntilConnected();

    InputStream in = ZkImport.class.getClassLoader().getResourceAsStream(fileName);
    byte[] bt = new byte[in.available()];
    in.read(bt);
    String val = new String(bt, "utf-8");
    importProperty(client, "", key, val);
    if (isFlush) {
      importProperty(client, "", "notice", UUID.randomUUID().toString());
    }
    client.close();
  }



  /**
   * by fw 2016-7-1
   *
   * @throws Exception
   */
  private static void importSync() throws Exception {
    String propertySuffix = "sync.properties";
//    importProperties(DEFAULT_NAME_SPACE + "/dev", propertySuffix);
    importProperties(DEFAULT_NAME_SPACE + "/test", propertySuffix);
    importProperties(DEFAULT_NAME_SPACE + "/gld", propertySuffix);
    importProperties(DEFAULT_NAME_SPACE + "/prod", propertySuffix);
    importProperties(DEFAULT_NAME_SPACE + "/ctitest", propertySuffix);
  }


  private static void importProperties(String fileName) throws Exception {
    importProperties(DEFAULT_NAME_SPACE, fileName);
  }


  private static void importProperties(String nameSpace, String fileName) throws Exception {
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    CuratorFramework client = CuratorFrameworkFactory.builder().namespace(nameSpace)
            .connectString(CONNECT_URL).retryPolicy(retryPolicy).build();
    client.start();
    client.blockUntilConnected();
    importConfig(client, null, fileName, null);
    client.close();
  }

  /**
   * by fw 2016-7-1
   *
   * @throws Exception
   */
  private static void importProfiles() throws Exception {
    RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    CuratorFramework client =
            CuratorFrameworkFactory.builder().namespace("configurations/on_fintech")
                    .connectString(CONNECT_URL).retryPolicy(retryPolicy).build();
    client.start();
    client.blockUntilConnected();
    importFile(client,
            "./config/src/main/profiles");
    client.close();
  }


  private static void importFile(CuratorFramework client, String profileDir) throws IOException {
    File file = new File(profileDir);
    File[] files = file.listFiles();
    for (File dir : files) {
      if (dir.isDirectory()) {
        String profileName = dir.getName();
        System.out.println(profileDir);
        File[] pfiles = dir.listFiles();
        for (File pfile : pfiles) {
          System.out.println(profileName + "," + pfile.getName());
          System.out.println(pfile.getAbsolutePath());
          importConfig(client, profileName, pfile.getName(), pfile.getAbsolutePath());
        }
      }
    }
  }


  static void importConfig(CuratorFramework client, String prefixPath, String fileName,
                           String absolutePath) throws IOException {
    // by fw 2016-4-28 读取profiles时需要绝对路径 否则类路径只会读取dev下的配置文件
    // InputStream in = ZkImport.class.getClassLoader().getResourceAsStream(fileName);
    InputStream in = null;
    if (absolutePath != null) {
      in = new FileInputStream(absolutePath);
    } else {
      in = ZkImport.class.getClassLoader().getResourceAsStream(fileName);
    }
    Properties properties = new Properties();
    properties.load(in);
    int i = fileName.indexOf('.');
    prefixPath = (prefixPath == null ? "" : prefixPath);
    prefixPath =
            (!"".equals(prefixPath) && !prefixPath.startsWith("/")) ? "/" + prefixPath : prefixPath;
    String bashPath = prefixPath + "/apps:" + fileName.substring(0, i);

    for (Map.Entry<Object, Object> entry : properties.entrySet()) {
      try {
        importProperty(client, bashPath, entry.getKey().toString(), entry.getValue().toString());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  static void importProperty(CuratorFramework client, String bashPath, String key, String value)
          throws Exception {
    String path = bashPath + "/" + toPath(key);
    System.out.println(path + ":" + value + ",  " + key);
    if (client.checkExists().forPath(path) == null) {
      client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path,
              value.getBytes("UTF-8"));

    } else {
      client.setData().forPath(path, value.getBytes("UTF-8"));

    }

  }

  static String toPath(String key) {
    return key.replaceAll("\\.", "/");
  }

  static String parentPath(String key) {
    String path = key.replaceAll("\\.", "/");
    int index = path.lastIndexOf('/');
    return path.substring(0, index);

  }
}
