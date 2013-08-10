package api;

public class TestAPI {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("test");
		api.API client = new api.API("phelrine");
		System.out.println(client.getUserId());
		System.out.println(client.getToken());
	}

}
