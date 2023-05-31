# ReadMe
## 实验环境:
**数据库平台：** *Mysql*
**开发工具：** *Java*
**IDE** *eclipse*
**JRE版本** *1.8*
**用户界面: ** *字符界面*


## 编译运行流程：
- 1.解压压缩包
- 2.打开Mysql，新建空数据库
- 3.打开eclipse，新建java工程.
- 4.添加JDBC的jar包
    - 1.右键工程名,进入properties->Java Build Path->Libraries->Add External JARs
    - 2.选择解压文件所在路径,添加解压目录下的"mysql-connector-java-8.0.19"
    - 3.点击Apply and Close
- 5.载入源代码及文件：
    - 1.将解压目录\src目录下的.java文件全部拷贝至新建工程的src目录下
    - 2.将解压目录下的"book.txt","card.txt","borrow.txt"拷贝至新建工程的根目录
- 6.在eclipse中打开Main.java
- 7.点击Run->Run即可编译运行
- 8.在主程序中输入新建的数据库名及数据库管理员用户名及密码,即可成功连接数据库.


## Attention:
第一次连接数据库时,数据库为空,请务必先在程序中选择**8号功能**, 建立图书管理系统表
之后可使用**7号功能**预先载入txt文件中的数据信息,便于程序查询测试.
