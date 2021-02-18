# Mybatis

[TOC]

## 1、Mybatis

### 1.1、简介

- MyBatis 是一款优秀的**持久层框架**
- 它支持自定义 SQL、存储过程以及高级映射。
- MyBatis 免除了几乎所有的 JDBC 代码以及设置参数和获取结果集的工作。
- MyBatis 可以通过简单的 XML 或注解来配置和映射原始类型、接口和 Java POJO（Plain Old Java Objects，普通老式 Java 对象）为数据库中的记录。
- MyBatis 本是<u>apache</u>的一个开源项目<u>iBatis</u>, 2010年这个项目由apache software foundation 迁移到了google code，并且改名为MyBatis 。
- 2013年11月迁移到<u>Github</u>。
- 帮助程序员将数据存入到数据库中方便，简化的自动化框架

- 优势
  - 简单易学
  - 灵活
  - sql和代码的分离，提高了可维护性
  - 提供映射标签，支持对象与数据库的orm字段关系映射
  - 提供对象关系映射标签,支持对象关系组建维护
  - 提供xm标签,支持编写动态sql

### 1.2、如何获取

GitHub官网：https://github.com/mybatis/mybatis-3

官方文档：https://mybatis.org/mybatis-3/zh/index.html

<u>本质是一个Maven项目</u>

gradle导入jar包

```java
compile group: 'org.mybatis', name: 'mybatis', version: '3.5.6'
```

### 1.3、持久化

数据持久化

- 持久化就是将程序的数据在持久状态和瞬时状态转化的过程。
- 由于内存的特性是**断电即失**，因此引出概念

为什么需要持久化

- 有一些对象，不能让他丢掉
- 内存太贵了

## 2、第一个Mybatis程序

搭建环境 -- 导入Mybatis -- 编写代码 -- 测试

### 2.1、搭建环境

- 1、建立数据库，添加初始数据

```sql
CREATE DATABASE `mybatis`;
USE `mybatis`; 

CREATE TABLE `user`
(
    `id` INT(20) NOT NULL PRIMARY KEY, 
    `name` VARCHAR(30) DEFAULT NULL, 
    `pwd` VARCHAR(30) DEFAULT NULL
)ENGINE=INNODB DEFAULT CHARSET=utf8; 

INSERT INTO `user`(`id`,`name`,`pwd`) 
VALUES 
(1,'TOM','123'), 
(2,'JERRY','321'), 
(3,'STEAV','456'), 
(4,'JOHN','654'), 
(5,'SAM','789'); 
```

- 2、建立gradle项目，导入jar包

```java
implementation group: 'junit', name: 'junit', version: '4.12'
implementation group: 'org.mybatis', name: 'mybatis', version: '3.5.6'
implementation group: 'mysql', name: 'mysql-connector-java', version: '5.1.49'
```

### 2.2、Mybatis核心配置文件

mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
  <environments default="development">
    <environment id="development">
      <transactionManager type="JDBC"/>
      <dataSource type="POOLED">
        <property name="driver" value="${driver}"/>
        <property name="url" value="${url}"/>
        <property name="username" value="${username}"/>
        <property name="password" value="${password}"/>
      </dataSource>
    </environment>
  </environments>
  
  <mappers>
    <mapper resource="org/mybatis/example/BlogMapper.xml"/>
  </mappers>
    
</configuration>
```

### 2.3、Mybatis工具类

```java
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

// sqlSessionFactory sqlSession
public class MybatisUtils {

    private static SqlSessionFactory sqlSessionFactory;

    static {
        String resource = "mybatis-config.xml";
        try {
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 既然有了 SqlSessionFactory，顾名思义，我们可以从中获得 SqlSession 的实例。
        // SqlSession 提供了在数据库执行 SQL 命令所需的所有方法。
    }

    public static SqlSession getSqlSession() {
        return sqlSessionFactory.openSession();
    }
}
```

### 2.4、搭建环境

dao层接口

```java
public interface UserMapper {
    // 查询全部用户
    List<User> getUserList();
}
```

UserMapper.xml：由原先的UserMapperImpl转换成了现在的mapper xml配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace=绑定一个对应的Dao/Mapper接口-->
<mapper namespace="com.neusoft.dao.UserMapper">
    <!--select查询所有用户-->
    <select id="getUserList" resultType="com.neusoft.pojo.User">
        select * from mybatis.user
    </select>
</mapper>
```

测试

```java
    @Test
    public void getAllUser() {

        // 获得sql session对象
        SqlSession sqlSession = MybatisUtils.getSqlSession();

        // 方式1：getMapper
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> userList = userMapper.getUserList();

        for (User user : userList) {
            System.out.println(user);
        }

        // 方式2：
//        List<User> userList1 = 
//        sqlSession.selectList("com.neusoft.dao.UserMapper.getUserList");
//
//        for (User user : userList1) {
//            System.out.println(user);
//        }

        sqlSession.close();
    }
```

##  3、CRUD：增删改查

### 3.1、namespace 命名空间

namespace中的包名要和接口包名一致 【Dao ---> Mapper】

【UserMapper.xml】

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--namespace=绑定一个对应的Dao/Mapper接口-->
<mapper namespace="com.neusoft.dao.UserMapper">
    <!--select查询语句-->
    <select id="getUserList" resultType="com.neusoft.pojo.User">
        select * from mybatis.user
    </select>
    
