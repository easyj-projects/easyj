# easyj

### 官网
[https://easyj.icu](https://easyj.icu)


### 介绍

本项目主要提供一系列的封装好的工具类或功能，供大家使用。 项目宗旨：能用一行代码解决的问题，就不用两行。

### 安装教程

1. springboot项目

```xml
<dependencyManagement>
	<dependencies>
		<groupId>icu.easyj.boot</groupId>
		<artifactId>easyj-spring-boot-bom</artifactId>
		<version>0.5.4-SNAPSHOT</version>
		<type>pom</type>
		<scope>import</scope>
	</dependencies>
</dependencyManagement>
```

```xml
<dependencies>
	<!-- @Cache304所需依赖 -->
	<dependency>
		<groupId>icu.easyj.boot</groupId>
		<artifactId>easyj-spring-boot-starter-web</artifactId>
	</dependency>
	<!-- @ExcelExport所需依赖 -->
	<dependency>
		<groupId>icu.easyj.boot</groupId>
		<artifactId>easyj-spring-boot-starter-poi-excel</artifactId>
	</dependency>
	<dependency>
		<groupId>icu.easyj.boot</groupId>
		<artifactId>easyj-spring-boot-starter-poi-excel-afterturn</artifactId>
	</dependency>
</dependencies>
```

2. 非springboot项目

```xml
<dependencies>
	<dependency>
		<groupId>icu.easyj</groupId>
		<artifactId>easyj-all</artifactId>
		<version>0.5.4-SNAPSHOT</version>
	</dependency>
</dependencies>
```

### 使用说明

###### # 注：下面的`MyEntity`为任意的自定义领域模型类。

#### 1、`@ExcelExport, @Excel, @ExcelCell`三个注解，实现Excel文件导出功能

1. 在Controller接口上，添加`@ExcelExport(fileNamePre = "文件名前缀", dataType = MyEntity.class)`
2. 在`MyEntity`类上添加`@Excel`注解，用于配置导出的表格的样式，如：是否添加序号列、是否冻结首行、是否添加数据筛选等等
3. 在`MyEntity`类的属性上添加`@ExcelCell`注解，用于配置列相关信息，如：列名、列号、列宽、列字体颜色、列背景颜色等等
4. 请求多传一个GET参数`&doExport=true`，表示此次请求为文件导出请求
5. 后端在接收到`doExport`参数时，此次请求，将分页功能禁用掉，主要是为了导出不分页情况下的所有数据

#### 2、`ExcelUtils`一个方法，`@Excel, @ExcelCell`两个注解，实现Excel文件导入功能

1.

```java
@RestController
public class XxxController {

	@PostMapping("/test/excel-import")
	public List<MyEntity> testExcelImport(@RequestPart("file") MultipartFile file) {
		// excel文件转为列表数据
		List<MyEntity> list = ExcelUtils.toList(file.getInputStream(), MyEntity.class);

		// 打印一下
		System.out.println(StringUtils.toString(list));

		// 将转换后的数据直接返回，方便查看
		return list;
	}
}
```

2. `@Excel`和`@ExcelCell`注解的使用方法同`Excel文件导出功能`中的使用方法

#### 3、`@Cache304`一个注解实现GET接口的304缓存功能

1. 在Controller的GET接口上，添加`@Cache304(cacheSeconds = 60, cacheDays = 1, useMaxAge = true)`
2. 在自定义领域模型类`MyEntity`上添加`@Excel`注解，用于配置导出的表格的样式
3. 在自定义领域模型类`MyEntity`的属性上添加`@ExcelCell`注解，用于配置列相关信息

### 使用示例

详见使用示例项目：[https://gitee.com/easyj-projects/easyj-samples](https://gitee.com/easyj-projects/easyj-samples)

### 参与贡献的方法

1. Fork 本仓库到自己的个人仓库中
2. 在Fork下来的仓库的最新代码上新建分支，名称分支可任意自定义
3. 编写代码，并Push
4. 回到本仓库提交 Pull Request，[传送门](https://gitee.com/easyj-projects/easyj/pull/new)
