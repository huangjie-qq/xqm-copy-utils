# Bean转换工具类
### 默认按照字列名相同转换 不同时通过@Copy注解指定列名
* CopyUtils类：提供Bean转换方法<br>
*  @Copy注解： 用于映射列名不一样<br>
* @Exclude注解：用于取消字段的映射 默认为true<br>

##  Api说明：
* 单个Bean转换 :  T copy(Object source,Class<T> targetClass)<br>
* 多个Bean转换 : List copy(List sourceList,Class<T> targetClass)<br>
* map转Bean   :  T toBean(Map<String,Object> map,Class<T> targetClass)<br>
* Bean转Map   :  Map<String,Object> toMap(Object target)<br>

## 注解用法
```
   @Data
   @Builder
   public class User {
    
    private String name;
    
    private Integer age;
    
   }
 
   @Data
   public class UserVo {
    
    @Copy("name")//指定要转换的列名
    private String nameVo;
    
    private Integer age;
    
    @Exclude//排除该字段 默认为true
    private String sex;
    
   }
   
```

## 工具类用法
```
   public static void main(String[] args) {
        
        //单个Bean转换
        User user = User.builder().name("小球某").age(20).build();
        UserVo UserVo =  CopyUtils.copy(user, UserVo.class);
        
        //多个Bean转换
        User user1 = User.builder().name("张三").age(20).build();
        User user2 = User.builder().name("李四").age(20).build(); 
        List<User> userList = Arrays.asList(user1, user2);
        List<UserVo> UserVoList = CopyUtils.copy(userList, UserVo.class);
        
        //map转Bean
        Map<String, Object> map = new HashMap<>();
        map.put("name","小球某");
        map.put("age",20);
        User user = CopyUtils.toBean(map, User.class);
        
        //Bean转Map
        Map<String, Object> map = CopyUtils.toMap(user);
    }
   
```

### 作者QQ：1181217392