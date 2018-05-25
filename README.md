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

## 演算法邏輯

## Reference
