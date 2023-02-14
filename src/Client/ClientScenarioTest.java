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
     * @return true if the test case passes, false otherwise
     */
    public boolean testCase1() throws InterruptedException {
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
        System.out.println("client1 received: " + client1.getCurrentMessage());
        System.out.println("client2 received: " + client2.getCurrentMessage());
        client1.leave("127.0.0.1", 1099);
        client2.leave("127.0.0.1", 1098);
        if (client1.getCurrentMessage().equals("Entertainment;UMN;;contents2") && client2.getCurrentMessage().equals("")){
            System.out.println("Test Case 1: PASSED");
            return true;
        }
        else {
            System.out.println("Test Case 1: FAILED");
            return false;
        }
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
        * @return true if the test case passes, false otherwise
     */

    public static void main(String[] args) throws InterruptedException {
        ClientScenarioTest test = new ClientScenarioTest();
        test.testCase1();

    }

    //
}