    <insert id="addUser" parameterType="com.neusoft.pojo.User">
        insert into mybatis.user (id, name, pwd) values (#{id}, #{name}, #{pwd})
    </insert>

    <update id="updateUserById" parameterType="com.neusoft.pojo.User">
        update mybatis.user set name=#{name}, pwd=#{pwd} where id=#{id};
    </update>
    
</mapper>
```

Line 6：需要**绑定Mapper接口**【UserMapper.java】接口类

### 3.2、select

选择，查询语句

```xml
- id：对应的namespace中的方法名
- resultType：Sql语句执行的返回值
- parameterType：参数类型
```

- 【完成一次查询需要的步骤】

  - 【UserMapper.java】接口中，编写接口

    ```java
    //根据ID查询用户
    User getUserById(int id);
    ```

  - 【UserMapper.xml】中编写select标签

    ```xml
    <!--select查询语句-->
    <select id="getUserList" resultType="com.neusoft.pojo.User">
        select * from mybatis.user
    </select>
    ```

  - 【UserMapperTest.java】中编写测试方法

    ```java
    @Test
    public void getUserById() {
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    
        User user = mapper.getUserById(1);
    
        System.out.println(user);
        sqlSession.close();
    }
    ```

### 3.3、insert

接口方法

```java
int addUser(User user);
```

insert 标签

```xml
<!--对象中的属性，可以直接取出来：#{id}, #{name}, #{pwd}-->
<insert id="addUser" parameterType="com.neusoft.pojo.User">
    insert into mybatis.user (id, name, pwd) values (#{id}, #{name}, #{pwd})
</insert>
```

测试方法

```java
//增删改需要提交事务
@Test
public void addUser() {
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);

    int res = mapper.addUser(new User(6, "Ore", "789"));
    if (res > 0)
    {
        System.out.println("insert success!");
    }

    sqlSession.commit();
    sqlSession.close();
}
```

### 3.4、update

接口方法

```java
int updateUserById(User user);
```

update标签

```xml
<update id="updateUserById" parameterType="com.neusoft.pojo.User">
    update mybatis.user set name=#{name}, pwd=#{pwd} where id=#{id};
</update>
```

测试方法

```java
public void updateUserById() {
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    
    mapper.updateUserById(new User(6,"OREA","987"));
    
    sqlSession.commit();
    sqlSession.close();
}
```

### 3.5、delete

接口

```java
int deleteUserById(int id);
```

标签

```xml
<delete id="deleteUserById" parameterType="int">
    delete from mybatis.user where id = #{id}
</delete>
```

测试方法

```java
@Test
public void deleteUserById() {
    SqlSession sqlSession = MybatisUtils.getSqlSession();

    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    int res = mapper.deleteUserById(7);
    if (res > 0){
        System.out.println("delete success!");
    }

    sqlSession.commit();
    sqlSession.close();

}
```

### 3.6、注意点

【PS：所有的“增删改”，都需要提交**事务**】

```java
sqlSession.commit();
```

### 3.7、错误分析

- 绑定接口的时候必须用【.】不能用【\】

```xml
<mapper namespace="com.neusoft.dao.UserMapper"></mapper>
```

 在【src/main/resources/mybatis-config.xml】配置文件下

绑定mapper则要使用【/】，             代表文件路径

```xml
<mappers>
    <mapper resource="com/neusoft/dao/UserMapper.xml"/>
</mappers>
```

- Mapper未在核心配置文件中注册

  ```java
  org.apache.ibatis.binding.BindingException: 
  Type interface com.neusoft.dao.UserMapper is not known to the MapperRegistry.
  ```

  ```xml
  <!--每一个mapper.xml都需要在Mybatis核心配置文件中注册！-->
  <mappers>
      <mapper resource="com/neusoft/dao/UserMapper.xml"/>
  </mappers>
  ```

- IDEA在新建模块的时候，重新构建全部gradle的时候，会出现bug，报错提示是【Mapper未注册】将dao下的mapper.xml命名空间错误修改，需要仔细检查，同时被变更的还有实体类的调用。

  错误情况为【com.neusoft.pojo.User】变成了【com.User】推测是由于操作了复制包的操作，导致了包名异常，因此全部构建gradle的时候会出现异常

### 3.8、万能的Map

假设，实体类中，或者数据库的表中，字段或者参数过多，应该考虑使用Map

【UserMapper.java】接口

```java
int addUser2(Map<String,Object> map);
User getUserById2(Map<String,Object> map);
```

【UserMapper.xml】

```xml
<insert id="addUser2" parameterType="map">
    insert into mybatis.user (id, name, pwd) values (#{userid}, #{username}, #{userpwd})
</insert>

<select id="getUserById2" parameterType="map" resultType="com.neusoft.pojo.User">
    select * from mybatis.user where id = #{UserId} and name = #{name}
</select>
```

【UserMapperTest.java】

```java
@Test
public void addUser2() {
    SqlSession sqlSession = MybatisUtils.getSqlSession();

    UserMapper mapper = sqlSession.getMapper(UserMapper.class);

    Map<String, Object> map = new HashMap<>();
    map.put("userid",7);
    map.put("username","fff");
    map.put("userpwd","2333");

    int res = mapper.addUser2(map);
    if (res > 0){
        System.out.println("insert2 success!");
    }

    sqlSession.commit();
    sqlSession.close();
}

// map查询
@Test
public void getUserById2() {
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);

    Map<String, Object> map = new HashMap<>();
    map.put("UserId",3);

    mapper.addUser2(map);
    User user = mapper.getUserById2(map);

    System.out.println(user);

    sqlSession.close();
}
```

Map传递参数，直接在sql取出key即可	【parameterType="map"】

对象传递参数，直接在sql取对象的属性即可	【parameterType="object"】

只有一个基本类型参数的情况下，可以直接在sql中取

多个参数用map，或者**注解**

### 3.9、模糊查询

【UserMapper.java】接口

```java
List<User> getUserLike(String name);
```

【UserMapper.xml】

```xml
<!--模糊查询-->
<select id="getUserLike" resultType="com.neusoft.pojo.User">
    select * from mybatis.user where name like "%"#{name}"%"
</select>
```

【UserMapperTest.java】测试方法

```java
@Test
public void getUserLike() {
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);

    List<User> userList = mapper.getUserLike("%J%");

    for (User user : userList) {
        System.out.println(user);
    }

    sqlSession.close();
}
```

- #### sql注入

  select * from mybatis.user where id =  **?**
  本来这个 **？**，应该由用户传个 **1** 进去，但是用户传了如下代码
  【select  * from mybatis.user where id = 1 or 1=1】
  【1】--->【1 or 1=1】
  这行sql执行，会将所有数据查询出来，出现严重信息泄露。

  #### 解决方案：

- java代码执行的时候，传递通配符%str%

  ```java
  List<User> userList = mapper.getUserLike("%J%");
  ```

- 在sql拼接中使用通配符

  ```xml
  select * from mybatis.user where name like "%"#{name}"%"
  ```

## 4、配置解析

### 4.1、核心配置文件

- 配置文件构成

  ```xml
  configuration（配置）
  properties（属性）
  settings（设置）
  typeAliases（类型别名）
  typeHandlers（类型处理器）
  objectFactory（对象工厂）
  plugins（插件）
  environments（环境配置）
  	environment（环境变量）
  		transactionManager（事务管理器）
  		dataSource（数据源）
  databaseIdProvider（数据库厂商标识）
  mappers（映射器）
  ```


- mybatis-config.xml

  ```xml
  <configuration>
    <environments default="development">
      <environment id="development">
        <transactionManager type="JDBC"/>
        <dataSource type="POOLED">
          <property name="driver" value="${driver}"/>
          <property name="url" value="${url}"/>
          <property name="username" value="${username}"/>
          <property name="password" value="${password}"/>
        </dataSource>
      </environment>
    </environments>
    <mappers>
      <mapper resource="com/neusoft/dao/UserMapper.xml"/>
    </mappers>
  </configuration>
  ```

### 4.2、环境配置（environments）

MyBatis 可以配置成适应多种环境

**尽管可以配置多个环境，但每个 SqlSessionFactory 实例只能选择一种环境。**

切换环境修改Line 1 的default就行，后面写各个环境的id

```xml
<environments default="mysql">
    
