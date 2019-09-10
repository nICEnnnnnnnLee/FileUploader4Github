# FileUploader4Github
![语言java](https://img.shields.io/badge/Require-java-green.svg)
![支持系统 Win/Linux/Mac](https://img.shields.io/badge/Platform-%20win%20|%20linux%20|%20mac-lightgrey.svg)
![测试版本64位Win10系统, jre 1.8.0_101](https://img.shields.io/badge/TestPass-Win10%20x64__java__1.8.0__101-green.svg)
![开源协议Apache2.0](https://img.shields.io/badge/license-apache--2.0-green.svg)  
![当前版本](https://img.shields.io/github/release/nICEnnnnnnnLee/FileUploader4Github.svg?style=flat-square)
![Release 下载总量](https://img.shields.io/github/downloads/nICEnnnnnnnLee/FileUploader4Github/total.svg?style=flat-square)

基于GitHub API的GitHub文件上传工具    
===============================
**仅需Java环境即可运行**

## :smile:如何配置  
+ 配置释义  
因为是上传到Github仓库，故而需要`owner`、`repo`，在仓库中的位置`path`，以及授权用的`token`。  
这些都应该实现保存在jar包同目录下的`app.config`文件中。举例如下：  
```
owner = nICEnnnnnnnLee
repo = AbcTest
path = /note/hello/
token = 06495cbd3c86f38dd4b1e5a75249b14804d08tt5
```

+ 注意事项  
    + `path`应该以`/`开头，并且以`/`结尾，如根目录，`path = /`  
    + `path`应当不包含中文等特殊字符  
    + `path`在上传前可以不存在，上传文件时自动建立  
    + `token`请在[这里](https://github.com/settings/tokens)设置并获取，`repo`相关权限打勾即可
    + 仅支持上传新文件，若路径存在同名文件，将上传失败


## :smile:如何使用  
+ 独立运行  
    + `Release`页下载、解压，并配置好`app.config`
    + 双击`FileUp4Github.jar`  
    + 确认上传至云端时是否保存为原来的文件名/根据时间重命名
    + 选择想要上传的文件  
        + 通过窗口选择(单个文件)  
        + 通过拖拽选择(单个或多个文件均可)  
    ![](https://raw.githubusercontent.com/nICEnnnnnnnLee/AbcTest/master/note/hello/drag.png)
    + 控制台查看结果(上传完毕后，文件链接将复制到系统剪贴板)  
    ![](https://raw.githubusercontent.com/nICEnnnnnnnLee/AbcTest/master/note/hello/upload.png)

+ 嵌入第三方程序  
以下为示例，  
```
// 构造url链接 https://api.github.com/repos/:owner/:repo/contents/:path
String url = "https://api.github.com/repos/nICEnnnnnnnLee/AbcTest/contents/note/test.jpg";

// 获取想要上传的文件
File file = new File("D:\\Workspace\\sources\\pics\\test.jpg");

// 上传，并获取结果
boolean result = FileUploader.create(url, file, ":token");
```

## :smile:其它  
* **下载地址**: [https://github.com/nICEnnnnnnnLee/FileUploader4Github/releases](https://github.com/nICEnnnnnnnLee/FileUploader4Github/releases)
* [**更新日志**](https://github.com/nICEnnnnnnnLee/FileUploader4Github/blob/master/UPDATE.md)

<details>
<summary>LICENSE</summary>


```
Copyright (C) 2019 NiceLee. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
</details>
