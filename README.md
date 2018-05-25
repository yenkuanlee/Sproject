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
    - schema : CREATE TABLE UrlMapping(url text, surl text, PRIMARY KEY(url));
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
  # 此 web.xml 會將API呼叫導向 URLShortening.class 進行處理
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
本專案主程式為 URLShortening.java, 大致上可分成三部分
  - 第一部分 : 判斷API行為, 是要進行縮網址, 抑或是要將已縮的網址導向正確的URL
  - 第二部分 : 縮網址處理
  - 第三部分 : 將縮網址導向正確URL
### 第一部分
- 判定URL是否有url這個參數 (本專案使用 request.getParameter("url"))
- 若沒有url參數, 可判定此行為是要將短網址導向URL (進入第三部分)
- 若有url參數, 我們要取得正確的url(長網址)值
  - 不可直接使用request.getParameter("url")所得到的值, 否則有帶其他參數的網址會出現錯誤
    - ex : https://www.google.com/search?client=ubuntu&channel=fs&q=群暉科技&ie=utf-8&oe=utf-8
  - 本專案使用 request.getQueryString, 再進行字串處理, 得到正確的長網址
- PathInfo 為 api-create, 則可進入第二部分
### 第二部分
- 先判定長網址是否已經在database table中, 有的話直接回傳對應的短網址
- 若長網址沒出現在db table中, 則將table中所有短網址讀入set (為了判定是否產生重複短網址)
- 亂數產生8個英文或數字作為短網址, 若不幸出現在短網址set中, 則重新產生
- 將長網址與對應的亂數紀錄進db table
- 回傳使用者短網址資訊 : http://web-server-ip:web-server-port/短網址亂數
### 第三部分
- 取得短網址, 並進入db table查詢對應的長網址
- 使用 response.setHeader("Location", 長網址) 將使用者導向長網址
## 專案特色
- 相同網址多次呼叫API, 回傳的短網址相同
- 短網址在server IP後面由8個英文數字組合, 因為人的[短期記憶](https://zh.wikipedia.org/wiki/%E7%9F%AD%E6%9C%9F%E8%AE%B0%E5%BF%86)常常參考的數字是7 ± 2 個元素
- 考量到開發速度, 使用sqlite紀錄url與對應的短網址, 若考慮到大量使用者應使用其他適合的資料庫
## Reference
- [Random String Generation](https://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string)
- [Java Servlet 文件](https://tomcat.apache.org/tomcat-5.5-doc/servletapi/javax/servlet/http/HttpServletRequest.html)
- [Tomcat web.xml 設定](https://mail-archives.apache.org/mod_mbox/tomcat-users/200605.mbox/%3C446C4F87.3030901@joedog.org%3E)
- [URL redirect in Java](https://www.logicbig.com/tutorials/java-ee-tutorial/java-servlet/servlet-redirect.html)