    <environment id="mysql"></environment>
    <environment id="postgresql"></environment>
    
</environments>
```

Mybatis默认的事务管理器就是JDBC，连接池：POOLED

- **事务管理器 （transactionManager）** (默认使用JDBC)
- **JDBC**：这个配置直接**使用了 JDBC 的提交和回滚设施**，它依赖**从数据源获得的连接**来**管理事务作用域**。
  
- **MANAGED** ： 这个配置几乎没做什么。它**从不提交或回滚一个连接**，而是让容器来管理事务的整个生命周期（比如 JEE 应用服务器的上下文）。 **默认情况下它会关闭连接**。
  
- **数据源（dataSource）**(默认使用POOLED)
  - **UNPOOLED**：这个数据源的实现会每次请求时打开和关闭连接。虽然有点慢，但对那些数据库连接可用性要求不高的简单应用程序来说，是一个很好的选择。 性能表现则依赖于使用的数据库。
  - **POOLED**： 这种数据源的实现利用“池”的概念将 JDBC 连接对象组织起来，避免了创建新的连接实例时所必需的初始化和认证时间。 这种处理方式很流行，能使并发 Web 应用快速响应请求。

### 4.3、属性（properties）

可以通过properties属性来引用配置文件

这些属性可以在外部进行配置，并可以进行动态替换。

可以在典型的 Java 属性文件中配置这些属性，也可以在 properties 元素的子元素中设置。

- 编写一个配置文件

  【datebase.properties】

  ```properties
  driver=com.mysql.jdbc.Driver
  url=jdbc:mysql://localhost:3306/mybatis?useSSL=false&useUnicode=true&characterEncoding=UTF-8
  username=root
  password=root
  ```

- 在**核心配置文件**中引入properties文件

  ```xml
  <!--引入外部配置文件-->
  <properties resource="datebase.properties">
      <property name="username" value="root"/>
      <property name="username" value="root"/>
  </properties>
  ```

可以直接引用外部配置文件，可以在其中增加一些配置属性

**注意：优先使用外部配置文件**

### 4.4、类型别名（typeAliases）

- 类型别名可为 Java 类型设置一个缩写名字。

- **仅用于 XML 配置**。

- 用来减少类完全限定名的冗余【com.neusoft.mybatis ---> Mybatis】。

- 在**核心配置文件**中修改。

  ```xml
  <!--方案1-->
  <typeAliases>
      <typeAlias type="com.neusoft.pojo.User" alias="User"/>
      <typeAlias type="com.neusoft.pojo.Student" alias="Student"/>
      <typeAlias type="com.neusoft.pojo.Employ" alias="Employ"/>
  </typeAliases>
  ```

  也可以指定一个包名，Mybatis会在包名下搜索需要的Java Bean

  扫描实体类的包，它的默认别名就为这类的 类名，首字母小写

  ```xml
  <!--方案2-->
  <typeAliases>
      <package name="com.neusoft.pojo"/>
  </typeAliases>
  ```

  **方案1适用于实体类较少的情况，方案2在实体类过多的情况下使用**

  第一种可以自定义别名，第二种可以使用注解起别名，在每个实体类下添加注解

  ```java
  @Alias("hello")
  public class User{}
  ```

### 4.5、设置（settings）

这是 MyBatis 中极为重要的调整设置，它们会改变 MyBatis 的运行时行为。 下表描述了设置中各项设置的含义、默认值等，**在核心配置文件中增加setting标签**。

| 设置名                       | 描述                                                         | 有效值                                                       | 默认值 |
| :--------------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- | :----- |
| **cacheEnabled**             | 全局性地开启或关闭所有映射器配置文件中已配置的任何缓存。     | **true** \| **false**                                        | true   |
| **lazyLoadingEnabled**       | 延迟加载的全局开关。当开启时，所有关联对象都会延迟加载。 特定关联关系中可通过设置 `fetchType` 属性来覆盖该项的开关状态。 | **true** \| **false**                                        | false  |
| **logImpl**                  | 指定 MyBatis 所用日志的具体实现，未指定时将自动查找。        | **SLF4J** \| **LOG4J** \| **LOG4J2** \| **JDK_LOGGING** \| **COMMONS_LOGGING** \| **STDOUT_LOGGING** \| **NO_LOGGING** | 未设置 |
| **mapUnderscoreToCamelCase** | 是否开启驼峰命名自动映射，即从经典数据库列名 A_COLUMN 映射到经典 Java 属性名 aColumn。 | **true** \| **false**                                        | False  |

### 4.6、映射器（mappers）

MapperRegistry：**核心配置文件**中注册绑定我们的Mapper.xml文件，**常用注册方式为==资源文件引用注册==**

- **绑定方式1：资源文件引用注册**

  ```xml
  <!-- 使用相对于类路径的资源引用 -->
  <mappers>
    <mapper resource="org/mybatis/builder/AuthorMapper.xml"/>
    <mapper resource="org/mybatis/builder/BlogMapper.xml"/>
    <mapper resource="org/mybatis/builder/PostMapper.xml"/>
  </mappers>
  ```

  可以在任意情况下使用

- 绑定方式2：使用class文件绑定注册

  ```xml
  <!-- 使用映射器接口实现类的完全限定类名 -->
  <mappers>
    <mapper class="org.mybatis.builder.AuthorMapper"/>
    <mapper class="org.mybatis.builder.BlogMapper"/>
    <mapper class="org.mybatis.builder.PostMapper"/>
  </mappers>
  ```

  PS：接口和他的Mapper.xml配置文件**必须同名**，接口和Mapper**必须在同一个包下**，不然绑定方式2无法找到对应的文件。

  ![image-20210122101907075](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210122101907075.png)

- 绑定方式3：扫描包进行注入绑定

  ```xml
  <!-- 将包内的映射器接口实现全部注册为映射器 -->
  <mappers>
    <package name="org.mybatis.builder"/>
  </mappers>
  ```

  PS：同绑定方式2，必须同名，必须在同一个包下

### 4.7、作用域（Scope）和生命周期

![image-20210122105821520](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210122105821520.png)

**作用域**和**生命周期类别**是至关重要的，因为错误的使用会导致非常严重的**并发问题**。

**SqlSessionFactoryBuilder**：

- 一旦创建了 SqlSessionFactory，就不再需要它了
- 局部变量即可

**SqlSessionFactory**

- 效果类似等同为数据库”连接池“
-  一旦被创建就应该在应用的运行期间一直存在，**没有任何理由丢弃它或重新创建另一个实例。**
- SqlSessionFactory 的最佳作用域是应用作用域。（全局变量）
- 最简单的就是使用**单例模式**或者静态单例模式。（保证全局只有一个变量）

**SqlSession**

- 连接到“连接池的”一个请求
- SqlSession 的实例不是线程安全的，因此是不能被共享的。
- 最佳的作用域是请求或方法作用域。（局部变量，方法内创建）
- 用完后关闭【sqlSession.close()】，避免资源占用

![image-20210122110301972](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210122110301972.png)

每一个mapper，代表一个具体的业务，执行一个sql，只做了一件事

## 5、ResultMap--解决属性名和字段名的问题

### 5.1、问题 -- 数据库字段名和实体类属性名不相同

数据库中的字段为

![数据库结构](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210122111911816.png)

实体类字段为

```java
public class User {
    private int id;
    private String name;
    private String password;
}
```

当实体类字段和数据库不相等的时候，无法查询到字段对应的值

```java
User{id=1, name='TOM', password='null'}
```

查询结果为空

```sql
select * from mybatis.user where id = #{id}
select id,name,pwd from mybatis.user where id = #{id}
select id,name,pwd as password from mybatis.user where id = #{id}
```

### 5.2、解决方案

- 起别名，sql第三行，添加as password

- ResultMap：结果集映射

  | 来源         | col1 | col2 | col3         |
  | ------------ | :--- | :--- | :----------- |
  | 数据库表字段 | id   | name | **pwd**      |
  | 实体类       | id   | name | **password** |

  让数据库表字段结果变成实体类的样子

  【UserMapper.xml】

  ```xml
  <!--结果集映射-->
  <resultMap id="UserMap" type="User">
      <!--column：数据库中的字段，property：实体类中的属性-->
      <result column="id" property="id"/>
      <result column="name" property="name"/>
      <result column="pwd" property="password"/>
  </resultMap>
  
