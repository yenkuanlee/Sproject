# Sproject

## 功能簡介
Sproject是一個縮網址專案, 使用者可透過瀏覽器或是curl呼叫API, 將複雜的網址轉換為短網址. 

## 使用說明
- 產生短網址
  - 透過瀏覽器
    - 在瀏覽器網址欄位輸入 http://120.125.73.105:8787/api-create?url=長網址
  - 透過shell curl
    - shell> curl http://120.125.73.105:8787/api-create.php?url=長網址
  - 以上兩種呼叫API方式皆會回傳短網址
- 使用短網址
  - 在瀏覽器網址欄位輸入短網址, 便會連接到使用者想去的網站

## 範例
  - 要縮的網址 : https://github.com/yenkuanlee
  - 在瀏覽器網址欄位中輸入 http://120.125.73.105:8787/api-create.php?url=https://github.com/yenkuanlee
    - 回傳 http://120.125.73.105:8787/o9lSaE7E
  - 在瀏覽器網址欄位中輸入 http://120.125.73.105:8787/o9lSaE7E , 便能參觀彥寬的github

## 系統架構
  - 程式語言 : Java
  - web server : Tomcat-7.0.86
  - database : SQLite
  - third party jar
    - json.jar
    - servlet-api.jar
    - sqlite.jar

## 環境
### 環境建置
- Java 8 或以上版本
- tomcat 7 或以上版本, 假設安裝於 /opt/tomcat7
- 專案部署
```
$ git clone https://github.com/yenkuanlee/Sproject
$ cd Sproject
$ sudo cp web.xml /opt/tomcat7/webapps/ROOT/WEB-INF
$ sh a.sh
  # 編譯 URLShortening.java
  # 將 URLShortening.class 搬移至tomcat classes目錄
  # 重啟 tomcat
```
### Docker
為了快速完成此專案, 可使用既有的Docker環境進行開發 (此專案中提供Dockerfile為可開發環境, 但包含其他專案所需套件, 故image較大, 故僅供參考)
```
$ docker pull yenkuanlee/mcu:latest
$ docker run -dti -p 8787:8080 yenkuanlee/mcu /bin/sh
$ docker exec -ti $CONTAINER_ID bash
$ cd
$ git clone https://github.com/yenkuanlee/Sproject
$ cd Sproject
$ sudo cp web.xml /opt/tomcat7/webapps/ROOT/WEB-INF
$ sh a.sh
```
## 演算法邏輯

## Reference
- [Random String Generation](https://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string)
- [Java Servlet 文件](https://tomcat.apache.org/tomcat-5.5-doc/servletapi/javax/servlet/http/HttpServletRequest.html)
- [Tomcat web.xml 設定](https://mail-archives.apache.org/mod_mbox/tomcat-users/200605.mbox/%3C446C4F87.3030901@joedog.org%3E)
- [URL redirect in Java](https://www.logicbig.com/tutorials/java-ee-tutorial/java-servlet/servlet-redirect.html)
