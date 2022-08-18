<p align="center"><img src="https://socialify.git.ci/lucasfroque/spring-boot-cache-example/image?description=1&amp;font=Source%20Code%20Pro&amp;pattern=Solid&amp;theme=Dark" alt="project-image"></p>

<p id="description">In this project i am going to show a example how to use the Caching in Spring and generally improve the performance of our system.</p>

<h2>💻 Built with</h2>

Technologies used in the project:

*   Java
*   Springboot
*   SpringData JPA
*   Spring Cache
*   Lombok

<h2>🚀 Getting started <h2/>
<h3> Add the spring cache dependency </h3>

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```
<h3>Enabling cache</h3>

```java

@SpringBootApplication
@EnableCaching
public class SpringBootCacheExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootCacheExampleApplication.class, args);
	}

}
```

<h3>Implementing the cache</h3>
<p>for that we need to use some @Annotations</p>

These are:

* `@Cacheable` - to cache the result of the method
* `@CacheEvict` - to evict the cache
* `@Caching` - to group the @Cacheable and @CacheEvict annotations

```java
@Service
public class UserServiceImpl implements UserService {

    public static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository repository;

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public User save(User user) {
        logger.info("Saving user: {}", user.getName());
        return repository.save(user);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "users", allEntries = true),
            @CacheEvict(value = "user", key = "#user.id")
    })
    public User update(Long id, User user) {
        User userToUpdate = repository.findById(id).get();
            userToUpdate.setName(user.getName());
            userToUpdate.setEmail(user.getEmail());

            logger.info("Updating user: {}", userToUpdate.getName());
            return repository.save(userToUpdate);
    }

    @Override
    @Cacheable(value = "users")
    public List<User> findAll() {
        logger.info("Finding all users");
        return repository.findAll();
    }

    @Override
    @Cacheable(value = "user", key = "#id", unless = "#result == null")
    public User findById(Long id) {
        logger.info("Finding user by id: {}", id);
        return repository.findById(id).orElse(null);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "users", allEntries = true),
            @CacheEvict(value = "user", key = "#id")
    })
    public void delete(Long id) {
        logger.info("Deleting user by id: {}", id);
        repository.deleteById(id);
    }
}
```

<h3>Testing the cache</h3>
<h4>Running the application</h4>

```bash
mvn clean install
```
```bash
mvn spring-boot:run
```
<h4>send a request to the application</h4>

```bash
GET localhost:8080/users to get all users
GET localhost:8080/users/1 to get user by id
PUT localhost:8080/users/1 to update user by id
POST localhost:8080/users to save user
DELETE localhost:8080/users/1 to delete user by id
```
<h4>Check the logs</h4>

For the first request if nothing is cached we have the following logs:
```bash
curl -X GET localhost:8080/users
```

```bash
LOGS:
INFO 1034 --- [nio-8080-exec-2] c.l.s.service.impl.UserServiceImpl       : Finding all users
```

For the second request if something is cached we don't have any logs:
```bash
curl -X GET localhost:8080/users
```
```bash
LOGS:

```

But if we delete some user we have the following logs:
```bash
curl -X DELETE localhost:8080/users/1
```

```bash
LOGS:
INFO 1034 --- [nio-8080-exec-8] c.l.s.service.impl.UserServiceImpl       : Deleting user by id: 1
```
Then will be deleted the cache for the user with id 1 and for the list of users.

And if we try to list the users again we have the following logs:
```bash
curl -X GET localhost:8080/users
```

```bash
LOGS:
INFO 1034 --- [nio-8080-exec-2] c.l.s.service.impl.UserServiceImpl       : Finding all users
```

<h3>To learn more about the spring cache visit:</h3>
<li>
<a href="https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/cache.html">https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/cache.html </a>
</li>
<li>
<a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/cache/annotation/Cacheable.html#:%7E:text=Annotation%20Type%20Cacheable&text=Annotation%20indicating%20that%20the%20result,invoked%20for%20the%20given%20arguments">
https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/cache/annotation/Cacheable.html#:%7E:text=Annotation%20Type%20Cacheable&text=Annotation%20indicating%20that%20the%20result,invoked%20for%20the%20given%20arguments
</a> 
</li>

