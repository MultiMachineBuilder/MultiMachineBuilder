**easy-events** is a simple Java event library which features a minimal and compact design.

## How do I use it?
For this example we will create a class that defines two events.
```java
public class Server {
    
    private final Event<String> userJoinedEvent = new SimpleEvent<>();
    private final Event<String> userLeftEvent = new SimpleEvent<>();
    
    private final List<String> users = new ArrayList<>();
    
    public Event<String> userJoinedEvent() {
        return userJoinedEvent;
    }
    
    public Event<String> userLeftEvent() {
        return userLeftEvent;
    }
    
    public void addUser(String user) {
        if (users.add(user)) {
            userJoinedEvent().trigger(user);
        }
    }
    
    public void removeUser(String user) {
        if (users.remove(user)) {
            userLeftEvent().trigger(user);
        }
    }
    
}
```

The following snippet will use this class:
```java
Server server = new Server();
        
server.userJoinedEvent().addListener(user -> System.out.println("User joined: " + user));
server.userLeftEvent().addListener(user -> System.out.println("User left: " + user));

server.addUser("Bob");
server.addUser("Jack");
server.addUser("Philipp");
server.addUser("John");

server.removeUser("Bob");
server.removeUser("John");
```

If we execute the above snippet the following output should appear in the console:
```
User joined: Bob
User joined: Jack
User joined: Philipp
User joined: John
User left: Bob
User left: John
```

## How do I get it?

You can obtain pre-built JARs in the Releases tab or clone the repository and compile it yourself.

Alternatively you can add it as a Maven dependency. **Note:** The Maven dependency is a 'fake' dependency which uses [JitPack](https://jitpack.io/) to fetch the git repository and use it. Because of this you have to add a custom repository to your POM.
```xml
<dependencies>
    <dependency>
        <groupId>com.github.Fylipp</groupId>
        <artifactId>easy-events</artifactId>
        <version>v1.1.0</version>
    </dependency>
</dependencies>
```
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

## What am I allowed to do with it?
**easy-events** is released under the [MIT license](https://tldrlegal.com/license/mit-license) (note that the summary is 
not a replacement of the full license and holds no legal value). The full license can be found in the `LICENSE` file which is located in the root folder of the repository.
