package Client;

public class ClientScenarioTest {

    public ClientScenarioTest() {};

    /**
     * Test case 1: Join
     * Two clients join, one subscribe "Sports",
     * Another one subscribe to "Entertainment", one client publishes an "Entertainment" article
     * How To Run: Test case 1 will run in Java main method.
     * In client #1 object, test will run "join(127.0.0.1, 1099)" method.
     * In client #1 object, test will run "subscribe("127.0.0.1", 1098, "Sports;;;");" method.
     * In client #2 object, test will run "join(127.0.0.1, 1099)" method.
     * In client #2 object, test will run "subscribe("127.0.0.1", 1098, "Entertainment;;;");" method.
     * In client #1 object, test will run "publish("Entertainment;UMN;;contents2","127.0.0.1", 1099);". method
     * Observe the results in both client #1 and client #2 terminals.
     * Expected results:
     * Client #2 should receive the published article.
     * Client #1 should not receive the article they published.
     * Test passed if the above steps are executed successfully, and expected results are observed in terminal.
     */
    public void testCase1() throws InterruptedException {
        Client client1 = new Client();
        Client client2 = new Client();
        boolean join_status = client1.join("127.0.0.1", 1099);
        boolean join_status2 = client2.join("127.0.0.1", 1098);
        client1.subscribe("127.0.0.1", 1099, "Sports;;;");
        client2.subscribe("127.0.0.1", 1098, "Entertainment;;;");
        client1.receiveUDP(1099,"127.0.0.1",client1);
        client2.receiveUDP(1098,"127.0.0.1",client2);
        boolean publish_status = client1.publish("Entertainment;UMN;;contents2","127.0.0.1", 1099);
        System.out.println("publish status is " + publish_status);
        Thread.sleep(100);
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
    public void testCase2() throws InterruptedException {
        Client client1 = new Client();
        Client client2 = new Client();
        boolean join_status = client1.join("127.0.0.1", 1097);
        boolean join_status2 = client2.join("127.0.0.1", 1096);
        client1.subscribe("127.0.0.1",1097,"Sports;;;");
        client2.subscribe("127.0.0.1",1096,"Sports;;;");
        client1.receiveUDP(1097,"127.0.0.1");
        client2.receiveUDP(1096,"127.0.0.1");
        boolean publish_status = client1.publish("Sports;UMN;;contents1","127.0.0.1", 1097);
        System.out.println("publish status is " + publish_status);
        client1.leave("127.0.0.1", 1097);
        boolean publish_status2 = client2.publish("Sports;UMN;;contents2","127.0.0.1", 1096);
        System.out.println("publish status is " + publish_status2);
        Thread.sleep(100);
        client2.leave("127.0.0.1", 1096);

    }

    public static void main(String[] args) throws InterruptedException {
        ClientScenarioTest test = new ClientScenarioTest();
        System.out.println("*****Test case 1 Start*****");
        test.testCase1();
        System.out.println("*****Test case 1 passed*****");
        System.out.println("*****Test case 2 Start*****");
        test.testCase2();
        System.out.println("*****Test case 2 passed*****");
    }

    //
}