  <select id="getUserById" resultMap="UserMap">
      select * from mybatis.user where id = #{id}
  </select>
  ```

- `resultMap` 元素是 MyBatis 中最重要最强大的元素。
- ResultMap 的设计思想是，对简单的语句做到零配置，**不需要配置显式的结果映射**
- 对于复杂一点的语句，只需要描述语句之间的关系就行了。

- `resultMap` 最优秀的地方在于，虽然已经对它相当了解了，但是完全可以不用显式地配置它们

  什么不一样，就映射什么，例如 Line 4、Line 5 都是不需要的

- type是你需要映射的实体类（配置了Aliases简称，因此只需要写User）

## 6、日志

### 6.1、日志工厂

如果一个数据库操作出现了异常，需要排错，**查看日志**可以很快速地找到

以前使用的方法【sout】【debug】

现在使用的方法【日志工厂 -- logImpl】

| 设置名  | 描述                                                      | 有效值                                                       | 默认值 |
| :------ | :-------------------------------------------------------- | :----------------------------------------------------------- | :----- |
| logImpl | **指定 MyBatis 所用日志的具体实现，未指定时将自动查找。** | SLF4J \| LOG4J \| LOG4J2 \| JDK_LOGGING \| COMMONS_LOGGING \| STDOUT_LOGGING \| NO_LOGGING | 未设置 |

- **SLF4J**  --- Simple Logging Facade for Java 简单Java日志门面
- **LOG4J**  --- Apache提供的一个强有力的日志操作包
- **LOG4J2**  --- LOG4J升级版
- **JDK_LOGGING** --- Java自带日志输出
- **COMMONS_LOGGING** ---  apache最早提供的日志的门面接口，提供简单的日志实现以及日志解耦功能。
- **STDOUT_LOGGING** --- 控制台输出日志
- **NO_LOGGING** --- 无日志输出

在Mybatis中，具体使用哪一个日志输出，在**核心配置文件**中设定

STDOUT_LOGGING 标准日志输出

```xml
<settings>
    <!--标准的日志工厂实现-->
    <setting name="logImpl" value="STDOUT_LOGGING"/>
</settings>
```

![image-20210122142926389](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210122142926389.png)

### 6.2、LOG4J

Log4J：Apache的一个开源项目

- 通过使用Log4j，可以控制日志信息输送的目的地是控制台、文件、GUI组件。
- 可以控制每一条日志的输出格式。
- 通过定义每一条日志信息的级别，更加细致地控制日志的生成过程。
- 通过一个配置文件来灵活地进行配置，而不需要修改应用的代码。

1、导入log4j的包

```xml
implementation group: 'log4j', name: 'log4j', version: '1.2.17'
```

2、log4j.properties

```properties
#将等级为DEBUG的日志信息输出到console和file这两个目的地，console和file的定义在下面的代码
log4j.rootLogger=DEBUG,console,file

#控制台输出的相关设置
log4j.appender.console = org.apache.log4j.ConsoleAppender
log4j.appender.console.Target = System.out
log4j.appender.console.Threshold = DEBUG
log4j.appender.console.layout = org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern = [%c]-%m%n

#文件输出的相关配置
log4j.appender.file = org.apache.log4j.RollingFileAppender
log4j.appender.file.File = ./log/mybatis-log.log
log4j.appender.file.MaxFileSize = 10mb
log4j.appender.file.Threshold = DEBUG
log4j.appender.file.layout = org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern = [%p][%d{yy-MM-dd}][%c]%m%n

#日志输出级别
log4j.logger.org.mybatis = DEBUG
log4j.logger.java.sql = DEBUG
log4j.logger.java.sql.Statement = DEBUG
log4j.logger.java.sql.ResultSet = DEBUG
log4j.logger.java.sql.PrepareStatement = DEBUG
```

3、**核心配置文件**配置log4j为日志的实现

```xml
<settings>
    <setting name="logImpl" value="LOG4J"/>
</settings>
```

4、Log4的使用，直接测试运行

![image-20210122152838880](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210122152838880.png)

简单使用

- 导包

  ```java
  import org.apache.log4j.Logger;
  ```

- 创建日志对象，参数为当前的class

  ```java
  static Logger logger = Logger.getLogger(UserMapperTest.class);
  ```

- 日志级别

  ```java
  logger.info("info:进入了testLog4j");
  logger.debug("debug:进入了testLog4j");
  logger.error("error:进入了testLog4j");
  ```

### 6.3、奇怪的BUG

在日志中出现了乱码

![image-20210126111250590](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210126111250590.png)

在导入的jar包中打开mybatis的源码，找到org.apache.ibatis.io.DefaultVFS.class

查看代码

![image-20210126111551452](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210126111551452.png)

看到这个参数有异常，进入InputStreamReader.java中查看

![image-20210126111725461](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210126111725461.png)

缺少第二个参数charset，字符编码

推测可以添加第二个参数为“utf-8”以解决乱码

```java
public InputStreamReader(InputStream in, String charsetName)
    throws UnsupportedEncodingException
{
    super(in);
    if (charsetName == null)
        throw new NullPointerException("charsetName");
    sd = StreamDecoder.forInputStreamReader(in, this, charsetName);
}
```

## 7、分页

目的

- 减少数据的处理量

### 7.1、**使用Limit分页**

sql语法

```sql
select * from user limit startIndex,pagesize
select * from user limit 5 #[0,5]
```

mybatis实现分页

1、接口

```java
// limit分页
List<User> getUserByLimit(Map<String,Integer> map);
```

2、mapper.xml

```xml
<!--结果集映射-->
<resultMap id="UserMap" type="User">
    <!--column：数据库中的字段，property：实体类中的属性-->
    <result column="id" property="id"/>
    <result column="name" property="name"/>
    <result column="pwd" property="password"/>
</resultMap>

<select id="getUserByLimit" resultMap="UserMap" parameterType="map">
    select * from mybatis.user limit #{startIndex},#{pageSize}
</select>
```

3、测试方法

```java
@Test
public void getUserByLimit() {
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    
    Map<String, Integer> map = new HashMap<>();
    map.put("startIndex",2);
    map.put("pageSize",3);
    List<User> userList = mapper.getUserByLimit(map);
    for (User user : userList) {
        System.out.println(user);
    }
    
    sqlSession.close();
}
```

### 7.2、使用RowBounds分页

不再使用sql实现分页，通过Java代码实现分页

1、接口

```java
// RowBounds分页
List<User> getUserByRowBounds();
```

2、mapper.xml

```xml
<!--RowBounds查询-->
<select id="getUserByRowBounds" resultMap="UserMap">
    select * from mybatis.user
</select>
```

3、测试方法

```java
@Test
public void getUserByRowBounds() {
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    // rowBounds实现
    RowBounds rowBounds = new RowBounds(1,2);
    // 通过JAVA代码层面实现分页
    List<User> userList = sqlSession.selectList(
        "com.neusoft.dao.UserMapper.getUserByRowBounds",
        null,
        rowBounds);
    for (User user : userList) {
        System.out.println(user);
    }
    mapper.getUserByRowBounds();
    
    sqlSession.close();
}
```

## 8、注解开发

### 8.1、面向接口编程

- 大家之前都学过面向对象编程，也学习过接口，但在真正的开发中，很多时候我们会选择面向接口编程
- 根本原因：==**解耦**==，可拓展,提高复用，分层开发中，上层不用管具体的实现,大家都遵守共同的标准，使得开发变得容易，规范性更好
- 在一个面向对象的系统中，系统的各种功能是由许许多多的不同对象协作完成的。在这种情況下,各个对象内部是如何实现自己的，对系统设计人员来讲就不那么重要了
- 而各个对象之间的协作关系则成为系统设计的关键。小到不同类之间的通信，大到各模块之间的交互，在系统设计之初都是要着重考虑的，这也是系统设计的主要工作内容。面向接口编程就是指按照这种思想来编程。

**关于接口的理解**

- 接口从更深层次的理解，应是定义(规范，约束)与实现(名实分离的原则)的分离
- 接口的本身反映了系统设计人员对系统的抽象理解
- 接口应有两类
  - 第一类是对一个个体的抽象，它可对应为一个抽象体(abstract class)
  - 第二类是对一个个体某一方面的抽象，即形成一个抽象面(interface)

- 一个体有可能有多个抽象面。抽象体与抽象面是有区别的。

**三个面向区别**

- 面向对象是指，我们考虑问题时，以对象为单位，考虑它的属性及方法
- 面向过程是指，我们考虑问题时，以一个具体的流程(事务过程)为单位，考虑它的实现
- 接口设计与非接口设计是针对复用技术而言的，与面向对象(过程)不是一个问题更多的体现就是对系统整体的架构

### 8.2、使用注解开发

相较于原来的方法，以前需要编写mapper.xml并在**核心配置文件**中注册，现在不需要编写mapper.xml文件，仅仅需要在**核心配置文件**中绑定接口，并在接口上添加注解

1.注解在接口上实现

2.需要在核心配置文件中绑定接口

3.测试

【UserMapper.java】接口，在此添加注解

```java
@Select("select * from mybatis.user where id=#{id}")
User getUserById(@Param("id") int id);
```

**核心配置文件**中绑定接口

```xml
<mappers>
    <mapper class="com.neusoft.dao.UserMapper"/>
</mappers>
```

测试方法【参考方法】

```java
@Test
public void test() {
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    // 底层主要应用反射
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    List<User> userList = mapper.getUser();

    for (User user : userList) {
        System.out.println(user);
    }

    sqlSession.close();
}
```

出现的问题是，无法在【Mapper.xml】配置ResultMap映射，无法解决字段名不相同的问题。

相较而言，注解开发较为死板，pojo实体类的元素必须和数据库的元素同名，才能避免无法查询到数据

<img src="https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210125103020504.png" alt="image-20210125103020504"  />

【官方文档】

- 使用注解来映射简单语句会使代码显得更加简洁，但对于稍微复杂一点的语句，Java 注解不仅力不从心，还会让你本就复杂的 SQL 语句更加混乱不堪。 

- 因此，如果你需要做一些很复杂的操作，最好用 XML 来映射语句。

注解开发本质：使用反射机制

底层：动态代理

![image-20210125104929159](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210125104929159.png)

### 8.3、注解实现CRUD

可以在工具类创建的时候自动提交事务，不用再手动提交事务了【见3.6】

openSession接口的实现，有很多重载方法

```java
@Override
public SqlSession openSession(boolean autoCommit) {
  return openSessionFromDataSource(configuration.getDefaultExecutorType(), null, autoCommit);
}
```

```java
public static SqlSession getSqlSession() {
    return sqlSessionFactory.openSession(true);
}
```

编写接口，添加注解

```java
import org.apache.ibatis.annotations.*;
import java.util.List;
public interface UserMapper {
    @Select("select * from mybatis.user")
    List<User> getUser();

