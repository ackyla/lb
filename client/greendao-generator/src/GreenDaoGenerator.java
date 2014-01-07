import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerator {
	
	public static void main(String[] args) throws Exception {
		Schema schema = new Schema(5, "com.lb");
		addAuth(schema);
		new DaoGenerator().generateAll(schema, "../android/src-gen");
	}
	
	private static void addAuth(Schema schema) {
		Entity auth = schema.addEntity("Auth");
		auth.addIdProperty().autoincrement();
		auth.addIntProperty("user_id").notNull();
		auth.addStringProperty("token");
		auth.addStringProperty("name");
		auth.addStringProperty("url");
	}
}
