package Client;

import static org.junit.Assert.*;

import org.junit.Test;

public class ClientScenarioTest {
    /**
     * Test case 1: Join
     * Two clients join, one subscribe "Sports",
     * Another one subscribe to "Entertainment", one client publishes an "Entertainment" article
     * How To Run: Test case 1 will run in Java main method.
     * In client #1 object, test will run "join(127.0.0.1, 1099)" method.
     * In client #1 object, test will run "subscribe("127.0.0.1", 1098, "Sports;;;");" method.
     * In client #2 object, test will run "join(127.0.0.1, 1099)" method.
     * In client #2 object, test will run "subscribe("127.0.0.1", 1098, "Entertainment;;;");" method.
     * In client #1 object, test will run "publish("Entertainment;;UMN;contents2","127.0.0.1", 1099);". method
     * Observe the results in both client #1 and client #2 terminals.
     * Expected results:
     * Client #2 should receive the published article.
     * Client #1 should not receive the article they published.
     * Test passed if the above steps are executed successfully, and expected results are observed in terminal.
     */
    @Test
    public void testCase1()  {
        Client client1 = new Client();
        Client client2 = new Client();
        boolean join_status = client1.join("127.0.0.1", 1099);
        boolean join_status2 = client2.join("127.0.0.1", 1098);
        client1.subscribe("127.0.0.1", 1099, "Sports;;;");
        client2.subscribe("127.0.0.1", 1098, "Entertainment;;;");
        client1.receiveUDP(1099,"127.0.0.1",client1);
        client2.receiveUDP(1098,"127.0.0.1",client2);
        boolean publish_status = client1.publish("Entertainment;;UMN;contents2","127.0.0.1", 1099);
        System.out.println("publish status is " + publish_status);
        String msg1 = client1.getCurrentMessage();
        String msg2 = client2.getCurrentMessage();
        System.out.println("msg1 is " + msg1);
        System.out.println("msg2 is " + msg2);
        assertNull(msg1);
        assertEquals("Entertainment;;UMN;contents2", msg2);
        client1.leave("127.0.0.1", 1099);
        client2.leave("127.0.0.1", 1098);

    }

    /**
        * Test case 2: Leave
        * Two clients join, both subscribe "Sports",
        * one client publishes an "Sports" article,
        * one client leaves the group,
        * one client publishes another "Sports" article,
        * Observe the results in both client #1 and client #2 terminals.
        * Expected results:
        * Client #2 should receive the first published article.
        * Client #1 should not receive the second published article.
        * Test Passed if the above steps are executed successfully, and expected results are observed in terminal.
        */
    @Test
    public void testCase2() {
        Client client1 = new Client();
        Client client2 = new Client();
        boolean join_status = client1.join("127.0.0.1", 1097);
        boolean join_status2 = client2.join("127.0.0.1", 1096);
        client1.subscribe("127.0.0.1",1097,"Sports;;;");
        client2.subscribe("127.0.0.1",1096,"Sports;;;");
        client1.receiveUDP(1097,"127.0.0.1",client1);
        client2.receiveUDP(1096,"127.0.0.1",client2);
        boolean publish_status = client1.publish("Sports;;UMN;contents1","127.0.0.1", 1097);
        System.out.println("publish status is " + publish_status);
        String msg1 = client1.getCurrentMessage();
        String msg2 = client2.getCurrentMessage();
        System.out.println("msg1 is " + msg1);
        System.out.println("msg2 is " + msg2);
        assertEquals("Sports;;UMN;contents1", msg1);
        assertEquals("Sports;;UMN;contents1", msg2);
        client1.leave("127.0.0.1", 1097);
        boolean publish_status2 = client2.publish("Sports;;UMN;contents2","127.0.0.1", 1096);
        System.out.println("publish status is " + publish_status2);
        String msg3 = client1.getCurrentMessage();
        String msg4 = client2.getCurrentMessage();
        System.out.println("msg3 is " + msg3);
        System.out.println("msg4 is " + msg4);
        assertEquals("Sports;;UMN;contents1", msg3);
        assertEquals("Sports;;UMN;contents2", msg4);
        client2.leave("127.0.0.1", 1096);
    }

    /**
     * Test case 3: client subscribe to the multiple topics
     * One client join, subscribe "Sports",
     * then subscribe "Sports and UMN" article,
     * Observe the number of UDP that client #1 received.
     * Expected results:
     * Client #1 should receive the UPD only 1 time.
     * Test Passed if the above steps are executed successfully, and expected results are observed in terminal.
     */
    @Test
    public void testCase3() {
        Client client1 = new Client();
        boolean join_status = client1.join("127.0.0.1", 1095);
        client1.subscribe("127.0.0.1", 1095,"Sports;;;");
        client1.subscribe("127.0.0.1", 1095,"Sports;;UMN;");
        client1.receiveUDP(1095, "127.0.0.1", client1);
        client1.publish("Sports;;UMN;UMN wins", "127.0.0.1", 1095);
        assertEquals(1, client1.getUdpCount());
    }

    /**
     * Test case 4: client subscribe to the multiple topics
     * One client join, subscribe "Sports",
     * then subscribe "Sports and UMN" article,
     * Observe the number of UDP that client #1 received.
     * Expected results:
     * Client #1 should receive the UPD only 1 time.
     * Test Passed if the above steps are executed successfully, and expected results are observed in terminal.
     */
    @Test
    public void testCase4() {
        Client client1 = new Client();
        Client client2 = new Client();
        boolean join_status = client1.join("127.0.0.1", 1094);
        boolean join_status2 = client2.join("127.0.0.1", 1093);
        client1.subscribe("127.0.0.1", 1094,"Sports;;;"); // subscribe "Sports"
        client1.receiveUDP(1094, "127.0.0.1", client1);
        client2.publish("Sports;;UMN;UMN wins", "127.0.0.1", 1093);
//        assertEquals(1, client1.getUdpCount());
        assertEquals("Sports;;UMN;UMN wins", client1.getCurrentMessage());
        client1.unsubscribe("127.0.0.1", 1094,"Sports;;;"); // unsubscribe "Sports
        client1.subscribe("127.0.0.1", 1094,"Sports;;UMN;"); // subscribe "Sports and UMN"
        client2.publish("Sports;;;UMN wins wins 2", "127.0.0.1", 1093);
//        assertEquals(2, client1.getUdpCount());
        assertEquals("Sports;;;UMN wins wins 2", client1.getCurrentMessage());
    }

}
