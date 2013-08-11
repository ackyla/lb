package api;

public class TestAPI {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Start");
		Client client = new Client("phelrine");
		System.out.println(client.getUserId());
		System.out.println(client.getToken());
	}
}
