package Client;

import static org.junit.Assert.*;

import org.junit.Test;

public class ClientMethodTest {

    @Test
    public void testJoin() {
        Client client = new Client();
        boolean joinResult = client.join("127.0.0.1", 1099);
        assertTrue(joinResult);
        System.out.println("Test Case 1: Join - PASSED");
        client.leave("127.0.0.1", 1099);
    }

    @Test
    public void testLeave() {
        Client client = new Client();
        client.join("127.0.0.1", 1099);
        client.leave("127.0.0.1", 1099);
        System.out.println("Test Case 2: Leave - PASSED");
    }

    @Test
    public void testSubscribe() {
        Client client = new Client();
        client.join("127.0.0.1", 1099);
        boolean subscribeResult = client.subscribe("127.0.0.1", 1099, "Sports;;;");
        assertTrue(subscribeResult);
        System.out.println("Test Case 3: Subscribe - PASSED");
        client.leave("127.0.0.1", 1099);
    }

    @Test
    public void testUnsubscribe() {
        Client client = new Client();
        client.join("127.0.0.1", 1099);
        client.subscribe("127.0.0.1", 1099, "Sports;;;");
        boolean unsubscribeResult = client.unsubscribe("127.0.0.1", 1099, "Sports;;;");
        assertTrue(unsubscribeResult);
        System.out.println("Test Case 4: Unsubscribe - PASSED");
        client.leave("127.0.0.1", 1099);
    }

    @Test
    public void testPublish() {
        Client client = new Client();
        client.join("127.0.0.1", 1099);
        boolean publishResult = client.publish("Sports;UMN;;contents", "127.0.0.1", 1099);
        assertTrue(publishResult);
        System.out.println("Test Case 5: Publish - PASSED");
        client.leave("127.0.0.1", 1099);
    }

    @Test
    public void testGreeting() {
        Client client = new Client();
        String greeting = client.greeting();
        assertNotNull(greeting);
        System.out.println("Test Case 6: Greeting - PASSED");
    }

    @Test
    public void testPing() {
        Client client = new Client();
        client.ping(1000, 3);
        System.out.println("Test Case 7: Ping - PASSED");
    }


}