    @Select("select * from mybatis.user where id=#{id}")
    User getUserById(@Param("id") int id);

    @Insert("insert into mybatis.user(id,name,pwd) values (#{id},#{name},#{pwd})")
    int addUser(User user);

    @Update("update mybatis.user set name=#{name},pwd=#{pwd} where id=#{id}")
    int updateUserById(User user);

    @Delete("delete from mybatis.user where id=#{id}")
    int deleteUserById(@Param("id") int id);
}
```

测试方法

```java
@Test
public void selectUserById() {
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    // 底层主要应用反射
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    User user = mapper.getUserById(3);

    System.out.println(user);

    sqlSession.close();
}

@Test
public void insertUser(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);

    int res = mapper.addUser(new User(7, "HANS", "147"));
    if (res>0){
        System.out.println("insert success");
    }

    sqlSession.close();
}

@Test
public void updateUserById(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    UserMapper mapper = sqlSession.getMapper(UserMapper.class);

    int res = mapper.updateUserById(new User(7, "ass", "741"));
    if (res>0){
        System.out.println("update success");
    }

    sqlSession.close();
}
```

以上前提：必须在**核心配置文件**中绑定接口

关于@Param()注解

```java
public void example(@Param("id") int id);
```

- 基本类型的参数或者String类型，需要加上
- 引用类型不需要加
- 如果只有一个基本类型的话，可以忽略，但是建议加上
- 在SQL中引用的就是@Param(“id”)设定的属性名，以@Param()中的为准

## 9、Lombok

### 9.1、使用

- IDEA在File -- setting -- plugin中搜索Marketplace，找到Lombok插件，安装

- build.gradle中导入包，

  ```java
  annotationProcessor group: 'org.projectlombok', name: 'lombok', version: '1.18.10'
  ```

  PS：不要使用implementation导入包，要使用annotationProcessor导入

### 9.2、所有注解

```java
@Getter and @Setter
@FieldNameConstants
@ToString
@EqualsAndHashCode
@AllArgsConstructor, @RequiredArgsConstructor and @NoArgsConstructor
@Log, @Log4j, @Log4j2, @Slf4j, @XSlf4j, @CommonsLog, @JBossLog, @Flogger, @CustomLog
@Data
@Builder
@SuperBuilder
@Singular
@Delegate
@Value
@Accessors
@Wither
@With
@SneakyThrows
@val
@var
experimental @var
@UtilityClass
Lombok config system
```

### 9.3、常用注解分析

@Data注解

![image-20210125155447101](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210125155447101.png)

@AllArgsConstructor注解

![image-20210125155612503](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210125155612503.png)

@NoArgsConstructor注解

![image-20210125155729850](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210125155729850.png)

@ToString

![image-20210125155922992](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210125155922992.png)

@Getter and @Setter

![image-20210125160023586](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210125160023586.png)

@EqualsAndHashCode

![image-20210125160254624](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210125160254624.png)

## 10、多对一处理

![image-20210126144517203](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210126144517203.png)

例如：多个学生，对应一个老师

对学生而言：**关联**

多个学生，关联一个老师

- association

   一个复杂类型的关联；许多结果将包装成这种类型

  - 嵌套结果映射 – 关联可以是 `resultMap` 元素，或是对其它结果映射的引用

- collection

   一个复杂类型的集合

  - 嵌套结果映射 – 集合可以是 `resultMap` 元素，或是对其它结果映射的引用

```sql
CREATE TABLE `teacher` (
  `id` INT(10) NOT NULL,
  `name` VARCHAR(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8; 

INSERT INTO teacher(`id`, `name`) VALUES (1, '老师'); 

CREATE TABLE `student` (
  `id` INT(10) NOT NULL,
  `name` VARCHAR(30) DEFAULT NULL,
  `tid` INT(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fktid` (`tid`),
  CONSTRAINT `fktid` FOREIGN KEY (`tid`) REFERENCES `teacher` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8; 

INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('1', '小明', '1'); 
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('2', '小红', '1'); 
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('3', '小张', '1'); 
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('4', '小李', '1'); 
INSERT INTO `student` (`id`, `name`, `tid`) VALUES ('5', '小王', '1');
```

### 10.1、测试环境搭建

1.导入 lombok

2.新建实体类 Teacher, Student

3.建立 Mapper接口

4.建立 Mapper.XML文件

5.在核心配置文件中绑定注册我们的 Mapper接口或者文件【方式很多,随心选】

6.测试查询是否能够成功

7.建立实体类

实体类如下

```java
@Data
public class Student {
    private int id;
    private String name;
//    学生关联一个老师
    private Teacher teacher;
}
```

```java
@Data
public class Teacher {
    private int id;
    private String name;
}
```

### 10.2、按照查询嵌套处理 -- 子查询

等同于子查询

- 需求：查询学生和老师的详细信息

  sql如下

  ```sql
  select s.id, s.name, t.name 
  from student s, teacher t 
  where s.tid = t.id;
  ```

- 思路：
  - 查询所有的学生信息
  - 根据查询出来的学生的tid，寻找对应的老师

StudentMapper.java 接口

```java
List<Student> getAllStudent();
```

StudentMapper.xml

```xml
<select id="getAllStudent" resultMap="StudentRS">
    select * from mybatis.student;
</select>
<resultMap id="StudentRS" type="Student">
    <!--复杂属性的处理
        如果是对象：association
        如果是集合：collection
        现在使用的是对象
        子查询-->
    <association property="teacher" column="tid" javaType="Teacher" select="getAllTeacher"/>
</resultMap>

<select id="getAllTeacher" resultType="Teacher" parameterType="int">
    select * from mybatis.teacher where id = #{id};
</select>
```

只绑定了一个接口

通过resultMap映射，使用association将teacher的tid返回变为Teacher对象

通过getAllStudent嵌套getAllTeacher查询，能获取所有信息，查询结果如下![image-20210126112540378](C:\Users\LiuFeiyu\AppData\Roaming\Typora\typora-user-images\image-20210126112540378.png)

类似于student对象嵌套了一个teacher对象

### 10.3、按照结果嵌套处理 -- 连表查询

sql

```sql
select s.id s_id, s.name s_name, t.id t_id, t.name t_name
from mybatis.student s, mybatis.teacher t
where s.tid = t.id;
```

连表查询

StudentMapper.java 接口

```java
List<Student> getAllStudent2();
```

StudentMapper.xml

```xml
<select id="getAllStudent2" resultMap="StudentRS2">
    select s.id s_id, s.name s_name, t.id t_id, t.name t_name
    from mybatis.student s, mybatis.teacher t
    where s.tid = t.id;
</select>

<resultMap id="StudentRS2" type="Student">
    <result property="id" column="s_id"/>
    <result property="name" column="s_name"/>
    <association property="teacher" javaType="Teacher">
        <result property="id" column="t_id"/>
        <result property="name" column="t_name"/>
    </association>
</resultMap>

<select id="getAllTeacher2" resultType="Teacher">
    select * from mybatis.teacher where id=#{id}
</select>
```

测试方法

```java
@Test
public void testStudent2() {
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);

    List<Student> studentList = mapper.getAllStudent2();

    System.out.println("----------------------");
    for (Student student : studentList) {
        System.out.println(student);
    }
    System.out.println("----------------------");
    sqlSession.close();
}
```

## 11、一对多处理

### 11.1、概念和环境搭建

![image-20210126145248816](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210126145248816.png)

例如：一个老师拥有多个学生

对于老师而言：**集合**

一个老师，有很多个学生

一对多的关系

配置环境，创建Teacher，Student实体类

```java
@Data
public class Teacher {
    private int id;
    private String name;

    // 一个老师拥有多个学生
    private List<Student> students;
}
```

```java
@Data
public class Student {
    private int id;
    private String name;
    private int tId;
}
```

### 11.2、按结果嵌套处理 -- 连表查询

TeacherMapper接口

```java
Teacher getTeacherById(@Param("tid") int id);
```

TeacherMapper.xml

```xml
<!--按结果嵌套查询-->
<select id="getTeacherById" resultMap="TeacherRS">
    select s.id s_id, s.name s_name, t.name t_name, t.id t_id
    from mybatis.student s, mybatis.teacher t
    where s.tid = t.id and t.id = #{tid};
</select>

<resultMap id="TeacherRS" type="Teacher">

    <result property="id" column="t_id"/>
    <result property="name" column="t_name"/>

    <collection property="students" ofType="Student">
        
        <result property="id" column="s_id"/>
        <result property="name" column="s_name"/>
        <result property="tid" column="t_id"/>
        
    </collection>

</resultMap>
```

测试方法

```java
@Test
public void testTeacher() {
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);

    Teacher teacher = mapper.getTeacherById(1);

    System.out.println("----------------------");
    System.out.println(teacher);
    System.out.println("----------------------");
    sqlSession.close();
}
```

### 11.3、按查询嵌套处理 -- 子查询

TeacherMapper接口

```java
Teacher getTeacherById2(@Param("tid") int id);
```

TeacherMapper.xml

```xml
<select id="getTeacherById2" resultMap="TeacherRS2">
    select * from mybatis.teacher where id = #{tid}
</select>

<resultMap id="TeacherRS2" type="Teacher">
    <!--可以忽略相同字段的映射，就不需要写-->
    <collection property="students" column="id" javaType="ArrayList" ofType="Student" select="getStudentByTeacherId"/>
</resultMap>

<select id="getStudentByTeacherId" resultType="Student">
    select * from mybatis.student where tid = #{teacherId};
</select>
```

测试类

```java
@Test
public void testTeacher2() {
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
    Teacher teacher = mapper.getTeacherById2(1);

    System.out.println("----------------------");
    System.out.println(teacher);
    System.out.println("----------------------");
    sqlSession.close();
}
```

## 12、小结：多对一、一对多

### 12.1、ResultMap中类型使用

- 关联 - association 【多对一】

- 集合- collection 【一对多】

- javaType           ofType

  - javaType -- 用来指定实体类中属性的类型

  - ofType -- 用来指定映射到List或者集合中的pojo类型，泛型中的约束类型

    ```java
    private List<Student> students
    ```

    在这之中，`List<Student>`为ofType，填写Student

`PS：javaType="type" 为指定的属性类型
type -- int`

`集合中的泛型信息（List）需要使用ofType("type")
type -- Student`

### 12.2、注意点

- 保证SQL的可行性，尽量保证通俗易懂
- 注意一对多和多对一中的属性名和字段的问题
- 排查问题可以用日志输出【LOG4J】
- 避免慢sql（sql执行时间要短，如果执行时间过长会导致效率低下【1ms ---- 5ms】）

### 12.3、面试问题

- MySQL存储引擎

  - 存储引擎是保存数据的核心技术
  - 存储引擎是服务于存储服务的，通过存储引擎将数据保存。
  - 类如计算机如何将数据保存到磁盘中一样。
  - 在数据库中，存储引擎提供了一种**存储解决方案**，实现了**新增数据**、**更新数据**和**建立索引**等功能

  ![image-20210126164413407](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210126164413407.png)

  常用的引擎有InnoDB，MyISAM，MEMORY，Archive

- InnoDB底层原理

  InnoDB的内存架构主要分为三大块，**缓冲池**（Buffer Pool）、**重做日志缓冲**（Redo Log Buffer）和**额外内存池**

  - **缓冲池**（Buffer Pool）

    - 面对大量的请求时，CPU的处理速度和磁盘的IO速度之间差距太大，为了提高整体的效率， InnoDB引入了**缓冲池**。

    - 不会直接去修改磁盘，而是会修改已经在缓冲池的页中的数据，然后再将数据刷回磁盘

    - 加速读，加速写，减少与磁盘的IO交互。

  - **重做日志缓冲**（Redo Log Buffer）

    - 当事务开始时，会先记录Redo Log到Redo Log Buffer中，然后再更新缓冲池页数据。
    - Redo Log Buffer中的数据会按照一定的频率写到重做日志中去。

- 索引

- 索引优化

## 13、动态 SQL

### 13.1、概念

**根据不同的条件，生成不同的sql语句**

主要是在Mapper.xml中通过各种标签实现

- if
- choose (when, otherwise)
- trim (where, set)
- foreach -- 最恶心的。。。

各类标签都很智能化，可以将多余的部分进行修整，以适配整个sql语句，

需要关注各个标签的特性，会删除哪些部分

一般建议语句写完整，由标签进行整合处理

**核心：拼接sql，编写仅仅需要保证sql的正确性，按照sql格式进行排列组合**

**本质还是sql语句，只是在sql层面，执行逻辑代码**

- 先写出完整sql
- 再修改成动态sql

### 13.2、搭建环境

创建表

```sql
CREATE TABLE `blog`
(
	`id` VARCHAR(50) NOT NULL COMMENT '博客id',
	`title` VARCHAR(100) NOT NULL COMMENT '博客标题',
	`author` VARCHAR(30) NOT NULL COMMENT '博客作者',
	`create_time` DATETIME NOT NULL COMMENT '创建时间',
	`views` INT(30) NOT NULL COMMENT '浏览量'
)ENGINE=INNODB DEFAULT CHARSET=utf8;
```

创建实体类Blog

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog {
    private String id;
    private String title;
    private String author;
    private Date createTime;
    private int view;
}
// 实体类使用的日期型应该为java.util.Date
```

创建ID工具类

```java
import java.util.UUID;
public class IdUtils {
    public static String getId(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
```

当数据库字段名和Java实体类名不统一的时候，可以在核心配置文件中添加如下设定

```xml
<setting name="mapUnderscoreToCamelCase" value="true"/>
<!--是否开启驼峰命名自动映射，即从经典数据库列名 A_COLUMN 映射到经典 Java 属性名 aColumn。-->
```

### 13.3、if语句的使用

创建接口和在Mapper.xml绑定

```java
List<Blog> queryBlogIf(Map map);
```

```xml
<select id="queryBlogIf" parameterType="map" resultType="Blog">
    select * from mybatis.blog where 1=1
    <if test="title != null">
        and title = #{title}
    </if>
    <if test="author != null">
        and author = #{author}
    </if>
</select>
```

编写测试方法

```java
    @Test
    public void test(){
        SqlSession sqlSession = MybatisUtils.getSqlSession();
        BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);


        HashMap<Object, Object> map = new HashMap<>();

//        map.put("title","JAVA");
        map.put("author","James");


        List<Blog> blogList = mapper.queryBlogIf(map);
        System.out.println("----------------");
        for (Blog blog : blogList) {
            System.out.println(blog);
        }
        System.out.println("----------------");

        sqlSession.close();
    }
```

IF的注意点

- 在select标签下编写
- 查询时会根据你传的参数来决定
- 进行语句的拼接
- 在之前的where 1=1后拼接，假如不传参数可以查询表中所有内容

### 13.4、Where标签

```xml
<select id="queryBlogIf" parameterType="map" resultType="Blog">
    select * from mybatis.blog
    <where>
        <if test="title != null">
            and title = #{title}
        </if>
        <if test="author != null">
            and author = #{author}
        </if>
    </where>
</select>
```

可以自动帮拼接语句，避免出现如同

```sql
 select * from example where and id=1
```

的情况，where和and连用的情况

可以智能修改语句

可自动消除多余的and，在无参情况下可以直接查询所有数据，类同于where 1=1

### 13.5、Choose标签

```xml
<select id="queryBlogChoose" parameterType="map" resultType="Blog">
    select * from mybatis.blog
    <where>

        <choose>
            <when test="title != null">
                and title = #{title}
            </when>
            <when test="title != null">
                and author = #{author}
            </when>
            <otherwise>
                and views = #{views}
            </otherwise>
        </choose>
        
    </where>
</select>
```

choose标签类似于java中的switch语句，但是不能break，也就是说，如果判断了一个when标签为真，就会忽略其他when标签，将数据以第一次when匹配的条件查出

test里面写条件（sql语句）

例如，查询如下数据

![image-20210127154146483](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210127154146483.png)

假设Title=java，author=sam，views=9999

这条数据依旧能被查询出来，因为title是符合条件的

sql只会拼接成如下语句

```sql
select * from mybatis.blog WHERE title = ?
```

 测试方法

```java
@Test
public void test(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);

    HashMap<Object, Object> map = new HashMap<>();
    map.put("title","JAVA");
    map.put("author","Oracle");

    List<Blog> blogList = mapper.queryBlogIf(map);
    for (Blog blog : blogList) {
        System.out.println(blog);
    }
    sqlSession.close();
}
```

### 13.6、Set标签

```xml
<update id="updateBlogById" parameterType="map">
    update mybatis.blog
    
    <set>
        <if test="title != null">
            title = #{title},
        </if>

        <if test="author != null">
            author = #{author},
        </if>
    </set>

    where id = #{id}

</update>
```

set中的if语句中，test写入sql语句，判断

### 13.7、Foreach 

动态SQL的一个常用的操作需求是对**一个集合进行遍历**，通常是在**构建IN条件语句**的时候。

样例sql

```sql
select * from user where 1=1 and 

  <foreach item="id" collection="ids"
      open="(" separator="or" close=")">
        #{id}
  </foreach>

(id=1 or id=2 or id=3)
```

接口和Mapper.xml

```java
List<Blog> queryBlogForeach(Map map);
```

```xml
<select id="queryBlogForeach" parameterType="map" resultType="blog">
    select * from mybatis.blog
    <where>
        <foreach collection="ids" item="id" open="and (" close=")" separator="or">
            id = #{id}
        </foreach>
    </where>
</select>
```

collection -- 集合，通过map参数传入（传入一个万能的map，map中存在一个集合，集合的名字为ids）
item -- 从集合中遍历出来的每一项的名字，称之为id
open -- 以xxxx开始【开始语句，就是where的条件开始 ”and ( “】
close -- 以xxxx结束【结束语句，where的条件结束符 ”)“ 】
seperator -- 分隔符，就是sql中每一项的分隔，”or“，样例中使用or查询，即分隔符为or
index -- 索引，从哪里开始查询

PS：and后面的空格不能省略

测试方法

```java
@Test
public void testForeach(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);

    ArrayList<Integer> ids = new ArrayList();
    ids.add(3);
    ids.add(2);

    HashMap<Object, Object> map = new HashMap<>();
    map.put("ids",ids);

    List<Blog> blogList = mapper.queryBlogForeach(map);

    System.out.println("----------------------------");
    for (Blog blog : blogList) {
        System.out.println(blog);
    }
    System.out.println("----------------------------");

    sqlSession.close();
}
```

map put的value等同于之前在mapper.xml定义的collection名称，这个value是一个ArrayList集合，见Line 6~11

执行结果

![image-20210128105440018](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210128105440018.png)

### 13.8、sql片段

有时候，我们需要将一些sql功能抽取出来，方便复用

1、使用SQL标签抽取公共的部分，id自己设定

```xml
<sql id="if-title-author">
    
    <if test="title != null">
        title = #{title},
    </if>
    <if test="author != null">
        author = #{author},
    </if>
    
</sql>
```

2、在需要使用片段的地方添加include标签，refid填写sql的id

```xml
<update id="updateBlogById" parameterType="map">
    update mybatis.blog
    
    <set>
        <include refid="if-title-author"/>
    </set>

    where id = #{id}
</update>
```

## 14、缓存

### 14.1、简介

所有的查询，都需要连接数据库，就会消耗资源

那么一次查询的结果，给他暂存在一个可以直接取到的地方 ---> 内存 ：缓存

我们再次查询相同的时候，直接走缓存，就不用走数据库了

 

- 什么是缓存[Cache]?
  - 存在内存中的临时数据
  - 将用户经常查询的数据放在缓存（内存）中，用户去查询数据就不用从磁盘上(关系型数据库数据文件）查询，从缓存中查询，从而提高查询效率，解决了高并发系统的性能问题

- 为什么使用缓存？
  - 减少和数据库的交互次数，减少系统开销,提高系统效率。

- 什么样的数据能使用缓存？
  - 经常查询并且不经常改变的数据。

### 14.2、Mybatis缓存

- MyBatis包含一个非常强大的査询缓存特性，它可以非常方便地定制和配置缓存。缓存可以极大的提升査询效率
- My Batis系统中默认定义了两级缓存：**一级缓存**和**二级缓存**。
  - 默认情况下，只有一级缓存开启。（SqlSession级别的缓存，也称为本地缓存）
  - 二级缓存需要手动开启和配置，是基于namespace级别的缓存。
  - 为了提高扩展性, MyBatis定义了缓存接口 Cache。我们可以通过实现 Cache接口来自定义二级缓存。

### 14.3、一级缓存

- 一级缓存也叫本地缓存：
  - 与数据库同一次会话期间查询到的数据会放在本地缓存中。
  - 以后如果需要获取相同的数据，直接从缓存中拿，没必要再去查询数据库。

- 测试步骤

  1.**核心配置文件**中开启日志，查看输出情况

  2.测试在一个 Sesion中查询两次相同记录

  ```java
  @Test
  public void test(){
      SqlSession sqlSession = MybatisUtils.getSqlSession();
      UserMapper mapper = sqlSession.getMapper(UserMapper.class);
  
      System.out.println("----------------");
      User user = mapper.selectUserById(1);
      System.out.println(user);
      System.out.println("----------------");
      User user2 = mapper.selectUserById(1);
      System.out.println(user2);
      System.out.println("----------------");
      System.out.println(user == user2);
      System.out.println("----------------");
  
      sqlSession.close();
  }
  ```

  3.查看日志输出

  ![image-20210128114624318](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210128114624318.png)

缓存失效的情况：

- 查询不一样的记录

  ![image-20210128114951880](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210128114951880.png)

- 增删改操作，可能会改变原来的数据，所以必定会刷新缓存

  ![image-20210128115826046](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210128115826046.png)

- 查询不同的Mapper.xml

- 手动清除缓存【sqlSession.clearCache();】

  ![image-20210128120023651](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210128120023651.png)

一级缓存默认是开启的，只在一次SqlSession中有效【创建一次SqlSession】从 **拿到连接**到**关闭连接**这段时间有效

相当于一个map，

### 14.4、二级缓存

- 二级缓存也叫全局缓存，一级缓存作用域太低了，所以诞生了二级缓存
- 基于 namespace级别的缓存，一个名称空间，对应一个二级缓存。
- 工作机制
  - 一个会话查询一条数据，这个数据就会被放在当前会话的一级缓存中。
  - 如果当前会话关闭了，这个会话对应的一级缓存就没了。但是我们想要的是，会话关闭了，一级缓存中的数据被保存到二级缓存中。
  - 新的会话查询信息，就可以从二级缓存中获取内容。
  - 不同的mapper查出的数据会放在自己对应的缓存(map)中。

开启缓存步骤

- **核心配置文件**显式开启全局缓存

  ```xml
  <setting name="cacheEnabled" value="true"/>
  ```

- Mapper.xml中开启二级缓存**<cache/>**

  ```xml
  <cache
      eviction="FIFO"
      flushInterval="60000"
      size="512"
      readOnly="true"/>
  
  <select id="selectUserById" resultType="User" useCache="true">
      select * from mybatis.user;
  </select>
  ```

| 属性名        | 含义     | 参数                      |
| ------------- | -------- | ------------------------- |
| eviction      | 清除策略 | LRU，FIFO，SOFT，WEAK     |
| flushInterval | 刷新间隔 | 任意的正整数，单位ms      |
| size          | 引用数目 | 任意正整数，默认值是 1024 |
| readOnly      | 只读     | true，false               |

测试二级缓存

```java
@Test
public void test(){
    SqlSession sqlSession = MybatisUtils.getSqlSession();
    SqlSession sqlSession2 = MybatisUtils.getSqlSession();


    UserMapper mapper = sqlSession.getMapper(UserMapper.class);
    User user = mapper.selectUserById(1);
    System.out.println(user);
    sqlSession.close();

    UserMapper mapper2 = sqlSession2.getMapper(UserMapper.class);
    User user2 = mapper2.selectUserById(1);
    System.out.println(user2);
    sqlSession2.close();
}
```

缓存生效状态

![image-20210128130644831](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210128130644831.png)

在sqlSession结束的时候，会将一级缓存放入二级缓存中，只有关闭的时候才生效。

相当于是SqlSession为一级缓存，Mapper（namespace）为二级缓存，二级缓存包含一级缓存

PS：我们需要将实体类序列化，不然缓存无法使用，会抛出异常：**没有序列化**

```
Caused by: java.io.NotSerializableException: com.neusoft.pojo.User
```

由于之前设置了缓存策略，而如果不指定策略，直接写**<cache/>**，由于实体类没有序列化，就相当于没有策略

二级缓存总结

- 只要开启了二级缓存，在同一个Mapper下都有效
- 所有数据都会先放在一级缓存中
- 只有当会话提交，或者关闭的时候，才会被提交到二级缓存中

### 14.5、缓存原理

![image-20210128133759603](https://raw.githubusercontent.com/LuoTianyi712/Typora-pic/master/typora/image-20210128133759603.png)

### 14.6、自定义缓存-Ehcache

Ehcache是一种广泛使用的开源Java分布式缓存。主要面向通用缓存,Java EE和轻量级容器。

Mapper中指定缓存

```xml
<cache type="org.mybatis.caches.ehcache.EhcacheCache"/>
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:noNamespaceSchemaLocation="http://ehcache.org/ehcache.xsd"
         updateCheck="false">

    <diskStore path="./tmpdir/Tmp_EhCache"/>

    <defaultCache
            eternal="false"
            maxElementsInMemory="10000"
            overflowToDisk="false"
            diskPersistent="false"
            timeToIdleSeconds="1800"
            timeToLiveSeconds="259200"
            memoryStoreEvictionPolicy="LRU"/>

    <cache
            name="cloud_user"
            eternal="false"
            maxElementsInMemory="5000"
            overflowToDisk="false"
            diskPersistent="false"
            timeToIdleSeconds="1800"
            timeToLiveSeconds="1800"
            memoryStoreEvictionPolicy="LRU"/>
</ehcache>
```

目前使用Redis数据库，K,V存储

